package com.sarafinmahtab.tripbd;

/**
 * Created by Arafin on 7/24/2017.
 */

class Place {
    private String pinPointID, pinPointName, ppBanglaName, 	detailsLink, centrePointID, latLongID;

    Place(String pinPointID, String pinPointName, String ppBanglaName, String detailsLink, String centrePointID, String latLongID) {
        this.pinPointID = pinPointID;
        this.pinPointName = pinPointName;
        this.ppBanglaName = ppBanglaName;
        this.detailsLink = detailsLink;
        this.centrePointID = centrePointID;
        this.latLongID = latLongID;
    }

    String getPinPointID() {
        return pinPointID;
    }

    String getPinPointName() {
        return pinPointName;
    }

    String getPpBanglaName() {
        return ppBanglaName;
    }

    String getDetailsLink() {
        return detailsLink;
    }

    String getCentrePointID() {
        return centrePointID;
    }

    String getLatLongID() {
        return latLongID;
    }
}
