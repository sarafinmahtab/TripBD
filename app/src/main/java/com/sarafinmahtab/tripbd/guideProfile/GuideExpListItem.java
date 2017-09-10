package com.sarafinmahtab.tripbd.guideProfile;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.sarafinmahtab.tripbd.R;

/**
 * Created by Arafin on 8/19/2017.
 */

public class GuideExpListItem {

    private String guideExpPlaceID, guideExpPlaceName, guideExpPlaceCentre, guideExpPlaceDetailLink, guideExpItemName;
    private int confirmImageID;

    public GuideExpListItem(String guideExpPlaceID, String guideExpPlaceName, String guideExpPlaceCentre, String guideExpPlaceDetailLink) {
        this.guideExpPlaceID = guideExpPlaceID;
        this.guideExpPlaceName = guideExpPlaceName;
        this.guideExpPlaceCentre = guideExpPlaceCentre;
        this.guideExpPlaceDetailLink = guideExpPlaceDetailLink;
        guideExpItemName = guideExpPlaceName + ", " + guideExpPlaceCentre;
        confirmImageID = R.drawable.ic_exp_choice_add_btn;
    }

    //SETTERS
    public void setGuideExpPlaceID(String guideExpPlaceID) {
        this.guideExpPlaceID = guideExpPlaceID;
    }

    public void setGuideExpPlaceName(String guideExpPlaceName) {
        this.guideExpPlaceName = guideExpPlaceName;
    }

    public void setGuideExpPlaceCentre(String guideExpPlaceCentre) {
        this.guideExpPlaceCentre = guideExpPlaceCentre;
    }

    public void setGuideExpPlaceDetailLink(String guideExpPlaceDetailLink) {
        this.guideExpPlaceDetailLink = guideExpPlaceDetailLink;
    }

    public void setGuideExpItemName(String guideExpItemName) {
        this.guideExpItemName = guideExpItemName;
    }

    public void setConfirmImageID(int confirmImageID) {
        this.confirmImageID = confirmImageID;
    }

    //GETTERS
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

    public String getGuideExpItemName() {
        return guideExpItemName;
    }

    public int getConfirmImageID() {
        return confirmImageID;
    }
}
