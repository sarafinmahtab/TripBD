package com.sarafinmahtab.tripbd.guideList;

/**
 * Created by Arafin on 8/20/2017.
 */

public class GuideData {

    private String guideID, guideNickName, guideMobile, guideEmail;

    public GuideData(String guideID, String guideNickName, String guideMobile, String guideEmail) {
        this.guideID = guideID;
        this.guideNickName = guideNickName;
        this.guideMobile = guideMobile;
        this.guideEmail = guideEmail;
    }

    //SETTERS
    public void setGuideID(String guideID) {
        this.guideID = guideID;
    }

    public void setGuideNickName(String guideNickName) {
        this.guideNickName = guideNickName;
    }

    public void setGuideMobile(String guideMobile) {
        this.guideMobile = guideMobile;
    }

    public void setGuideEmail(String guideEmail) {
        this.guideEmail = guideEmail;
    }


    //GETTERS
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
