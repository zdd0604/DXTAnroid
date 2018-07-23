package com.dxtnerp.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.util.img.BitmapUtils;
import com.dxtnerp.util.StringUtil;

/**
 * @author zhangdongdong
 * 返回上级菜单、中间标题、右边标题(默认隐藏)
 */
public class ActivityBaseHeader extends ActionBarWidgetActivity {
    Toolbar base_toolbar;
    protected TextView base_leftTv;
    protected TextView base_centerTv;
    protected TextView base_rightTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.module_activity_base_header);

        init();
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View.inflate(this, layoutResID, (ViewGroup) findViewById(R.id.base_content));
    }


    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.base_toolbar));
        base_toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        base_leftTv = (TextView) findViewById(R.id.base_leftTv);
        base_centerTv = (TextView) findViewById(R.id.base_centerTv);
        base_rightTv = (TextView) findViewById(R.id.base_rightTv);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);


        //设置返回按钮图片
        setLeftTvImg(R.drawable.module_tolbar_backup);
        //设置标题
        if (StringUtil.isStrTrue(Constant.Clicked_xml_nameModel))
            setBaseCenterTv(Constant.Clicked_xml_nameModel);

        base_leftTv.setOnClickListener(onClickListener);
        base_rightTv.setOnClickListener(onClickListener);

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.base_leftTv:
                    onLeftClick();
                    break;
                case R.id.base_rightTv:
                    onRightClick();
                    break;
            }
        }
    };


    /**
     * 设置资源文件的图片
     *
     * @param imgID
     */
    protected void setTolbarResourcesDw(int imgID) {
        base_toolbar.setBackground(BitmapUtils.getResourcesDw(context, imgID));
    }

    /**
     * 设置左边标题 图片适用于返回
     *
     * @param imgID
     */
    protected void setLeftTvImg(int imgID) {
        Drawable drawable = getResources().getDrawable(imgID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        base_leftTv.setCompoundDrawables(drawable, null, null, null);//画在右边
    }

    /**
     * 左边标题的点击事件
     */
    protected void onLeftClick() {
        finish();
    }

    /**
     * 设置中间标题
     *
     * @param tvText
     */
    public void setBaseCenterTv(String tvText) {
        if (tvText != null) {
            if (base_centerTv != null) {
                base_centerTv.setText(tvText);
            }
        }
    }

    /**
     * 设置右边标题
     */
    protected void setBase_rightTv(String tvText) {
        if (tvText != null) {
            if (base_rightTv != null) {
                base_rightTv.setText(tvText);
            }
        }
    }

    /**
     * 设置右边标题显示与隐藏
     */
    protected void setBase_rightTvType(int tvType) {
        base_rightTv.setVisibility(tvType);
    }

    /**
     * 右标题点击事件
     */
    protected void onRightClick() {

    }

}
