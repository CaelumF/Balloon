package com.gmail.caelum119.utils;

/**
 * Created by Caelum on 6/18/14.
 */
@Deprecated
public interface Task{
  Object[] store = new Object[10];

  void init(Object... args);

  void run();
}
