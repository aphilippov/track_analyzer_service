package tracks.types;

public class ZoneVisit {
	private short zoneID;
	private String zoneName;
	private float duration;
	
	public ZoneVisit (short id, String name, float time){
		zoneID = id;
		zoneName = name;
		duration = time;
	}
	
	
	public short getZoneID() {
		return zoneID;
	}
	
	public void setZoneID(short zoneID) {
		this.zoneID = zoneID;
	}
	
	public String getZoneName() {
		return zoneName;
	}
	
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
}
