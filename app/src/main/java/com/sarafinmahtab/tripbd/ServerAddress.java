package com.sarafinmahtab.tripbd;

/**
 * Created by Arafin on 9/10/2017.
 */

public class ServerAddress {
    private static String myServerAddress = "http://192.168.43.65/TripBD/";

    public static void setMyServerAddress(String myServerAddress) {
        ServerAddress.myServerAddress = myServerAddress;
    }

    public static String getMyServerAddress() {
        return myServerAddress;
    }
}
