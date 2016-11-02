package com.gmail.caelum119.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Caelum on 12/29/14.
 */
@Deprecated
public class Network{
  private boolean connected = false;
  private InetAddress server;
  private int serverPort;
  private DatagramSocket socket = null;

  public void connect(InetAddress address, int port){
    server = address;
    serverPort = port;
    connected = true;

    try{
      socket = new DatagramSocket();
    }catch(SocketException e){
      e.printStackTrace();
    }
  }

  public static void sendData(byte[] message, InetAddress too, int port){
    try{
      new DatagramSocket(port, too).send(new DatagramPacket(message, message.length, too, port));
    }catch(IOException e){
      e.printStackTrace();
    }
  }


  public void sendData(byte[] message){
    try{
      socket.send(new DatagramPacket(message, message.length, server, serverPort));
    }catch(NullPointerException e){

      if(message != null)
        System.out.println("Must be connected to a server before sending a message without a destination");
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}

