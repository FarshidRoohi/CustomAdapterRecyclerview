package ir.farshid_roohi.customadapterrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir.farshid_roohi.customadapterrecycleview.listener.OnLoadMoreListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView    recyclerView = findViewById(R.id.recycler_view);

        final MyAdapter adapter      = new MyAdapter();
        adapter.endLessScrolled(recyclerView);
        adapter.addItems(getTempItems());
        recyclerView.setAdapter(adapter);

        adapter.endLessScrolled(recyclerView);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                adapter.showLoading();

                // request or load other items...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItems(getTempItems());
                    }
                }, 3000);
            }
        });
    }

    int countItemTemp = 0;

    public List<String> getTempItems() {
        List<String> items = new ArrayList<>();

        for (int i = countItemTemp; i <= 15; i++) {
            items.add("item #" + new Random().nextInt(1000));
        }

        return items;
    }
}
