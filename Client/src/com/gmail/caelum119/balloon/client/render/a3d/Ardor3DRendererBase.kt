package com.gmail.caelum119.balloon.client.render.a3d

import com.ardor3d.annotation.MainThread
import com.ardor3d.example.PropertiesDialog
import com.ardor3d.example.PropertiesGameSettings
import com.ardor3d.framework.*
import com.ardor3d.framework.jogl.JoglCanvasRenderer
import com.ardor3d.framework.jogl.JoglNewtWindow
import com.ardor3d.image.TextureStoreFormat
import com.ardor3d.image.util.awt.ScreenShotImageExporter
import com.ardor3d.image.util.jogl.JoglImageLoader
import com.ardor3d.input.*
import com.ardor3d.input.control.FirstPersonControl
import com.ardor3d.input.jogl.JoglNewtFocusWrapper
import com.ardor3d.input.jogl.JoglNewtKeyboardWrapper
import com.ardor3d.input.jogl.JoglNewtMouseManager
import com.ardor3d.input.jogl.JoglNewtMouseWrapper
import com.ardor3d.input.logical.*
import com.ardor3d.intersection.PickResults
import com.ardor3d.intersection.PickingUtil
import com.ardor3d.intersection.PrimitivePickResults
import com.ardor3d.light.PointLight
import com.ardor3d.math.ColorRGBA
import com.ardor3d.math.Ray3
import com.ardor3d.math.Vector2
import com.ardor3d.math.Vector3
import com.ardor3d.renderer.ContextManager
import com.ardor3d.renderer.Renderer
import com.ardor3d.renderer.TextureRendererFactory
import com.ardor3d.renderer.jogl.JoglTextureRendererProvider
import com.ardor3d.renderer.queue.RenderBucketType
import com.ardor3d.renderer.state.LightState
import com.ardor3d.renderer.state.WireframeState
import com.ardor3d.renderer.state.ZBufferState
import com.ardor3d.scenegraph.Node
import com.ardor3d.scenegraph.event.DirtyType
import com.ardor3d.util.*
import com.ardor3d.util.Timer
import com.ardor3d.util.geom.Debugger
import com.ardor3d.util.resource.ResourceLocatorTool
import com.ardor3d.util.resource.SimpleResourceLocator
import com.ardor3d.util.screen.ScreenExporter
import com.ardor3d.util.stat.StatCollector
import com.gmail.caelum119.balloon.client.render.a3d.impl.A3DNodeImpl
import com.gmail.caelum119.balloon.world.scenegraph.visual.BalloonNode
import com.gmail.caelum119.balloon.world.scenegraph.visual.BalloonRenderer
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.ListenerInterface
import com.google.common.base.Predicates
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import java.awt.EventQueue
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Level
import java.util.logging.Logger

/**
 * First created 5/11/2016 in Engine
 */
abstract class Ardor3DRendererBase : Scene, Runnable, Updater, BalloonRenderer() {
    override var rootNode: BalloonNode = A3DNodeImpl()
    protected val _logicalLayer = LogicalLayer()

    lateinit protected var _physicalLayer: PhysicalLayer

    protected val _timer = Timer()
    protected val _frameHandler = FrameHandler(_timer)

    lateinit protected var _settings: DisplaySettings

    protected val _root = Node()

    lateinit protected var _lightState: LightState

    lateinit protected var _wireframeState: WireframeState

    @Volatile protected var _exit = false

    protected var _showBounds = false
    protected var _showNormals = false
    protected var _showDepth = false

    protected var _doShot = false

    lateinit protected var _canvas: NativeCanvas

    protected var _screenShotExp = ScreenShotImageExporter()

    lateinit protected var _mouseManager: MouseManager

    lateinit protected var _controlHandle: FirstPersonControl

    protected var _worldUp = Vector3(0.0, 1.0, 0.0)

    protected var light: PointLight = PointLight()

    private val eventCollection = EventCollection()

    override var events: ListenerInterface = ListenerInterface(eventCollection)

    override fun run() {
        try {
            if (_canvas is JoglNewtWindow) {
                (_canvas as JoglNewtWindow).addWindowListener(object : WindowAdapter() {
                    override fun windowDestroyNotify(e: WindowEvent?) {
                        val cr = _canvas.canvasRenderer
                        // grab the graphics context so cleanup will work out.
                        cr.makeCurrentContext()
                        ContextGarbageCollector.doFinalCleanup(cr.renderer)
                        cr.releaseCurrentContext()
                    }
                })
            }
            // else if (_canvas instanceof JoglAwtWindow) {
            // ((JoglAwtWindow) _canvas).addWindowListener(new java.awt.event.WindowAdapter() {
            // @Override
            // public void windowClosing(final java.awt.event.WindowEvent e) {
            // final CanvasRenderer cr = _canvas.getCanvasRenderer();
            // // grab the graphics context so cleanup will work out.
            // cr.makeCurrentContext();
            // ContextGarbageCollector.doFinalCleanup(cr.getRenderer());
            // cr.releaseCurrentContext();
            // }
            // });
            // }

            _frameHandler.init()

            while (!_exit) {
                _frameHandler.updateFrame()
                Thread.`yield`()
            }
        } catch (t: Throwable) {
            System.err.println("Throwable caught in MainThread - exiting")
            t.printStackTrace(System.err)
        } finally {
            // quit even though the cleanup has just failed
            if (QUIT_VM_ON_EXIT) {
                System.exit(0)
            }
        }
    }

    fun exit() {
        _exit = true
    }

    @MainThread
    override fun init() {
        super.start()
        val caps = ContextManager.getCurrentContext().capabilities
        logger.info("Display Vendor: " + caps.displayVendor)
        logger.info("Display Renderer: " + caps.displayRenderer)
        logger.info("Display Version: " + caps.displayVersion)
        logger.info("Shading Language Version: " + caps.shadingLanguageVersion)

        registerInputTriggers()

        JoglImageLoader.registerLoader()

        try {
            var srl = SimpleResourceLocator(ResourceLocatorTool.getClassPathResource(
                    Ardor3DRendererBase::class.java, "images/"))
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, srl)
            srl = SimpleResourceLocator(ResourceLocatorTool.getClassPathResource(Ardor3DRendererBase::class.java,
                    "com/ardor3d/example/media/models"))
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL, srl)
        } catch (ex: URISyntaxException) {
            ex.printStackTrace()
        }

        /**
         * Create a ZBuffer to display pixels closest to the camera above farther ones.
         */
        val buf = ZBufferState()
        buf.isEnabled = true
        buf.function = ZBufferState.TestFunction.LessThanOrEqualTo
        _root.setRenderState(buf)

        // ---- LIGHTS
        /** Set up a basic, default light.  */

        light.diffuse = ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f)
        light.ambient = ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f)
        light.location = Vector3(100.0, 100.0, 100.0)
        light.isEnabled = true

        /** Attach the light to a lightState and the lightState to rootNode.  */
        _lightState = LightState()
        _lightState.isEnabled = true
        _lightState.attach(light)
        _root.setRenderState(_lightState)

        _wireframeState = WireframeState()
        _wireframeState.isEnabled = false
        _root.setRenderState(_wireframeState)

        _root.sceneHints.renderBucketType = RenderBucketType.Opaque

        //Trigger pre init callbacks, call implemented initRenderer() method, trigger post init callbacks.
        eventCollection.triggerEvent(BalloonRenderer.EventTypes.preInitRender(this))
        initRenderer()
        eventCollection.triggerEvent(BalloonRenderer.EventTypes.postInitRender(this))

        _root.updateGeometricState(0.0)
    }

    protected abstract fun initRenderer()

    @MainThread
    override fun update(timer: ReadOnlyTimer) {
        if (_canvas.isClosing) {
            exit()
        }

        /** update stats, if enabled.  */
        if (Constants.stats) {
            StatCollector.update()
        }

        updateLogicalLayer(timer)

        // Execute updateQueue item
        GameTaskQueueManager.getManager(_canvas.canvasRenderer.renderContext).getQueue(GameTaskQueue.UPDATE)
                .execute()

        /** Call simpleUpdate in any derived classes of ExampleBase.  */
        updateExample(timer)

        /** Update controllers/render states/transforms/bounds for rootNode.  */
        _root.updateGeometricState(timer.timePerFrame, true)
    }

    protected fun updateLogicalLayer(timer: ReadOnlyTimer) {
        // check and execute any input triggers, if we are concerned with input
        _logicalLayer?.checkTriggers(timer.timePerFrame)
    }

    protected open fun updateExample(timer: ReadOnlyTimer) {
        // does nothing
    }

    @MainThread
    override fun renderUnto(renderer: Renderer): Boolean {
        // Execute renderQueue item
        GameTaskQueueManager.getManager(_canvas.canvasRenderer.renderContext).getQueue(GameTaskQueue.RENDER)
                .execute(renderer)

        // Clean up card garbage such as textures, vbos, etc.
        ContextGarbageCollector.doRuntimeCleanup(renderer)

        /** Draw the rootNode and all its children.  */
        if (!_canvas.isClosing) {
            /** Call renderExample in any derived classes.  */
            renderExample(renderer)
            renderDebug(renderer)

            if (_doShot) {
                // force any waiting scene elements to be renderer.
                renderer.renderBuckets()
                ScreenExporter.exportCurrentScreen(renderer, _screenShotExp)
                _doShot = false
            }
            return true
        } else {
            return false
        }
    }

    protected fun renderExample(renderer: Renderer) {
        _root.onDraw(renderer)
    }

    protected fun renderDebug(renderer: Renderer) {
        if (_showBounds) {
            Debugger.drawBounds(_root, renderer, true)
        }

        if (_showNormals) {
            Debugger.drawNormals(_root, renderer)
            Debugger.drawTangents(_root, renderer)
        }

        if (_showDepth) {
            renderer.renderBuckets()
            Debugger.drawBuffer(TextureStoreFormat.Depth16, Debugger.NORTHEAST, renderer)
        }
    }

    override fun doPick(pickRay: Ray3): PickResults {
        val pickResults = PrimitivePickResults()
        pickResults.setCheckDistance(true)
        PickingUtil.findPick(_root, pickRay, pickResults)
        processPicks(pickResults)
        return pickResults
    }

    protected fun processPicks(pickResults: PrimitivePickResults) {
        val pick = pickResults.findFirstIntersectingPickData()
        if (pick != null) {
            System.err.println("picked: " + pick.target + " at: "
                    + pick.intersectionRecord.getIntersectionPoint(0))
        } else {
            System.err.println("picked: nothing")
        }
    }

    protected fun quit(renderer: Renderer) {
        ContextGarbageCollector.doFinalCleanup(renderer)
        _canvas.close()
    }

    protected fun getAttributes(settings: PropertiesGameSettings): PropertiesGameSettings? {
        // Always show the dialog in these examples.
        var dialogImage: URL? = null
        val dflt = settings.defaultSettingsWidgetImage
        if (dflt != null) {
            try {
                dialogImage = ResourceLocatorTool.getClassPathResource(Ardor3DRendererBase::class.java, dflt)
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Resource lookup of '$dflt' failed.  Proceeding.")
            }

        }
        if (dialogImage == null) {
            logger.fine("No dialog image loaded")
        } else {
            logger.fine("Using dialog image '$dialogImage'")
        }

        val dialogImageRef = dialogImage
        val dialogRef = AtomicReference<PropertiesDialog>()
        val mainThreadTasks = Stack<Runnable>()
        try {
            if (EventQueue.isDispatchThread()) {
                dialogRef.set(PropertiesDialog(settings, dialogImageRef, mainThreadTasks))
            } else {
                EventQueue.invokeLater { dialogRef.set(PropertiesDialog(settings, dialogImageRef, mainThreadTasks)) }
            }
        } catch (e: Exception) {
            logger.logp(Level.SEVERE, Ardor3DRendererBase::class.java.javaClass.toString(), "ExampleBase.getAttributes(settings)",
                    "Exception", e)
            return null
        }

        var dialogCheck: PropertiesDialog? = dialogRef.get()
        while (dialogCheck == null || dialogCheck.isVisible) {
            try {
                // check worker queue for work
                while (!mainThreadTasks.isEmpty()) {
                    mainThreadTasks.pop().run()
                }
                // go back to sleep for a while
                Thread.sleep(50)
            } catch (e: InterruptedException) {
                logger.warning("Error waiting for dialog system, using defaults.")
            }

            dialogCheck = dialogRef.get()
        }

        if (dialogCheck.isCancelled) {
            System.exit(0)
        }
        return settings
    }

    protected fun registerInputTriggers() {
        // check if this example worries about input at all
        if (_logicalLayer == null) {
            return
        }

        _logicalLayer.registerTrigger(InputTrigger(MouseButtonClickedCondition(MouseButton.RIGHT),
                TriggerAction { source, inputStates, tpf ->
                    val pos = Vector2.fetchTempInstance().set(
                            inputStates.current.mouseState.x.toDouble(),
                            inputStates.current.mouseState.y.toDouble())
                    val pickRay = Ray3()
                    _canvas.canvasRenderer.camera.getPickRay(pos, false, pickRay)
                    Vector2.releaseTempInstance(pos)
                    doPick(pickRay)
                }, "pickTrigger"))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.ESCAPE), TriggerAction { source, inputState, tpf -> exit() }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.L), TriggerAction { source, inputState, tpf ->
            _lightState.isEnabled = !_lightState.isEnabled
            // Either an update or a markDirty is needed here since we did not touch the affected spatial directly.
            _root.markDirty(DirtyType.RenderState)
        }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.F4), TriggerAction { source, inputState, tpf -> _showDepth = !_showDepth }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.T), TriggerAction { source, inputState, tpf ->
            _wireframeState.isEnabled = !_wireframeState.isEnabled
            // Either an update or a markDirty is needed here since we did not touch the affected spatial directly.
            _root.markDirty(DirtyType.RenderState)
        }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.B), TriggerAction { source, inputState, tpf -> _showBounds = !_showBounds }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.C), TriggerAction { source, inputState, tpf -> println("Camera: " + _canvas.canvasRenderer.camera) }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.N), TriggerAction { source, inputState, tpf -> _showNormals = !_showNormals }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.F1), TriggerAction { source, inputState, tpf -> _doShot = true }))

        val clickLeftOrRight = Predicates.or(MouseButtonClickedCondition(
                MouseButton.LEFT), MouseButtonClickedCondition(MouseButton.RIGHT))

        _logicalLayer.registerTrigger(InputTrigger(clickLeftOrRight, TriggerAction { source, inputStates, tpf -> System.err.println("clicked: " + inputStates.current.mouseState.clickCounts) }))

        _logicalLayer.registerTrigger(InputTrigger(MouseButtonPressedCondition(MouseButton.LEFT),
                TriggerAction { source, inputState, tpf ->
                    if (_mouseManager.isSetGrabbedSupported) {
                        _mouseManager.grabbed = GrabbedState.GRABBED
                    }
                }))
        _logicalLayer.registerTrigger(InputTrigger(MouseButtonReleasedCondition(MouseButton.LEFT),
                TriggerAction { source, inputState, tpf ->
                    if (_mouseManager.isSetGrabbedSupported) {
                        _mouseManager.grabbed = GrabbedState.NOT_GRABBED
                    }
                }))

        _logicalLayer.registerTrigger(InputTrigger(AnyKeyCondition(), TriggerAction { source, inputState, tpf -> println("Key character pressed: " + inputState.current.keyboardState.keyEvent.keyChar) }))

    }

    companion object {
        private val logger = Logger.getLogger(Ardor3DRendererBase::class.java.name)

        /**
         * If true (the default) we will call System.exit on end of demo.
         */
        var QUIT_VM_ON_EXIT = true

        protected var _stereo = false

        protected var _minDepthBits = -1
        protected var _minAlphaBits = -1
        protected var _minStencilBits = -1

        fun start(implementationRenderer: Ardor3DRendererBase) {

            // Ask for propertyList
            val prefs = implementationRenderer.getAttributes(PropertiesGameSettings(
                    "ardorSettings.propertyList", null))

            // Convert to DisplayProperties (XXX: maybe merge these classes?)
            val settings = DisplaySettings(prefs!!.width, prefs.height, prefs.depth,
                    prefs.frequency,
                    // alpha
                    if (_minAlphaBits != -1) _minAlphaBits else prefs.alphaBits,
                    // depth
                    if (_minDepthBits != -1) _minDepthBits else prefs.depthBits,
                    // stencil
                    if (_minStencilBits != -1) _minStencilBits else prefs.stencilBits,
                    // samples
                    prefs.samples,
                    // other
                    prefs.isFullscreen, _stereo)

            implementationRenderer._settings = settings

            // get our framework
            val canvasRenderer = JoglCanvasRenderer(implementationRenderer)
            implementationRenderer._canvas = JoglNewtWindow(canvasRenderer, settings)
            val canvas = implementationRenderer._canvas as JoglNewtWindow
            implementationRenderer._mouseManager = JoglNewtMouseManager(canvas)
            implementationRenderer._physicalLayer = PhysicalLayer(JoglNewtKeyboardWrapper(canvas), JoglNewtMouseWrapper(
                    canvas, implementationRenderer._mouseManager), DummyControllerWrapper.INSTANCE, JoglNewtFocusWrapper(canvas))
            TextureRendererFactory.INSTANCE.setProvider(JoglTextureRendererProvider())

            implementationRenderer._logicalLayer.registerInput(implementationRenderer._canvas, implementationRenderer._physicalLayer)

            // Register our example as an updater.
            implementationRenderer._frameHandler.addUpdater(implementationRenderer)

            // register our native canvas
            implementationRenderer._frameHandler.addCanvas(implementationRenderer._canvas)

            Thread(implementationRenderer).start()
        }
    }


}
