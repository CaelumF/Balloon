//package com.gmail.caelum119.utils.time;
//
//
//import com.gmail.caelum119.utils.Control;
//
//public class Timer extends Thread
//{
//    private Control.Listener receiver;
//    private long delay;
//    private String event;
//
//    public Timer(int delay)
//    {
//        this.delay = delay;
//        this.start();
//    }
//
//    public Timer(int delay, Control.Listener receiver)
//    {
//        this.delay = delay;
//        this.receiver = receiver;
//        this.start();
//    }
//
//    public Timer(int delay, String event, Control.Listener receiver)
//    {
//        this.delay = delay;
//        this.receiver = receiver;
//        this.event = event;
//        this.start();
//    }
//
//
//    @Override
//    public void run()
//    {
//        while(true)
//        {
//
//            try
//            {
//                Thread.sleep(delay);
//            }
//            catch(InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//
//            receiver.onTick(event);
//        }
//    }
//
//}
//
////