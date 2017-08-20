package com.sarafinmahtab.tripbd.guide_list_for_hire;

/**
 * Created by Arafin on 8/20/2017.
 */

public class GuideData {

    String guideID, guideNickName, guideMobile, guideEmail;

    public GuideData(String guideID, String guideNickName, String guideMobile, String guideEmail) {
        this.guideID = guideID;
        this.guideNickName = guideNickName;
        this.guideMobile = guideMobile;
        this.guideEmail = guideEmail;
    }

    public String getGuideID() {
        return guideID;
    }

    public String getGuideNickName() {
        return guideNickName;
    }

    public String getGuideMobile() {
        return guideMobile;
    }

    public String getGuideEmail() {
        return guideEmail;
    }
}
