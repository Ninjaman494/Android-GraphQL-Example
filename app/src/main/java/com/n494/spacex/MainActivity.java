package com.n494.spacex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.apollographql.apollo.api.Response;
import com.n494.RocketsQuery;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hook up recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Show loading
        ProgressBar progressBar = findViewById(R.id.main_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Server.fetchRockets().subscribeWith(new DisposableObserver<Response<RocketsQuery.Data>>() {
            @Override
            public void onNext(@NonNull Response<RocketsQuery.Data> dataResponse) {
                if (dataResponse.getData() != null) {
                    RocketsAdapter adapter = new RocketsAdapter(dataResponse.getData().rockets());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                // Query's done, so hide loading
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}