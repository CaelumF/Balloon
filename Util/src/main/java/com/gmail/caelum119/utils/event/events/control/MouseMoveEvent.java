package com.gmail.caelum119.utils.event.events.control;


import com.gmail.caelum119.utils.event.Event;

import java.awt.event.MouseEvent;

/**
 * Created by Caelum on 2/13/15.
 */
@Deprecated
public class MouseMoveEvent extends Event{
  MouseEvent e;
  private double aimX, aimY;

  public MouseMoveEvent(MouseEvent e, double aimX, double aimY){
    this.e = e;
    this.aimX = aimX;
    this.aimY = aimY;
  }

  public double getAimX(){
    return aimX;
  }

  public double getAimY(){
    return aimY;
  }
}//( ͡° ͜ʖ ͡°)