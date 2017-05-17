package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.ui.adapter.view.CategoryView;
import com.example.newbiechen.ireader.ui.base.BaseAdapter;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

import org.w3c.dom.Text;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-17.
 */

public class CategoryAdapter extends BaseAdapter<BookChapterBean,CategoryAdapter.CategoryViewHolder>{

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    //数据绑定
    @Override
    public void bindData(CategoryViewHolder holder, BookChapterBean data, int position){

    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView mTvChapter;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            mTvChapter = ButterKnife.findById(itemView, R.id.category_tv_chapter);
        }
    }
}
