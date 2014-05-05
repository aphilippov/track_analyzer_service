package tracks.web.services;

import java.sql.SQLException;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import tracks.main.TrackAnalysis;
import tracks.types.*;

@Path("/visits")
public class ZonesPercentageService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_XML)
	public Map<Zone,Float> getZonesSorted(@QueryParam("type") String type,
			@QueryParam("min") float minTime, @QueryParam("date") String date){
		
		//validating user input not required
		//null objects are handled in the main method
		
		try{
		return TrackAnalysis.CalculatePercentages(date, minTime, type);
		} catch (SQLException e){
			return new LinkedHashMap<Zone,Float>();
		}
	}

}
