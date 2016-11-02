package com.gmail.caelum119.utils;

/**
 * Created by Caelum on 10/30/14.
 */
@Deprecated
public class Timers{
  static class remindMe{
    public remindMe(Task target, int millis, int nanos){
      try{
        Thread.sleep(millis, nanos);
      }catch(InterruptedException e){
        e.printStackTrace();
      }
      target.run();

    }
  }

  static class runLater{
    public runLater(Runnable runnable, int millis, int nanos){
      try{
        Thread.sleep(millis, nanos);
      }catch(InterruptedException e){
        e.printStackTrace();
      }
      runnable.run();
    }

  }

  public static void runLater(Runnable runnable, int millis, int nanos){
    new runLater(runnable, millis, nanos);
  }

  public static void runLater(Runnable runnable, int millis){
    new runLater(runnable, millis, 0);
  }

  public static void remindMe(Task target, int millis, int nanos){
    new remindMe(target, millis, nanos);
  }

  public static void remindMe(Task target, int millis){
    new remindMe(target, millis, 0);
  }
}
