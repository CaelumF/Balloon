//package com.gmail.caelum119.utils.network
//
//import com.gmail.caelum119.utils.Debug
//import com.gmail.caelum119.utils.event.Event
//import com.gmail.caelum119.utils.event.NetworkEvent
//import sun.security.rsa.RSAPublicKeyImpl
//
//import javax.crypto.Cipher
//import javax.crypto.NoSuchPaddingException
//import java.io.IOException
//import java.lang.reflect.Method
//import java.net.DatagramPacket
//import java.net.DatagramSocket
//import java.net.InetAddress
//import java.security.InvalidKeyException
//import java.security.KeyPair
//import java.security.KeyPairGenerator
//import java.security.NoSuchAlgorithmException
//import java.util.ArrayList
//import java.util.Arrays
//import java.util.HashMap
//
///**
// * Stores and establishes connection information, as well as an means for receiving messages sent from the other end of this connection.
// * Not kotlin because it uses reflection, maybe convert sometime
// */
///*TODO:Optional async+sync key renewal
//  With an asynchronous encryption guarding the key to a synchronous encryption, we lose the extra security of a synchronous encryption.
//  Someone, with time, could crack the async and retrieve the sync key and then they could impersonate server and client
// */
//open class Connection1: Thread {
//  private var isClient: Boolean = false
//  private var isServer: Boolean = false
//  private var socket: DatagramSocket? = null
//  private var theirAddress: InetAddress? = null
//  private var theirPort: Int = 0
//
//  private var keys: KeyPair? = null
//  private val encrypter: Cipher? = null
//  private var decrypter: Cipher? = null
//
//  private val friendDetails: ConnectionDetail? = null
//  private var friendPublicKey: RSAPublicKeyImpl? = null
//
//  //TODO: These
//  private val rsaEncrypted: Boolean = false
//  private val senderConfirmation: Boolean = false
//  /**
//   * All that both constructors have in common
//   */
//  private fun init(socket: DatagramSocket, theirAddress: InetAddress, theirPort: Int) {
//    this.socket = socket
//    this.theirAddress = theirAddress
//    this.theirPort = theirPort
//
//    isClient = false
//    isServer = true
//
//    try {
//      keys = KeyPairGenerator.getInstance("RSA").generateKeyPair()
//      encipher = Cipher.getInstance("RSA")
//      encipher.init(Cipher.ENCRYPT_MODE, keys!!.public)
//
//      decrypter = Cipher.getInstance("RSA")
//      decrypter!!.init(Cipher.DECRYPT_MODE, keys!!.private)
//    } catch (e1: NoSuchAlgorithmException) {
//      e1.printStackTrace()
//    } catch (e1: NoSuchPaddingException) {
//      e1.printStackTrace()
//    } catch (e: InvalidKeyException) {
//      e.printStackTrace()
//    }
//
//  }
//
//  private val networkListeners = HashMap<Class<out NetworkEvent>, ArrayList<Listener>>()
//
//  /**
//   * this constructor is for servers or UDP punch-through PtP connections.
//
//   */
//  constructor(socket: DatagramSocket, theirAddress: InetAddress, theirPort: Int) {
//    init(socket, theirAddress, theirPort)
//    //Establish connection with whoever is described in connectionDetails. The clients initiates a connection, so they have already sent us their public key.
//    try {
//      friendPublicKey = RSAPublicKeyImpl(waitForPacket())
//      sendPacketImmediately(*(keys!!.public as RSAPublicKeyImpl).publicExponent.toByteArray())
//    } catch (e: IOException) {
//      e.printStackTrace()
//    } catch (e: InvalidKeyException) {
//      e.printStackTrace()
//    } finally {
//      Debug.error("Failed to establish connection with client" + theirAddress + ":" + theirPort + " on " + socket.localPort)
//    }
//  }
//
//  /**
//   * Initiates a connection with whoever is described in connectionDetails. For clients.
//   */
//  constructor(socket: DatagramSocket, theirAddress: InetAddress, theirPort: Int) {
//    this.socket = socket
//    this.theirAddress = theirAddress
//    this.theirPort = theirPort
//
//    isServer = false
//    isClient = false
//    try {
//      try {
//
//        keys = KeyPairGenerator.getInstance("RSA").generateKeyPair()
//        encipher = Cipher.getInstance("RSA")
//        encipher.init(Cipher.ENCRYPT_MODE, keys!!.public)
//
//        decrypter = Cipher.getInstance("RSA")
//        decrypter!!.init(Cipher.DECRYPT_MODE, keys!!.private)
//
//      } catch (e1: NoSuchAlgorithmException) {
//        e1.printStackTrace()
//      } catch (e1: NoSuchPaddingException) {
//        e1.printStackTrace()
//      } catch (e: InvalidKeyException) {
//        e.printStackTrace()
//      }
//
//      //>>>Establish connection with whoever is described in connectionDetails by sending our public key.
//      try {
//
//        sendPacketImmediately((keys!!.public as RSAPublicKeyImpl).publicExponent.toByteArray())
//        friendPublicKey = RSAPublicKeyImpl(waitForPacket())
//        //TODO: Receive faster, symmetrical encryption key from the server, encrypted with our slow, public key.
//      } catch (e: IOException) {
//        e.printStackTrace()
//      } catch (e: InvalidKeyException) {
//        e.printStackTrace()
//      }
//
//      //>>>Receive packets and generate,throw events based off them.
//      while (true) {
//        NetworkTag.deserialize()
//      }
//    } finally {
//      socket.close()
//    }
//  }
//
//  @Throws(IOException::class)
//  private fun waitForPacket(): ByteArray {
//    val receiveBytes = ByteArray(1024)
//    val receivedPacket = DatagramPacket(receiveBytes, receiveBytes.size())
//    socket!!.receive(receivedPacket)
//    return receiveBytes
//  }
//
//  fun addPayloadToQueue(payload: Payload) {
//
//  }
//
//  fun sendPacketImmediately(payload: Payload) {
//    val payloadData = payload.processTags()
//    try {
//      friendDetails!!.socket.send(DatagramPacket(payloadData, payloadData.size(), friendDetails.address, friendDetails.port))
//    } catch (e: IOException) {
//      e.printStackTrace()
//    }
//  }
//
//
//  fun addNetworkEventListener(listener: Any, invoker: Any) {
//    var klass: Class<*> = listener.javaClass
//
//    //Looped to search for methods inside of provided class and all superclasses up to Object.
//    while (klass != Any::class.java) {
//      val allMethods = ArrayList(Arrays.asList(*klass.declaredMethods))
//
//      for (curiMethod in allMethods) {
//        val listenerAnnotation = curiMethod.getAnnotation(NetworkEvent::class.java)
//
//        //if listenerAnnotation is null, curiMethod does not have the annotation NetworkEventSettings
//        if (listenerAnnotation != null) {
//          val listeningFor = curiMethod.genericParameterTypes[0] as Class<out NetworkEvent>
//
//          Debug.info(Event::class.java.isAssignableFrom(listeningFor))
//          if (listeningFor != null && Event::class.java.isAssignableFrom(listeningFor)) {
//            var listenerList: ArrayList<Listener>? = networkListeners[listeningFor]
//
//            val listener1 = Listener(curiMethod, invoker)
//            listener1.limit = listenerAnnotation.limit()
//            listener1.ignoreCancelled = listenerAnnotation.ignoreCancelled()
//
//            if (listenerList == null) {
//              listenerList = ArrayList<Listener>()
//              networkListeners.put(listeningFor, listenerList)
//              Debug.info("Listening for " + listeningFor)
//            }
//
//            listenerList.add(listener1)
//          }
//        }
//      }
//      klass = klass.getSuperclass()
//    }
//  }
//
//  private class Listener private constructor(internal var method: Method, internal var invoker: Any) {
//    internal var limit = -1
//    internal var ignoreCancelled: Boolean = false
//  }
//
//}
