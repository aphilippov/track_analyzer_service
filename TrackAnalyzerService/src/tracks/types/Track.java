package tracks.types;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Track {
	private double trackID;
	private String trackType;
	private Date trackBegin;
	private Date trackEnd;
	private ArrayList<ZoneVisit> zoneVisits;
	/**
	 * @return the trackID
	 */
	public double getTrackID() {
		return trackID;
	}
	/**
	 * @param trackID the trackID to set
	 */
	public void setTrackID(double trackID) {
		this.trackID = trackID;
	}
	/**
	 * @return the trackType
	 */
	public String getTrackType() {
		return trackType;
	}
	/**
	 * @param trackType the trackType to set
	 */
	public void setTrackType(String trackType) {
		this.trackType = trackType;
	}
	/**
	 * @return the trackBegin
	 */
	public Date getTrackBegin() {
		return trackBegin;
	}
	/**
	 * @param trackBegin the trackBegin to set
	 */
	public void setTrackBegin(Timestamp trackBegin) {
		this.trackBegin = trackBegin;
	}
	/**
	 * @return the trackEnd
	 */
	public Date getTrackEnd() {
		return trackEnd;
	}
	/**
	 * @param trackEnd the trackEnd to set
	 */
	public void setTrackEnd(Timestamp trackEnd) {
		this.trackEnd = trackEnd;
	}
	/**
	 * @return the zoneVisits
	 */
	public ArrayList<ZoneVisit> getZoneVisits() {
		return zoneVisits;
	}
	/**
	 * @param zoneVisits the zoneVisits to set
	 */
	public void setZoneVisits(ArrayList<ZoneVisit> zoneVisits) {
		this.zoneVisits = zoneVisits;
	}	
}
