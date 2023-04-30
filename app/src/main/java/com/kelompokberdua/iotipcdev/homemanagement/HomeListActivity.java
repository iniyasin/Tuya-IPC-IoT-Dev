package com.kelompokberdua.iotipcdev.homemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.adapter.HomeListAdapter;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeListActivity extends AppCompatActivity {

    HomeListAdapter adapter;
    RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);

        rvList = findViewById(R.id.rvList);

        // Set list
        adapter = new HomeListAdapter();
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Query home list from server
        ThingHomeSdk.getHomeManagerInstance().queryHomeList(new IThingGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {

                adapter.data = (ArrayList<HomeBean>) homeBeans;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorCode, String error) {
                Toast.makeText(
                        HomeListActivity.this,
                        "code: " + errorCode + "error:" + error,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}