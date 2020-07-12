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

}