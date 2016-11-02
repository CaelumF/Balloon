package com.gmail.caelum119.utils.network


import com.gmail.caelum119.utils.debug.printStackTrace
import com.sun.istack.internal.Nullable
import java.io.IOException
import java.net.DatagramPacket
import java.util.*

/**
 * For serializing NetworkTags
 * Payload syntax: single char identifier, NetworkTag comma char 6969 separated list of 'parameters',
 * Both client and server should have identical identifiers, insured by the fact they have equal mods/content packs, so
 * no communication of network tag definitions is necessary,
 * example: !
 */
class Payload(vararg networkTags: NetworkTag) {
  @Nullable
  private val destination: ConnectionDetail? = null
  private val senderID: String? = null

  private var tags = networkTags.toSet() as ArrayList<NetworkTag>

  init {
  }

  fun send() {
    if (destination == null) {
      throw IllegalStateException("Destination must be defined to use the polite send method")
    }
    val processed = processTags()
    try {
      destination.socket.send(DatagramPacket(processed, processed.size))
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun addTag(toAdd: NetworkTag) {
    tags.add(toAdd)
  }

  fun processTags(): ByteArray {
    //Find size of all the tags and their attributes before initializing an array.
    var sizeNeeded = 0
    for (tag in tags) {
      //TODO: Possibly not process each tag, and instead just get their size, for optimization.
      sizeNeeded += tag.getBytes().size + 1 // + 1 to accommodate tag separation character.
    }

    val processed = ByteArray(sizeNeeded)
    var processedIndex = 0
    for (i in tags) {
      val bytes = i.getBytes()
      //Add curTag's bytes to the processed array.
      for (b in bytes.indices) {
        processed[processedIndex] = bytes[b]
        processedIndex++
      }
      processed[processedIndex] = 6969.toByte()
    }
    return processed
  }
}
