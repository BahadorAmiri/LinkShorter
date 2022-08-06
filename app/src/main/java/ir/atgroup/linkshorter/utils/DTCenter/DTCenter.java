/*
 * ~ Copyright (c) 2021
 * ~ Dev : Amir Bahador , Amiri
 * ~ City : Iran / Abadan
 * ~ time & date : 5/4/21 1:12 PM
 * ~ email : abadan918@gmail.com
 */

package ir.atgroup.linkshorter.utils.DTCenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.Map;

public class DTCenter {

    private final DataBaseH db;

    public DTCenter(Context context, String db_name, String tb_name, int version, Rows rows) {
        db = new DataBaseH(context, db_name, tb_name, version, rows);
    }


    public void onInsert(Value value) {
        db.insert(value, null);
    }

    public void onInsert(Value value, onActionListener onActionListener) {
        db.insert(value, onActionListener);
    }


    public void onDelete(String row, String thing) {
        db.delete(row, thing, null);
    }

    public void onDelete(String row, String thing, onActionListener onActionListener) {
        db.delete(row, thing, onActionListener);
    }


    public void getAllData(onGetCursor getCursor) {
        new GetCursor(getCursor).execute();
    }


    @SuppressLint("StaticFieldLeak")
    public class GetCursor extends AsyncTask<String, Void, Cursor> {

        onGetCursor actionListener;

        public GetCursor(onGetCursor actionListener) {
            this.actionListener = actionListener;
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            return db.getData();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            actionListener.onGet(cursor);
        }
    }


    public interface onActionListener {
        void onSuccess(boolean isSuccess);
    }

    public interface onGetCursor {
        void onGet(Cursor cursor);
    }

    public interface Rows {
        Map<String, Row> getRows();
    }

    public interface Value {
        Map<String, String> getValue();
    }

}
