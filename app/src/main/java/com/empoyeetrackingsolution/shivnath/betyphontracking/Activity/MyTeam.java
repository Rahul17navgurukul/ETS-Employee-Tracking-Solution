package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.MyTeamAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.MyTeamModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyTeam extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    RecyclerView recyclerView;
    ArrayList<MyTeamModel>myTeamModels;
    MyTeamAdapter adapter;
    Button sendInvitation;
    EditText name,email;
    String radoname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Rahul@gmail.com");
        animalNames.add("shivnath@gmail.com");
        animalNames.add("Vikas@gmail.com");
        animalNames.add("Rahul@gmail.com");
        animalNames.add("shivnath@gmail.com");
        animalNames.add("Vikas@gmail.com");
        animalNames.add("Rahul@gmail.com");
        animalNames.add("shivnath@gmail.com");
        animalNames.add("Vikas@gmail.com");


//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
////                radioGroup = findViewById()
//            }
//        });


        sendInvitation = findViewById(R.id.sendInvite);
        radioGroup = findViewById(R.id.radogrp);
        int selected = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selected);

        recyclerView = findViewById(R.id.teamRecycler);
        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyTeamAdapter(this, animalNames);
        recyclerView.setAdapter(adapter);



        sendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postOnserver();


            }
        });

    }

    private void postOnserver() {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", "rahulkashyap112.rk@gmail.com");
            jsonObject.put("subscriber_id","17");
            jsonObject.put("full_name", "Rahul");
            jsonObject.put("type", "manager");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/account_activation_mail/")
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
