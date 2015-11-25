package com.drcarter.recycler.fastscroll.sample.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.drcarter.recycler.fastscroll.sample.R;

public class SampleViewHolder extends RecyclerView.ViewHolder {

    private TextView textTitle;

    public static SampleViewHolder newInstance(View itemView) {
        return new SampleViewHolder(itemView);
    }

    private SampleViewHolder(View itemView) {
        super(itemView);

        this.textTitle = (TextView) itemView.findViewById(R.id.text_title);
    }

    public void setTitle(@NonNull String title) {
        this.textTitle.setText(title);
    }
}
