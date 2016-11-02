package com.gmail.caelum119.utils.files;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Caelum on 9/11/14.
 */

//Barely finished and not sure if it ever will be so I'm gonna stick with deprecated.
@Deprecated
public class XMLLoader extends DefaultHandler{


  public XMLLoader(String path){
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(true);

    try{

      SAXParser saxParser = factory.newSAXParser();
      File file = new File(path);

      saxParser.parse(file, this);
    }catch(ParserConfigurationException e1){}catch(SAXException e1){}catch(IOException e){}

  }

  @Override public void startElement(String uri, String localName, String qName, Attributes attributes){

    for(int i = 0; i < 200; i++){
      String name = attributes.getValue(1);

      if(name == null)
        break;

    }
  }

  public class Tag{
    private String name;
    private ArrayList<Object> elements = new ArrayList<>();
    //        private ArrayList<Object> childElements=new ArrayList<dddd>();


    public Tag(String name){
      this.name = name;
    }

    public class Attributes{
      private String name;
      private Object data;

      public Attributes(String name, Object data){
        this.name = name;
        this.data = data;
      }

    }

  }

}
