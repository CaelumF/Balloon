package com.gmail.caelum119.utils.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Caelum on 11/24/14.
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface EventSettings{
  /**
   * The amount of times the event can be received before cancelling itself.
   * <i>Note: <code>ignoreCancelled</code> makes no difference to this.</i>
   */
  int limit() default - 1;

  /**
   * Whether or not to ignore a event if it's cancelled, true will not receive cancelled events and false will receive all events.
   * <i>Note: if a event reaches it's <code>limit</code>, this value makes no difference.</i>
   */
  boolean ignoreCancelled() default true;
}
