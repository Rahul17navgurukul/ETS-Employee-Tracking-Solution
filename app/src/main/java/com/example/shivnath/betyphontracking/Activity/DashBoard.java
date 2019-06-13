package com.example.shivnath.betyphontracking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.data.Mapping;
import com.anychart.enums.Align;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.SolidFill;
import com.example.shivnath.betyphontracking.Adapter.InCallLogAdapter;
import com.example.shivnath.betyphontracking.MainActivity;
import com.example.shivnath.betyphontracking.R;
import com.example.shivnath.betyphontracking.liveLocation;
import com.example.shivnath.betyphontracking.model.Incoming_Detect;
import com.example.shivnath.betyphontracking.model.Miss_Detect;
import com.example.shivnath.betyphontracking.model.Outgoing_Detect;
import com.example.shivnath.betyphontracking.service.CallDetectService;
import com.example.shivnath.betyphontracking.service.CheckPermission;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.anychart.data.Set;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class DashBoard extends AppCompatActivity {

    private TextView noofCall, noofOut, noofIn, noofMiss;
    private Toolbar toolbar;
    AnyChartView anyChartView;
    AnyChartView baranychart;
    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;
    int a, b, c, d;
    BarChart chart;

    private InCallLogAdapter adapter;
    private RealmResults<Incoming_Detect> incpmingCallList;

    private MaterialSearchView mMaterialSearchView;
    private LinearLayout layout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nv;
    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getFromServer();


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        nv = findViewById(R.id.dashNav);
        anyChartView =  findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        baranychart = findViewById(R.id.barchart);
//        APIlib.getInstance().setActiveAnyChartView(baranychart);

        PieChartData();
        BarChartData();

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

//        chart = findViewById(R.id.barchart);

        toolbarMenuClick();
//        askPermission();
        startService(new Intent(DashBoard.this, CallDetectService.class));
        startService(new Intent(DashBoard.this, liveLocation.class));

        //creating  database oject
//        RealmResults<Incoming_Detect> results = realm.where(Incoming_Detect.class).findAllAsync();
//        //fetching the data
//        results.load();
//        for(Incoming_Detect numberCount:results){
//
//            a = numberCount.getCount();
//        }

        //creating  database oject
//        RealmResults<Outgoing_Detect> outresults = realm.where(Outgoing_Detect.class).findAllAsync();
//        //fetching the data
//        outresults.load();
//        for(Outgoing_Detect outgoing_detect:outresults){
//
//            b=outgoing_detect.getOutCount();
//
//        }

//       //creating  database oject
//        RealmResults<Miss_Detect> missResult = realm.where(Miss_Detect.class).findAllAsync();
//        //fetching the data
//        missResult.load();
//        for(Miss_Detect miss_detect:missResult){
//
//            noofMiss.setText("Number of Misssed call : "+miss_detect.getMissCount());
//            d = miss_detect.getMissCount();
//        }

        c = a + b;
        noofCall.setText("Number of Call : " + c);
        noofOut.setText("Number of Outgoing : " + b);
        noofIn.setText("Number of Incoming : " + a);

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
                    default:
                        return true;
                }

                return true;
            }
        });

    }

    private void BarChartData() {

//
//
        Cartesian3d column3d = AnyChart.column3d();

        column3d.yScale().stackMode(ScaleStackMode.VALUE);

        column3d.animation(true);

        column3d.title("Types of Coffee");
        column3d.title().padding(0d, 0d, 15d, 0d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("Espresso", 1, null, null, null, null, null));
        seriesData.add(new CustomDataEntry("Doppio", 2, null, null, null, null, null));
        seriesData.add(new CustomDataEntry("Trippio", 3, null, null, null, null, null));
        seriesData.add(new CustomDataEntry("Americano", 1, 3, null, null, null, null));
        seriesData.add(new CustomDataEntry("Cappuchino", 1, null, 1, 2, null, null));
        seriesData.add(new CustomDataEntry("Macchiato", 2.5, null, null, 1, null, null));
        seriesData.add(new CustomDataEntry("Latte", 1, null, 2, 1, null, null));
        seriesData.add(new CustomDataEntry("Latte Macchiato", 1, null, 2, null, 1, null));
        seriesData.add(new CustomDataEntry("Vienna Coffee", 1, null, null, null, 2, null));
        seriesData.add(new CustomDataEntry("Mocco", 1, null, 1, null, 1, 1));

        com.anychart.data.Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Data = set.mapAs("{ x: 'x', value: 'value3' }");
        Mapping series4Data = set.mapAs("{ x: 'x', value: 'value4' }");
        Mapping series5Data = set.mapAs("{ x: 'x', value: 'value5' }");
        Mapping series6Data = set.mapAs("{ x: 'x', value: 'value6' }");

        Column3d series1 = column3d.column(series1Data);
        series1.name("Espresso");
        series1.fill(new SolidFill("#3e2723", 1d));
        series1.stroke("1 #f7f3f3");
        series1.hovered().stroke("3 #f7f3f3");

        Column3d series2 = column3d.column(series2Data);
        series2.name("Water");
        series2.fill(new SolidFill("#64b5f6", 1d));
        series2.stroke("1 #f7f3f3");
        series2.hovered().stroke("3 #f7f3f3");

        Column3d series3 = column3d.column(series3Data);
        series3.name("Milk");
        series3.fill(new SolidFill("#fff3e0", 1d));
        series3.stroke("1 #f7f3f3");
        series3.hovered().stroke("3 #f7f3f3");

        Column3d series4 = column3d.column(series4Data);
        series4.name("Steamed milk");
        series4.fill(new SolidFill("#bcaaa4", 1d));
        series4.stroke("1 #f7f3f3");
        series4.hovered().stroke("3 #f7f3f3");

        Column3d series5 = column3d.column(series5Data);
        series5.name("Cream");
        series5.fill(new SolidFill("#e6c1b5", 1d));
        series5.stroke("1 #f7f3f3");
        series5.hovered().stroke("3 #f7f3f3");

        Column3d series6 = column3d.column(series6Data);
        series6.name("Chocolate");
        series6.fill(new SolidFill("#bf360c", 1d));
        series6.stroke("1 #f7f3f3");
        series6.hovered().stroke("3 #f7f3f3");

        column3d.legend().enabled(true);
        column3d.legend().fontSize(13d);
        column3d.legend().padding(0d, 0d, 20d, 0d);

        column3d.yScale().ticks("[0, 1, 2, 3, 4, 5]");
        column3d.xAxis(0).stroke("1 #a18b7e");
        column3d.xAxis(0).labels().fontSize("#a18b7e");
        column3d.yAxis(0).stroke("1 #a18b7e");
        column3d.yAxis(0).labels().fontColor("#a18b7e");
        column3d.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        column3d.yAxis(0).title().enabled(true);
        column3d.yAxis(0).title().text("Portions of Ingredients");
        column3d.yAxis(0).title().fontColor("#a18b7e");

        column3d.interactivity().hoverMode(HoverMode.BY_X);

        column3d.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .format("{%Value} {%SeriesName}");

        column3d.yGrid(0).stroke("#a18b7e", 1d, null, null, null);
        column3d.xGrid(0).stroke("#a18b7e", 1d, null, null, null);
        baranychart.setChart(column3d);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4, Number value5, Number value6) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
            setValue("value5", value5);
            setValue("value6", value6);
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

                            for (int i = 0;i<jsonArray.length();i++){

                                JSONObject user_call_data = jsonArray.getJSONObject(i);

                                String user_id = user_call_data.getString("user_id");
                                System.out.println("testttting"+user_id);

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

    public void AddValuesToPIEENTRY() {

        entries.add(new BarEntry(c, 0));
        entries.add(new BarEntry(a, 1));
        entries.add(new BarEntry(b, 2));
        entries.add(new BarEntry(d, 3));
        entries.add(new BarEntry(5, 4));


    }

    public void AddValuesToPieEntryLabels() {

        PieEntryLabels.add("Number of Call");
        PieEntryLabels.add("Incoming");
        PieEntryLabels.add("Outgoing");
        PieEntryLabels.add("Missedcall");
        PieEntryLabels.add("Talk-Time");


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


//                if(item.getItemId()==R.id.callLog)
//                {
//                    Intent i = new Intent(DashBoard.this, Frag_Nav.class);
//                    startActivity(i);
//                }
//                else if(item.getItemId()== R.id.callrecoding)
//                {
//                    Intent i = new Intent(DashBoard.this, CallRecoding.class);
//                    startActivity(i);
//
//                    // do something
//                }
//                else if (item.getItemId()==R.id.profile){
//                    // do something
//
//                    Intent i = new Intent(DashBoard.this, MyTeam.class);
//                    startActivity(i);
//                }
//                else if (item.getItemId()==R.id.location){
//                    Intent i = new Intent(DashBoard.this, UserLocation.class);
//                    startActivity(i);
//                    // do something
//                }
//                else if (item.getItemId()==R.id.setting){
//                    // do something
//                }
//
//
//
//                else if (item.getItemId()==R.id.setting){
//                    // do something
//                }
//
//                else if (item.getItemId()==R.id.logout){
//                    // do something
//                }
                return false;
            }
        });
    }
}
