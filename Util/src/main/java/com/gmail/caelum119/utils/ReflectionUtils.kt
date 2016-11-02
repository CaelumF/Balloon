package com.gmail.caelum119.utils

import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass

/**
 *  Search for method by parameter types recursively up the inheritance of it's containing class all the way to Object
 *  @return An array with any java.lang.reflect.Method found with matching [parameterTypes]
 */
public object ReflectionUtils{


  fun methodSuperSearchByParameters(objectToSearch: Any, vararg parameterTypes: Class<*>): Array<Method>? {
    val results = ArrayList<Method>()
    for(curiMethod in objectToSearch.javaClass.methods){
      if(curiMethod.parameterTypes.equals(parameterTypes)){
        results.add(curiMethod)
      }
    }
    objectToSearch.javaClass.superclass.let {
      methodSuperSearchByParameters(it)
    }
    return results.toArray() as Array<Method>?
  }

  fun methodSearchByAnnotation(objectToSearch: Any, vararg filterAnnotations: KClass<out Annotation>) : Array<Method> {
    val results = ArrayList<Method>()
    for (curiMethod in objectToSearch.javaClass.methods) {
      for (curiAnnotation in filterAnnotations) {
        if (curiMethod.getAnnotation(curiAnnotation.java) != null) {
          results.add(curiMethod)
        }
      }
    }
    return results.toTypedArray()
  }

  /**
   * I don't want to name it something like "isUpstreamRelative",
   */
  fun isSuperSuperClass(child: Any, possibleParent: Any): Boolean{
    if(child.javaClass.equals(Any::class.java)) return false
    if(child.javaClass.superclass.equals(possibleParent.javaClass)) return true
    else return isSuperSuperClass(child, possibleParent)
  }

  /**
   * Check if the types and length of [args] matches [method]'s parameters.
   */
  fun argumentsMatchParameters(method: Method, vararg args: Any): Boolean {
    val methodParameters = method.parameterTypes
    if(args.size == methodParameters.size){
      for((index, arg) in args.withIndex()){
        if(!methodParameters[index].isInstance(arg)){
          return false
        }
      }
      return true
    }
    return false
  }
}

