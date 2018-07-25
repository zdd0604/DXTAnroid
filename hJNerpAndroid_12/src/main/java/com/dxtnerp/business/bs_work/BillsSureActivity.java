package com.dxtnerp.business.bs_work;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dxtnerp.R;
import com.dxtnerp.adapter.bs_work.Bs_SureRLAdapter;
import com.dxtnerp.common.ActivityBaseHeader;
import com.dxtnerp.common.Constant;
import com.dxtnerp.model.bs_work.TextModule;
import com.dxtnerp.model.bs_work.TextModule2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author zhangdongdong
 * 单据选择
 */
public class BillsSureActivity extends ActivityBaseHeader implements Bs_SureRLAdapter.OnItemClickLitener {

    @BindView(R.id.bs_sure_rel)
    RecyclerView bs_sure_rel;

    Bs_SureRLAdapter bs_sureRLAdapter;

    //选中的下标
    private int vernier;
    //根据此数据类型判断加载那个实体类
    private int intDataType;

    //测试数据
    List<TextModule> list;
    List<TextModule2> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_bills_sure);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Bs_SureRLAdapter.setOnItemClickLitener(this);
        base_rightTv.setVisibility(View.VISIBLE);
        base_rightTv.setText(getString(R.string.action_right_content_sure));
        base_centerTv.setText(getIntent().getStringExtra(Constant.OperationName));

        intDataType = getIntent().getIntExtra(Constant.OperationID,0);

        setdata();
        setdata2();


        bs_sure_rel.setLayoutManager(new LinearLayoutManager(this));
        bs_sureRLAdapter = new Bs_SureRLAdapter(context,intDataType,list,list2);
        bs_sure_rel.setAdapter(bs_sureRLAdapter);
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();

        switch (intDataType) {
            case Constant.OperationIDNumb2:
                Constant.textModule = list.get(vernier);
                break;
            case Constant.OperationIDNumb3:
                Constant.textModule2 = list2.get(vernier);
                break;
        }

        setResult(100);

        finish();
    }

    private void setdata() {
        list = new ArrayList<>();
        list.add(new TextModule("1","和佳1"));
        list.add(new TextModule("2","和佳2"));
        list.add(new TextModule("3","和佳3"));
        list.add(new TextModule("4","和佳4"));
        list.add(new TextModule("5","和佳5"));
        list.add(new TextModule("7","和佳7"));
        list.add(new TextModule("8","和佳8"));
        list.add(new TextModule("9","和佳9"));
        list.add(new TextModule("61","和佳61"));
        list.add(new TextModule("62","和佳62"));
        list.add(new TextModule("63","和佳63"));
        list.add(new TextModule("64","和佳64"));
        list.add(new TextModule("65","和佳65"));
        list.add(new TextModule("66","和佳66"));
        list.add(new TextModule("67","和佳67"));

    }

    private void setdata2() {
        list2 = new ArrayList<>();
        list2.add(new TextModule2("1","双创1"));
        list2.add(new TextModule2("2","双创2"));
        list2.add(new TextModule2("3","双创3"));
        list2.add(new TextModule2("4","双创4"));
        list2.add(new TextModule2("5","双创5"));
        list2.add(new TextModule2("7","双创7"));
        list2.add(new TextModule2("8","双创8"));
        list2.add(new TextModule2("9","双创9"));
        list2.add(new TextModule2("61","双创61"));
        list2.add(new TextModule2("62","双创62"));
        list2.add(new TextModule2("63","双创63"));
        list2.add(new TextModule2("64","双创64"));
        list2.add(new TextModule2("65","双创65"));
        list2.add(new TextModule2("66","双创66"));
        list2.add(new TextModule2("67","双创67"));

    }

    @Override
    public void onItemClick(int position) {
        vernier = position;
    }
}
