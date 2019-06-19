package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.AttendenceAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.TimelineAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.TimelineModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.locationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Attendence extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    private AttendenceAdapter adapter;
    private DividerItemDecoration dividerItemDecoration;
    private List<AttendenceModel> attendenceModels;
    Spinner sp1,sp2;
    ArrayList<String> UserName;
    private LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);


        recyclerView = findViewById(R.id.attenRecycler);
        sp1 = findViewById(R.id.sp1);
        sp2 = findViewById(R.id.sp2);
        sp1.setOnItemSelectedListener(this);
        sp2.setOnItemSelectedListener(this);
        final CardView cardView = findViewById(R.id.filetrOpt);
        final ImageView imageView = findViewById(R.id.rec_filter);
        final ImageView imageView1 = findViewById(R.id.rec_unfilter);

        getUserNameFromServer();
        getDataFromServer();

        imageView1.setVisibility(View.INVISIBLE);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString("subscriber", "0");

        Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();

        if (value.equals("subscriber")){

            sp2.setVisibility(View.VISIBLE);
        }else {
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.INVISIBLE);
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Today");
        categories.add("Yesterday");
        categories.add("Last 7 days");
        categories.add("Last 10 days");
        categories.add("Last 30 days");
        categories.add("This Month");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Attendence.this, android.
                R.layout.simple_spinner_item, categories);

        List<String> categories2 = new ArrayList<String>();
        categories2.add("Select User");
        categories2.add("Rahul");
        categories2.add("Shiv Halder");
        categories2.add("User 3");
        categories2.add("User 4");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Attendence.this,
                android.R.layout.simple_spinner_item, categories2);

        sp1.setAdapter(dataAdapter);
        sp2.setAdapter(dataAdapter2);
//
        attendenceModels = new ArrayList<>();
        adapter = new AttendenceAdapter(Attendence.this,attendenceModels);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getDataFromServer() {

        String url = "http://159.65.145.32/api/user/attendence/17";


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        System.out.println("hjgfhjfdv"+jsonObject);

                        AttendenceModel attendenceModel = new AttendenceModel();

                        attendenceModel.setFull_name(jsonObject.getString("full_name"));
                        attendenceModel.setLogout_time(jsonObject.getString("logout_time"));
                        attendenceModel.setUser_id(jsonObject.getString("user_id"));
                        attendenceModel.setLogin_time(jsonObject.getString("login_time"));
                        attendenceModel.setCreation_datetime(jsonObject.getString("date"));
                        attendenceModel.setStatus(jsonObject.getString("status"));


                        attendenceModels.add(attendenceModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void getUserNameFromServer() {


        String url = "http://159.65.145.32/api/user/8";
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String data1 = jsonObject.getString("user_id");
                        String userName = jsonObject.getString("full_name");


//                        String [] aa = {String.valueOf(userName)};
//
//                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Attendence.this,
//                                android.R.layout.simple_spinner_item, aa);
//                        sp2.setAdapter(dataAdapter2);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(this, "jcnjc"+position, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
