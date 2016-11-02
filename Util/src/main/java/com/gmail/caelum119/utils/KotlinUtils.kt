
package com.gmail.caelum119.utils

import java.lang.Boolean as JBoolean
import java.lang.Double as JDouble
import java.lang.Float as JFloat
import java.lang.Integer as JInteger
/**
 * First created 4/18/2016 in Engine
 */
fun javaNativeClassToKotlinNativeClass(javaNativeClass: Any): Any {
  return when(javaNativeClass){
    JInteger::class.java -> Int::class.java
    JDouble::class.java -> Double::class.java
    JFloat::class.java -> Float::class.java
    JBoolean::class.java -> Boolean::class.java
    else -> javaNativeClass
  }
}
