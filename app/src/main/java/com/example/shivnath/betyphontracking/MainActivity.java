package com.example.shivnath.betyphontracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.shivnath.betyphontracking.Activity.DashBoard;
import com.example.shivnath.betyphontracking.service.CallDetectService;
import com.example.shivnath.betyphontracking.service.CheckPermission;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    Button longinButton;
    EditText userid, password;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        askPermission();

        Context context;
        dialog = new ProgressDialog(MainActivity.this);

        startService(new Intent(MainActivity.this, CallDetectService.class));
        startService(new Intent(MainActivity.this, liveLocation.class));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor editor = preferences.edit();


        longinButton = findViewById(R.id.loginBnt);
        userid = findViewById(R.id.userId);
        password = findViewById(R.id.password);

        longinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(editor);

                dialog.setMessage("Please wait... ");
                dialog.show();
            }
        });



    }

    private void askPermission() {
        CheckPermission checkPermission = new CheckPermission();
        checkPermission.checkPermission(this);

    }

    private void login(SharedPreferences.Editor editor) {

        String userName = userid.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            dialog.dismiss();
            userid.setError("This field should not be empty");

        } else {
            PostLogin(userName, pass, editor);
        }
    }

    private void PostLogin(final String userName, String password, final SharedPreferences.Editor editor) {

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/login")
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
                    dialog.dismiss();
                    throw new IOException("Unexpected code " + response);
                } else {
                    String json = response.body().string();
                    System.out.println("jsonUserId" + json);

                    Intent myIntent = new Intent(MainActivity.this, DashBoard.class);
                    startActivity(myIntent);

                    try {
                        JSONObject jsonobj = new JSONObject(json);
                        String str_value = jsonobj.getString("user_id");

                        System.out.println("JsonUserId >> " + str_value);

                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        final String newstartDate = sdf.format(currentTime);


                        editor.putString("user_id", str_value).commit();
                        editor.putString("currentDate", newstartDate).commit();
                        editor.putString("userName",userid.getText().toString());
                        editor.commit();

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();

                    }



                }
            }
        });

    }


}

