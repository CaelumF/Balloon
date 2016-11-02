package com.gmail.caelum119.utils

import java.util.*

/**
 * First created 5/7/2016 in Engine
 */
/**
 * Returns a version of this, but with [dataToInsert] inserted before [insertIndex]
 */
fun ByteArray.withInsertedBefore(dataToInsert: ByteArray, insertIndex: Int): ByteArray{
    val bytes = ArrayList<Byte>()
    for((index, byte) in dataToInsert.withIndex()){
        if(index == insertIndex)
            dataToInsert.forEach { bytes.add(it) }
        bytes.add(byte)
    }
    return bytes.toByteArray()
}

fun ByteArray.splitWithSectionLengthMarkers(delimiter: Byte): Array<ByteArray> {
    val sections = ArrayList<ByteArray>()
    var i = 0
    while (i < this.size) {
        val curiByte = this[i]
        if (curiByte == delimiter) {
            //Add a section which consists of the content between the Length marker and
            // the current position + the length the length marker describes
            val lengthOfSection = i + this[i + 1]
            sections.add(this.copyOfRange(i + 2, lengthOfSection))
            //Skip 2 extra to account for Delimiter and Length Marker
            i += 2 + lengthOfSection
        }
    }
    return sections.toTypedArray()
}

fun ByteArray.splitWithSectionLengthMarkers(): Array<ByteArray> {
    val sections = ArrayList<ByteArray>()
    var i = 0
    while (i < this.size) {
        //Add a section which consists of the content between the Length marker and
        // the current position + the length the length marker describes
        val lengthOfSection = i + this[i + 1]
        sections.add(this.copyOfRange(i + 1, lengthOfSection))
        //Skip one extra to account for Length Marker
        i += 1 + lengthOfSection
    }
    return sections.toTypedArray()
}
