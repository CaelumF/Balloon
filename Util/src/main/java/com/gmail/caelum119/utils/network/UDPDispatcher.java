package com.gmail.caelum119.utils.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Dispatches packets to their designated location every <i>poolDispatchFrequency</i> ms. Also includes static methods
 * for sending packets directly.
 */
@Deprecated
public class UDPDispatcher{
  private long poolDispatchFrequency;
  //All packets in this are dispatched every *poolDispatchFrequency* ms
  private ArrayList<Payload>       payloadDispatchPool      = new ArrayList<>();
  private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

  public static UDPDispatcher greetingDispatcher = new UDPDispatcher(100);

  public UDPDispatcher(long poolDispatchFrequency){
    this.poolDispatchFrequency = poolDispatchFrequency;
    scheduledExecutorService.scheduleWithFixedDelay(new Runnable(){
      @Override public void run(){
        for(Payload payload : payloadDispatchPool){
          payload.send();
        }
      }
    }, poolDispatchFrequency, poolDispatchFrequency, TimeUnit.MILLISECONDS);

  }

  public static void sendPacket(InetAddress address, int port, DatagramSocket socket, byte[] sendData){
    try{
      socket.send(new DatagramPacket(sendData, sendData.length, address, port));
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  public static void sendPacket(ConnectionDetail packet, byte[] data){

    sendPacket(packet.address, packet.port, packet.socket, data);
  }

  /**
   * Sends a UDP message to the location designated by *abstractConnection*. IDs not included, use UDPServer or UDPClient
   * @param connectionDetail location to send *data*
   * @param networkTagOutdated an identifier so the receiver knows how to process the data.
   * @param data data to add to the sending queue.
   */
  public static void sendPacket(ConnectionDetail connectionDetail, NetworkTagOutdated networkTagOutdated, byte[] data){
    byte[] combined = new byte[networkTagOutdated.identifierBytes.length + data.length];

    System.arraycopy(networkTagOutdated.identifierBytes, 0, combined, 0, networkTagOutdated.identifierBytes.length);
    System.arraycopy(data, 0, combined, networkTagOutdated.identifierBytes.length, data.length);

    sendPacket(connectionDetail, combined);
  }

  public static void send(){

  }

}
