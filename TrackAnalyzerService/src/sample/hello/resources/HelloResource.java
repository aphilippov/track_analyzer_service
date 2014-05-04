package sample.hello.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/hello")
public class HelloResource {
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String sayHello() {
//		return "Hello Jersey";
//	}
	
	@XmlRootElement
	public class Cluster {
	    private String name;
	    private String configRef;
	    private boolean gmsEnabled;
	    private String broadcast = "udpmulticast";
	    private String gmsBindInterfaceAddress;
	    private String gmsMulticastAddress;
	    private int gmsMulticastPort;
	    // getters, setters, and toString() not shown
	}
	
    private static Map<String, Cluster> clusters = new HashMap<String, Cluster>();

	
	@GET
    //@Produces("text/plain")
	//@Produces("application/json")
	@Produces(MediaType.APPLICATION_JSON)
    public HashMap<String,String>  sayHelloToName(@QueryParam("name") String name) {
        if (name != null) {
            // if the query parameter "name" is there
            return new HashMap<String,String>();
        }
        return new HashMap<String,String>();
    }        


}
