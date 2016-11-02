package com.gmail.caelum119.utils.network

import com.gmail.caelum119.utils.ReflectionUtils
import com.gmail.caelum119.utils.event.NetworkEvent
import sun.invoke.empty.Empty
import java.lang.reflect.Method
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


/**
 * Connection for Server AND client
 * TODO: Update doc comments from separation of TCPConnection
 */
abstract class UDPConnection(val UDPSocket: DatagramSocket, val theirAddress: InetAddress? = null, val theirPort: Int? = null) : Connection() {

    private data class Listener(val method: Method?, val invoker: Any?, val listeningFor: Class<out NetworkTag>, val secondParameter: Boolean = false, val onReceive: (Any) -> Unit = {})

    private val networkTagListeners = HashMap<Class<out NetworkTag>, ArrayList<Listener>>()

    override val keys: KeyPair
    override val encipher: Cipher
    override val decryter: Cipher
    /**
     * The public key used to communicate with the other side.<br>
     *
     * Abstract; must be set in implementation's primary constructor.
     * Use sendPacketImmediately() to communicate with
     * other side
     */
    override var theirPublicKey: PublicKey? = null


    /**
     * Code to run after every instance is constructed
     */
    companion object {
        public val postInit: ArrayList<(UDPConnection) -> Unit> = ArrayList()
    }

    init {
        keys = KeyPairGenerator.getInstance("RSA").generateKeyPair()
        encipher = Cipher.getInstance("RSA")
        encipher.init(Cipher.ENCRYPT_MODE, keys.public)

        decryter = Cipher.getInstance("RSA")
        decryter.init(Cipher.DECRYPT_MODE, keys.private)

        for (postInitLambda in postInit) {
            postInitLambda(this)
        }
    }

    /**
     *  Sends a message to the other side immediately, instead of adding it to a queue.
     *  Should only really be used for establishing a connection.
     *  TODO: Make internal, encourage usage of sendPacket
     *  @param UDP Whether to send via UDP or TCP; true = send via UDP; false = send via TCP.
     *  Throws IllegalStateException if the chosen protocol does not conform with the constructor used to create
     *  this instance.
     */
    override fun sendPacketImmediately(vararg data: Byte) {
        UDPSocket.send(DatagramPacket(data, data.size, theirAddress!!, theirPort!!))
    }

    /**
     * Pauses the thread until a packet is received from the other side.
     *
     * Do not use for anything other than establishing a connection, as only one //TODO: this
     */
    override fun waitForPacket(): ByteArray {
        var receiveBytes: ByteArray = ByteArray(0)
        val receivedPacket = DatagramPacket(receiveBytes, receiveBytes.size)
        UDPSocket.receive(receivedPacket)
        return receiveBytes
    }

    override fun getPublicKey(key: ByteArray): PublicKey {
        val keySpec = X509EncodedKeySpec(key);
        val keyFactory = KeyFactory.getInstance("RSA");
        val publicKey: PublicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    override fun run() {
        startListening()
    }

    /**
     * Just to keep the init clean. Also because never-ending loops don't work in primary constructors.
     * Will repeatedly wait for an event, deserialize it, and invoke all methods listening for that type of deserialized event.
     */
    override fun startListening() {
        while (true) {
            val deserializedTag: NetworkTag = NetworkTag.deserialize(*waitForPacket())
            networkTagListeners[deserializedTag.javaClass]?.let { //it: Class<Class out NetworkTagListener<*>>
                for (curEventListener in it) {
                    //Call method listening for [it], if it has a second(<Connection>) argument, call with [this] passed last.
                    curEventListener.method?.let {
                        if (curEventListener.secondParameter) curEventListener.method.invoke(curEventListener.invoker, deserializedTag, this)
                        else curEventListener.method.invoke(curEventListener.invoker, deserializedTag)
                    }
                    //If listener has provided a onReceive anonyfun, call it.
                    try {
                        curEventListener.onReceive(deserializedTag)
                    } catch(e: Exception) {
                        println(2)
                    }
                }
            }
        }
    }

    /**
     * Scans all methods in [listener], and adds any method with the [NetworkEvent] annotation to this instance's listener list for the event specified in the first parameter
     * of said event.
     * When a NetworkTag is received, all methods stored as listeners for that type of NetworkTag will be called, with the specific instance of the NetworkTag passed to the listening method.
     * If the first parameter of any of these methods is not <out NetworkTag> or doesn't exist, the method will still be called but all parameters will be passed as null.
     */
    override fun addListener(listener: Any, invoker: Any) {
        val listenerMethods: Array<Method> = ReflectionUtils.methodSearchByAnnotation(listener, NetworkEvent::class)
        //I over every method in [listener] has NetworkEvent annotation ? add new [Listener()] to HashMap<k=firstParameter?>
        for (curiMethod in listenerMethods) {
            var listeningFor: Class<out NetworkTag>? = null
            curiMethod.parameterTypes[0].let { //it: Class<*>
                if (NetworkTag::class.java.isAssignableFrom(it)) {
                    listeningFor = it as Class<out NetworkTag>? //I don't think that warning should be there, shouldn't it be scope inferred by the if?
                }
            }

            var newListener: Listener? = null
            listeningFor?.let {
                newListener = Listener(curiMethod, invoker, listeningFor as Class<out NetworkTag>, Connection::class.java.isAssignableFrom((curiMethod.parameterTypes.getOrNull(1)?.apply {} ?: Empty::class.java)))
            }

            //Add our newly constructed listener to receiveListeners, if it doesn't have an entry for listeningFor, make one.
            networkTagListeners[listeningFor]?.let {
                if (newListener != null) it.add(newListener as Listener)
            } ?: let {
                val newArrayList = ArrayList<Listener>()
                newArrayList.add(newListener as Listener)
                networkTagListeners.put(listeningFor as Class<out NetworkTag>, newArrayList)
            }
        }
    }

    /**
     * Registers [onReceive] to be called whenever a tag of type [listeningFor] is received.
     */
    override fun <listeningFoR> addListener(listeningFor: Class<out NetworkTag>, onReceive: (listeningFoR) -> Unit) {
        val listener = Listener(null, null, listeningFor, false, onReceive as (Any) -> Unit)
        val arrayList = networkTagListeners[listeningFor] ?: let {
            val newList = ArrayList<Listener>()
            networkTagListeners[listeningFor] = newList
            newList.add(listener)
            return
        }
        arrayList.add(listener)
    }

}