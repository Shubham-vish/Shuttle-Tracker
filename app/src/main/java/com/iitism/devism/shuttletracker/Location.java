package com.iitism.devism.shuttletracker;

/**
 * Created by Abhinav on 09-08-2017.
 */

public class Location {

    double longitude;
    double lattitude;

    public Location(double longi,double latti){
        this.longitude=longi;
        this.lattitude=latti;

    }



    public  double getLongi(){
        return longitude;
    }

    public  double getLatti(){
        return lattitude;
    }
    public float getSpeed(){
        return 1;
    }
}
