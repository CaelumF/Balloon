package com.gmail.caelum119.balloon.world.scenegraph

import com.gmail.caelum119.balloon.world.engine.components.Component
import java.util.*

/**
 * First created 12/30/2016 in BalloonEngine
 *
 * 'End' spatial partition. Holds Entities, components, actual world data etc.
 *
 * Non-intermittent doesn't necessarily imply that it has no child partitions, TODO: Come up with better name.
 * OR, TODO: Remove [SpatialPartition] inheritance.
 */
interface NonintermittentSpatialPartition<ParentType, EntityStorageType : Collection<GeneralEntity>> :
        SpatialPartition<ParentType,
                GeneralEntity, EntityStorageType> {

    val allEntities: EntityStorageType
    val allComponents: ArrayList<Component>

}