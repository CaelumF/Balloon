//package com.gmail.caelum119.utils;
//
//
//import java.util.Arrays;
//
//@Deprecated
//public class Debug{
//
//  //  private static Map<String, Object> debugSettings = new Gson().fr
//  //This way we can keep our debugs organised, and change the way debug messages are handled without having to edit millions of lines.
//  public static void error(Object message, Class origin){
//    System.out.println("ERROR: " + String.valueOf(message));
//  }
//
//  public static void error(Object msg){
//    System.out.println("ERROR: " + msg);
//  }
//
//  public static void error(int msg){
//    System.out.println("ERROR: " + msg);
//  }
//
//  public static void info(Object msg){
//    System.out.println("info: " + msg);
//  }
//
//  public static void info(Object... msg){
//    System.out.println(Arrays.toString(msg));
//  }
//
//  public static void error(Exception e){
//    e.printStackTrace();
//    System.exit(0);
//  }
//
//  public static void info(String msg, Class origin){
//    System.out.println("info: " + msg);
//  }
//
//  public static void spam(Object message){
//    System.out.println(message);
//  }
//
//  public static void exit(int status){
//    System.exit(status);
//  }
////
////  @Deprecated
////  public static int getDebugIntSetting(String key){
////    Map<String, String> debugSettings = null;
////    try{
////      debugSettings = new Gson().fromJson(FileIO.readText(FileIO.getLocalPath() + "\\content\\debug\\quicksettings.json"), HashMap.class);
////    }catch(IOException e){
////      e.printStackTrace();
////    }
////    return Integer.valueOf(debugSettings.get(key));
////  }
////  @Deprecated
////  public static String getDebugStringSetting(String key){
////    Map<String, String> debugSettings = null;
////    try{
////      debugSettings = new Gson().fromJson(FileIO.readText(FileIO.getLocalPath() + "\\content\\debug\\quicksettings.json"), HashMap.class);
////    }catch(IOException e){
////      e.printStackTrace();
////    }
////    return String.valueOf(debugSettings.get(key));
////  }
//
//}
