package com.gmail.caelum119.utils.network

import com.gmail.caelum119.utils.event.NetworkEvent
import java.io.DataOutputStream
import java.lang.reflect.Method
import java.security.KeyFactory
import java.security.KeyPair
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


/**
 * Connection for Server AND client
 *
 */
abstract class Connection(): Runnable {

  private data class Listener(val method: Method?, val invoker: Any?, val listeningFor: Class<out NetworkTag>, val secondParameter: Boolean = false, val onReceive: (Any) -> Unit = {})

  private val networkTagListeners = HashMap<Class<out NetworkTag>, ArrayList<Listener>>()

  //For TCP connections, shouldn't really be mutable. TODO: Figure out how to make this read-only
  private var outTCPStream: DataOutputStream? = null

  abstract val keys: KeyPair
  abstract val encipher: Cipher
  abstract val decryter: Cipher
  /**
   * The public key used to communicate with the other side.<br>
   *
   * Abstract; must be set in implementation's primary constructor.
   * Use sendPacketImmediately() to communicate with
   * other side
   */
  abstract var theirPublicKey: PublicKey?


  /**
   * Code to run after every instance is constructed
   */
  companion object {
    public val postInit: ArrayList<(Connection) -> Unit> = ArrayList()
  }



  /**
   *  Sends a message to the other side immediately, instead of adding it to a queue.
   *  Should only really be used for establishing a connection.
   *  TODO: Make internal, encourage usage of sendPacket
   *  @param UDP Whether to send via UDP or TCP; true = send via UDP; false = send via TCP.
   *  Throws IllegalStateException if the chosen protocol does not conform with the constructor used to create
   *  this instance.
   */
  abstract fun sendPacketImmediately(vararg data: Byte)

  /**
   * Pauses the thread until a packet is received from the other side.
   *
   * Do not use for anything other than establishing a connection, as only one //TODO: this
   */
  internal abstract fun waitForPacket(): ByteArray

  open fun getPublicKey(key: ByteArray): PublicKey {
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
  internal abstract fun startListening()


  /**
   * Scans all methods in [listener], and adds any method with the [NetworkEvent] annotation to this instance's listener list for the event specified in the first parameter
   * of said event.
   * When a NetworkTag is received, all methods stored as listeners for that type of NetworkTag will be called, with the specific instance of the NetworkTag passed to the listening method.
   * If the first parameter of any of these methods is not <out NetworkTag> or doesn't exist, the method will still be called but all parameters will be passed as null.
   */
  abstract fun addListener(listener: Any, invoker: Any)

  /**
   * Registers [onReceive] to be called whenever a tag of type [listeningFor] is received.
   */
  abstract fun <listeningFoR> addListener(listeningFor: Class<out NetworkTag>, onReceive: (listeningFoR) -> Unit)

  /**
   * Registers [onReceive] to be called whenever a tag of type [listeningFor] is received.
   */
  open fun <listeningFoR> addListener(onReceive: (listeningFoR) -> Unit){}

  fun sendTag(tag: NetworkTag) {
    sendPacketImmediately(*tag.getData())
  }
}