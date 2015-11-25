package com.drcarter.recycler.fastscroll.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.drcarter.recycler.fastscroll.sample.adapter.SampleAdapter;
import com.drcarter.recyclerview.fastscroll.FastScroller;
import com.drcarter.recyclerview.fastscroll.layoutmanager.ScrollingLinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SampleAdapter adapter;
    private FastScroller fastScroller;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.layoutManager = new ScrollingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false, getResources().getInteger(R.integer.default_animatin_duration));
        this.adapter = new SampleAdapter(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.adapter);

        this.fastScroller = (FastScroller) findViewById(R.id.fastscroller);
        this.fastScroller.setRecyclerView(this.recyclerView, this.layoutManager);
        this.fastScroller.setIndexer(this.adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
