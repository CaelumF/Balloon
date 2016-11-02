//package com.gmail.caelum119.utils.network
//
//import com.gmail.caelum119.utils.network.InstantlySyncedMPProperty
//import com.gmail.caelum119.utils.event.NetworkEvent
//import com.gmail.caelum119.utils.network.Connection
//import java.util.*
//
///**
// * Managers synced properties. Every connection established will automatically add this singleton as an instance
// */
//public object SyncedPropertyManager{
//
//  public val syncedProperties = HashMap<String, InstantlySyncedMPProperty<*>>()
//
//  init {
//    Connection.postInit.add { it.addListener(this, this) }
//  }
//
//  /**
//   * When a method on the other side creates a SyncedProperty, create one here too.
//   */
//  @NetworkEvent
//  public fun onPropertyCreate(createdProperty: SyncedPropertyCreateTag<*>, connection: Connection){
//    if(syncedProperties.get(createdProperty) == null)
//      throw IllegalStateException("Property created when one is already indexed under the same key.")
//    else
//      syncedProperties[createdProperty.ID] = InstantlySyncedMPProperty<Any>(true, createdProperty.challangeable, createdProperty.ID, connection)
//  }
//}