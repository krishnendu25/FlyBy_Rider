package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flyby_riders.R;
import com.flyby_riders.Ui.Adapter.Ride.Contact_Adapter;
import com.flyby_riders.Ui.Adapter.Ride.FlyBy_Contact_Adapter;
import com.flyby_riders.Ui.Listener.onClick;
import com.flyby_riders.Ui.Model.Contact_Model;
import com.flyby_riders.Ui.Model.FlyBy_Contact_Model;
import com.flyby_riders.Utils.ListUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invite_new_member extends BaseActivity implements onClick {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 546;
    @BindView(R.id.Back_Btn)
    RelativeLayout BackBtn;
    @BindView(R.id.search_view)
    EditText searchView;
    @BindView(R.id.search_container)
    LinearLayout searchContainer;
    List<Contact_Model> local_contact = new ArrayList<>();
    List<FlyBy_Contact_Model> FlyBy_local_contact = new ArrayList<>();
    Contact_Adapter contact_adapter;
    FlyBy_Contact_Adapter flyBy_contact_adapter;
    @BindView(R.id.Flyby_user)
    ListView FlybyUser;
    @BindView(R.id.Not_Flyby_user)
    ListView NotFlybyUser;
    private ArrayList<FlyBy_Contact_Model> filteredDataList;
    private ArrayList<Contact_Model> filteredDataList_Contact_Model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_new_member);
        ButterKnife.bind(this);
        Instantiation();
        Hit_Get_FlyBy_User();
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flyBy_contact_adapter!=null && contact_adapter!=null )
                {flyBy_contact_adapter.setFilter( F_filter(FlyBy_local_contact, s.toString()));
                    contact_adapter.setFilter( filter(local_contact, s.toString())); }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private List<FlyBy_Contact_Model> F_filter(List<FlyBy_Contact_Model> dataList, String newText) {
        newText=newText.toLowerCase();
        String text = null;
        filteredDataList=new ArrayList<FlyBy_Contact_Model>();
        for(FlyBy_Contact_Model dataFromDataList:dataList){

            if (newText.matches("^[0-9]*$"))
            {
                text= dataFromDataList.getContact_Number().toLowerCase();
            }else
            {
                text= dataFromDataList.getContact_Name().toLowerCase();
            }
            if(text.toLowerCase().contains(newText)){
                filteredDataList.add(dataFromDataList);
            }
        }

        return filteredDataList;
    }
    private List<Contact_Model> filter(List<Contact_Model> dataList, String newText) {
        newText=newText.toLowerCase();
        String text = null;
        filteredDataList_Contact_Model =new ArrayList<Contact_Model>();
        for(Contact_Model dataFromDataList:dataList){

            if (newText.matches("^[0-9]*$"))
            {
                text= dataFromDataList.getContact_Number().toLowerCase();
            }else
            {
                text= dataFromDataList.getContact_Name().toLowerCase();
            }
            if(text.toLowerCase().contains(newText)){
                filteredDataList_Contact_Model.add(dataFromDataList);
            }
        }

        return filteredDataList_Contact_Model;
    }


    private void Hit_Get_FlyBy_User() {
        show_ProgressDialog();
        Call<ResponseBody> requestCall = retrofitCallback.GetAllUSER();

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
                            FlyBy_local_contact.clear();
                            JSONArray jsonArray_ALLMODEL = jsonObject.getJSONArray("RIDERDETAILS");
                            for (int i = 0; i < jsonArray_ALLMODEL.length(); i++) {
                                FlyBy_Contact_Model FB = new FlyBy_Contact_Model();
                                FB.setUser_id(jsonArray_ALLMODEL.getJSONObject(i).getString("USERID").toString());
                                FB.setContact_Number(jsonArray_ALLMODEL.getJSONObject(i).getString("PHONE").toString());
                                FlyBy_local_contact.add(FB);
                            }
                            requestContactPermission();
                        } else {
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
                hide_ProgressDialog();
            }
        });
    }

    private void Filter_Contact() {
        List<FlyBy_Contact_Model> FlyBy_local_contact_temp = new ArrayList<>();
        for (int i = 0; i < local_contact.size(); i++) {
            for (int j = 0; j < FlyBy_local_contact.size(); j++) {
                if (local_contact.get(i).getContact_Number().equalsIgnoreCase(FlyBy_local_contact.get(j).getContact_Number())) {
                    FlyBy_local_contact.get(j).setContact_Name(local_contact.get(i).getContact_Name());
                    local_contact.remove(i);
                }
            }
        }

        for (int i = 0; i < FlyBy_local_contact.size(); i++) {
            if (!FlyBy_local_contact.get(i).getContact_Name().equalsIgnoreCase("null")) {
                FlyBy_local_contact_temp.add(FlyBy_local_contact.get(i));
            }
        }
        FlyBy_local_contact.clear();

        FlyBy_local_contact = FlyBy_local_contact_temp;


    }

    private void Instantiation() {

    }

    @OnClick(R.id.Back_Btn)
    public void onViewClicked() {
        finish();

    }

    @Override
    public void onClick(String Value) {
        Intent intent = new Intent();
        intent.putExtra("Rider_Id", FlyBy_local_contact.get(Integer.valueOf(Value)).getUser_id());
        intent.putExtra("Rider_Name", FlyBy_local_contact.get(Integer.valueOf(Value)).getContact_Name());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLongClick(String Value) {

    }

    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            show_ProgressDialog();
        }

        protected String doInBackground(Void... arg0) {
            local_contact.clear();
            local_contact = getContactsForm_Phone(Invite_new_member.this);
            Filter_Contact();
            return "You are at PostExecute";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            contact_adapter = new Contact_Adapter(Invite_new_member.this, local_contact);
            NotFlybyUser.setAdapter(contact_adapter);
            flyBy_contact_adapter = new FlyBy_Contact_Adapter(Invite_new_member.this, FlyBy_local_contact);
            FlybyUser.setAdapter(flyBy_contact_adapter);
            ListUtils.setDynamicHeight(FlybyUser);
            ListUtils.setDynamicHeight(NotFlybyUser);
            setListViewHeightBasedOnItems(FlybyUser);
            setListViewHeightBasedOnItems(NotFlybyUser);
            hide_ProgressDialog();

        }
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    requestPermissions(
                            new String[]
                                    {android.Manifest.permission.READ_CONTACTS}
                            , PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                new TestAsync().execute();
            }
        } else {
            new TestAsync().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new TestAsync().execute();
                } else {
                    requestContactPermission();
                }
                return;
            }
        }
    }

}