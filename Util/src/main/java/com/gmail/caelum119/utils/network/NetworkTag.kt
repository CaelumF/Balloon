package com.gmail.caelum119.utils.network
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import java.io.*
import java.util.*

/**
 * An abstract class for custom networking tags.
 * @param E: The type of any child class extending this, should always be the class that is extending it.
 */
public abstract class NetworkTag( ) : Serializable{
  /**
   * Copmanion object to manage network tags.
   */
  companion object{
    var registeredTags : HashMap<Byte, Class<out NetworkTag>> = HashMap()
    var tagIndexes : HashMap<Class<out NetworkTag>, Byte> = HashMap()

    //TODO: Perhaps make a custom, faster serialisation method.
    public fun deserialize(vararg bytes : Byte): NetworkTag{
      try {
        return ObjectInputStream(ByteArrayInputStream(bytes)).readObject() as NetworkTag
      }catch(e: StreamCorruptedException){
        println(e.message)
      }
      return null!!
    }

    fun tagLookup(tagType: Class<out NetworkTag>): Byte = tagIndexes.get(tagType) ?: -1
  }

  val identifier: Byte = (NetworkTag.registeredTags.size + 1).toByte()

  init {
    NetworkTag.registeredTags.put(identifier, this.javaClass)
    NetworkTag.tagIndexes.put(this.javaClass, identifier)
  }

  /**
   * @return this.toString().getBytes();
   */
  fun getBytes(): ByteArray {
    return toString().toByteArray()
  }

  /**
   * @return A string representation of this tag, and all it's attributes. TODO: Use this/make this use custom serialization method?
   */
  override fun toString(): String {
    val stringified = StringBuilder()
    stringified.append(identifier)
//
//    for (i in attributes.indices) {
//      val attribute = attributes.get(i)
//      stringified.append(attribute.data)
//      if (i - 1 < attributes.size) {
//        stringified.append(696.toChar())
//      }
//    }
    return stringified.toString()
  }

  fun getData(): ByteArray{
    val bos = ByteOutputStream()
    ObjectOutputStream(bos).writeObject(this)
    return bos.bytes
  }
}


