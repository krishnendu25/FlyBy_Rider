package com.flyby_riders.Ui.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KRISHNENDU MANNA on 27,September,2020
 */
public class TeamMateLocation {
    String UserID , Name;
    LatLng location;

    public TeamMateLocation() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
