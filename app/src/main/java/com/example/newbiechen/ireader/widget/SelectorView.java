package com.example.newbiechen.ireader.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-4-17.
 * 1. 根据传入的参数，决定生成多少个View
 * 2. 设置每个第一个参数为默认的值
 * 3. 设置旋转动画特效
 */

public class SelectorView extends LinearLayout {

    private OnItemSelectedListener mListener;

    private ViewGroup parent;
    public SelectorView(Context context) {
        this(context, null);
    }

    public SelectorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parent = this;
        setOrientation(HORIZONTAL);
    }

    public void setSelectData(List<List<String>> selectType){
        for (int i=0; i<selectType.size(); ++i){
            createChildView(i,selectType.get(i));
        }
    }

    public void setSelectData(List<String> ... selectType){
        for (int i=0; i<selectType.length; ++i){
            createChildView(i,selectType[i]);
        }
    }

    private void createChildView(int flag,List<String> types){
        SelectItem item = new SelectItem(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        item.setLayoutParams(params);
        item.setTag(flag);
        item.setData(types);

        addView(item);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        mListener = listener;
    }

    public interface OnItemSelectedListener{
        /**
         * @param type:选中的类型
         * @param pos:类型中的位置
         * 位置都是从0开始的
         */
        void onItemSelected(int type,int pos);
    }


    private class SelectItem extends LinearLayout implements OnClickListener,
            AdapterView.OnItemClickListener,PopupWindow.OnDismissListener{

        private TextView tvSelected;
        private ImageView ivArrow;

        private ListPopupWindow popupWindow;
        private SelectorAdapter popupAdapter;
        private final List<String> typeList = new ArrayList<>();

        private Animation rotateAnim;
        private Animation restoreAnim;

        private boolean isOpen = false;
        public SelectItem(Context context) {
            this(context,null);
        }

        public SelectItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
            initWidget();
            initClick();
        }

        private void initView(){
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_selector,this,false);
            addView(view);

            tvSelected = ButterKnife.findById(view,R.id.selector_tv_selected);
            ivArrow = ButterKnife.findById(view,R.id.selector_iv_arrow);
            ivArrow.setScaleType(ImageView.ScaleType.MATRIX);
        }

        private void initWidget(){
            setUpAnim();
        }

        private void setUpAnim(){

            rotateAnim = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_0_to_180);
            restoreAnim = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_180_to_360);

            rotateAnim.setInterpolator(new LinearInterpolator());
            restoreAnim.setInterpolator(new LinearInterpolator());
            rotateAnim.setFillAfter(true);
            restoreAnim.setFillAfter(true);
        }

        private void openPopWindow(){
            if (popupWindow == null){
                createPopWindow();
            }
            popupWindow.show();
        }

        private void createPopWindow(){
            popupWindow = new ListPopupWindow(getContext());
            popupAdapter = new SelectorAdapter();
            popupWindow.setAnchorView(parent.getChildAt(0));
            popupWindow.setAdapter(popupAdapter);
            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            //获取焦点
            popupWindow.setModal(true);

            popupWindow.setOnItemClickListener(this);
            popupWindow.setOnDismissListener(this);
        }

        private void closePopWindow(){
            if (popupWindow != null && popupWindow.isShowing()){
                popupWindow.dismiss();
            }
        }

        private void initClick(){
            setOnClickListener(this);
        }

        private void setData(List<String> types){
            typeList.addAll(types);
            tvSelected.setText(typeList.get(0));
        }

        @Override
        public void onClick(View v) {
            if (isOpen){
                closePopWindow();
                isOpen = false;
                ivArrow.startAnimation(restoreAnim);
            }
            else{
                openPopWindow();
                isOpen = true;
                ivArrow.startAnimation(rotateAnim);
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //切换text
            tvSelected.setText(typeList.get(position));
            //设置监听器
            if (mListener != null){
                mListener.onItemSelected((int)getTag(),position);
            }
            popupAdapter.current = position;
            popupWindow.dismiss();
        }

        @Override
        public void onDismiss() {
            if (isOpen){
                isOpen = false;
                ivArrow.startAnimation(restoreAnim);
            }
        }

        /*PopupWindow内容显示类*/
        private class SelectorAdapter extends BaseAdapter{
            int current = 0;

            @Override
            public int getCount() {
                return typeList.size();
            }

            @Override
            public Object getItem(int position) {
                return typeList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_selector,null,false);
                    holder = new ViewHolder();
                    holder.tvName = ButterKnife.findById(convertView,R.id.selector_tv_type);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (current == position){
                    holder.tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_popup_text_selected));
                }
                else {
                    holder.tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_text_default));
                }
                holder.tvName.setText(typeList.get(position));
                return convertView;
            }

            private class ViewHolder{
                TextView tvName;
            }
        }
    }
}
