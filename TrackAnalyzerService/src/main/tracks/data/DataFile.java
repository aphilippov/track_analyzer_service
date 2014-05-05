package tracks.data;

public class DataFile {
	//the location of the .csv file
	private static String file="/Users/anton/Downloads/tracks.csv";
	private static String zones="/Users/anton/Downloads/zones.csv";
	
	public static String getDataFile(){
		return file;
	}
	
	public static void setDataFile(String newFile){
		file = newFile;
	}

	/**
	 * @return the zones
	 */
	public static String getZonesFile() {
		return zones;
	}

	/**
	 * @param zones - the zones to set
	 */
	public static void setZonesFile(String zones) {
		DataFile.zones = zones;
	}
}
