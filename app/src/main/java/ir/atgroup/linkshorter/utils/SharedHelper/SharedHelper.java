/*
 * ~ Copyright (c) 2021
 * ~ Dev : Amir Bahador , Amiri
 * ~ City : Iran / Abadan
 * ~ time & date : 4/4/21 4:25 PM
 * ~ email : abadan918@gmail.com
 */

package ir.atgroup.linkshorter.utils.SharedHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    private SharedPreferences shp;
    private SharedPreferences.Editor shpe;

    @SuppressLint("CommitPrefEdits")
    public SharedHelper(Context context, String name) {
        shp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        shpe = shp.edit();
    }

    public String read(String key) {
        return shp.getString(key, "");
    }

    public void insert(String key, String value) {
        shpe.putString(key, value);
        shpe.apply();
    }
}
