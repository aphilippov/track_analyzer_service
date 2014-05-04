package tracks.main;

import java.io.IOException;
import java.sql.SQLException;
//import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.*;
import tracks.types.*;

public class StarterFast {

	//
	// public enum VisitType{
	// BASKET("BASKET"),
	// CART("CART")
	// }
	//
	// the location of the database file
	static String TRACKS_FILE = "/Users/anton/Downloads/tracks.csv";
	public static boolean ASC = true;
	public static boolean DESC = false;

	private static ArrayList<ZoneVisit> parseZoneVisits(String zoneVisit) {

		String[] zoneVisitList = zoneVisit.split("\\|");

		ArrayList<ZoneVisit> zoneVisits = new ArrayList<ZoneVisit>();

		String txt = zoneVisitList[0];

		String re1 = "(\\d+)"; // Integer Number 1
		String re2 = "\\("; // Any Single Character 1
		String re3 = "(.*?)"; // Non-greedy match on filler
		String re4 = ";"; // Any Single Character 2
		String re5 = "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])"; // Float 1
		String re6 = "\\)"; // Any Single Character 3

		String re = re1 + re2 + re3 + re4 + re5 + re6;

		Pattern p = Pattern.compile(re, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		Matcher m = p.matcher(txt);
		if (m.find()) {
			String int1 = m.group(1);
			String c1 = m.group(2);
			String float1 = m.group(3);
			float fl = Float.parseFloat(float1);
			short shrt = Short.parseShort(int1);
			zoneVisits.add(new ZoneVisit(shrt, c1, fl));
		}

		for (int i = 1; i < zoneVisitList.length; i++) {
			m = p.matcher(zoneVisitList[i]);
			if (m.find()) {
				String int1 = m.group(1);
				String c1 = m.group(2);
				String float1 = m.group(3);
				float fl = Float.parseFloat(float1);
				// System.out.println(fl);

				short shrt = Short.parseShort(int1);

				ZoneVisit lastZoneVisit = zoneVisits.get(zoneVisits.size() - 1);

				if (shrt != lastZoneVisit.getZoneID())
					zoneVisits.add(new ZoneVisit(shrt, c1, fl));
				else
					lastZoneVisit.setDuration(lastZoneVisit.getDuration() + fl);
			}
		}

		return zoneVisits;
	}

	/**
	 * @param args
	 */
	public static Map<Zone,Float> CalculatePercentages() throws SQLException {
		
		String inputDate = "2014-04-230";

		float filterTime = 30;
		Date filterDate = parseInputDate(inputDate);
		String filterType = "BASKET";

		String[] line = new String[4];

		final CSV csv = CSV.create();

		ArrayList<ZoneVisit> zoneVisits;

		CSVReader reader = csv.reader(TRACKS_FILE);

		// reading the first line with headers
		// do nothing
		try {
			line = reader.readNext();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// assuming we don't know the zones
		// if we know them beforehand -> use array instead of hashtable
		//
		// The structure is: Zone (ID, Description) -> VisitorsCount,
		// LastVisitor
		// LastVisitor is a temporary value of the last processed track counted
		// for this zone
		// in order to avoid counting the same track for the zone twice or more
		HashMap<Zone, int[]> visitCount = new HashMap<Zone, int[]>();

		double trackID;
		String trackType;
		Date trackBegin;
		Date trackEnd;
		int totalVisitors = 0;

		try {
			while ((line = reader.readNext()) != null) {
				if (line.length == 5) {
					trackID = Double.parseDouble(line[0]);
					trackType = line[1];

					// convert unix time to the Date
					trackBegin = convertToDate(line[2]);
					trackEnd = convertToDate(line[3]);

					// apply all the filters and then parse the zone visits
					zoneVisits = parseZoneVisits(line[4]);

				} else {
					throw new SQLException(
							"The .CSV file provided is not of the right format");
				}

				//visitCount = addTrackToCount(visitCount, zoneVisits, trackID);
				visitCount = addTrackToCount(visitCount, zoneVisits, trackID,
						trackType, trackBegin, trackEnd, filterType,
						filterDate, filterTime);

				totalVisitors++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LinkedHashMap<Zone, int[]> sortedVisits = sortVisitsMap(visitCount,
				DESC);

		LinkedHashMap<Zone, Float> percentZones = calculatePecentage(
				sortedVisits, totalVisitors);
		
		return percentZones;

//		Set<Zone> zones = percentZones.keySet();
//		for (Zone z : zones) {
//			System.out.println(Short.toString(z.getZoneID()) + "-"
//					+ z.getZoneDescription() + " "
//					+ String.format("%.2f", percentZones.get(z)) + "%");
//		}
	}

	private static Date parseInputDate(String inputDate) {
	      SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
	      ft.setLenient(false);

	      Date newDate = null;
	      
	      try { 
	          newDate = ft.parse(inputDate); 
	          //System.out.println(t); 
	      } catch (ParseException e) {
	    	  e.printStackTrace();
	      }
	      
		
		return newDate;
	}

	private static HashMap<Zone, int[]> addTrackToCount(
			HashMap<Zone, int[]> hashMap, ArrayList<ZoneVisit> zoneVisits,
			double trackID, String trackType, Date trackBegin,
			Date trackEnd, String filterType, Date filterDate,
			float filterTime) {

		// if filter is present, but the track does not satisfy it,
		// skip the track
		if ((filterType != null) && (!filterType.equals(trackType)))
			return hashMap;
		else if ((filterDate != null) && (!isSameDay(filterDate, trackBegin)))
			return hashMap;
		else if (filterTime > 0) {
			// counting zones in a single track
			for (int i = 0; i < zoneVisits.size(); i++) {
				ZoneVisit visit = zoneVisits.get(i);
				// if (Float.compare(visit.getDuration(), filterTime))
				if (visit.getDuration() >= filterTime) {

					Zone keyZone = new Zone(visit.getZoneID(),
							visit.getZoneName());
					if (hashMap.containsKey(keyZone)) {
						// the zone is already in the list
						// check if this track is already counted or not

						int[] countZone = hashMap.get(keyZone);

						// if we haven't counted this track yet for this zone,
						// increase the counter and note the last counted track
						// for this zone
						// (in order to not count the same track twice)
						if (countZone[1] != trackID) {
							countZone[0]++;
							countZone[1] = (int) trackID;
						}
					} else {
						// this is a new zone, add it to the list count
						hashMap.put(keyZone, new int[] { 1, (int) trackID });
					}
				}
			}
			return hashMap;

		} else {
			// counting zones in a single track
			for (int i = 0; i < zoneVisits.size(); i++) {
				ZoneVisit visit = zoneVisits.get(i);
				Zone keyZone = new Zone(visit.getZoneID(), visit.getZoneName());
				if (hashMap.containsKey(keyZone)) {
					// the zone is already in the list
					// check if this track is already counted or not

					int[] countZone = hashMap.get(keyZone);

					// if we haven't counted this track yet for this zone,
					// increase the counter and note the last counted track for
					// this zone
					// (in order to not count the same track twice)
					if (countZone[1] != trackID) {
						countZone[0]++;
						countZone[1] = (int) trackID;
					}
				} else {
					// this is a new zone, add it to the list count
					hashMap.put(keyZone, new int[] { 1, (int) trackID });
				}
			}
			return hashMap;

		}

	}

	private static boolean isSameDay(Date date1, Date date2) {
		
		if ((date1 == null ) || (date2 == null)){
			throw new NullPointerException("the date is null");
		}
		
		 Calendar cal1 = Calendar.getInstance();
		 Calendar cal2 = Calendar.getInstance();
		 cal1.setTime(date1);
		 cal2.setTime(date2);
		 boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
		 &&
		 cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		
		 return sameDay;
//		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
//		return fmt.format(date1).equals(fmt.format(date2));

	}

	private static LinkedHashMap<Zone, Float> calculatePecentage(
			Map<Zone, int[]> sortedVisits, int totalVisitors) {

		LinkedHashMap<Zone, Float> returnMap = new LinkedHashMap<Zone, Float>(
				sortedVisits.size());
		for (Zone z : sortedVisits.keySet()) {
			Float percentage = (float) sortedVisits.get(z)[0] * 100
					/ totalVisitors;
			returnMap.put(z, percentage);
		}
		return returnMap;
	}

	private static LinkedHashMap<Zone, int[]> sortVisitsMap(
			HashMap<Zone, int[]> unsortedMap, boolean sortAsc) {

		List<Entry<Zone, int[]>> list = new LinkedList<Entry<Zone, int[]>>(
				unsortedMap.entrySet());

		if (sortAsc)
			Collections.sort(list, new VisitsComparatorAsc());
		else
			Collections.sort(list, new VisitsComparatorDesc());

		LinkedHashMap<Zone, int[]> sortedMap = new LinkedHashMap<Zone, int[]>();
		for (Entry<Zone, int[]> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	static class VisitsComparatorAsc implements Comparator<Entry<Zone, int[]>> {
		@Override
		public int compare(Entry<Zone, int[]> entry1, Entry<Zone, int[]> entry2) {
			return entry1.getValue()[0] - entry2.getValue()[0];
			// return 0;
		}
	}

	static class VisitsComparatorDesc implements Comparator<Entry<Zone, int[]>> {
		@Override
		public int compare(Entry<Zone, int[]> entry1, Entry<Zone, int[]> entry2) {
			return entry2.getValue()[0] - entry1.getValue()[0];
		}
	}

	private static HashMap<Zone, int[]> addTrackToCount(
			HashMap<Zone, int[]> hashMap, ArrayList<ZoneVisit> zoneVisits,
			double trackID) {

		// counting zones in a single track
		for (int i = 0; i < zoneVisits.size(); i++) {
			ZoneVisit visit = zoneVisits.get(i);
			Zone keyZone = new Zone(visit.getZoneID(), visit.getZoneName());
			if (hashMap.containsKey(keyZone)) {
				// the zone is already in the list
				// check if this track is already counted or not

				int[] countZone = hashMap.get(keyZone);

				// if we haven't counted this track yet for this zone,
				// increase the counter and note the last counted track for this
				// zone
				// (in order to not count the same track twice)
				if (countZone[1] != trackID) {
					countZone[0]++;
					countZone[1] = (int) trackID;
				}
			} else {
				// this is a new zone, add it to the list count
				hashMap.put(keyZone, new int[] { 1, (int) trackID });
			}
		}
		return hashMap;
	}

	// convert unix time to the Date
	private static Date convertToDate(String string) {
		return new Date(Long.parseLong(string));
	}

}
