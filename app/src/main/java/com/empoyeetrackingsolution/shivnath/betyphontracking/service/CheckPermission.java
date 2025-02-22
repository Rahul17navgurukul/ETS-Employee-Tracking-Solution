package com.empoyeetrackingsolution.shivnath.betyphontracking.service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckPermission extends AppCompatActivity {
    private final int REQ_CODE_ASK_ALL_PERMISSIONS = 20;
    Activity activity;

    public void checkPermission(Activity activity) {
        this.activity = activity;
        doAllPermissionChecking();


    }

    private void doAllPermissionChecking() {
        List<String> permissionsNeededForNow = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeededForNow.add("Record Audio");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeededForNow.add("Write Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeededForNow.add("Phone State");

        if (permissionsList.size() > 0) {
            if (permissionsNeededForNow.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeededForNow.get(0);
                for (int i = 1; i < permissionsNeededForNow.size(); i++)
                    message = message + ", " + permissionsNeededForNow.get(i);

                showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,
                                permissionsList.toArray(new String[permissionsList.size()]),
                                REQ_CODE_ASK_ALL_PERMISSIONS);
                    }
                });

                return;
            }
            ActivityCompat.requestPermissions(activity,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    REQ_CODE_ASK_ALL_PERMISSIONS);

            return;
        }




    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        // Marshmallow+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            }
        }
        // Pre-Marshmallow
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // *********** PERMISSION GRANTED FUNCTIONALITY ***********
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQ_CODE_ASK_ALL_PERMISSIONS:

                Map<String, Integer> permissionsMap = new HashMap<>();
                permissionsMap.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                permissionsMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permissionsMap.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    permissionsMap.put(permissions[i], grantResults[i]);
                // Check for permissions
                if (permissionsMap.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && permissionsMap.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && permissionsMap.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(activity, "Some Permission is denied. Allow it in App settings", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
                    intent.setData(uri);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                }

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
