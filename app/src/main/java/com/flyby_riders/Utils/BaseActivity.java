package com.flyby_riders.Utils;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.flyby_riders.NetworkOperation.IJSONParseListener;
import com.flyby_riders.R;
import com.flyby_riders.Retrofit.RetrofitCallback;
import com.flyby_riders.Retrofit.RetrofitClient;
import com.flyby_riders.SQLite_DatabaseHelper.TestAdapter;
import com.flyby_riders.Ui.Model.Contact_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements IJSONParseListener {
    AlertDialog alertDialog_loader =null;
    public RetrofitCallback retrofitCallback;
    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };public Prefe mPrefe;
   public TestAdapter testAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loading_page, null);
        dialogBuilder.setView(dialogView);
        //dialogBuilder.setCancelable(false);
        alertDialog_loader = dialogBuilder.create();
        alertDialog_loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        testAdapter = new TestAdapter(this);
        testAdapter.createDatabase();
        testAdapter.open();
        retrofitCallback = RetrofitClient.getRetrofitClient().create(RetrofitCallback.class);
        mPrefe = new Prefe(this);


       /* if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }*/


    }
    public  void show_ProgressDialog()
    {
        try{
            try {
                if (alertDialog_loader !=null)
                {
                    try{
                        if (!isFinishing())
                            alertDialog_loader.show();
                    }catch (Exception e){
                      }

                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = (this).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.loading_page, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);
                    alertDialog_loader = dialogBuilder.create();
                    alertDialog_loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    try{
                        if (!isFinishing())
                            alertDialog_loader.show();
                         }catch (Exception e){}
                }
            } catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        }catch (Exception e)
        {

        }



    }
    public void hide_ProgressDialog()
    {
        try{
            try {
                if (alertDialog_loader!=null )
                {
                    alertDialog_loader.hide();
                }
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        }catch (Exception e)
        {

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( alertDialog_loader !=null && alertDialog_loader.isShowing() ){
            alertDialog_loader.cancel();
        }
    }
    @Override
    public void ErrorResponse(VolleyError error, int requestCode, JSONObject networkresponse) {

    }
    //Set LayoutManager to Listview
    public LinearLayoutManager Set_LayoutManager(RecyclerView recyclerView, boolean HORIZONTAL, boolean VERTICAL)
    {
        LinearLayoutManager linearLayoutManager=null;
        if (HORIZONTAL)
        {  linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }else if (VERTICAL)
             linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        { recyclerView.setLayoutManager(linearLayoutManager);
        }
        return linearLayoutManager;
    }




    public ArrayList<Contact_Model> getContactsForm_Phone(Context mContext) {
        Log.d("START","Getting all Contacts");
        ArrayList<Contact_Model> arrContacts = new ArrayList<Contact_Model>();
        Contact_Model Contact_Model=null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


            Contact_Model = new Contact_Model();
            Contact_Model.setContact_Name(contactName);
            Contact_Model.setContact_Number(contactNumber);
            if (Contact_Model != null)
            {
                arrContacts.add(Contact_Model);
            }
            Contact_Model = null;
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        return arrContacts;
    }
    @Override
    public void SuccessResponse(JSONObject response, int requestCode) {

    }

    @Override
    public void SuccessResponseArray(JSONArray response, int requestCode) {

    }

    @Override
    public void SuccessResponseRaw(String response, int requestCode) {

    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }

}
