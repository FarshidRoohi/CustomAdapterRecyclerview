package ir.farshid_roohi.customadapterrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir.farshid_roohi.customadapterrecycleview.listener.OnClickItemListener;
import ir.farshid_roohi.customadapterrecycleview.listener.OnLoadMoreListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        final MyAdapter adapter = new MyAdapter();
        adapter.endLessScrolled(recyclerView);
        adapter.addItems(getTempItems());
        adapter.endLessScrolled(recyclerView);

        recyclerView.setAdapter(adapter);


        // handled click item in recyclerView
        adapter.setOnClickItemListener(recyclerView, new OnClickItemListener<String>() {
            @Override
            public void onClickItem(int position, String element) {
                Toast.makeText(getApplicationContext(), "item click : " + element, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickItem(int position, String element) {
                Toast.makeText(getApplicationContext(), "item long click : " + element, Toast.LENGTH_SHORT).show();

            }
        });


        // handled endless recyclerView
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
                }, 2500);
            }
        });
    }

    // get temp list item
    public List<String> getTempItems() {
        List<String> items = new ArrayList<>();

        for (int i = 0; i <= 15; i++) {
            items.add("item ");
        }

        return items;
    }
}
