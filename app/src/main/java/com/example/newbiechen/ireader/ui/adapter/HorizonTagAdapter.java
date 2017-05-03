package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.adapter.view.HorizonTagView;
import com.example.newbiechen.ireader.ui.base.BaseAdapter;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-2.
 */

public class HorizonTagAdapter extends BaseAdapter<String,HorizonTagAdapter.TagViewHolder> {
    private int curSelected = -1;

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag,parent,false);
        return new TagViewHolder(view);
    }

    @Override
    public void setUpViewHolder(TagViewHolder holder, int position){
        String value = getItem(position);
        holder.tvName.setText(value);
        if (curSelected == position){
            holder.tvName.setTextColor(ContextCompat.getColor(App.getContext(),R.color.light_red));
        }
        else {
            holder.tvName.setTextColor(ContextCompat.getColor(App.getContext(),R.color.nb_text_common_h2));
        }
    }

    public void setSelectedTag(int pos){
        curSelected = pos;
        notifyDataSetChanged();
    }

    class TagViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;

        public TagViewHolder(View itemView) {
            super(itemView);
            tvName = ButterKnife.findById(itemView,R.id.tag_tv_name);
        }
    }
}
