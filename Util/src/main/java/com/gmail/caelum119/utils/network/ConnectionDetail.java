package com.gmail.caelum119.utils.network;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A container class containing IP address, socket and port
 */
public class ConnectionDetail{
  public InetAddress address;
  public DatagramSocket socket;
  public int port;

  public ConnectionDetail(InetAddress address, DatagramSocket socket, int port){
    this.address = address;
    this.socket = socket;
    this.port = port;
  }

  @Override public String toString(){
    return "ConnectionDetail{" +
            "address=" + address +
            ", socket=" + socket +
            ", port=" + port +
            '}';
  }
}

