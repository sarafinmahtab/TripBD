package com.sarafinmahtab.tripbd;

/**
 * Created by Arafin on 8/5/2017.
 */

public class ExpPlace {
    String expPlaceID, expPlaceName;

    public ExpPlace(String expPlaceID, String expPlaceName) {
        this.expPlaceID = expPlaceID;
        this.expPlaceName = expPlaceName;
    }

    public String getExpPlaceID() {
        return expPlaceID;
    }

    public String getExpPlaceName() {
        return expPlaceName;
    }
}
