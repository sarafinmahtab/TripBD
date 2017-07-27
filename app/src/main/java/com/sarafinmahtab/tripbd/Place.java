package com.sarafinmahtab.tripbd;

/**
 * Created by Arafin on 7/24/2017.
 */

class Place {
    private String centrePointID, centrePointName;

    Place(String centrePointID, String centrePointName) {
        this.centrePointID = centrePointID;
        this.centrePointName = centrePointName;
    }

    String getCentrePointID() {
        return this.centrePointID;
    }

    String getCentrePointName() {
        return centrePointName;
    }
}
