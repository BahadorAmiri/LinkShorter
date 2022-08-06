/*
 * ~ Copyright (c) 2021
 * ~ Dev : Amir Bahador , Amiri
 * ~ City : Iran / Abadan
 * ~ time & date : 5/4/21 1:13 PM
 * ~ email : abadan918@gmail.com
 */

package ir.atgroup.linkshorter.utils.DTCenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Map;

public class DataBaseH extends SQLiteOpenHelper {

    private final String tb_name;

    private DTCenter.Rows rows;

    public DataBaseH(@Nullable Context context, @Nullable String db_name, @Nullable String tb_name, int version, DTCenter.Rows rows) {
        super(context, db_name, null, version);
        this.tb_name = tb_name;
        this.rows = rows;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tb_name).append(" (");
        if (rows.getRows() != null) {
            boolean isFirst = true;
            for (Map.Entry<String, Row> entry : rows.getRows().entrySet()) {
                if (!isFirst) {
                    sb.append(",");
                }
                isFirst = false;
                sb.append(entry.getKey()).append(" ").append(entry.getValue().getType());
                if (entry.getValue().isPrimary()) {
                    sb.append(" PRIMARY KEY");
                }
                if (entry.getValue().isAutoincrement()) {
                    sb.append(" AUTOINCREMENT");
                }
                if (entry.getValue().isNotNull()) {
                    sb.append(" NOT NULL");
                }
            }
            sb.append(")");
        }
        db.execSQL(sb.toString());
        rows = null;
    }

    public void insert(DTCenter.Value value, DTCenter.onActionListener onActionListener) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, String> entry : value.getValue().entrySet()) {
            contentValues.put(entry.getKey(), entry.getValue());
        }
        long result = db.insert(tb_name, null, contentValues);
        if (onActionListener != null) {
            onActionListener.onSuccess(result != -1);
        }
        contentValues.clear();
        db.close();
    }

    public void delete(String row, String thing, DTCenter.onActionListener onActionListener) {

        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(tb_name, row + "=?", new String[]{thing});

        if (onActionListener != null) {
            onActionListener.onSuccess(i > 0);
        }
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("ALTER TABLE " + tb_name + " ADD COLUMN NOTES TEXT");
            onCreate(sqLiteDatabase);
        }
    }


    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + tb_name, null);
    }


}
