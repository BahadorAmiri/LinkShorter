package ir.atgroup.linkshorter.DaTaBaCe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.atgroup.linkshorter.models.URLs;

@Dao
public interface DAO {

    @Query("SELECT * FROM URLs")
    List<URLs> getAll();

    @Insert
    void insert(List<URLs> urLs);

    @Delete
    void delete(URLs urLs);

}
