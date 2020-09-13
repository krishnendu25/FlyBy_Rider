package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Adapter.Ride.Ride_Members_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Real_Time_Latlong;
import com.flyby_riders.Ui.Model.Ride_Member_model;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Ui.Listener.StringUtils.PREMIUM;

public class Ride_Members_Management extends BaseActivity implements onClick {
    String My_Ride_ID = "",Admin_User_Id="";
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.member_count_tv)
    TextView memberCountTv;
    @BindView(R.id.Buy_Subscption_tv)
    TextView BuySubscptionTv;
    @BindView(R.id.Add_member_btn)
    TextView AddMemberBtn;
    @BindView(R.id.Ride_member_list)
    RecyclerView RideMemberList;
    ArrayList<Ride_Member_model> Member_List = new ArrayList<>();
    Ride_Members_Adapter ride_members_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_members_management);
        ButterKnife.bind(this);
        try {
            My_Ride_ID = getIntent().getStringExtra("My_Ride_ID");
            Admin_User_Id = getIntent().getStringExtra("Admin_User_Id");
        } catch (Exception e) {My_Ride_ID = "0";Admin_User_Id="0";}
        Instantiation();
    }
    private void Instantiation() {
        Set_LayoutManager(RideMemberList,false,true);
        Hit_Fetch_Member(My_Ride_ID);
        BuySubscptionTv.setSelected(true);
    }
    private void Hit_Fetch_Member(String my_ride_id) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.fetch_member_to_ride(my_ride_id);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Member_List.clear();
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                            JSONArray RIDERDETAILS_array = jsonObject.getJSONArray("RIDERDETAILS");
                            JSONArray ADMINDETAILS_array = jsonObject.getJSONArray("ADMINDETAILS");

                            for (int i=0 ; i<RIDERDETAILS_array.length() ; i++) {
                                Ride_Member_model rm = new Ride_Member_model();
                                rm.setPHONE(RIDERDETAILS_array.getJSONObject(i).getString("PHONE"));
                                rm.setUSERID(RIDERDETAILS_array.getJSONObject(i).getString("USERID"));
                                rm.setFULL_NAME(RIDERDETAILS_array.getJSONObject(i).getString("Full_Name"));
                                if (RIDERDETAILS_array.getJSONObject(i).getString("USERID").equalsIgnoreCase(RIDERDETAILS_array.getJSONObject(i).getString("admin_id")))
                                {rm.setAdmin(true);}
                                Member_List.add(rm);
                            }
                            if (new Session(getApplicationContext()).get_MEMBER_STATUS().equalsIgnoreCase(PREMIUM))
                            {memberCountTv.setText(String.valueOf(Member_List.size()) +" of 50 members added");
                            }else
                            {memberCountTv.setText(String.valueOf(Member_List.size()) +" of 5 members added");}
                            ride_members_adapter = new Ride_Members_Adapter(Ride_Members_Management.this,Member_List);
                            RideMemberList.setAdapter(ride_members_adapter);
                        } else {
                            Constant.Show_Tos(getApplicationContext(),"No Members Found");
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        Constant.Show_Tos(getApplicationContext(),"Something Wrong");
                        hide_ProgressDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(getApplicationContext(),true,false);
                hide_ProgressDialog();
            }
        });



    }

    @OnClick({R.id.Back_Btn, R.id.Buy_Subscption_tv, R.id.Add_member_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                finish();
                break;
            case R.id.Buy_Subscption_tv:
                hit_Payment_Bottomsheet();
                break;
            case R.id.Add_member_btn:
                    if (new Session(getApplicationContext()).get_MEMBER_STATUS().equalsIgnoreCase(PREMIUM))
                    {
                        if (Member_List.size()<=50)
                        {
                            Intent intent = new Intent(getApplicationContext(),Invite_new_member.class);
                            startActivityForResult(intent,569);
                        }else{Constant.Show_Tos(getApplicationContext(),"This Ride Reach Maximum Member Number");}
                    }else
                    {
                        if (Member_List.size()<=5)
                        {
                            Intent intent = new Intent(getApplicationContext(),Invite_new_member.class);
                            startActivityForResult(intent,569);
                        }else{Constant.Show_Tos(getApplicationContext(),"This Ride Reach Maximum Member Number");}

                    }
                break;
        }
    }

    private void hit_Payment_Bottomsheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.can_add_members, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dialogView);
        TextView view_plan_details = bottomSheetDialog.findViewById(R.id.view_plan_details);
        view_plan_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Upgrade_To_Premium.class);
                i.putExtra("My_Ride_ID",My_Ride_ID);
                startActivity(i);
                bottomSheetDialog.hide();
            }
        });
        bottomSheetDialog.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 569) {
            if (resultCode == RESULT_OK) {
                String New_Member = data.getStringExtra("Rider_Id");
                String Rider_Name = data.getStringExtra("Rider_Name");
                Hit_Add_New_member(My_Ride_ID,New_Member,Rider_Name);
            }
        }
    }

    private void Hit_Add_New_member(String my_ride_id, String new_member,String Ride_Name)
    {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.add_memeber_toride(new Session(this).get_LOGIN_USER_ID(),my_ride_id,new_member,Ride_Name);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hide_ProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = null;
                        try {
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Constant.Show_Tos(getApplicationContext(),Ride_Name+" added Successfully");
                            Hit_Fetch_Member(My_Ride_ID);
                        } else {
                            Constant.Show_Tos(getApplicationContext(),jsonObject.getString("msg"));
                            hide_ProgressDialog();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hide_ProgressDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_ProgressDialog();Constant.Show_Tos_Error(getApplicationContext(),true,false);

            }
        });

    }


    @Override
    public void onClick(String Value) {
        hit_remove_member(Member_List.get(Integer.valueOf(Value)).getUSERID());
    }

    private void hit_remove_member(String userid)
    {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.remove_member(My_Ride_ID,userid);
        requestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    hide_ProgressDialog();
                    try {
                        JSONObject jsonObject = null;
                        try {
                            String output = Html.fromHtml(response.body().string()).toString();
                            output = output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1);
                            jsonObject = new JSONObject(output);
                        } catch (Exception e) {}
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            Constant.Show_Tos(getApplicationContext(),"Member Removed Successfully");
                            Hit_Fetch_Member(My_Ride_ID);
                        }
                    } catch (Exception e) {hide_ProgressDialog();}
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {hide_ProgressDialog();}
        });
    }

    @Override
    public void onLongClick(String Value) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new Session(getApplicationContext()).get_MEMBER_STATUS().equalsIgnoreCase(PREMIUM))
        { BuySubscptionTv.setVisibility(View.GONE);
        }else
        {BuySubscptionTv.setVisibility(View.VISIBLE);}

        if (new Session(getApplicationContext()).get_MEMBER_STATUS().equalsIgnoreCase(PREMIUM))
        {memberCountTv.setText(String.valueOf(Member_List.size()) +" of 50 members added");
        }else
        {memberCountTv.setText(String.valueOf(Member_List.size()) +" of 5 members added");}
    }
}