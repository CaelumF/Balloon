package com.gmail.caelum119.utils.event;

/**
 * Created by Caelum on 6/20/14.
 */
public abstract class Event{
  private boolean cancelled = false;

  public boolean isCancelled(){
    return cancelled;
  }

  public void setCancelled(boolean cancelled){
    this.cancelled = cancelled;
  }
}
