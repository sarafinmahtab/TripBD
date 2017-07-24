package com.sarafinmahtab.tripbd;

/**
 * Created by Arafin on 7/24/2017.
 */

public class Place {
    private String pinPointID, pinPointName;

    public Place(String pinPointID, String pinPointName) {
        this.pinPointID = pinPointID;
        this.pinPointName = pinPointName;
    }

    public String getPinPointID() {
        return pinPointID;
    }

    public String getPinPointName() {
        return this.pinPointName;
    }
}
