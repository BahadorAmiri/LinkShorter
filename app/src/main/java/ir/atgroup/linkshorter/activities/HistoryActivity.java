package ir.atgroup.linkshorter.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.atgroup.linkshorter.DaTaBaCe.MyDaTaBaSe;
import ir.atgroup.linkshorter.R;
import ir.atgroup.linkshorter.adapters.HistoryAdapter;
import ir.atgroup.linkshorter.models.URLs;

public class HistoryActivity extends AppCompatActivity {

    public MyDaTaBaSe myDaTaBaSe;
    RecyclerView recyclerView;
    List<URLs> urLsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        myDaTaBaSe = Room.databaseBuilder(HistoryActivity.this, MyDaTaBaSe.class, "mydb").allowMainThreadQueries().build();
        urLsList = myDaTaBaSe.dao().getAll();

        recyclerView = findViewById(R.id.history_activity_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        HistoryAdapter adapter = new HistoryAdapter(this, myDaTaBaSe);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}