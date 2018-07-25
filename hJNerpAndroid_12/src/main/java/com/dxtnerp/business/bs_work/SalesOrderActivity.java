package com.dxtnerp.business.bs_work;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dxtnerp.R;
import com.dxtnerp.common.ActivityBaseHeader;
import com.dxtnerp.common.Constant;

/**
 * @author zhangdongdong
 * 销售订单
 */
public class SalesOrderActivity extends ActivityBaseHeader {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_sales_order);

        initView();
    }

    private void initView(){
        setBase_rightTvType(View.VISIBLE);
        setBase_rightTv(getString(R.string.action_right_content_add));
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();
        intentActivity(SalesOrderAddActivity.class);
    }
}
