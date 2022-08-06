package ir.atgroup.linkshorter.DaTaBaCe;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ir.atgroup.linkshorter.models.URLs;

@Database(entities = {URLs.class}, version = 1, exportSchema = false)
public abstract class MyDaTaBaSe extends RoomDatabase {

    public abstract DAO dao();

}