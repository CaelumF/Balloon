//package com.gmail.caelum119.utils.time;
//
//import client.util.Listener;
//
///**
// * Created by Caelum on 3/12/14.
// */
//
//public class Time implements Runnable, Listener
//{
//    private static long time;
//    private static Time t;
//
//    private Time()
//    {
//        if(time < 1)
//        {
//            new Timer(1, this);
//            t = this;
//        }
//
//    }
//    @Override public void run()
//    {
//
//    }
//
//    @Override public void onTick(String event)
//    {
//        time++;
//    }
//
//    public static long getMillis()
//    {
//        if(t == null)
//            new Thread(new Time());
//        return time;
//    }
//}
