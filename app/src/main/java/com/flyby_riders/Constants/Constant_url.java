package com.flyby_riders.Constants;

import com.flyby_riders.Ui.Model.MyLocation_Model;

public class Constant_url {


    public static String BASE_URL = "http://flybyapp.com/flybyapp/RiderApi/";
    public static String ADD_POST = "http://flybyapp.com/flybyapp/StoreApi/add_post";
    public static String ADD_POST_Modifiy = "http://flybyapp.com/flybyapp/StoreApi/add_post_modify";
    public static String  GetMyLocationIP (String IPaddress)
    {return "http://ip-api.com/json/"+IPaddress; }


}