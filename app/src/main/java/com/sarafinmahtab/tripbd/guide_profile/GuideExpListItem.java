package com.sarafinmahtab.tripbd.guide_profile;

/**
 * Created by Arafin on 8/19/2017.
 */

public class GuideExpListItem {

    String guideExpPlaceID, guideExpPlaceName, guideExpPlaceCentre, guideExpPlaceDetailLink;

    public GuideExpListItem(String guideExpPlaceID, String guideExpPlaceName, String guideExpPlaceCentre, String guideExpPlaceDetailLink) {
        this.guideExpPlaceID = guideExpPlaceID;
        this.guideExpPlaceName = guideExpPlaceName;
        this.guideExpPlaceCentre = guideExpPlaceCentre;
        this.guideExpPlaceDetailLink = guideExpPlaceDetailLink;
    }

    public String getGuideExpPlaceID() {
        return guideExpPlaceID;
    }

    public String getGuideExpPlaceName() {
        return guideExpPlaceName;
    }

    public String getGuideExpPlaceCentre() {
        return guideExpPlaceCentre;
    }

    public String getGuideExpPlaceDetailLink() {
        return guideExpPlaceDetailLink;
    }
}
