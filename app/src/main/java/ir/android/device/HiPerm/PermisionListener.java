/*
 * ~ Copyright (c) 2021
 * ~ Dev : Amir Bahador , Amiri
 * ~ City : Iran / Abadan
 * ~ time & date : 4/4/21 4:25 PM
 * ~ email : abadan918@gmail.com
 */

package ir.android.device.HiPerm;

import java.util.List;

public interface PermisionListener {
    void onChecked(List<String> permissionGranted, List<String> permissionDenied);
}
