package com.sarafinmahtab.tripbd;

/**
 * Created by Arafin on 7/24/2017.
 */

public class Place {
    private String pinPointID, pinPointName, latitude, longitude;

    public Place(String pinPointID, String pinPointName, String latitude, String longitude) {
        this.pinPointID = pinPointID;
        this.pinPointName = pinPointName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPinPointID() {
        return pinPointID;
    }

    public String getPinPointName() {
        return this.pinPointName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
