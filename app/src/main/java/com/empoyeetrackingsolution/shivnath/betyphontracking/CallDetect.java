package com.empoyeetrackingsolution.shivnath.betyphontracking;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Miss_Detect;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Outgoing_Detect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import static com.example.shivnath.betyphontracking.Checking.PhoneStateReceiver.phoneNumber;


public class CallDetect extends BroadcastReceiver {


    private static Date callStartTime, date2;
    private static boolean incoming, wasRinging;
    static long start_time, end_time;
    private static String savedNumber, info, name;
    private String number;
    private SimpleDateFormat sdf;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private Context context;
    private Realm realm;
    static final String TAG = "State";
    static final String TAG1 = " Inside State";
    private SharedPreferences pref;
    private int cout = 0;
    private static int _counter = 0;
    private int _stringVal;
    private static int Otcounter = 0;
    private int OtstringVal;
    private int misscounter = 0;
    public int misstringVal;
    long endlong, startlong;


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.context = context;

        pref = PreferenceManager.getDefaultSharedPreferences(context);


        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            info = intent.getExtras().getString("android.intent.extra.NAME");
            name = intent.getExtras().getString("name");
            callStartTime = new Date();

        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            checkState(stateStr, pref);


        }

    }


    private void checkState(String stateStr, SharedPreferences pref) {
        int state = 0;

        if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            state = TelephonyManager.CALL_STATE_IDLE;
//
//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//            getOutgoinEndTime = sdf.format(c.getTime());
//            String aa = sdf.format(c.getTime());
//            System.out.println("outg" + aa);

            end_time = System.currentTimeMillis();
            //Total time talked =
            long total_time = end_time - start_time;

            System.out.println("tootal" + total_time);

            long yourmilliseconds = total_time;
            SimpleDateFormat sdf2 = new SimpleDateFormat("mm:ss");
            Date resultdate = new Date(yourmilliseconds);
            System.out.println("tottoot" + sdf2.format(resultdate));


        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            state = TelephonyManager.CALL_STATE_OFFHOOK;
            System.out.println("outg" + number);


        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            state = TelephonyManager.CALL_STATE_RINGING;

            start_time = System.currentTimeMillis();

        }
        onCallStateChanged(context, state, number);
    }

    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                incoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                wasRinging = true;

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    incoming = false;
                    onOutgoingCallStarted(context, number, callStartTime);
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    misscounter++;
                    misstringVal = misscounter;
                    onMissedCall(context, savedNumber, callStartTime, new Date(), misstringVal);
                    wasRinging = true;

                } else if (incoming) {
                    callStartTime = new Date();
                    _counter++;
                    _stringVal = _counter;

                    onIncomingCallEnded(context, number, callStartTime, new Date(), _stringVal);

                } else {

                    callStartTime = new Date();
                    startlong = new Date().getTime();
                    Otcounter++;
                    OtstringVal = Otcounter;
                    onOutgoingCallEnded(context, number, callStartTime, new Date(), OtstringVal, startlong);

                }
                break;
        }
        lastState = state;
    }

    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallEnded(final Context ctx, final String number, Date start, Date end, final int _stringVal) {
        Toast.makeText(ctx, "Incoming Call Ended :)", Toast.LENGTH_SHORT).show();


        cout = new Integer(cout + 1);
        sdf = new SimpleDateFormat("EEE-dd-MM-yyy hh:mm:ss");
        final String strDate = sdf.format(callStartTime);
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Incoming_Detect inCall = bgRealm.createObject(Incoming_Detect.class);
                inCall.setNum(number);
                inCall.setIncomingDate(strDate);
                inCall.setCount(_stringVal);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.

                Toast.makeText(ctx, "SuccessData", Toast.LENGTH_SHORT).show();
                Log.v("Success", ">>>>>>>>>>Ok<<<<<<<<<");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Feild", error.getMessage());

            }
        });


        postingIncoming(start, end, number);


    }




    private void postingIncoming(Date end, Date start, String number) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("user_id", "0");

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String newstartDate = sdf.format(start_time);
        final String newEndDate = sdf.format(end_time);


        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", value);
            jsonObject.put("prospect_number", number);
            jsonObject.put("call_type", "incoming");
            jsonObject.put("talk_time", "30");
            jsonObject.put("start_datetime", newstartDate);
            jsonObject.put("end_datetime", newEndDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/call/summary")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();


                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Incoming_Detect k = realm.where(Incoming_Detect.class).findFirst();
                        k.setTest(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);

                } else {
                    String json = response.body().string();
                    System.out.println("jsonUserId" + json);

                }
            }
        });
    }


    protected void onOutgoingCallEnded(final Context ctx, String number, Date start, Date end, final int otstringVal, long startlong) {
        Toast.makeText(ctx, "OutGoing Call Ended :)", Toast.LENGTH_SHORT).show();


        sdf = new SimpleDateFormat("EEE-dd-MM-yyy hh:mm:ss");
        final String strDate = sdf.format(callStartTime);

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Outgoing_Detect outgoing_detect = bgRealm.createObject(Outgoing_Detect.class);
                outgoing_detect.setOutNumer(CallDetect.this.number);
                outgoing_detect.setOutDate(strDate);
                outgoing_detect.setOutCount(otstringVal);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.

                Log.v("Success", ">>>>>>>>>>Ok<<<<<<<<<");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Feild", error.getMessage());

            }
        });

        postingOutgoing(start, end, this.number);

        callDuration();


    }

    private void callDuration() {

        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        try {
            Cursor managedCursor = context.getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {

                HashMap rowDataCall = new HashMap<String, String>();

                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                String callDayTime = new Date(Long.valueOf(callDate)).toString();
                // long timestamp = convertDateToTimestamp(callDayTime);
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");


            }
            managedCursor.close();
            System.out.println("Testiiig"+sb);
//            textView.setText(sb);
        }
        catch (SecurityException e)
        {
            System.out.println();
            // lets the user know there is a problem with the code
        }


    }


    private void postingOutgoing(Date start, Date end, String number) {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("user_id", "0");

        String aaaa = null;
        RealmResults<Outgoing_Detect> missResult = realm.where(Outgoing_Detect.class).findAllAsync();
        //fetching the data
        missResult.load();
        for(Outgoing_Detect miss_detect:missResult){

            aaaa=miss_detect.getOutNumer();
            System.out.println("OutDetect"+aaaa);

        }


        Toast.makeText(context, "" + value, Toast.LENGTH_SHORT).show();

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String newstartDate = sdf.format(start_time);
        final String newEndDate = sdf.format(end_time);


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", value);
            jsonObject.put("prospect_number",number);
            jsonObject.put("call_type", "out_going");
            jsonObject.put("talk_time", "30");
            jsonObject.put("start_datetime", newstartDate);
            jsonObject.put("end_datetime", newEndDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/call/summary")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String json = response.body().string();
                    System.out.println("jsonUserId" + json);

                }
            }
        });

    }


    protected void onMissedCall(final Context ctx, final String number, Date end, Date start, int misstringVal) {


        Toast.makeText(ctx, "Missed Call :)", Toast.LENGTH_SHORT).show();

        sdf = new SimpleDateFormat("EEE-dd-MM-yyy hh:mm:ss");
        final String strDate = sdf.format(CallDetect.callStartTime);
        System.out.println("datechecking" + strDate);
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Miss_Detect miss_detect = bgRealm.createObject(Miss_Detect.class);
                miss_detect.setMissNum(number);
                miss_detect.setMissDate(strDate);
                miss_detect.setMissCount(CallDetect.this.misstringVal);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.

                Toast.makeText(ctx, "SuccessData", Toast.LENGTH_SHORT).show();
                Log.v("Success", ">>>>>>>>>>Ok<<<<<<<<<");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Feild", error.getMessage());

            }
        });

        postingMissedCall(start, end, number);

    }

    private void postingMissedCall(Date start, Date end, String number) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("user_id", "0");


        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String newstartDate = sdf.format(start);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String newEndDate = sdf.format(end);

        System.out.println("newDate"+newstartDate);


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", value);
            jsonObject.put("prospect_number", number);
            jsonObject.put("call_type", "missed_call");
            jsonObject.put("talk_time", "30");
            jsonObject.put("start_datetime", newstartDate);
            jsonObject.put("end_datetime", newEndDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/call/summary")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String json = response.body().string();
                    System.out.println("jsonUserId" + json);

                }
            }
        });

    }

}
