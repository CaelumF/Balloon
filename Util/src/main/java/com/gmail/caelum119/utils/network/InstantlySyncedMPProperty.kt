package com.gmail.caelum119.utils.network
import sun.plugin.dom.exception.InvalidStateException
import java.io.Serializable

/**


 */
@Deprecated("Working on different implementation")
public class InstantlySyncedMPProperty<dataTypE>(val otherEndHasAuthority: Boolean = false, val challangeable: Boolean = false, val ID : String, val sharedWith : Connection, val UDP: Boolean =
false) : Property<dataTypE>, Serializable {
  var data : dataTypE? = null

  init {
    if(!otherEndHasAuthority) {
      sharedWith.sendPacketImmediately(*SyncedPropertyCreateTag<dataTypE>(challangeable, ID, UDP).getData())
    }else{
      sharedWith.addListener<SyncedPropertyUpdateTag>(SyncedPropertyUpdateTag::class.java, {
        if (it.ID.equals(this.ID)) data = it.data as dataTypE
      })
    }
  }

  override fun get(): dataTypE? {
    return data
  }

  /**
   * If this end has authority, set the value of this property and updates [sharedWith]'s  property with the same [ID].
   * If this end does not have authority([ot;herEndHasAuthority] = true;), InvalidStateException is thrown.
   */
  override fun set(data: dataTypE?) {// TODO: UDP support with custom, faster serialisation for physics
    if(!otherEndHasAuthority){
      sharedWith.sendPacketImmediately(*SyncedPropertyUpdateTag(ID, data as Any).getData())
    }else{
      throw InvalidStateException("Synced MPProperty's value set from this end when the other end has authority(otherEndHasAuthority = true)")
    }
  }

  override fun toString(): String {
    return "$ID: $data}"
  }
}