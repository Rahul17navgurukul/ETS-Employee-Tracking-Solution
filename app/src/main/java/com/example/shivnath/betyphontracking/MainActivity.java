package com.example.shivnath.betyphontracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shivnath.betyphontracking.Activity.DashBoard;
import com.example.shivnath.betyphontracking.service.CallDetectService;
import com.example.shivnath.betyphontracking.service.CheckPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        startService(new Intent(MainActivity.this, CallDetectService.class));
        startService(new Intent(MainActivity.this, liveLocation.class));


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void login(View view) {
        TextView user = findViewById(R.id.userId);
        String userId = user.getText().toString();
        TextView pass = findViewById(R.id.password);
        String password = pass.getText().toString();

        callApi("http://159.65.145.32/api/login",userId,password);
    }

    public void callApi(String url, final String userId, String password) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",userId);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
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

                    Intent myIntent = new Intent(MainActivity.this, DashBoard.class);
                    startActivity(myIntent);


                }
            }
        });
    }
}

