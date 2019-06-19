package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.data.Mapping;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.anychart.scales.Linear;
import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.InCallLogAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.MainActivity;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.liveLocation;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.CallDurationCategory;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.DashboardModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.TotalCallDatum;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.locationModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.service.CallDetectService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.anychart.data.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class DashBoard extends AppCompatActivity {

    private TextView noofCall, noofOut, noofIn, noofMiss, talk_time;
    private Toolbar toolbar;
    AnyChartView anyChartView;
    AnyChartView baranychart;
    private InCallLogAdapter adapter;
    private List<Incoming_Detect> incpmingCallList;
    private MaterialSearchView mMaterialSearchView;
    private LinearLayout layout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nv;
    private RequestQueue requestQueue;

    private SwitchCompat switchCompat;
    Realm realm;
    String startDate;
    String newstartDate;

    String[] list;

    int allCalls = 0;
    int missCall = 0;
    int incomingCall = 0;
    int outgoingCall = 0;
    int talkTime = 0;

    String logOutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getFromServer();


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        startDate = sdf2.format(date);

        switchCompat = findViewById(R.id.simpleSwitch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (switchCompat.isChecked()) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DashBoard.this);
                    String value = preferences.getString("user_id", "0");

                    OkHttpClient client = new OkHttpClient();

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://159.65.145.32/api/attendence")
                            .method("POST", body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            } else {
                                String json = response.body().string();
                                System.out.println("jsonUserId" + json);

                                try {
                                    JSONObject jsonobj = new JSONObject(json);
                                    logOutId = jsonobj.getString("id");
                                    System.out.println("logoutid " + logOutId);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        }
                    });

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    newstartDate = sdf.format(currentTime);

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy");
                    startDate = sdf2.format(date);


                    Toast.makeText(DashBoard.this, "On duty", Toast.LENGTH_SHORT).show();

//

                } else {


                    System.out.println("logoutid>>"+logOutId);

                    OkHttpClient client = new OkHttpClient();

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", logOutId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://159.65.145.32/api/attendence")
                            .method("POST", body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            } else {
                                String json = response.body().string();
                                System.out.println("LogoutRes " + json);




                            }
                        }
                    });

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    final String offTime = sdf.format(currentTime);


                }
            }
        });

        baranychart = findViewById(R.id.barchart22);
        APIlib.getInstance().setActiveAnyChartView(baranychart);

        barChart2();
        nv = findViewById(R.id.dashNav);
        anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        PieChartData();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mMaterialSearchView = findViewById(R.id.searchView);

        list = new String[]{"No of Call", "Incoming Call", "Outgoinh Call", "Missed call", "Talk-Time"};
//        mMaterialSearchView.setSuggestions(list);

        System.out.println("fkjvklf" + incpmingCallList);

        mMaterialSearchView.bringToFront();

        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

                Toast.makeText(DashBoard.this, "Searching", Toast.LENGTH_SHORT).show();

                Realm realm = Realm.getDefaultInstance(); //creating  database oject
                RealmResults<Incoming_Detect> results = realm.where(Incoming_Detect.class).findAllAsync();
                results.load();

                for (Incoming_Detect incoming_detect : results){

                }

                OkHttpClient client = new OkHttpClient();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("prospect_number", "+919990925305");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://159.65.145.32/api/search")
                        .method("POST", body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String json = response.body().string();
                            System.out.println("searchResponse " + json);

                        }
                    }
                });

            }

            @Override
            public void onSearchViewClosed() {
//
            }
        });

        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        layout = findViewById(R.id.layout);
        Realm.init(DashBoard.this);
        toolbar = findViewById(R.id.dash_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.inflateMenu(R.menu.item_menu);


        noofCall = findViewById(R.id.noofcall);
        noofIn = findViewById(R.id.noofIncall);
        noofOut = findViewById(R.id.otgoinC);
        noofMiss = findViewById(R.id.misc);
        talk_time = findViewById(R.id.talktime);

        toolbarMenuClick();
//        askPermission();
        startService(new Intent(DashBoard.this, CallDetectService.class));
        startService(new Intent(DashBoard.this, liveLocation.class));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
                if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(GravityCompat.START);

            }
        });


        noofCall = findViewById(R.id.noofcall);


        nv.bringToFront();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.dash:
//
                        Toast.makeText(DashBoard.this, "My DashboardModel", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        Intent i1 = new Intent(DashBoard.this, MyTeam.class);
                        startActivity(i1);
                        Toast.makeText(DashBoard.this, "My-Team", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.callLog:
                        Intent i = new Intent(DashBoard.this, Frag_Nav.class);
                        startActivity(i);
                        Toast.makeText(DashBoard.this, "Call Details", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.callrecoding:
                        Intent rec = new Intent(DashBoard.this, CallRecoding.class);
                        startActivity(rec);
                        Toast.makeText(DashBoard.this, "Call Recoidng", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.location:
                        Intent i2 = new Intent(DashBoard.this, UserLocation.class);
                        startActivity(i2);
                        Toast.makeText(DashBoard.this, "Location", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.prospect:
                        Toast.makeText(DashBoard.this, "Prospect", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.report:
                        Toast.makeText(DashBoard.this, "Repost", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.attendence:
                        Intent intent = new Intent(DashBoard.this, Attendence.class);
                        startActivity(intent);
                        break;

                    case R.id.callreport:
                        Intent intent2 = new Intent(DashBoard.this, CallReport.class);
                        startActivity(intent2);
                        break;

                    case R.id.logout:
                        SharedPreferences preferences =getSharedPreferences("user_id",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.remove("user_id");
                        editor.commit();
                        Intent log = new Intent(DashBoard.this, MainActivity.class);
                        startActivity(log);
                        finish();
                        Toast.makeText(DashBoard.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }

                return true;
            }
        });

    }

    private void barChart2() {

        Cartesian cartesian = AnyChart.cartesian();

        cartesian.animation(true);

        cartesian.title("Hour Wise");

        cartesian.yScale().stackMode(ScaleStackMode.VALUE);

        Linear scalesLinear = Linear.instantiate();
        scalesLinear.minimum(0d);
        scalesLinear.maximum(140);
        scalesLinear.ticks("{ interval: 35 }");

        com.anychart.core.axes.Linear extraYAxis = cartesian.yAxis(1d);
        extraYAxis.orientation(Orientation.LEFT)
                .scale(scalesLinear);
        extraYAxis.labels()
                .padding(0d, 0d, 0d, 5d)
                .format("");

        List<DataEntry> data = new ArrayList<>();

        data.add(new CustomDataEntry("Incoming Call, Outgoing Call,Talk-Time", 96.5, 1000, 1200, 800));
        data.add(new CustomDataEntry("Outgoing Call", 77.1, 1000, 1124, 1724));
        data.add(new CustomDataEntry("Talk-Time", 73.2, 1000, 1006, 1806));
        data.add(new CustomDataEntry("P4", 61.1, 1000, 921, 1621));
        data.add(new CustomDataEntry("P5", 70.0, 1000, 1500, 1700));
        data.add(new CustomDataEntry("P6", 60.7, 1507, 1007, 1907));


        Set set = Set.instantiate();
        set.data(data);
        Mapping column1Data = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping column2Data = set.mapAs("{ x: 'x', value: 'value3' }");
        Mapping column3Data = set.mapAs("{ x: 'x', value: 'value4' }");
        Mapping column4Data = set.mapAs("{ x: 'x', value: 'value4' }");


        cartesian.column(column1Data);
        cartesian.crosshair(true);
        cartesian.column(column2Data);
        cartesian.column(column3Data);
        cartesian.column(column4Data);


        cartesian.legend()
                .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                .align(Align.BOTTOM);

        baranychart.setChart(cartesian);

    }

    public static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);

        }
    }


    private void PieChartData() {

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(DashBoard.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        anyChartView.setZoomEnabled(false);


    }

    private void getFromServer() {


        final String url = "http://159.65.145.32/api/dashboard/17/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            System.out.println("testgghgh >> " + obj.getString("call_duration_category"));
                            System.out.println("testgghgh >> " + obj.getString("user_call_data"));
                            System.out.println("testgghgh >> " + obj.getString("total_call_data"));


                            JSONArray data = obj.getJSONArray("user_call_data");

                            for (int i = 0; i != data.length(); i++) {

                                JSONObject eachData = data.getJSONObject(i);
                                String a = eachData.getString("call_type");
                                String b = eachData.getString("talk_time");

                                allCalls = allCalls + 1;

                                noofCall.setText("No of calls " + allCalls);

                                if (a.equals("incoming")) {

                                    incomingCall = incomingCall + 1;
                                    noofIn.setText("Incoming calls " + incomingCall);

                                } else if (a.equals("out_going")) {

                                    outgoingCall = outgoingCall + 1;
                                    noofOut.setText("Outgoing calls " + outgoingCall);
                                } else {

                                    missCall = missCall + 1;
                                    noofMiss.setText("Missed call " + missCall);
                                }

                                int c = Integer.parseInt(b);
                                talkTime = talkTime + c;


                                int tot_seconds = talkTime;
                                int hours = tot_seconds / 3600;
                                int minutes = (tot_seconds % 3600) / 60;
                                int seconds = tot_seconds % 60;

                                String timeString = String.format("%02d :  %02d : %02d : ", hours, minutes, seconds);

                                talk_time.setText("Talktime " + timeString);


                            }

                            JSONObject reader = new JSONObject(response);
                            JSONObject jsonObject = reader.getJSONObject("call_duration_category");

                            Pie pie = AnyChart.pie();

                            pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                                @Override
                                public void onClick(Event event) {
                                    Toast.makeText(DashBoard.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
                                }
                            });

                            anyChartView.setZoomEnabled(false);
                            List<DataEntry> data2 = new ArrayList<>();
                            data2.add(new ValueDataEntry("0 sec", Integer.parseInt(jsonObject.getString("0"))));
                            data2.add(new ValueDataEntry("0 to 10 sec", Integer.parseInt(jsonObject.getString("0-10"))));
                            data2.add(new ValueDataEntry("11 to 30 sec", Integer.parseInt(jsonObject.getString("11-30"))));
                            data2.add(new ValueDataEntry("31 to 60 sec", Integer.parseInt(jsonObject.getString("31-60"))));
                            data2.add(new ValueDataEntry("61 to 180 sec", Integer.parseInt(jsonObject.getString("61-180"))));
                            data2.add(new ValueDataEntry("180 + sec", Integer.parseInt(jsonObject.getString("180+"))));
                            pie.data(data2);
                            pie.title("Call Duration");
                            pie.labels().position("outside");
                            pie.legend()
                                    .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                                    .align(Align.BOTTOM);

                            anyChartView.setChart(pie);


                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        mMaterialSearchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);

    }

    private void toolbarMenuClick() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                mMaterialSearchView.setMenuItem(item);

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        mMaterialSearchView.closeSearch();
        mMaterialSearchView.clearFocus();
        super.onBackPressed();
    }
}
