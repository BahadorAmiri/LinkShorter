/*
 * ~ Copyright (c) 2021
 * ~ Dev : Amir Bahador , Amiri
 * ~ City : Iran / Abadan
 * ~ time & date : 4/4/21 4:25 PM
 * ~ email : abadan918@gmail.com
 */

package ir.android.device.HiPerm;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class HiPerm {

    public static void onRequestPermissions(Activity activity, String[] permissions, int req) {
        ActivityCompat.requestPermissions(activity, permissions, req);
    }

    public static void onCheckPermissions(Activity activity, String[] permissions, PermisionListener permisionListener) {
        List<String> permissionGranted = new ArrayList<>();
        List<String> permissionDenied = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(activity, perm) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted.add(perm);
            } else {
                permissionDenied.add(perm);
            }
        }
        permisionListener.onChecked(permissionGranted, permissionDenied);
    }

}
