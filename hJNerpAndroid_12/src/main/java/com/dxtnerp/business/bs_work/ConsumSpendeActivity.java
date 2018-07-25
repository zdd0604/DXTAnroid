package com.dxtnerp.business.bs_work;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.common.ActivityBaseHeader;
import com.dxtnerp.common.Constant;
import com.dxtnerp.util.StringUtil;
import com.dxtnerp.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsumSpendeActivity extends ActivityBaseHeader implements View.OnClickListener {
    //
    @BindView(R.id.bs_ConsumSpende_input1)
    TextView bs_ConsumSpende_input1;
    //
    @BindView(R.id.bs_ConsumSpende_input2)
    TextView bs_ConsumSpende_input2;
    //
    @BindView(R.id.bs_ConsumSpende_title3)
    TextView bs_ConsumSpende_title3;
    @BindView(R.id.bs_ConsumSpende_input3)
    TextView bs_ConsumSpende_input3;
    //
    @BindView(R.id.bs_ConsumSpende_input4)
    ClearEditText bs_ConsumSpende_input4;
    //
    @BindView(R.id.bs_ConsumSpende_input5)
    ClearEditText bs_ConsumSpende_input5;
    //
    @BindView(R.id.bs_ConsumSpende_input6)
    ClearEditText bs_ConsumSpende_input6;
    //
    @BindView(R.id.bs_ConsumSpende_input7)
    ClearEditText bs_ConsumSpende_input7;
    //
    @BindView(R.id.bs_ConsumSpende_input8)
    TextView bs_ConsumSpende_input8;

    private String st_ConsumSpende_input1;
    private String st_ConsumSpende_input2;
    private String st_ConsumSpende_input3;
    private String st_ConsumSpende_input4;
    private String st_ConsumSpende_input5;
    private String st_ConsumSpende_input6;
    private String st_ConsumSpende_input7;
    private String st_ConsumSpende_input8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_consum_spende);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        setBase_rightTvType(View.VISIBLE);
        setBase_rightTv(getString(R.string.action_right_content_commit));

        bs_ConsumSpende_input2.setOnClickListener(this);
        bs_ConsumSpende_input3.setOnClickListener(this);
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();
        commit();
    }

    //提交数据
    private void commit() {

        st_ConsumSpende_input1 = getTvVaule(bs_ConsumSpende_input1);
        st_ConsumSpende_input2 = getTvVaule(bs_ConsumSpende_input2);
        st_ConsumSpende_input3 = getTvVaule(bs_ConsumSpende_input3);
        st_ConsumSpende_input4 = getTvVaule(bs_ConsumSpende_input4);
        st_ConsumSpende_input5 = getTvVaule(bs_ConsumSpende_input5);
        st_ConsumSpende_input6 = getTvVaule(bs_ConsumSpende_input6);
        st_ConsumSpende_input7 = getTvVaule(bs_ConsumSpende_input7);
        st_ConsumSpende_input8 = getTvVaule(bs_ConsumSpende_input8);

        if (!StringUtil.isStrTrue(st_ConsumSpende_input1)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint1));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input2)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint2));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input3)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint3));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input4)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint4));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input5)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint5));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input6)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint6));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input7)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint7));
            return;
        }
        if (!StringUtil.isStrTrue(st_ConsumSpende_input8)) {
            myToastShow(getString(R.string.bs_ConsumSpende_Hint8));
            return;
        }


    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.bs_ConsumSpende_input2:
                showCalendar(bs_ConsumSpende_input2);
                break;
            case R.id.bs_ConsumSpende_input3:
                bundle.putString(Constant.OperationName, getTvVaule(bs_ConsumSpende_title3));
                bundle.putInt(Constant.OperationID, 2);
                intentActivity(BillsSureActivity.class, bundle, 20);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //实体类需要更改
        if (requestCode == 20 && resultCode == 100) {
            bs_ConsumSpende_input3.setText(Constant.textModule.getName());
        }
    }
}
