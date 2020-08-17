package com.flyby_riders.Retrofit;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitCallback {



    @FormUrlEncoded
    @POST("getRiderLogin")
    Call<ResponseBody> RiderLogin(@Field("mobile_no") String contact);

    @FormUrlEncoded
    @POST("validate_otp")
    Call<ResponseBody> validate_otp(@Field("mobile_no") String mobile_no,
                                    @Field("otp") String otp,
                                    @Field("device_id") String device_id);

    @GET("Fetchbikebrand")
    Call<ResponseBody> GetAllBrand();
    @GET("fetch_all_app_user")
    Call<ResponseBody> GetAllUSER();

    @FormUrlEncoded
    @POST("Fetchbikemodel")
    Call<ResponseBody> Fetchbikemodel(@Field("bike_brand_id") String bike_brand_id);


    @FormUrlEncoded
    @POST("add_bike_rider_profile")
    Call<ResponseBody> Add_Bike_To_Profile(@Field("userid") String userid,
                                    @Field("bike_model_id") String bike_model_id,
                                    @Field("bike_brand_id") String bike_brand_id);

    @FormUrlEncoded
    @POST("fetch_rider_detils")
    Call<ResponseBody> USER_DETAILS(@Field("userid") String userid);

    @POST("add_bike_document_album")
    Call<ResponseBody> uploadMultiFile(@Body RequestBody file);

    @POST("update_bike_document_album")
    Call<ResponseBody> updateuploadMultiFile(@Body RequestBody file);

    @FormUrlEncoded
    @POST("get_document_album")
    Call<ResponseBody> Fetch_Album(@Field("userid") String userid,
                                    @Field("my_bike_id") String my_bike_id);

    @FormUrlEncoded
    @POST("deldocumentalbum")
    Call<ResponseBody> Delete_Album(@Field("userid") String userid,
                                   @Field("album_id") String album_id);


    @FormUrlEncoded
    @POST("add_memeber_toride")
    Call<ResponseBody> add_memeber_toride(@Field("admin_userid") String admin_userid,
                                    @Field("Ride_id") String Ride_id,
                                    @Field("list_of_member_userid") String list_of_member_userid,
                                          @Field("Full_Name") String Full_Name    );

    @FormUrlEncoded
    @POST("fetch_member_to_ride")
    Call<ResponseBody> fetch_member_to_ride(@Field("Ride_id") String admin_userid);



    @FormUrlEncoded
    @POST("create_bike_ride")
    Call<ResponseBody> create_bike_ride(@Field("admin_user_id") String admin_user_id,
                                    @Field("ride_name") String ride_name,
                                        @Field("start_location_address") String start_location_address,
                                        @Field("start_location_latitude") String start_location_latitude,
                                        @Field("start_location_longitude") String start_location_longitude);

    @FormUrlEncoded
    @POST("fetch_my_ride")
    Call<ResponseBody> fetch_fetch_my_ride(@Field("userid") String userid);

    @POST("upload_ride_media_file")
    Call<ResponseBody> updateuploadupload_ride_media_file(@Body RequestBody file);
    @FormUrlEncoded
    @POST("fetch_ride_album")
    Call<ResponseBody> fetch_ride_album(@Field("Ride_id") String admin_userid);


    @FormUrlEncoded
    @POST("buy_subcription_plan")
    Call<ResponseBody> buy_subcription_plan(
            @Field("Userid") String Userid,
                                        @Field("plan_name") String plan_name,
                                        @Field("plan_id") String plan_id,
                                        @Field("payment_amount") String payment_amount,
                                        @Field("marchent_name") String marchent_name,
                                        @Field("payment_id") String payment_id,
                                        @Field("payment_ref_no") String payment_ref_no,
                                        @Field("order_id") String order_id);

   //--------------------------------------------------------------------------------------------//
    @FormUrlEncoded
    @POST("fetch_all_advertise")
    Call<ResponseBody> fetch_all_advertise(@Field("bike_model_id") String bike_model_id,
                                           @Field("bike_brand_id") String bike_brand_id,
                                           @Field("city") String city);


    @FormUrlEncoded
    @POST("click_advertise")
    Call<ResponseBody> click_advertise(@Field("userid") String userid,
                                       @Field("advertisement_id") String advertisement_id);



    @FormUrlEncoded
    @POST("location_tracker_start")
    Call<ResponseBody> START_RIDE(
            @Field("Ride_id") String Ride_Id,
            @Field("Admin_id") String Admin_id,
            @Field("starttime") String starttime,
            @Field("start_location_address") String start_location_address,
            @Field("start_location_latitude") String start_location_latitude,
            @Field("start_location_longitude") String start_location_longitude,
            @Field("Ride_status") String Ride_status);

    @FormUrlEncoded
    @POST("location_tracker_end")
    Call<ResponseBody> END_RIDE(
            @Field("Ride_id") String Ride_Id,
            @Field("Admin_id") String Admin_id,
            @Field("Endtime") String Endtime,
            @Field("end_location_address") String end_location_address,
            @Field("end_location_latitude") String end_location_latitude,
            @Field("end_location_longitude") String end_location_longitude,
            @Field("Ride_status") String Ride_status);


    @FormUrlEncoded
    @POST("location_tracker")
    Call<ResponseBody> location_tracker(@Field("longitude") String longitude,
                                       @Field("latitude") String latitude,
                                        @Field("Ride_id") String Ride_id,
                                        @Field("member_id") String member_id,
                                        @Field("spend_timestamp") String spend_timestamp);



    @FormUrlEncoded
    @POST("fetch_location_tracker")
    Call<ResponseBody> fetch_location_tracker(@Field("Ride_id") String Ride_id,
                                        @Field("member_id") String member_id);


    @FormUrlEncoded
    @POST("fetch_location_tracker")
    Call<ResponseBody> fetch_location_tracker(@Field("Ride_id") String Ride_id);


    @GET("Get_all_garageowner")
    Call<ResponseBody> Get_all_garageowner();

    @GET("http://flybyapp.com/flybyapp/StoreApi/getAllServices")
    Call<ResponseBody> getAllServices();

    @FormUrlEncoded
    @POST("http://flybyapp.com/flybyapp/StoreApi/ad_post_fetch")
    Call<ResponseBody> ad_post_fetch(@Field("Userid") String Userid);


    @FormUrlEncoded
    @POST("http://flybyapp.com/flybyapp/StoreApi/Getgarageownerdetails")
    Call<ResponseBody> Getgarageownerdetails(@Field("user_id") String user_id);



}
