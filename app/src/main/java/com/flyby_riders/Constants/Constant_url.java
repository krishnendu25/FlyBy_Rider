package com.flyby_riders.Constants;

import com.flyby_riders.Ui.Model.MyLocation_Model;

public class Constant_url {


    public static String BASE_URL = "http://flybyapp.in/flybyapp/RiderApi/";
    public static String ADD_POST = "http://flybyapp.com/flybyapp/StoreApi/add_post";
    public static String ADD_POST_Modifiy = "http://flybyapp.com/flybyapp/StoreApi/add_post_modify";
    public static String  GetMyLocationIP (String IPaddress)
    {return "http://ip-api.com/json/"+IPaddress; }

    public static String DirectionApi(double StartLat,double StartLong,double EndLat,double EndLong)
    {
        return " https://maps.googleapis.com/maps/api/directions/json?origin="+StartLat+","+StartLong+"&destination="+EndLat+","+EndLong+"&mode=directionMode&key=AIzaSyB7kAsF427wwIhs1ZPFChtlbOt5UnyA9Yo";
    }



}