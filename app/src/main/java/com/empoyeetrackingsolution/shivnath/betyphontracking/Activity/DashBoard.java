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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.liveLocation;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;
import com.empoyeetrackingsolution.shivnath.betyphontracking.service.CallDetectService;
import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.anychart.data.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class DashBoard extends AppCompatActivity {

    private TextView noofCall, noofOut, noofIn, noofMiss;
    private Toolbar toolbar;
    AnyChartView anyChartView;
    AnyChartView baranychart;
    private InCallLogAdapter adapter;
    private RealmResults<Incoming_Detect> incpmingCallList;
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
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss" );
        startDate = sdf2.format(date);

        switchCompat = findViewById(R.id.simpleSwitch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
                if (switchCompat.isChecked()){

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    newstartDate = sdf.format(currentTime);

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy" );
                    startDate = sdf2.format(date);


                    Toast.makeText(DashBoard.this, "On duty", Toast.LENGTH_SHORT).show();

                    realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            AttendenceModel attendence = bgRealm.createObject(AttendenceModel.class);
                            attendence.setOnDutyTime(newstartDate);
                            attendence.setSartDate(startDate);



                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {


                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.e("Feild", error.getMessage());

                        }
                    });

                }else {

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    final String offTime = sdf.format(currentTime);

                    realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            AttendenceModel attendence = bgRealm.createObject(AttendenceModel.class);
                            attendence.setOffDutyTime(offTime);
                            attendence.setSartDate(startDate);
                            attendence.setOnDutyTime(newstartDate);

                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {


                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.e("Feild", error.getMessage());

                        }
                    });


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

        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, SUGGESTION);
//                listView.setAdapter(arrayAdapter);
            }
        });

        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                incpmingCallList = Realm.getDefaultInstance().where(Incoming_Detect.class).findAllAsync();
//                adapter = new InCallLogAdapter(DashBoard.this, incpmingCallList);

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
                        Toast.makeText(DashBoard.this, "My Dashboard", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(DashBoard.this,Attendence.class);
                        startActivity(intent);
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

    private class CustomDataEntry extends ValueDataEntry {
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

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("0 sec", 50));
        data.add(new ValueDataEntry("0 to 10 sec", 100));
        data.add(new ValueDataEntry("11 to 30 sec", 150));
        data.add(new ValueDataEntry("31 to 60 sec", 200));
        data.add(new ValueDataEntry("61 to 180 sec", 250));
        data.add(new ValueDataEntry("180 + sec", 300));
        pie.data(data);
        pie.title("Call Duration");
        pie.labels().position("outside");
        pie.legend()
                .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                .align(Align.BOTTOM);

        anyChartView.setChart(pie);

    }

    private void getFromServer() {


        String url = "http://159.65.145.32/api/dashboard/17/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("user_call_data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject user_call_data = jsonArray.getJSONObject(i);

                                String user_id = user_call_data.getString("user_id");
                                System.out.println("testttting" + user_id);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

//        requestQueue.add(request);

    }


//    private void askPermission() {
//        CheckPermission checkPermission = new CheckPermission();
//        checkPermission.checkPermission(this);
//    }


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
}
