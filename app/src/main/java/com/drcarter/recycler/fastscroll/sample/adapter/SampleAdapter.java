package com.drcarter.recycler.fastscroll.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.drcarter.recycler.fastscroll.sample.R;
import com.drcarter.recycler.fastscroll.sample.viewholder.SampleViewHolder;
import com.drcarter.recyclerview.fastscroll.OnFastScrollerIndexerLIstener;

import java.util.ArrayList;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleViewHolder> implements OnFastScrollerIndexerLIstener {

    private Context context;
    private List<String> titleList;

    public SampleAdapter(@NonNull Context context) {
        this.context = context;

        this.titleList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            this.titleList.add(i + " : " + "TEST");
        }
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return SampleViewHolder.newInstance(LayoutInflater.from(this.context).inflate(R.layout.layout_viewholder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SampleViewHolder sampleViewHolder, int i) {
        sampleViewHolder.setTitle(titleList.get(i));
    }

    @Override
    public int getItemCount() {

        if (titleList != null) {
            return titleList.size();
        }

        return 0;
    }

    @Override
    public String getIndexer(int position) {

        if (this.titleList != null) {
            return this.titleList.get(position).substring(0, 1);
        }

        return "";
    }
}
