package com.flyby_riders.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.flyby_riders.Constants.Constant;
import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Ui.Adapter.Garage.DeleteMyBike;
import com.flyby_riders.Ui.Adapter.Garage.MyBikeMannageAdapter;
import com.flyby_riders.Ui.Model.My_Bike_Model;
import com.flyby_riders.Utils.BaseActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flyby_riders.Constants.StringUtils.BASIC;
import static com.flyby_riders.Constants.StringUtils.PREMIUM;
import static com.flyby_riders.Constants.StringUtils.WhatsappNumberBus;

public class MyAccount extends BaseActivity implements DeleteMyBike {

    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;

    @BindView(R.id.categoryTitle)
    TextView categoryTitle;
    @BindView(R.id.myPhoneNoTV)
    TextView myPhoneNoTV;
    @BindView(R.id.planIndicatorTV)
    TextView planIndicatorTV;
    @BindView(R.id.basicAccountPointsView)
    LinearLayout basicAccountPointsView;
    @BindView(R.id.premiumAccountPointsView)
    LinearLayout premiumAccountPointsView;
    @BindView(R.id.callBTN)
    LinearLayout callBTN;
    @BindView(R.id.whatsappBTN)
    LinearLayout whatsappBTN;
    @BindView(R.id.rateMyAppBTN)
    LinearLayout rateMyAppBTN;
    @BindView(R.id.logoutBTN)
    LinearLayout logoutBTN;
    Context mContext;
    Activity mActivity;
    ArrayList<My_Bike_Model> My_Bike_els = new ArrayList<>();
    @BindView(R.id.viewPremiumPlan)
    TextView viewPremiumPlan;
    @BindView(R.id.NestedScrollView_view)
    NestedScrollView NestedScrollViewView;
    MyBikeMannageAdapter myBikeMannageAdapter;
    @BindView(R.id.myBikeList)
    RecyclerView myBikeList;
    @BindView(R.id.empty_view)
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        iniView();
    }

    private void iniView() {
        mContext = getApplicationContext();
        mActivity = MyAccount.this;
        categoryTitle.setText("My Account");
        myPhoneNoTV.setText(new Prefe(this).getUserPhoneNO());
        Set_LayoutManager(myBikeList, false, true);
    }

    @OnClick({R.id.Back_Btn, R.id.Back_, R.id.callBTN, R.id.whatsappBTN, R.id.rateMyAppBTN, R.id.logoutBTN, R.id.viewPremiumPlan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Back_Btn:
                Intent intentt = new Intent(this, DashBoard.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentt);
                break;
            case R.id.Back_:
                Intent intent = new Intent(this, DashBoard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.callBTN:
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    phoneCall();
                } else {
                    final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
                }
                break;
            case R.id.whatsappBTN:
                openWhatsApp(WhatsappNumberBus, "");
                break;
            case R.id.rateMyAppBTN:
                Constant.showRateDialog(mActivity);
                break;
            case R.id.logoutBTN:
                new Prefe(mContext).logOut(mActivity);
                break;
            case R.id.viewPremiumPlan:
                Intent i = new Intent(getApplicationContext(), UpgradeAccountPlan.class);
                startActivity(i);
                break;
        }
    }

    private void openWhatsApp(String numero, String mensaje) {

        try {
            PackageManager packageManager = this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {
                Constant.Show_Tos(getApplicationContext(), "Whatsapp Application Not Found");
            }
        } catch (Exception e) {
            Log.e("ERROR WHATSAPP", e.toString());
            Constant.Show_Tos(getApplicationContext(), "No not found");
        }

    }
    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:8800278211"));
            this.startActivity(callIntent);
        } else {
            Constant.Show_Tos(getApplicationContext(), "You don't assign permission.");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Hit_Rider_Details(new Prefe(mContext).getUserID());
    }
    private void Hit_Rider_Details(String userid) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.USER_DETAILS(userid);
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
                            JSONArray BIKEBRANDDETAILS = null;
                            try {
                                BIKEBRANDDETAILS = jsonObject.getJSONObject("ALLRIDERDETAILS").getJSONArray("BIKEBRANDDETAILS");
                                Check_Payment(jsonObject.getJSONObject("ALLRIDERDETAILS").getJSONObject("USERDETAILS"));
                            } catch (Exception E) {
                            }

                            if (BIKEBRANDDETAILS != null) {
                                if (BIKEBRANDDETAILS.length() > 0) {
                                    My_Bike_els.clear();

                                    for (int i = 0; i < BIKEBRANDDETAILS.length(); i++) {
                                        My_Bike_Model mb = new My_Bike_Model();
                                        mb.setMY_BIKE_ID(BIKEBRANDDETAILS.getJSONObject(i).getString("MY_BIKE_ID"));
                                        mb.setBRANDID(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDID"));
                                        mb.setBRANDNAME(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDNAME"));
                                        mb.setBRANDPIC(BIKEBRANDDETAILS.getJSONObject(i).getString("BRANDPIC"));
                                        mb.setMODELID(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELID"));
                                        mb.setMODELNAME(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELNAME"));
                                        mb.setMODELPIC(BIKEBRANDDETAILS.getJSONObject(i).getString("MODELPIC"));
                                        My_Bike_els.add(mb);
                                    }
                                    if (My_Bike_els.size() > 0) {
                                        myBikeMannageAdapter = new MyBikeMannageAdapter(My_Bike_els, mActivity);
                                        myBikeList.setAdapter(myBikeMannageAdapter);
                                        emptyView.setVisibility(View.GONE);
                                    }else
                                        emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    Constant.Show_Tos(mContext, "Someting Error..");
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                My_Bike_els.clear();
                                if (myBikeMannageAdapter!=null)
                                    myBikeMannageAdapter.notifyDataSetChanged();

                                Constant.Show_Tos(mContext, "Someting Error..");
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            hide_ProgressDialog();
                            Constant.Show_Tos(mContext, "Someting Error..");
                            emptyView.setVisibility(View.VISIBLE);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Constant.Show_Tos(mContext, "Someting Error..");
                        hide_ProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Show_Tos_Error(mActivity, true, false);
                hide_ProgressDialog();
            }
        });
    }
    private void Check_Payment(JSONObject jsonObject) {

        if (jsonObject.has("PLANNAME")) {
            try {
                if (jsonObject.getString("PLANNAME") != null) {
                    if (jsonObject.getString("PLANNAME").equalsIgnoreCase("")) {
                        new Prefe(mContext).setAccountPlanStatus(BASIC);
                    } else if (jsonObject.getString("PLANNAME").equalsIgnoreCase("null")) {
                        new Prefe(mContext).setAccountPlanStatus(BASIC);
                    } else {
                        if (jsonObject.getString("PLANNAME").equalsIgnoreCase(BASIC)) {
                            new Prefe(mContext).setAccountPlanStatus(BASIC);
                        } else {
                            new Prefe(mContext).setAccountPlanStatus(PREMIUM);
                        }
                    }
                } else {
                    new Prefe(mContext).setAccountPlanStatus(BASIC);
                }
            } catch (Exception e) {
                Constant.Show_Tos_Error(mContext, false, true);
            }
        }

        if (new Prefe(mActivity).getAccountPlanStatus().equalsIgnoreCase(PREMIUM)) {
            basicAccountPointsView.setVisibility(View.GONE);
            viewPremiumPlan.setVisibility(View.GONE);
            premiumAccountPointsView.setVisibility(View.VISIBLE);
            planIndicatorTV.setText("PREMIUM ACCOUNT");
            planIndicatorTV.setBackground(getResources().getDrawable(R.drawable.premium_plan_indicate));
        } else {
            basicAccountPointsView.setVisibility(View.VISIBLE);
            premiumAccountPointsView.setVisibility(View.GONE);
            planIndicatorTV.setText("BASIC ACCOUNT");
            viewPremiumPlan.setVisibility(View.VISIBLE);
            planIndicatorTV.setBackground(getResources().getDrawable(R.drawable.basic_plan_indicate));


        }
    }
    @Override
    public void onClickDeleteMyBike(String bikeID, String bikeName) {
        showBottomSheet(bikeID, bikeName);
    }
    private void showBottomSheet(String bikeID, String bikeName) {

        View dialogView = getLayoutInflater().inflate(R.layout.deletebikealert_bottomsheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setContentView(dialogView);
        TextView infoText = bottomSheetDialog.findViewById(R.id.infoText);
        TextView cancleDialog = bottomSheetDialog.findViewById(R.id.cancleDialog);
        TextView deleteMyBike = bottomSheetDialog.findViewById(R.id.deleteMyBike);
        infoText.setText("Any documents, if added to the bike will also be deleted. Please confirm to delete " + bikeName);
        cancleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
            }
        });
        deleteMyBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
                hitDeleteMyBike(bikeID, new Prefe(mContext).getUserID(),bikeName);
            }
        });

        bottomSheetDialog.show();
    }
    private void hitDeleteMyBike(String bikeID, String userid, String bikeName) {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.deleteMyBike(userid,bikeID);
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
                            Hit_Rider_Details(userid);
                            Constant.Show_Tos(mContext,bikeName+" Deleted Successfully ");
                        }else
                        {Constant.Show_Tos(mContext,"Bike Deletion failed");
                        }
                    } catch (Exception e) {hide_ProgressDialog();}
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {hide_ProgressDialog();}
        });
    }
}