package com.gmail.caelum119.balloon.world.scenegraph

/**
 * First created 12/30/2016 in BalloonEngine
 */
interface OrganisedSpatialPartition<ParentType, ChildType, ChildStorageType : Collection<ChildType>> : SpatialPartition<ParentType,
        ChildType, ChildStorageType> {
}