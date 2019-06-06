package com.example.shivnath.betyphontracking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.shivnath.betyphontracking.Fragment.IncomingCallFrag;
import com.example.shivnath.betyphontracking.Fragment.MissCallFrag;
import com.example.shivnath.betyphontracking.Fragment.OutgoingCallFrag;
import com.example.shivnath.betyphontracking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.realm.Realm;

public class Frag_Nav extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_nav);
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
