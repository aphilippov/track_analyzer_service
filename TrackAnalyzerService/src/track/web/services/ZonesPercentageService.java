package track.web.services;

import java.sql.SQLException;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import tracks.main.StarterFast;
import tracks.types.*;

@Path("/visits")
public class ZonesPercentageService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Zone,Float> getZonesSorted(){
		StarterFast sf = new StarterFast();
		try{
		return StarterFast.CalculatePercentages();
		} catch (SQLException e){
			return new LinkedHashMap<Zone,Float>();
		}
	}

}
