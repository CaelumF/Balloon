package com.gmail.caelum119.utils.network.server;

/**
 * @Deprecated: Working on  a different implementation
 */

public class NetworkTagAttribute<DataT>{
    public String name;
    public DataT  data;

    public NetworkTagAttribute(String name, DataT data){
        this.name = name;
        this.data = data;
    }
}
