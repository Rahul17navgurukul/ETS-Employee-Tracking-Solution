package com.example.shivnath.betyphontracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.shivnath.betyphontracking.Activity.DashBoard;
import com.example.shivnath.betyphontracking.model.Incoming_Detect;
import com.example.shivnath.betyphontracking.model.Miss_Detect;
import com.example.shivnath.betyphontracking.model.Outgoing_Detect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import static com.example.shivnath.betyphontracking.Checking.PhoneStateReceiver.phoneNumber;


public class CallDetect extends BroadcastReceiver {


    private static Date callStartTime;
    private static boolean incoming, wasRinging;
    private static String savedNumber,info,name;
    private String number;
    private SimpleDateFormat sdf;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private Context context;
    private Realm realm;
    static final String TAG="State";
    static final String TAG1=" Inside State";
    private SharedPreferences pref;
    private int cout = 0;
    private static int _counter = 0;
    private int _stringVal;
    private static int Otcounter = 0;
    private int OtstringVal;
    private int misscounter = 0;
    public int misstringVal;
    long endlong,startlong;





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
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
            checkState(stateStr,pref);
        }

    }

    private void checkState(String stateStr, SharedPreferences pref) {
        int state = 0;

        if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            state = TelephonyManager.CALL_STATE_IDLE;
        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            state = TelephonyManager.CALL_STATE_OFFHOOK;
        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            state = TelephonyManager.CALL_STATE_RINGING;
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
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    misscounter++;
                    misstringVal = misscounter;
                    onMissedCall(context, savedNumber, callStartTime,new Date(),misstringVal);
                    wasRinging = true;

                } else if (incoming) {
                    callStartTime = new Date();
                    _counter++;
                    _stringVal =_counter;

                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date(),_stringVal);


                } else {

                    callStartTime = new Date();
                    startlong = new Date().getTime();
                    Otcounter++;
                    OtstringVal = Otcounter;

                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date(),OtstringVal,startlong);

                }
                break;
        }
        lastState = state;
    }

    protected void onIncomingCallStarted(Context ctx, String number, Date start ){

    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallEnded(final Context ctx, final String number, Date start, Date end, final int _stringVal) {
        Toast.makeText(ctx, "Incoming Call Ended :)", Toast.LENGTH_SHORT).show();

        cout = new Integer(cout+1);
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
                Log.v("Success",">>>>>>>>>>Ok<<<<<<<<<");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Feild",error.getMessage());

            }
        });


        postingIncoming(start,end,number);



    }

    private void postingIncoming(Date end, Date start, String number) {

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String newstartDate = sdf.format(start);
        final String newEndDate = sdf.format(end);


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id","1");
            jsonObject.put("prospect_number",number);
            jsonObject.put("call_type","incoming");
            jsonObject.put("talk_time","30");
            jsonObject.put("start_datetime",newstartDate);
            jsonObject.put("end_datetime",newEndDate);



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


    protected void onOutgoingCallEnded(final Context ctx, final String number, Date start, Date end, final int otstringVal, long startlong) {
        Toast.makeText(ctx, "OutGoing Call Ended :)", Toast.LENGTH_SHORT).show();

//        endlong = end.getTime();
//        long diff = endlong - startlong;
//        long minutes = (diff / 1000) / 60;
//        long seconds = (diff / 1000) % 60;
//        Toast.makeText(context, savedNumber + " Call time " + minutes + " and " + seconds, Toast.LENGTH_SHORT).show();
//
//        System.out.println("talktime"+diff);


        sdf = new SimpleDateFormat("EEE-dd-MM-yyy hh:mm:ss");
        final String strDate = sdf.format(callStartTime);
        System.out.println("datechecking"+strDate);
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Outgoing_Detect outgoing_detect = bgRealm.createObject(Outgoing_Detect.class);
                outgoing_detect.setOutNumer(number);
                outgoing_detect.setOutDate(strDate);
                outgoing_detect.setOutCount(otstringVal);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.

                Toast.makeText(ctx, "SuccessData", Toast.LENGTH_SHORT).show();
                Log.v("Success",">>>>>>>>>>Ok<<<<<<<<<");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Feild",error.getMessage());

            }
        });

        postingOutgoing(start,end,number);

    }

    private void postingOutgoing(Date start, Date end, String number) {

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String newstartDate = sdf.format(start);
        final String newEndDate = sdf.format(end);


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id","1");
            jsonObject.put("prospect_number",number);
            jsonObject.put("call_type","out_going");
            jsonObject.put("talk_time","30");
            jsonObject.put("start_datetime",newstartDate);
            jsonObject.put("end_datetime",newEndDate);



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
//                    Toast.makeText(context, "Successfull posting OT >>> "+json, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    protected void onMissedCall(final Context ctx, final String number, Date end, Date start, int misstringVal) {


        Toast.makeText(ctx, "Missed Call :)", Toast.LENGTH_SHORT).show();

        sdf = new SimpleDateFormat("EEE-dd-MM-yyy hh:mm:ss");
        final String strDate = sdf.format(CallDetect.callStartTime);
        System.out.println("datechecking"+strDate);
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
                Log.v("Success",">>>>>>>>>>Ok<<<<<<<<<");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Feild",error.getMessage());

            }
        });

        postingMissedCall(start,end,number);

    }

    private void postingMissedCall(Date start, Date end, String number) {

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String newstartDate = sdf.format(start);
        final String newEndDate = sdf.format(end);


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id","1");
            jsonObject.put("prospect_number",number);
            jsonObject.put("call_type","missed_call");
            jsonObject.put("talk_time","30");
            jsonObject.put("start_datetime",newstartDate);
            jsonObject.put("end_datetime",newEndDate);



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
