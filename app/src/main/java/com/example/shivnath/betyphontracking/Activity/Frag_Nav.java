package com.example.shivnath.betyphontracking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivnath.betyphontracking.Fragment.IncomingCallFrag;
import com.example.shivnath.betyphontracking.Fragment.MissCallFrag;
import com.example.shivnath.betyphontracking.Fragment.OutgoingCallFrag;
import com.example.shivnath.betyphontracking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class Frag_Nav extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Realm realm;
    Toolbar toolbar;
    CardView cardView;
    ImageView imageView,unfiletrImg;
    TextView cardDate,cardAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_nav);

        cardDate = findViewById(R.id.card_date);
        cardAgent = findViewById(R.id.card_agent);

        cardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(Frag_Nav.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
//                                etFromDate.setText(sdf.format(calendar.getTime()));
//                                etToDate.setText("");
//
//                                startDate = calendar.getTime();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        imageView = findViewById(R.id.filerImage);
        unfiletrImg = findViewById(R.id.unfilerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.VISIBLE);
                unfiletrImg.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);


            }
        });

        unfiletrImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                unfiletrImg.setVisibility(View.INVISIBLE);


            }
        });
        cardView = findViewById(R.id.navCard);
//        toolbar = findViewById(R.id.toolbar_class);
//        toolbar.inflateMenu(R.menu.filter);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//
//                if(item.getItemId()==R.id.filetrMenu)
//                {
//                    cardView.setVisibility(View.VISIBLE);
////                    item.setVisible(false);
//
//                }
//
//
//                return false;
//            }
//        });

//        textView = findViewById(R.id.textview_call);
//        getCall();

        bottomNavigationView = findViewById(R.id.navigation);
        final IncomingCallFrag incomingCall = new IncomingCallFrag();
        final OutgoingCallFrag outgoingCall = new OutgoingCallFrag();
        final MissCallFrag missCall = new MissCallFrag();

//        RealmResults<Incoming_Detect> results = Realm.getDefaultInstance().where(Incoming_Detect.class).findAllAsync();
//
//        for (Incoming_Detect in :results){
//
//            Toast.makeText(this, in.getIncomingDate(), Toast.LENGTH_SHORT).show();
//        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.in) {
                    setFragment(incomingCall);
                    return true;
                } else if (id == R.id.ot) {
                    setFragment(outgoingCall);
                    return true;
                } else if (id == R.id.mc) {
                    setFragment(missCall);
                    return true;
                }

                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.in);

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }



//    public void getCall() {
//        StringBuffer sb = new StringBuffer();
//        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
//        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
//        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
//        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
//        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
//        sb.append("Call Log :");
//        while (managedCursor.moveToNext()) {
//            String phNumber = managedCursor.getString(number);
//            String callType = managedCursor.getString(type);
//            String callDate = managedCursor.getString(date);
//            Date callDayTime = new Date(Long.valueOf(callDate));
//            String callDuration = managedCursor.getString(duration);
//            String dir = null;
//            int dircode = Integer.parseInt(callType);
//            switch (dircode) {
//                case CallLog.Calls.OUTGOING_TYPE:
//                    dir = "OUTGOING";
//                    break;
//                case CallLog.Calls.INCOMING_TYPE:
//                    dir = "INCOMING";
//                    break;
//                case CallLog.Calls.MISSED_TYPE:
//                    dir = "MISSED";
//                    break;
//            }
//            sb.append("\nPhone Number:--- " +phNumber + " \nCall Type:--- "+ dir + " \nCall Date:--- "+
//                    callDayTime+ " \nCall duration in sec:--- " + callDuration);
//            sb.append("\n----------------------------------");
//        }
//        textView.setText(sb);
//    }
}
