
package com.grover101.minitwitter.retrofit.request;

//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("jsonschema2pojo")
public class RequestCreateTweet {

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RequestCreateTweet() {
    }

    /**
     * 
     * @param mensaje
     */
    public RequestCreateTweet(String mensaje) {
        super();
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
