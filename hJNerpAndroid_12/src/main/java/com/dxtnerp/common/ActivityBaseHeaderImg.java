package com.dxtnerp.common;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.util.img.BitmapUtils;


/**
 * @author zhangdongdong
 *  左边返回，中间标题，右边图片
 *
 */
public class ActivityBaseHeaderImg extends ActivitySupport implements View.OnClickListener,
        PopupWindow.OnDismissListener{

    protected DrawerLayout main_header_layout;

    protected Toolbar base_toolbar;
    protected TextView base_imgleftTv;
    protected TextView base_imgCenterTitle;
    protected ImageView base_imgRightImg;
    //popupwindow选择框
    protected PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.module_activity_base_header_img);

        init();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View.inflate(this, layoutResID, (ViewGroup) findViewById(R.id.mainFrameLayout));
    }

    private void init() {

        base_toolbar = (Toolbar) findViewById(R.id.main_headerToolbar);
        base_imgleftTv = (TextView) findViewById(R.id.base_imgleftTv);
        base_imgCenterTitle = (TextView) findViewById(R.id.base_imgCenterTitle);
        base_imgRightImg = (ImageView) findViewById(R.id.base_imgRightImg);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);


        base_imgleftTv.setOnClickListener(this);
        base_imgRightImg.setOnClickListener(this);


        //设置左边标题图片
        setTbLeftResourcesDw(R.drawable.module_tolbar_backup);
        //设置右边标题图片
        setTbRightResourcesDw(R.drawable.icon_action_back_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.base_imgleftTv:
                onLeftClick();
                break;
            case R.id.base_imgRightImg:
                onTbRightClick(v);
                break;
            case R.id.tv_pick_pw1:
                //popupWindow标题1

                break;
            case R.id.tv_pick_pw2:
                //popupWindow标题2

                break;
        }
    }

    /**
     * 左边标题的点击事件
     */
    protected void onLeftClick() {
        finish();
    }

    /**
     * toolbar 设置左边标题图片
     * @param imgID
     */
    protected void setTbLeftResourcesDw(int imgID){
        Drawable drawable = getResources().getDrawable(imgID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        base_imgleftTv.setCompoundDrawables(drawable, null, null, null);//画在右边
    }


    /**
     * 设置标题
     * @param tvText
     */
    protected void setMainTitle(String tvText){
        if (tvText != null) {
            if (base_imgCenterTitle != null) {
                base_imgCenterTitle.setText(tvText);
            }
        }
    }

    /**
     * toolbar 右边图片点击事件
     */
    protected void onTbRightClick(View view) {
        openPopupWindow(view);
    }

    /**
     * toolbar 设置右边标题图片
     * @param imgID
     */
    protected void setTbRightResourcesDw(int imgID){
        base_imgRightImg.setImageDrawable(BitmapUtils.getResourcesDw(context,imgID));
    }

    /**
     * 显示popup
     *
     * @param v
     */
    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_pw1, tv_pick_pw2, tv_cancel;
        tv_pick_pw1 = (TextView) view.findViewById(R.id.tv_pick_pw1);
        tv_pick_pw2 = (TextView) view.findViewById(R.id.tv_pick_pw1);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_pick_pw1.setOnClickListener(this);
        tv_pick_pw2.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }


    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (main_header_layout.isDrawerOpen(Gravity.LEFT)) {
                //返回操作

            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
