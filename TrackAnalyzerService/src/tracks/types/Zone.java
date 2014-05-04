package tracks.types;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement()
public class Zone {
	@XmlElement
	private short zoneID;
	@XmlElement
	private String zoneDescription;
	
	public Zone(short id, String descr) {
		zoneID = id;
		zoneDescription = descr;
	}
	/**
	 * @return the zoneID
	 */
	public short getZoneID() {
		return zoneID;
	}
	/**
	 * @param zoneID the zoneID to set
	 */
	public void setZoneID(short zoneID) {
		this.zoneID = zoneID;
	}
	/**
	 * @return the zoneDescription
	 */
	public String getZoneDescription() {
		return zoneDescription;
	}
	/**
	 * @param zoneDescription the zoneDescription to set
	 */
	public void setZoneDescription(String zoneDescription) {
		this.zoneDescription = zoneDescription;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + zoneID;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Zone other = (Zone) obj;
		if (zoneID != other.zoneID)
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + zoneID + ", "
				+ zoneDescription + "]";
	}

}
