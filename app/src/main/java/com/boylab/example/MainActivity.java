package com.boylab.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.boylab.andlab.view.RefreshRecycler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RefreshRecycler refreshRecycler = findViewById(R.id.refreshRecycler);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("item"+i);
        }
        TestAdapter testAdapter = new TestAdapter(this, list);
        refreshRecycler.setAdapter(testAdapter);

        refreshRecycler.setOnRefreshCallBack(new RefreshRecycler.OnRefreshCallBack() {
            @Override
            public void onRefresh(@NonNull RefreshRecycler refreshRecycler) {
                Log.i("___boylab>>>___", "onRefresh: ");
            }

            @Override
            public void onLoadMore(@NonNull RefreshRecycler refreshRecycler) {
                Log.i("___boylab>>>___", "onLoadMore: ");
            }
        });




    }
}