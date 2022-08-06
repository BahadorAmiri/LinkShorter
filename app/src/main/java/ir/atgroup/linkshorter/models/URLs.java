package ir.atgroup.linkshorter.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class URLs {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "shortened")
    String shortened;
    @ColumnInfo(name = "url")
    String url;

    @Ignore
    public URLs() {

    }

    public URLs(String url, String shortened) {
        this.shortened = shortened;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortened() {
        return shortened;
    }

    public void setShortened(String shortened) {
        this.shortened = shortened;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
