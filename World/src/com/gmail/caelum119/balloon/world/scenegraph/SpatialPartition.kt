package com.gmail.caelum119.balloon.world.scenegraph

/**
 * First created 12/28/2016 in BalloonEngine
 */
interface SpatialPartition<ParentType, ChildType, ChildStorageType : Collection<ChildType>> {
    var parent: ParentType
    var children: ChildStorageType

    fun addChild(entityToAdd: ChildType): Boolean

    fun removeChild(child: ChildType): Boolean
}