package com.empoyeetrackingsolution.shivnath.betyphontracking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.empoyeetrackingsolution.shivnath.betyphontracking.CallDetect;
import com.aykuttasil.callrecord.CallRecord;
import com.empoyeetrackingsolution.shivnath.betyphontracking.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallHelper {

    private CallRecord callRecord = null;
    private Context ctx;
    private CallDetect outgoingReceiver;
    private String savedNumber, number;
    private String mediaPath;


    MediaRecorder recorder;
    static final String TAGS=" Inside Service";

    SharedPreferences preferences;


    public CallHelper(Context ctx) {
        this.ctx = ctx;
        outgoingReceiver = new CallDetect();
    }

    public class CallingBroadReciever extends BroadcastReceiver {
        private int lastState = TelephonyManager.CALL_STATE_IDLE;
        private Date callStartTime;
        private boolean isIncoming, wasRinging;
        private Context context = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;

            PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();

            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                callStartTime = new Date();

            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                checkState(stateStr,editor);
            }
        }

        private void checkState(String stateStr, SharedPreferences.Editor editor) {
            int state = 0;

            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number,editor);
        }

        protected void onIncomingCallStarted(Context ctx, String number, Date start, SharedPreferences.Editor editor) {
            recorder.start();
            callRecord.stopCallReceiver();
            editor.putString("recodingNumber",number);
            editor.putString("recodingType","Imcoming");

        }


        protected void onOutgoingCallStarted(Context ctx, String number, Date start, SharedPreferences.Editor editor) {
            callRecord.startCallReceiver();
            recorder.start();

            editor.putString("recodingNumber",number);
            editor.putString("recodingType","Outgoing");

        }

        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
            callRecord.stopCallReceiver();
            recorder.stop();
        }

        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
            callRecord.stopCallReceiver();
            recorder.stop();
        }

        protected void onMissedCall(Context ctx, String number, Date start) {
        }

        public void onCallStateChanged(Context context, int state, String number, SharedPreferences.Editor editor) {
            if (lastState == state) {
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    callStartTime = new Date();
                    savedNumber = number;
                    onIncomingCallStarted(context, number, callStartTime,editor);
                    wasRinging = true;

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false;
                        callStartTime = new Date();

                        onOutgoingCallStarted(context, savedNumber, callStartTime,editor);
                    }

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {

                        onMissedCall(context, savedNumber, callStartTime);
                        wasRinging = true;

                    } else if (isIncoming) {
                        onIncomingCallEnded(context, savedNumber, callStartTime, new Date());

                    } else {

                        onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());

                    }
                    break;


            }

            lastState = state;
        }
    }

    public void start() {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        ctx.registerReceiver(outgoingReceiver, intentFilter);
        callRecord = new CallRecord.Builder(ctx)
                .setRecordFileName(new SimpleDateFormat("HH:mm:ss").format(new Date()))
                .setRecordDirName("ETS")
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath())
                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                .setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                .setShowPhoneNumber(true)
                .build();



        callRecord.startCallRecordService();


    }



    /**
     * Stop calls detection.
     */
    public void stopRecording() {

        callRecord.stopCallReceiver();

        callRecord.disableSaveFile();



    }
}
