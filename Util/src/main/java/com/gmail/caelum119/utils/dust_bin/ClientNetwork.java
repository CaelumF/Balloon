//package com.gmail.caelum119.utils;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * Created by Caelum on 12/29/14.
// */
//@Deprecated
//public class ClientNetwork extends Network{
//  private static ClientNetwork ourInstance = new ClientNetwork();
//
//  public static ClientNetwork getInstance(){
//    return ourInstance;
//  }
//
//  private ClientNetwork(){
//
//  }
//
//  public void connect(String hostname, int port){
//    try{
//      if(hostname.equalsIgnoreCase("localhost")){
//        connect(InetAddress.getLocalHost(), port);
//      }
//    }catch(UnknownHostException e){
//      e.printStackTrace();
//    }
//
//    sendData(NetworkProtocols.REQUEST_ID);
//  }
//}
