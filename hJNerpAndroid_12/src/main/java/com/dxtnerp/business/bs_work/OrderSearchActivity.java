package com.dxtnerp.business.bs_work;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.activity.SetActivity;
import com.dxtnerp.adapter.bs_work.OrderSearchAdapter;
import com.dxtnerp.common.ActionBarWidgetActivity;
import com.dxtnerp.common.ActivityBaseHeader;
import com.dxtnerp.common.Constant;
import com.dxtnerp.dao.BusinessQueryDao;
import com.dxtnerp.model.bs_work.ProductInfo;
import com.dxtnerp.net.HttpClientBuilder;
import com.dxtnerp.net.HttpClientManager;
import com.dxtnerp.util.myscom.StringUtils;
import com.dxtnerp.widget.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangdongdong
 * 销售订单查询
 */
public class OrderSearchActivity extends ActivityBaseHeader implements ActionBarWidgetActivity.NsyncDataConnector,
        OrderSearchAdapter.OrderSearchkListener {
    //搜索框
    @BindView(R.id.orderSearch_clEd)
    ClearEditText orderSearch_clEd;
    //列表
    @BindView(R.id.orderSearch_rvl)
    RecyclerView orderSearch_rvl;

    @BindView(R.id.orderSearch_error)
    LinearLayout orderSearchh_error;
    @BindView(R.id.orderSearch_tvCorr)
    TextView orderSearch_tvCorr;

    //判断哪个功能进去的
    private int operationID;
    //网络获取的数据
    List<ProductInfo> productInfoList;

    OrderSearchAdapter orderSearchAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            switch (msg.what) {
                case Constant.HANDLERTYPE_0:
                    //查询数据
                    searchDtata(content);
                    break;
                case Constant.HANDLERTYPE_1:
                    //设置数据
                    orderSearch_rvl.setVisibility(View.VISIBLE);
                    orderSearchh_error.setVisibility(View.GONE);
                    setRlvData(productInfoList);
                    break;
                case Constant.HANDLERTYPE_2:
                    //无数据
                    orderSearch_rvl.setVisibility(View.GONE);
                    orderSearchh_error.setVisibility(View.VISIBLE);
                    break;
                case Constant.HANDLERTYPE_3:

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_order_search);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setBase_rightTvType(View.VISIBLE);
        setBase_rightTv(getString(R.string.action_right_content_sure));
        ActionBarWidgetActivity.setNsyncDataConnector(this);
        OrderSearchAdapter.setOrderSearchkListener(this);

        base_centerTv.setText(getIntent().getStringExtra(Constant.OperationName));
        operationID = getIntent().getIntExtra(Constant.OperationID, 0);

        orderSearch_clEd.addTextChangedListener(textWatcher);

        orderSearch_rvl.setLayoutManager(new LinearLayoutManager(this));

        setHandlerMsg(mHandler, Constant.HANDLERTYPE_0, getEdVaule(orderSearch_clEd));


    }

    @Override
    protected void onRightClick() {
        super.onRightClick();

        Constant.productInfos = new ArrayList<>();

        for (int i = 0; i < productInfoList.size(); i++) {
            if (productInfoList.get(i).isCheck()){
                Constant.productInfos.add(productInfoList.get(i));
            }
        }

        if (Constant.productInfos == null){
            myToastShow(getString(R.string.toast_title_plData));
            return;
        }

        setResult(100);

        finish();
    }

    //搜索框的监听事件
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setHandlerMsg(mHandler, Constant.HANDLERTYPE_0, getEdVaule(orderSearch_clEd));
        }
    };

    /**
     * 网络获取数据
     *
     * @param edTv
     */
    private void searchDtata(String edTv) {
        waitDialog.show();
        try {
            HttpClientBuilder.HttpClientParam param = HttpClientBuilder
                    .createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
            param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                    .addKeyValue("id_table", "ctlm1020")
                    .addKeyValue("condition", "var_value like '%" + edTv + "%'");
            HttpClientManager.addTask(responseHandler, param.getHttpPost());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processJsonValue(String value) {
        waitDialog.dismiss();
        productInfoList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                String temp = jsonArray.getString(i);
                String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}") + 1);
                logShow(subValue);
                ProductInfo productInfo = mGson.fromJson(subValue, ProductInfo.class);
                productInfoList.add(productInfo);
            }

            if (productInfoList.size() == 0) {
                setHandlerMsg(mHandler, Constant.HANDLERTYPE_2, getString(R.string.toast_title_noData));
                return;
            }

            setHandlerMsg(mHandler, Constant.HANDLERTYPE_1, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置数据
     */
    private void setRlvData(List<ProductInfo> list) {
        orderSearchAdapter = new OrderSearchAdapter(context, operationID, list);
        orderSearch_rvl.setAdapter(orderSearchAdapter);
        orderSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedListener(int pos) {
        if (productInfoList.get(pos).isCheck()) {
            productInfoList.get(pos).setCheck(false);
        } else {
            productInfoList.get(pos).setCheck(true);
        }
        orderSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEdInputListener(int pos, String content) {
        productInfoList.get(pos).setDec_qty(content);
        orderSearchAdapter.notifyDataSetChanged();
    }
}
