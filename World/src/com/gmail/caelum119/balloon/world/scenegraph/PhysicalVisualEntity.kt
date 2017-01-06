package com.gmail.caelum119.balloon.world.scenegraph

/**
 * First created 12/16/2016 in BalloonEngine
 * Represents an entity that exists both physically and visually.
 *
 * Visual allEntities should not exist on the server engine instances, so components utilising this class
 * should ONLY be for bridging physical data over to the renderer.
 */
class PhysicalVisualEntity(residingChunk: Chunk, val visualEntity: VisualEntity, val physicalEntity: PhysicalEntity) : GeneralEntity
(residingChunk) {


//    constructor(visualEntity: VisualEntity, physicalEntity: PhysicalEntity): this(physicalEntity.)

}