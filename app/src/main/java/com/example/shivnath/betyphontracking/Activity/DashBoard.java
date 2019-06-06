package com.example.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.example.shivnath.betyphontracking.R;
import com.example.shivnath.betyphontracking.liveLocation;
import com.example.shivnath.betyphontracking.model.Incoming_Detect;
import com.example.shivnath.betyphontracking.model.Miss_Detect;
import com.example.shivnath.betyphontracking.model.Outgoing_Detect;
import com.example.shivnath.betyphontracking.service.CallDetectService;
import com.example.shivnath.betyphontracking.service.CheckPermission;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DashBoard extends AppCompatActivity {

    private TextView noofCall,noofOut,noofIn,noofMiss;
    private Toolbar toolbar;
    AnyChartView anyChartView;
    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    int a,b,c,d;

    private LinearLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        layout = findViewById(R.id.layout);

        Realm.init(DashBoard.this);
        Realm realm = Realm.getDefaultInstance();

        toolbar = findViewById(R.id.dash_toolbar);
        toolbar.inflateMenu(R.menu.item_menu);
        noofCall = findViewById(R.id.noofcall);
        noofIn = findViewById(R.id.noofIncall);
        noofOut = findViewById(R.id.otgoinC);
        noofMiss = findViewById(R.id.misc);

        toolbarMenuClick();
        askPermission();
        startService(new Intent(DashBoard.this, CallDetectService.class));
        startService(new Intent(DashBoard.this, liveLocation.class));

         //creating  database oject
        RealmResults<Incoming_Detect> results = realm.where(Incoming_Detect.class).findAllAsync();
        //fetching the data
        results.load();
        for(Incoming_Detect numberCount:results){

            a = numberCount.getCount();
        }

         //creating  database oject
        RealmResults<Outgoing_Detect> outresults = realm.where(Outgoing_Detect.class).findAllAsync();
        //fetching the data
        outresults.load();
        for(Outgoing_Detect outgoing_detect:outresults){

            b=outgoing_detect.getOutCount();

        }

       //creating  database oject
        RealmResults<Miss_Detect> missResult = realm.where(Miss_Detect.class).findAllAsync();
        //fetching the data
        missResult.load();
        for(Miss_Detect miss_detect:missResult){

            noofMiss.setText("Number of Misssed call : "+miss_detect.getMissCount());
            d = miss_detect.getMissCount();
        }

        c = a+b;
        noofCall.setText("Number of Call : "+c);
        noofOut.setText("Number of Outgoing : "+b);
        noofIn.setText("Number of Incoming : "+a);


        noofCall = findViewById(R.id.noofcall);

        pieChart = findViewById(R.id.chart1);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");

        pieChart.setUsePercentValues(false);

        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(3000);

        pieChart.setRotationEnabled(false);


    }

    private void askPermission() {
        CheckPermission checkPermission = new CheckPermission();
        checkPermission.checkPermission(this);
    }

    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(c, 0));
        entries.add(new BarEntry(a, 1));
        entries.add(new BarEntry(b,2 ));
        entries.add(new BarEntry(d, 3));
        entries.add(new BarEntry(5, 4));


    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("Number of Call");
        PieEntryLabels.add("Incoming");
        PieEntryLabels.add("Outgoing");
        PieEntryLabels.add("Missedcall");
        PieEntryLabels.add("Talk-Time");


    }

    private void toolbarMenuClick() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.callLog)
                {
                    Intent i = new Intent(DashBoard.this, Frag_Nav.class);
                    startActivity(i);
                }
                else if(item.getItemId()== R.id.callrecoding)
                {
                    Intent i = new Intent(DashBoard.this, CallRecoding.class);
                    startActivity(i);

                    // do something
                }
                else if (item.getItemId()==R.id.profile){
                    // do something
                }
                else if (item.getItemId()==R.id.location){
                    Intent i = new Intent(DashBoard.this, UserLocation.class);
                    startActivity(i);
                    // do something
                }
                else if (item.getItemId()==R.id.setting){
                    // do something
                }
                else if (item.getItemId()==R.id.logout){
                    // do something
                }
                return false;
            }
        });
    }
}
