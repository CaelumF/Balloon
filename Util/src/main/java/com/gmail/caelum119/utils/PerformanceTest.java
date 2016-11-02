package com.gmail.caelum119.utils;


/**
 * Created by Caelum on 2/11/15.
 */
@Deprecated
public class PerformanceTest{
  private long delay;
  private timer timer;

  private int checks;
  private int combined;
  private double average;


  public PerformanceTest(){
    timer = new PerformanceTest.timer(this);
    new Thread(timer).start();
  }

  public void getDelay(){
    checks++;
    if(checks < 2){
      delay = 0;
      return;
    }
    if(checks > 100){
      average = combined / 101;
      //            Debug.info(combined);

      System.exit(0);
      timer.running = false;
    }

    combined += delay;
    delay = 0;
  }

  private class timer extends Thread{
    boolean running = true;

    PerformanceTest host;

    private timer(PerformanceTest host){
      this.host = host;
    }

    @Override public void run(){
      while(running){
        try{
          Thread.sleep(0, 1);
        }catch(InterruptedException e){
          e.printStackTrace();
        }

        host.delay += 1;
      }
    }
  }
}
