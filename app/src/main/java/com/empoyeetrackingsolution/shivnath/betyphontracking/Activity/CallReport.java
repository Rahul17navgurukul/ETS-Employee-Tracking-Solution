package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.anychart.scales.Linear;
import com.empoyeetrackingsolution.shivnath.betyphontracking.MainActivity;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CallReport extends AppCompatActivity {

    AnyChartView anyChartView;
    AnyChartView baranychart;
    private TextView noofCall, noofOut, noofIn, noofMiss, talk_time;

    int allCalls = 0;
    int missCall = 0;
    int incomingCall = 0;
    int outgoingCall = 0;
    int talkTime = 0;

    Toolbar toolbar;
    Spinner sp1,sp2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_report);


        toolbar = findViewById(R.id.callReportTool);
        toolbar.inflateMenu(R.menu.logout_menu);


        baranychart = findViewById(R.id.barchart22);
        APIlib.getInstance().setActiveAnyChartView(baranychart);
        barChart2();


        anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        noofCall = findViewById(R.id.noofcall);
        noofIn = findViewById(R.id.noofIncall);
        noofOut = findViewById(R.id.otgoinC);
        noofMiss = findViewById(R.id.misc);
        talk_time = findViewById(R.id.talktime);

        getFromServer();


        PieChartData();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.logout:

                        SharedPreferences preferences =getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.remove("user_id");
                        editor.commit();
                        Intent log = new Intent(CallReport.this, MainActivity.class);
                        startActivity(log);
                        finish();
                        Toast.makeText(CallReport.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return false;
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

        data.add(new CallReport.CustomDataEntry("Incoming Call, Outgoing Call,Talk-Time", 96.5, 1000, 1200, 800));
        data.add(new CallReport.CustomDataEntry("Outgoing Call", 77.1, 1000, 1124, 1724));
        data.add(new CallReport.CustomDataEntry("Talk-Time", 73.2, 1000, 1006, 1806));
        data.add(new CallReport.CustomDataEntry("P4", 61.1, 1000, 921, 1621));
        data.add(new CallReport.CustomDataEntry("P5", 70.0, 1000, 1500, 1700));
        data.add(new CallReport.CustomDataEntry("P6", 60.7, 1507, 1007, 1907));


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

    private void getFromServer() {

        final String url = "http://159.65.145.32/api/dashboard/17/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);


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
                                    Toast.makeText(CallReport.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
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

    private void PieChartData() {

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(CallReport.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

//        anyChartView.setZoomEnabled(false);
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("0 sec", 50));
//        data.add(new ValueDataEntry("0 to 10 sec", 100));
//        data.add(new ValueDataEntry("11 to 30 sec", 150));
//        data.add(new ValueDataEntry("31 to 60 sec", 200));
//        data.add(new ValueDataEntry("61 to 180 sec", 250));
//        data.add(new ValueDataEntry("180 + sec", 300));
//        pie.data(data);
//        pie.title("Call Duration");
//        pie.labels().position("outside");
//        pie.legend()
//                .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
//                .align(Align.BOTTOM);
//
//        anyChartView.setChart(pie);

    }





}
