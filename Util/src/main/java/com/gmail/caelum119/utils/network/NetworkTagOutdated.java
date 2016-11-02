package com.gmail.caelum119.utils.network;

import com.gmail.caelum119.utils.network.server.NetworkTagAttribute;

import java.util.ArrayList;

/**
 * A list of network tags to mark what proceeding data is for. Tags are not closed, the end is marked with another
 * keycode.
 */
@Deprecated
public abstract class NetworkTagOutdated{
  //  beginHandshake('#', "Begins handshake, must be proceeded with numbers");
  //  engineObject('e', );

  public int    identifierInteger;
  public String identifierString;
  public char   identifierCharacter;
  public byte   identifierBytes[];

  public String description;
  public ArrayList<NetworkTagAttribute<?>> attributes = new ArrayList<>();

  protected NetworkTagOutdated(){
  }

  public NetworkTagOutdated(int identifierInteger){
    this.identifierInteger = identifierInteger;
    this.identifierCharacter = (char) identifierInteger;
    this.identifierString = String.valueOf(identifierCharacter);
    this.identifierBytes = identifierString.getBytes();
  }

  public NetworkTagOutdated(String identifierString){
    if(identifierString.length() > 1){
      throw new IllegalArgumentException("NetworkKeycode identifierString cannot be longer than 1 identifierCharacter");
    }
    this.identifierString = identifierString;
    this.identifierCharacter = identifierString.charAt(0);
    this.identifierInteger = (int) identifierCharacter;
    this.identifierBytes = identifierString.getBytes();
  }

  public NetworkTagOutdated(char identifierCharacter){
    this.identifierString = String.valueOf(identifierCharacter);
    this.identifierCharacter = identifierCharacter;
    this.identifierInteger = (int) identifierCharacter;
    this.identifierBytes = identifierString.getBytes();
  }

  /********************************************************************************************************************|
   * Same constructors with descriptions
   *
  //*******************************************************************************************************************/

  public NetworkTagOutdated(int identifierInteger, String description){
    this.identifierInteger = identifierInteger;
    this.identifierCharacter = (char) identifierInteger;
    this.identifierString = String.valueOf(identifierCharacter);
    this.identifierBytes = identifierString.getBytes();
    this.description = description;
  }

  public NetworkTagOutdated(String identifierString, String description){
    if(identifierString.length() > 1){
      throw new IllegalArgumentException("NetworkTag String cannot be longer than 1 identifierCharacter");
    }
    this.identifierString = identifierString;
    this.identifierCharacter = identifierString.charAt(0);
    this.identifierInteger = (int) identifierCharacter;
    this.identifierBytes = identifierString.getBytes();
    this.description = description;
  }

  public NetworkTagOutdated(char identifierCharacter, String description){
    this.identifierString = String.valueOf(identifierCharacter);
    this.identifierCharacter = identifierCharacter;
    this.identifierInteger = (int) identifierCharacter;
    this.identifierBytes = identifierString.getBytes();
    this.description = description;
  }
  /**
   * @return this.toString().getBytes();
   */
  public byte[] getBytes(){
    return toString().getBytes();
  }
  /**
   * @return A string representation of this tag, and all it's attributes.
   */
  @Override public String toString(){
    StringBuilder stringified = new StringBuilder();
    stringified.append(identifierCharacter);

    for(int i = 0; i < attributes.size(); i++){
      NetworkTagAttribute attribute = attributes.get(i);
      stringified.append(attribute.data);
      if(i - 1 < attributes.size()){
        stringified.append((char)696);
      }
    }
    return stringified.toString();
  }
}
