package com.gmail.caelum119.utils

import java.math.BigInteger
import java.nio.ByteBuffer


/**
 * First created 4/18/2016 in Engine
 */
fun String.byteStringToType(type: Class<*>): Any {
  val charArray = this.toCharArray()
  val bytes: ByteArray = ByteArray(charArray.size, {charArray[it].toByte()})

  try {
    //Defaults:
    if(bytes.size == 0 || (bytes.size == 1 && bytes[0] == 0.toByte())){
      return when (type) {
      //      Int::class.java -> BigInteger(bytes).toInt()
        Int::class.java -> 0.toInt()
        Long::class.java -> 0.toLong()
        Double::class.java -> 0.toDouble()
        Float::class.java -> 0.toFloat()
        Boolean::class.java -> false
        String::class.java -> this
        else -> throw IllegalArgumentException("Byte string cannot be converted to $type")
      }
    }
    return when(type){
      Int::class.java -> BigInteger(bytes).toInt()
      Long::class.java -> BigInteger(bytes).toLong()
      Double::class.java -> ByteBuffer.wrap(bytes).getDouble()
      Float::class.java -> ByteBuffer.wrap(bytes).getFloat()
      Boolean::class.java -> charArray[0] != '\u0000'
      String::class.java -> this
      else -> throw IllegalArgumentException("Byte string cannot be converted to $type")
    }


  } catch(e: Exception) {
    e.printStackTrace()
  }
  return ""
}
//TODO:Better variable names
/**
 * Takes a signed byte array, and returns a String from that byte array unsigned.
 */
fun String.Companion.fromSignedBytes(bytes: ByteArray): String{
  val curatedBytes: IntArray = IntArray(bytes.size, { if (bytes[it] < 0) bytes[it] + 256 else bytes[it] + 0 })
  val charArray = CharArray(bytes.size, { curatedBytes[it].toChar() })
  return String(charArray)
}
/**
 * Returns [any] in the form of a ByteArray. The array will be shortened, for example 16777216 will only take up 1 byte
 *
 * Supported types:
 * Int: [4]
 * Long [8]
 * Float [4]
 * Double [8]
 * Boolean [1]
 * String [string.length * 8]
 */
public fun anyToByteArray(any: Any): ByteArray {
  var filteredOutput: ByteArray

  fun findUnnecessaryByteCount(byteCount: Int, value: Any): Unit {

  }

  when (any) {
    is Int -> {
      val dropcount = when {
        any % 16777216 == 0 -> 3
        any % 65536 == 0 -> 2
        any % 256 == 0 -> 1
        else -> 0
      }
      val output = ByteArray(4)
      ByteBuffer.wrap(output).putInt(any)
      filteredOutput = output.dropLast(dropcount).toByteArray()
    }
    is Long -> {
      val output = ByteArray(8)
      ByteBuffer.wrap(output).putLong(any)
      filteredOutput = output
    }
    is Float -> {
      val output = ByteArray(4)
      ByteBuffer.wrap(output).putFloat(any)
      filteredOutput = output
    }
    is Double -> {
      val output = ByteArray(8)
      ByteBuffer.wrap(output).putDouble(any)
      filteredOutput = output
    }
    is Boolean -> {
      val output = ByteArray(1)
      output[0] = (if (any) 1 else 0)
      filteredOutput = output
    }
    is String -> {
      return any.toByteArray()
    }
    else -> throw Exception("[any] not a workable type.")
  }
  //Since values are comma separated, empty bytes do not need to be written. The reader can assume them.
  //    filteredOutput = filteredOutput.filter { it == 0.toByte() }.toByteArray()

  return filteredOutput
}

fun String.toSignedByteArray(): ByteArray{
  val thisByteArray = this.toByteArray()
  return ByteArray(thisByteArray.size, {if(thisByteArray[it] > 126) (thisByteArray[it] - 256 ) as Byte else thisByteArray[it]})
}

