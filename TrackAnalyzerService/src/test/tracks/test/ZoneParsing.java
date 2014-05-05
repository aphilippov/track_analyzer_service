package tracks.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriter;

import tracks.data.DataFile;
import tracks.main.TrackAnalysis;
import tracks.types.Zone;

public class ZoneParsing {

	/**
	 * Testing the two entires of the same zone in a track
	 */
	@Test
	public void DoubleZone() {

		//creating a new csv file with the test data
		final CSV csv = CSV.create();		
		final List<String[]> container = new ArrayList<String[]>();

		container.add(new String[]{"ID","TYPE", "TS_BEGIN", "TS_END", 
		"ZONE VISITS"});
		
		container.add(new String[]{"1","BASKET", "1398149080895", "1398149476816", 
				"11(Frutta e verdura;3.5250)|11(Frutta e verdura;2.3710)|73(Promozionale;1.0560)|34(No food, dolciario;1.2320)"});		

		container.add(new String[]{"2","BASKET", "1398149080895", "1398149476816", 
				"11(Frutta e verdura;1.5250)|36(Caffe, orzo, modificatori;1.5280)|35(Soffiati, fiocchi, muesli;1.4760)"});
		CSVWriter writer = csv.writer(new File("test.csv"));
		
		for (String[] values : container){
		writer.writeNext(values);
		}
		
		
		try {
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		DataFile.setDataFile("test.csv");
		
		Map<Zone,Float> result = new LinkedHashMap<Zone,Float>();
		try {
			result = TrackAnalysis.CalculatePercentages(null, 0, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		//expected output:
		//zone 11 - 100%
		//the rest - 50%
		//for the filter min=5:
		//zone 11 - 50%
		//for the filter min=2:
		//zone 11 - 50%
		Zone firstZone = (Zone)result.keySet().toArray()[0];
		float firstPercent = result.get(firstZone);
		assertEquals("ID",11, firstZone.getZoneID());
		assertTrue(Float.compare(100, firstPercent)==0);
		
		//trying with filter min=5
		try {
			result = TrackAnalysis.CalculatePercentages(null, 5, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		firstZone = (Zone)result.keySet().toArray()[0];
		firstPercent = result.get(firstZone);
		assertEquals("ID",11, firstZone.getZoneID());
		assertTrue(Float.compare(50, firstPercent)==0);
		
		//trying with filter min=2
		try {
			result = TrackAnalysis.CalculatePercentages(null, 2, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		firstZone = (Zone)result.keySet().toArray()[0];
		firstPercent = result.get(firstZone);
		assertEquals("ID",11, firstZone.getZoneID());
		assertTrue(Float.compare(50, firstPercent)==0);
		
	}
	
	@Test
	public void RepeatedZone(){
		//creating a new csv file with the test data
				final CSV csv = CSV.create();		
				final List<String[]> container = new ArrayList<String[]>();

				container.add(new String[]{"ID","TYPE", "TS_BEGIN", "TS_END", 
				"ZONE VISITS"});
				
				container.add(new String[]{"1","BASKET", "1398149080895", "1398149476816", 
						"11(Frutta e verdura;3.5250)|73(Promozionale;1.0560)|11(Frutta e verdura;2.3710)|34(No food, dolciario;1.2320)"});		

				container.add(new String[]{"2","BASKET", "1398149080895", "1398149476816", 
						"11(Frutta e verdura;1.5250)|36(Caffe, orzo, modificatori;1.5280)|35(Soffiati, fiocchi, muesli;1.4760)"});
				CSVWriter writer = csv.writer(new File("test.csv"));
				
				for (String[] values : container){
				writer.writeNext(values);
				}
				
				
				try {
					writer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				DataFile.setDataFile("test.csv");
				
				Map<Zone,Float> result = new LinkedHashMap<Zone,Float>();
				try {
					result = TrackAnalysis.CalculatePercentages(null, 0, null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				//expected output:
				//zone 11 - 100%
				//the rest - 50%
				//for the filter min=5:
				//empty set
				//for the filter min=2:
				//zone 11 - 50%
				Zone firstZone = (Zone)result.keySet().toArray()[0];
				float firstPercent = result.get(firstZone);
				assertEquals("ID",11, firstZone.getZoneID());
				assertTrue(Float.compare(100, firstPercent)==0);

				//trying with filter min=5
				try {
					result = TrackAnalysis.CalculatePercentages(null, 5, null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				
				assertTrue(result.isEmpty());
				
				//trying with filter min=2
				try {
					result = TrackAnalysis.CalculatePercentages(null, 2, null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				firstZone = (Zone)result.keySet().toArray()[0];
				firstPercent = result.get(firstZone);
				assertEquals("ID",11, firstZone.getZoneID());
				assertTrue(Float.compare(50, firstPercent)==0);
				
	}

}
