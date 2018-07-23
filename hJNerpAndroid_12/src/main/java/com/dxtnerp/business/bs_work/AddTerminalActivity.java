package com.dxtnerp.business.bs_work;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.business.bs_callback.BFlagCallBack;
import com.dxtnerp.common.ActivityBaseHeader;
import com.dxtnerp.common.ActivitySupport;
import com.dxtnerp.common.Constant;
import com.dxtnerp.common.EapApplication;
import com.dxtnerp.model.md_business.BusinessFlag;
import com.dxtnerp.util.StringUtil;
import com.dxtnerp.widget.ClearEditText;
import com.lzy.okgo.OkGo;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhangdongdong
 * 新增终端
 */
public class AddTerminalActivity extends ActivityBaseHeader implements View.OnClickListener,
        ActivitySupport.OnMyLocationLitener{
    //终端名称
    @BindView(R.id.bs_addterminal_input_1)
    ClearEditText bs_addterminal_input_1;
    //终端类型
    @BindView(R.id.bs_addterminal_title_2)
    TextView bs_addterminal_title_2;
    @BindView(R.id.bs_addterminal_input_2)
    TextView bs_addterminal_input_2;
    //所属部门
    @BindView(R.id.bs_addterminal_title_3)
    TextView bs_addterminal_title_3;
    @BindView(R.id.bs_addterminal_input_3)
    TextView bs_addterminal_input_3;
    //终端区域
    @BindView(R.id.bs_addterminal_title_4)
    TextView bs_addterminal_title_4;
    @BindView(R.id.bs_addterminal_input_4)
    TextView bs_addterminal_input_4;
    //担当经销商
    @BindView(R.id.bs_addterminal_title_5)
    TextView bs_addterminal_title_5;
    @BindView(R.id.bs_addterminal_input_5)
    TextView bs_addterminal_input_5;
    //担当业务员
    @BindView(R.id.bs_addterminal_input_6)
    TextView bs_addterminal_input_6;
    //终端级别
    @BindView(R.id.bs_addterminal_title_7)
    TextView bs_addterminal_title_7;
    @BindView(R.id.bs_addterminal_input_7)
    TextView bs_addterminal_input_7;
    //终端属性
    @BindView(R.id.bs_addterminal_title_8)
    TextView bs_addterminal_title_8;
    @BindView(R.id.bs_addterminal_input_8)
    TextView bs_addterminal_input_8;
    //是否合作
    @BindView(R.id.bs_addterminal_title_9)
    TextView bs_addterminal_title_9;
    @BindView(R.id.bs_addterminal_input_9)
    TextView bs_addterminal_input_9;
    //联系人
    @BindView(R.id.bs_addterminal_input_10)
    ClearEditText bs_addterminal_input_10;
    //联系电话
    @BindView(R.id.bs_addterminal_input_11)
    ClearEditText bs_addterminal_input_11;
    //详细地址
    @BindView(R.id.bs_addterminal_input_12)
    ClearEditText bs_addterminal_input_12;
    //获取位置
    @BindView(R.id.bs_AddTerminal_TitleTv13)
    TextView bs_AddTerminal_TitleTv13;
    @BindView(R.id.bs_addterminal_input_13)
    TextView bs_addterminal_input_13;
    //录入人
    @BindView(R.id.bs_addterminal_input_14)
    ClearEditText bs_addterminal_input_14;


    private String st_addterminal_input_1;
    private String st_addterminal_input_2;
    private String st_addterminal_input_3;
    private String st_addterminal_input_4;
    private String st_addterminal_input_5;
    private String st_addterminal_input_6;
    private String st_addterminal_input_7;
    private String st_addterminal_input_8;
    private String st_addterminal_input_9;
    private String st_addterminal_input_10;
    private String st_addterminal_input_11;
    private String st_addterminal_input_12;
    private String st_addterminal_input_13;
    private String st_addterminal_input_14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_add_terminal);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setBase_rightTvType(View.VISIBLE);
        setBase_rightTv(getString(R.string.action_right_content_commit));
        ActivitySupport.setOnMyLocationLitener(this);
        bs_addterminal_input_2.setOnClickListener(this);
        bs_addterminal_input_3.setOnClickListener(this);
        bs_addterminal_input_4.setOnClickListener(this);
        bs_addterminal_input_5.setOnClickListener(this);
        bs_addterminal_input_7.setOnClickListener(this);
        bs_addterminal_input_8.setOnClickListener(this);
        bs_addterminal_input_9.setOnClickListener(this);
        bs_AddTerminal_TitleTv13.setOnClickListener(this);
        bs_AddTerminal_TitleTv13.setTextColor(getResources().getColor(R.color.deepskyblue));

    }



    @Override
    protected void onRightClick() {
        super.onRightClick();

        commit();
    }


    /**
     * 1、判断数据
     * 2、生成数据
     * 3、提交数据
     */
    private void commit() {

        getData();

        if (!StringUtil.isStrTrue(st_addterminal_input_1)) {
            myToastShow(getString(R.string.toast_Error_Hint1));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_2)) {
            myToastShow(getString(R.string.toast_Error_Hint2));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_3)) {
            myToastShow(getString(R.string.toast_Error_Hint3));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_4)) {
            myToastShow(getString(R.string.toast_Error_Hint4));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_7)) {
            myToastShow(getString(R.string.toast_Error_Hint7));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_8)) {
            myToastShow(getString(R.string.toast_Error_Hint8));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_9)) {
            myToastShow(getString(R.string.toast_Error_Hint9));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_10)) {
            myToastShow(getString(R.string.toast_Error_Hint10));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_11)) {
            myToastShow(getString(R.string.toast_Error_Hint11));
            return;
        }
        if (!StringUtil.isStrTrue(st_addterminal_input_12)) {
            myToastShow(getString(R.string.toast_Error_Hint12));
            return;
        }

    }

    private void getData() {
        st_addterminal_input_1 = getEditextCt(bs_addterminal_input_1);
        st_addterminal_input_2 = getTvVaule(bs_addterminal_input_2);
        st_addterminal_input_3 = getTvVaule(bs_addterminal_input_3);
        st_addterminal_input_4 = getTvVaule(bs_addterminal_input_4);
        st_addterminal_input_5 = getTvVaule(bs_addterminal_input_5);
        st_addterminal_input_6 = getTvVaule(bs_addterminal_input_6);
        st_addterminal_input_7 = getTvVaule(bs_addterminal_input_7);
        st_addterminal_input_8 = getTvVaule(bs_addterminal_input_8);
        st_addterminal_input_9 = getTvVaule(bs_addterminal_input_9);
        st_addterminal_input_10 = getEditextCt(bs_addterminal_input_10);
        st_addterminal_input_11 = getEditextCt(bs_addterminal_input_11);
        st_addterminal_input_12 = getEditextCt(bs_addterminal_input_12);
        st_addterminal_input_13 = getTvVaule(bs_addterminal_input_13);
        st_addterminal_input_14 = getEditextCt(bs_addterminal_input_14);

    }

    /**
     * 提交数据
     *
     * @param datas
     */
    private void getBusinessList(String datas) {
        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + "/servlet/DataUpdateServlet")
                .isMultipart(true)
                .params("datas", datas)
                .cacheKey(Constant.ID_MENU)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .execute(new BFlagCallBack<BusinessFlag>() {
                    @Override
                    public void onSuccess(BusinessFlag businessFlag, Call call, Response response) {
                        String content = businessFlag.getMessage();
                        myToastShow(getString(R.string.toast_Message_CommitSucceed));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        myToastShow(getString(R.string.toast_Message_CommitFail));
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.bs_addterminal_input_2:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_2));
                bundle.putInt(Constant.OperationID, 2);
                intentActivity(BillsSureActivity.class, bundle, 2);
                break;
            case R.id.bs_addterminal_input_3:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_3));
                bundle.putInt(Constant.OperationID, 3);
                intentActivity(BillsSureActivity.class, bundle, 3);
                break;
            case R.id.bs_addterminal_input_4:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_4));
                bundle.putInt(Constant.OperationID, 4);
                intentActivity(BillsSureActivity.class, bundle, 4);
                break;
            case R.id.bs_addterminal_input_5:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_5));
                bundle.putInt(Constant.OperationID, 5);
                intentActivity(BillsSureActivity.class, bundle, 5);
                break;
            case R.id.bs_addterminal_input_7:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_7));
                bundle.putInt(Constant.OperationID, 7);
                intentActivity(BillsSureActivity.class, bundle, 7);
                break;
            case R.id.bs_addterminal_input_8:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_8));
                bundle.putInt(Constant.OperationID, 8);
                intentActivity(BillsSureActivity.class, bundle, 8);
                break;
            case R.id.bs_addterminal_input_9:
                bundle.putString(Constant.OperationName, getTvVaule(bs_addterminal_title_9));
                bundle.putInt(Constant.OperationID, 9);
                intentActivity(BillsSureActivity.class, bundle, 9);
                break;
            case R.id.bs_AddTerminal_TitleTv13:
                waitDialog.show();
                waitDialog.setText("正在获取地理位置");
                getLocationInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //实体类需要更改
        if (requestCode == 2 && resultCode == 100) {
            bs_addterminal_input_2.setText(Constant.textModule.getName());
        } else if (requestCode == 3 && resultCode == 100) {
            bs_addterminal_input_3.setText(Constant.textModule2.getName());
        } else if (requestCode == 4 && resultCode == 100) {
            bs_addterminal_input_4.setText(Constant.textModule.getName());
        } else if (requestCode == 5 && resultCode == 100) {
            bs_addterminal_input_5.setText(Constant.textModule.getName());
        } else if (requestCode == 7 && resultCode == 100) {
            bs_addterminal_input_7.setText(Constant.textModule.getName());
        } else if (requestCode == 8 && resultCode == 100) {
            bs_addterminal_input_8.setText(Constant.textModule.getName());
        } else if (requestCode == 9 && resultCode == 100) {
            bs_addterminal_input_9.setText(Constant.textModule.getName());
        }
    }

    @Override
    public void OnMyLocationLitener(String myLocation, double mLatitude, double mLongitude) {

        if (!StringUtil.isStrTrue(myLocation))
            myToastShow("定位失败");

        bs_addterminal_input_13.setText(myLocation);

        if (mLocationClient.isStarted() && mLocationClient != null)
            mLocationClient.stop();

        waitDialog.dismiss();
    }
}
