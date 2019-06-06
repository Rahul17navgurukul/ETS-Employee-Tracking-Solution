package com.example.shivnath.betyphontracking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.shivnath.betyphontracking.CallDetect;
import com.aykuttasil.callrecord.CallRecord;

import java.util.Date;

public class CallHelper {

    private CallRecord callRecord = null;
    private Context ctx;
    private CallDetect outgoingReceiver;
    private String savedNumber, number;


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

            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                callStartTime = new Date();

            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                checkState(stateStr);
            }
        }

        private void checkState(String stateStr) {
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

        protected void onIncomingCallStarted(Context ctx, String number, Date start) {

        }

        protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
            callRecord.startCallReceiver();
        }

        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
            callRecord.stopCallReceiver();
        }

        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
            callRecord.stopCallReceiver();
        }

        protected void onMissedCall(Context ctx, String number, Date start) {
        }

        public void onCallStateChanged(Context context, int state, String number) {
            if (lastState == state) {
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    callStartTime = new Date();
                    savedNumber = number;
                    onIncomingCallStarted(context, number, callStartTime);
                    wasRinging = true;

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false;
                        callStartTime = new Date();

                        onOutgoingCallStarted(context, savedNumber, callStartTime);
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

        // start call recorder
        callRecord = new CallRecord.Builder(ctx)
                .setRecordDirName("Betyphon")
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
                .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
//                .setShowSeed(true)
                .build();;
        // Start recording
        callRecord.startCallReceiver();
    }

    /**
     * Stop calls detection.
     */
    public void stop() {
        ctx.unregisterReceiver(outgoingReceiver);
    }

}
