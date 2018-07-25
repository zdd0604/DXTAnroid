package com.dxtnerp.business.bs_activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.adapter.ad_biness.popupAdapter;
import com.dxtnerp.business.bs_callback.BFlagCallBack;
import com.dxtnerp.common.ActionBarWidgetActivity;
import com.dxtnerp.common.Constant;
import com.dxtnerp.common.EapApplication;
import com.dxtnerp.dao.QiXinBaseDao;
import com.dxtnerp.model.PerformanceBean;
import com.dxtnerp.model.PerformanceDatas;
import com.dxtnerp.model.UserInfo;
import com.dxtnerp.model.md_business.BusinessFlag;
import com.dxtnerp.model.md_business.BusinessPerformanceColumn;
import com.dxtnerp.model.md_business.BusinessPerformanceData;
import com.dxtnerp.model.md_business.BusinessPerformanceInfo;
import com.dxtnerp.util.Log;
import com.dxtnerp.util.StringUtil;
import com.dxtnerp.util.businessutils.BusinessTimeUtils;
import com.dxtnerp.util.date.DateUtil;
import com.dxtnerp.widget.ClearEditText;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 绩效录入
 * 绩效列表界面进入
 */
public class BusinessPerformanceInput extends ActionBarWidgetActivity implements View.OnClickListener {
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    //考核年
    @BindView(R.id.input_int_year)
    TextView input_int_year;
    //考评周期
    @BindView(R.id.name_kpiperiod_tx)
    TextView name_kpiperiod_tx;
    //单据号
    @BindView(R.id.dkpipost_no)
    TextView dkpipost_no;
    //被评人
    @BindView(R.id.input_id_clerk)
    TextView input_id_clerk;
    //添加绩效详情
    @BindView(R.id.btn_add_kpiview)
    LinearLayout btn_addKpiView;
    //添加目标详情
    @BindView(R.id.btn_add_gsview)
    LinearLayout btn_addGsView;
    //计划日期
    @BindView(R.id.input_conclude_time)
    TextView input_conclude_time;
    //驳回意见 显示
    @BindView(R.id.input_var_reject)
    TextView input_var_reject;
    //驳回意见 输入
    @BindView(R.id.input_approval_context)
    EditText input_approval_context;
    //驳回按钮
    @BindView(R.id.input_btn_disagree)
    Button input_btn_disagree;
    //保存按钮
    @BindView(R.id.input_performanc_save)
    Button input_performanc_save;
    //布局 计划日期
    @BindView(R.id.layout_conclude_time)
    LinearLayout layout_conclude_time;
    //布局 考评周期
    @BindView(R.id.layout_name_kpiperiod)
    LinearLayout layout_name_kpiperiod;
    //布局 总分
    @BindView(R.id.layout_dec_smark)
    LinearLayout layout_dec_smark;
    //布局 驳回意见显示框
    @BindView(R.id.layout_var_reject)
    LinearLayout layout_var_reject;
    //布局 绩效详情
    @BindView(R.id.kpiview)
    LinearLayout lay_kpiView;
    //布局 工作目标
    @BindView(R.id.gsview)
    LinearLayout lay_gsView;
    //布局 驳回输入框
    @BindView(R.id.layout_reject)
    LinearLayout layout_reject;
    //布局 被评人
    @BindView(R.id.layout_id_clerk)
    LinearLayout layout_id_clerk;
    //布局 考核年
    @BindView(R.id.layout_int_year)
    LinearLayout layout_int_year;

    LayoutInflater inflater;
    private PerformanceDatas pds;
    private PerformanceDatas.MainBean mainBean;
    private String conclude_time_et;
    private UserInfo myInfo;
    private String companyID;
    private String userID;
    //行号
    private int line_numb = 0;
    List<View> kpiListView = new ArrayList<>();
    List<View> gsListView = new ArrayList<>();
    private int kpiindexofView = 0;
    private int gsindexofView = 0;
    private List<PerformanceBean> details;
    private List<Integer> line_no = new ArrayList<>();

    private List<PerformanceBean> kpiBean = new ArrayList<>();
    private List<PerformanceBean> gsBean = new ArrayList<>();

    private List<BusinessPerformanceInfo> infoList = new ArrayList<>();
    private List<BusinessPerformanceInfo> oldList = new ArrayList<>();
    private List<Double> kpi_Widget = new ArrayList<>();
    private List<Double> gs_Widget = new ArrayList<>();
    //权重的综合
    private Double widget_sum = 0.0;
    private Double widgetkpi_sum = 0.0;

    private HashSet<Integer> oldLine_no = new HashSet<>();
    private HashSet<Integer> newLine_no = new HashSet<>();
    private HashSet infoHashSet = new HashSet();
    private List<Integer> nLine_no = new ArrayList<>();
    private List<Integer> uLine_no = new ArrayList<>();
    private List<Integer> dLine_no = new ArrayList<>();

    private static String cgrgound;
    private static String idclerk;
    private static String idkpiperiod;
    private static String idsupclerk;
    private static String dataplan;
    private static String idkpicate;
    private static String regect = "";
    //考评周期
    private String Id_kpiperiod;

    private Boolean reject_isture = false;
    private static int intyear;
    private static int SUCCEED_SAVE = 0;//保存成功
    private static int SUCCEED_SEND = 1;//上传成功
    private static int SUCCEED_ERROR = 2;//上传失败
    private static int SUCCEED_THREAD = 3;//延迟销毁
    //确认按钮的状态
    private Boolean submit_type = false;
    private Thread mThread;
    //判断类型
    private final int KPIType = 0;
    private final int GSTYPE = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    showFailToast(message);
                    submit_type = true;
                    //要在提交完成后删除
                    removeData();
                    break;
                case 1:
                    setResult(22);
                    showFailToast(message);
                    waitThread();
                    break;
                case 2:
                    showFailToast(message);
                    waitDialog.dismiss();
                    break;
                case 3:
                    submit_type = false;
                    //要在提交完成后删除
                    removeData();
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_performance_input);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        actionCenterTv.setText(getString(R.string.perf_Title_TvActivity));
        actionRightTv.setText(getString(R.string.action_right_content_commit));
        actionRightTv1.setText(getString(R.string.action_right_content_save));
        actionLeftTv.setOnClickListener(this);
        actionRightTv1.setVisibility(View.VISIBLE);
        actionRightTv1.setOnClickListener(this);
        actionRightTv.setOnClickListener(this);
        input_conclude_time.setOnClickListener(this);
        btn_addKpiView.setOnClickListener(this);
        btn_addGsView.setOnClickListener(this);
        name_kpiperiod_tx.setOnClickListener(this);
        inflater = LayoutInflater.from(this);

        layout_id_clerk.setVisibility(View.VISIBLE);
        layout_name_kpiperiod.setVisibility(View.VISIBLE);

        pds = Constant.performanceDatas;
        mainBean = pds.getMain();
        input_id_clerk.setText(mainBean.getName_user());
        input_int_year.setText(mainBean.getInt_year() + "");
        name_kpiperiod_tx.setText(mainBean.getName_kpiperiod());
        String conclude_time = mainBean.getDate_plan();
        conclude_time_et = conclude_time.substring(0, 10);
        input_conclude_time.setText(conclude_time_et);
        dkpipost_no.setText(mainBean.getDkpipost_no());

        //设置默认当前考评周期
        if (!StringUtil.isStrTrue(mainBean.getName_kpiperiod()))
            for (int i = 0; i < monthIntList.size(); i++) {
                if (monthIntList.get(i).equals(DateUtil.getCurrentTime(Constant.TIME_MM))) {
                    name_kpiperiod_tx.setText(montStList.get(i));
                    Id_kpiperiod = monthIntList.get(i);
                }
            }

        if (Constant.ID_MENU.equals("002055")) {
            reject_isture = true;
            input_approval_context = (EditText) findViewById(R.id.input_approval_context);
            input_btn_disagree = (Button) findViewById(R.id.input_btn_disagree);
            input_btn_disagree.setOnClickListener(this);
            layout_reject.setVisibility(View.VISIBLE);
            layout_var_reject.setVisibility(View.GONE);
        } else {
            layout_reject.setVisibility(View.GONE);
            actionRightTv1.setVisibility(View.VISIBLE);
        }

        if (StringUtil.isStrTrue(mainBean.getVar_rejust())) {
            layout_var_reject.setVisibility(View.VISIBLE);
            input_var_reject = (TextView) findViewById(R.id.input_var_reject);
            input_var_reject.setText(mainBean.getVar_rejust());
        }

        kpiType();
    }


    /**
     * 区分类别
     */
    private void kpiType() {
        details = pds.getDetails();
        for (int i = 0; i < details.size(); i++) {
            PerformanceBean per = details.get(i);
            line_no.add(Integer.valueOf(per.getLine_no()));
            oldLine_no.add(Integer.valueOf(per.getLine_no()));
            infoHashSet.add(Integer.valueOf(per.getLine_no()));
            if (per.getId_kpicate().equals("001")) {
                kpiBean.add(per);
            } else {
                gsBean.add(per);
            }
        }
        if (kpiBean.size() > 0)
            setLayouViewDatas(kpiBean, KPIType);
        if (gsBean.size() > 0)
            setLayouViewDatas(gsBean, GSTYPE);

        Collections.sort(line_no);
        line_numb = line_no.get(line_no.size() - 1);

        myInfo = QiXinBaseDao.queryCurrentUserInfo();
        if (myInfo != null) {
            userID = myInfo.userID;
            companyID = myInfo.companyID;
        }

        //保存第一次进来加载的数据方便后面进行对比
        addArryListData();
    }


    /**
     * 设置界面数据
     *
     * @param datas    kpi或者GS的数据集合
     * @param viewType 判断是KPI还是GS
     */
    private void setLayouViewDatas(List<PerformanceBean> datas, final int viewType) {
        for (int i = 0; i < datas.size(); i++) {
            final View view = inflater.inflate(R.layout.performance_item, null);
            TextView prfItemLineTitle = (TextView) view.findViewById(R.id.prfItemLineTitle);
            TextView line_no = (TextView) view.findViewById(R.id.input_line_id);
            TextView line_delete = (TextView) view.findViewById(R.id.input_line_delete);
            ClearEditText line_target = (ClearEditText) view.findViewById(R.id.input_id_kpipost);
            ClearEditText line_standard = (ClearEditText) view.findViewById(R.id.input_var_evastd);
            ClearEditText line_weight = (ClearEditText) view.findViewById(R.id.input_dec_scale);
            PerformanceBean pBean = datas.get(i);
            line_no.setText(pBean.getLine_no());
            line_target.setText(pBean.getId_kpipost());
            line_target.setSelection(pBean.getId_kpipost().length());
            line_standard.setText(pBean.getVar_evastd());
            line_standard.setSelection(pBean.getVar_evastd().length());
            line_weight.setText("" + Double.valueOf(pBean.getDec_scale()) * 100);
            Double d = Double.valueOf(pBean.getDec_scale()) * 100;
            line_weight.setText("" + d);
            line_weight.setSelection(String.valueOf(d).length());

            switch (viewType) {
                case KPIType:
                    prfItemLineTitle.setText(getString(R.string.pfItem_Title_perfKPI));
                    lay_kpiView.addView(view);
                    kpiListView.add(view);
                    break;
                case GSTYPE:
                    prfItemLineTitle.setText(getString(R.string.pfItem_Title_perfGS));
                    lay_gsView.addView(view);
                    gsListView.add(view);
                    break;
            }

            line_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (viewType) {
                        case KPIType:
                            deletelLayoutView(view, viewType);
                            break;
                        case GSTYPE:
                            deletelLayoutView(view, viewType);
                            break;
                    }
                }
            });
        }
    }

    /**
     * 删除View
     *
     * @param view
     */
    private void deletelLayoutView(final View view, final int viewType) {
        final Dialog noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);
        TextView dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.dialog_notice_tv);
        notice.setText("是否要删除该绩效明细");
        dialog_cancel_rl = (TextView) noticeDialog.findViewById(R.id.dialog_cancel_tv);
        TextView dialog_cancel_tv = (TextView) noticeDialog.findViewById(R.id.dialog_cancel_tv);
        dialog_cancel_tv.setText("取消");
        dialog_confirm_rl = (TextView) noticeDialog.findViewById(R.id.dialog_confirm_tv);
        TextView dialog_confirm_tv = (TextView) noticeDialog.findViewById(R.id.dialog_confirm_tv);
        dialog_confirm_tv.setText("删除");
        dialog_cancel_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
            }
        });
        dialog_confirm_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                switch (viewType) {
                    case KPIType:
                        //计算
                        kpiindexofView = kpiListView.indexOf(view);
                        lay_kpiView.removeView(view);
                        kpiListView.remove(view);
                        LogShow("计算后的下标：" + kpiindexofView + ",,,,," + kpiListView.size());
                        if (kpi_Widget.size() >= kpiindexofView && kpi_Widget.size() > 0) {
                            kpi_Widget.remove(kpiindexofView);
                        }
                        break;
                    case GSTYPE:
                        //计算
                        gsindexofView = gsListView.indexOf(view);
                        lay_gsView.removeView(view);
                        gsListView.remove(view);
                        LogShow("计算后的下标：" + gsindexofView + ",,,,," + kpiListView.size());
                        if (gs_Widget.size() >= gsindexofView && gs_Widget.size() > 0) {
                            gs_Widget.remove(gsindexofView);
                        }
                        break;
                }
            }
        });
        noticeDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_left_tv:
                finish();
                break;
            case R.id.name_kpiperiod_tx:
                showPopupWindow(this, name_kpiperiod_tx, montStList);
                break;
            case R.id.btn_add_kpiview:
                addLayoutView(KPIType);
                break;
            case R.id.btn_add_gsview:
                addLayoutView(GSTYPE);
                break;
            case R.id.input_performanc_save:
                if (Constant.JUDGE_TYPE) {
                    submitDatas("L", "save", "1002", userID);
                } else {
                    submitDatas(mainBean.getFlag_sts(), "save", "1002", mainBean.getId_recorder());
                }
                break;
            case R.id.action_right_tv1:
                if (Constant.JUDGE_TYPE) {
                    submitDatas("L", "save", "1002", userID);
                } else {
                    submitDatas(mainBean.getFlag_sts(), "save", "1002", mainBean.getId_recorder());
                }
                break;
            case R.id.action_right_tv:
                if (Constant.JUDGE_TYPE) {
                    submitDatas("S", "send", "1001", userID);
                } else {
                    submitDatas(mainBean.getFlag_sts(), "send", "1001", mainBean.getId_recorder());
                }
                break;
            case R.id.input_conclude_time:
                showCalendar(input_conclude_time);
                break;
            case R.id.input_btn_disagree:
                if (submit_type) {
                    if (Constant.JUDGE_TYPE) {
                        submitDatas("L", "reject", "1003", userID);
                    } else {
                        submitDatas(mainBean.getFlag_sts(), "reject", "1003", mainBean.getId_recorder());
                    }
                } else {
                    showFailToast("数据必须先保存才能驳回!");
                    removeData();
                    return;
                }
                break;
        }
    }


    /**
     * 保存一次加载的数据
     */
    private void addArryListData() {
        conclude_time_et = input_conclude_time.getText().toString();
        BusinessPerformanceInfo businessInfo = new BusinessPerformanceInfo();
        businessInfo.setTableid("dkpipost");
        businessInfo.setNo(mainBean.getDkpipost_no());
        businessInfo.setUserid(mainBean.getId_recorder());
        businessInfo.setComid(mainBean.getId_com());
        //存放绩效详情的数据
        List<BusinessPerformanceColumn> columns = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            PerformanceBean per = details.get(i);
            List<BusinessPerformanceData> datas = new ArrayList<>();
            BusinessPerformanceData businessPerformanceData = new BusinessPerformanceData();
            businessPerformanceData.setTable("dkpipost_03");
            BusinessPerformanceColumn businessPerformanceColumn = new BusinessPerformanceColumn();
            businessPerformanceColumn.setFlag_sts(mainBean.getFlag_sts());
            businessPerformanceColumn.setFlag_psts(mainBean.getFlag_sts());
            businessPerformanceColumn.setId_flow(mainBean.getId_flow());
            businessPerformanceColumn.setDate_audit(mainBean.getDate_audit());
            businessPerformanceColumn.setDate_eva(mainBean.getDate_eva());
            businessPerformanceColumn.setDate_plan(mainBean.getDate_plan());
            businessPerformanceColumn.setDec_cmark(Double.valueOf(mainBean.getDec_cmark()));
            businessPerformanceColumn.setDec_dmark(Double.valueOf(mainBean.getDec_dmark()));
            businessPerformanceColumn.setDec_emark(Double.valueOf(mainBean.getDec_emark()));
            businessPerformanceColumn.setDec_smark(Double.valueOf(mainBean.getDec_smark()));
            businessPerformanceColumn.setDec_import(Double.valueOf(per.getDec_import()));
            businessPerformanceColumn.setDec_stdmark(Double.valueOf(per.getDec_stdmark()));
            businessPerformanceColumn.setDec_mark(Double.valueOf(per.getDec_mark()));
            Double scale = Double.valueOf(per.getDec_scale()) / 100.0;
            businessPerformanceColumn.setDec_scale(scale);
            businessPerformanceColumn.setId_audit(mainBean.getId_audit());
            businessPerformanceColumn.setId_flow(mainBean.getId_flow());
            businessPerformanceColumn.setId_cgroup(mainBean.getId_cgroup());
            businessPerformanceColumn.setId_clerk(mainBean.getId_clerk());
            businessPerformanceColumn.setId_dept(mainBean.getId_dept());
            businessPerformanceColumn.setId_evapsn(mainBean.getId_evapsn());
            businessPerformanceColumn.setId_kpicate(per.getId_kpicate());
            businessPerformanceColumn.setId_kpiperiod(mainBean.getId_kpiperiod());
            businessPerformanceColumn.setId_kpiphase(mainBean.getId_kpiphase());
            businessPerformanceColumn.setId_kpigrade(mainBean.getId_kpigrade());
            businessPerformanceColumn.setId_kpipost(per.getId_kpipost());
            businessPerformanceColumn.setId_kpitype(mainBean.getId_kpitype());
            businessPerformanceColumn.setId_post(mainBean.getId_post());
            businessPerformanceColumn.setId_supclerk(mainBean.getId_supclerk());
            businessPerformanceColumn.setId_table(per.getId_table());
            businessPerformanceColumn.setInt_year(Integer.valueOf(mainBean.getInt_year()));
            businessPerformanceColumn.setLine_no(Integer.valueOf(per.getLine_no()));
            businessPerformanceColumn.setOri_line_no(Integer.valueOf(per.getOri_line_no()));
            businessPerformanceColumn.setVar_comp(per.getVar_comp());
            businessPerformanceColumn.setVar_dremark(per.getVar_dremark());
            businessPerformanceColumn.setVar_evastd(per.getVar_evastd());
            businessPerformanceColumn.setOri_no(per.getOri_no());
            businessPerformanceColumn.setVar_evares(mainBean.getVar_evares());
            businessPerformanceColumn.setVar_rejust(mainBean.getVar_rejust());
            businessPerformanceColumn.setVar_remark(mainBean.getVar_remark());
            columns.add(businessPerformanceColumn);
            businessPerformanceData.setData(columns);
            datas.add(businessPerformanceData);
            businessInfo.setData(datas);
        }
        infoList.add(businessInfo);
    }


    private void showPopupWindow(Context context, View anchorView, List<String> content) {
        View contentView = getPopupWindowContentView(context, content);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = anchorView.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
        popupWindow.showAsDropDown(anchorView, xOffset, 0);    // 在mButton2的中间显示
    }

    private View getPopupWindowContentView(Context context, final List<String> content) {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        final View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        ListView dgtdrech_list = (ListView) contentView.findViewById(R.id.dgtdrech_list);
        popupAdapter popupAdapter = new popupAdapter(context, content);

        dgtdrech_list.setAdapter(popupAdapter);
        dgtdrech_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name_kpiperiod_tx.setText(content.get(position));
                Id_kpiperiod = monthIntList.get(position);
                LogShow(Id_kpiperiod);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
        return contentView;
    }

    private void submitDatas(String flag_sts, String dealtype, String buttonid, String id_recorder) {
        removeData();
        waitDialog.show();
        if (kpiListView.size() == 0 && gsListView.size() == 0) {
            showFailToast("至少添加一条详情记录");
            removeData();
            return;
        }
        conclude_time_et = input_conclude_time.getText().toString();
        if (reject_isture) {
            regect = input_approval_context.getText().toString();
        }
        BusinessPerformanceInfo businessInfo = new BusinessPerformanceInfo();
        businessInfo.setTableid("dkpipost");
        businessInfo.setUserid(userID);
        businessInfo.setComid(companyID);
        businessInfo.setNo(mainBean.getDkpipost_no());

        //存放绩效详情的数据
        List<BusinessPerformanceColumn> columns = new ArrayList<>();
        for (int i = 0; i < kpiListView.size(); i++) {
            View view = kpiListView.get(i);
            TextView line_no = (TextView) view.findViewById(R.id.input_line_id);
            TextView line_delete = (TextView) view.findViewById(R.id.input_line_delete);
            ClearEditText input_id_kpipost = (ClearEditText) view.findViewById(R.id.input_id_kpipost);
            ClearEditText input_var_evastd = (ClearEditText) view.findViewById(R.id.input_var_evastd);
            EditText input_dec_scale = (EditText) view.findViewById(R.id.input_dec_scale);
            if (!StringUtil.isStrTrue(input_id_kpipost.getText().toString())) {
                showFailToast("关键绩效 " + line_no.getText().toString().trim() + " 指标不能为空");
                removeData();
                return;
            }
            if (!StringUtil.isStrTrue(input_var_evastd.getText().toString())) {
                showFailToast("关键绩效 " + line_no.getText().toString().trim() + " 评价标准不能为空");
                removeData();
                return;
            }
            if (!StringUtil.isStrTrue(input_dec_scale.getText().toString())) {
                showFailToast("关键绩效 " + line_no.getText().toString().trim() + " 权重不能为空");
                removeData();
                return;
            }
            List<BusinessPerformanceData> datas = new ArrayList<>();
            BusinessPerformanceData businessPerformanceData = new BusinessPerformanceData();
            businessPerformanceData.setTable("dkpipost_03");
            BusinessPerformanceColumn businessPerformanceColumn = new BusinessPerformanceColumn();
            businessPerformanceColumn.setFlag_sts(flag_sts);
            businessPerformanceColumn.setFlag_psts("");
            businessPerformanceColumn.setDate_audit("1900-01-01 00:00:00");
            businessPerformanceColumn.setDate_eva(DateUtil.getCurrentTime(Constant.TIME_yyyy_MM_dd_HH_mm_ss));
//            String time = conclude_time_et + BusinessTimeUtils.getCurrentTime(Constant.NOWTIME2);
            businessPerformanceColumn.setDate_plan(conclude_time_et);
            Log.v("show", "时间：" + conclude_time_et);
//            businessPerformanceColumn.setDate_plan(mainBean.getDate_plan());
            businessPerformanceColumn.setDec_cmark(0.00);
            businessPerformanceColumn.setDec_dmark(0.00);
            businessPerformanceColumn.setDec_emark(0.00);
            businessPerformanceColumn.setDec_smark(0.00);
            businessPerformanceColumn.setDec_import(0.0000000);
            businessPerformanceColumn.setDec_stdmark(0.0000000);
            businessPerformanceColumn.setDec_mark(0.0);
            Double scale = Double.valueOf(input_dec_scale.getText().toString()) / 100.0;
            businessPerformanceColumn.setDec_scale(scale);
            kpi_Widget.add(scale);
            businessPerformanceColumn.setId_audit("");
            businessPerformanceColumn.setId_flow("0601");
            businessPerformanceColumn.setId_cgroup(mainBean.getId_cgroup());
            businessPerformanceColumn.setId_clerk(mainBean.getId_clerk());
            businessPerformanceColumn.setId_dept(mainBean.getId_dept());
            businessPerformanceColumn.setId_evapsn("");
            businessPerformanceColumn.setId_kpicate("001");
            businessPerformanceColumn.setId_kpiperiod(Id_kpiperiod);
            businessPerformanceColumn.setId_kpiphase(mainBean.getId_kpiphase());
            businessPerformanceColumn.setId_kpigrade(mainBean.getId_kpigrade());
            businessPerformanceColumn.setId_kpipost(input_id_kpipost.getText().toString());
            businessPerformanceColumn.setId_kpitype("001");
            businessPerformanceColumn.setId_post(mainBean.getId_post());
            businessPerformanceColumn.setId_supclerk(mainBean.getId_supclerk());
            businessPerformanceColumn.setId_table("");
            businessPerformanceColumn.setId_recorder(id_recorder);
            businessPerformanceColumn.setInt_year(Integer.valueOf(mainBean.getInt_year()));
            businessPerformanceColumn.setLine_no(Integer.valueOf(line_no.getText().toString()));
            businessPerformanceColumn.setOri_line_no(0);
            businessPerformanceColumn.setVar_comp("");
            businessPerformanceColumn.setVar_dremark("");
            businessPerformanceColumn.setVar_evastd(input_var_evastd.getText().toString());
            businessPerformanceColumn.setOri_no("");
            businessPerformanceColumn.setVar_evares("");
            businessPerformanceColumn.setVar_rejust(regect);
            businessPerformanceColumn.setVar_remark("");
            newLine_no.add(Integer.valueOf(line_no.getText().toString()));
//            infoHashSet.add(Integer.valueOf(item_line_id.getText().toString()));
            columns.add(businessPerformanceColumn);
            businessPerformanceData.setData(columns);
            datas.add(businessPerformanceData);
            businessInfo.setData(datas);
        }

        for (int i = 0; i < gsListView.size(); i++) {
            View view = gsListView.get(i);
            TextView line_no = (TextView) view.findViewById(R.id.input_line_id);
            TextView line_delete = (TextView) view.findViewById(R.id.input_line_delete);
            ClearEditText input_id_kpipost = (ClearEditText) view.findViewById(R.id.input_id_kpipost);
            ClearEditText input_var_evastd = (ClearEditText) view.findViewById(R.id.input_var_evastd);
            EditText input_dec_scale = (EditText) view.findViewById(R.id.input_dec_scale);
            if (!StringUtil.isStrTrue(input_id_kpipost.getText().toString())) {
                showFailToast("工作目标 " + line_no.getText().toString().trim() + "  指标不能为空");
                removeData();
                return;
            }
            if (!StringUtil.isStrTrue(input_var_evastd.getText().toString())) {
                showFailToast("工作目标 " + line_no.getText().toString().trim() + "  评价标准不能为空");
                removeData();
                return;
            }
            if (!StringUtil.isStrTrue(input_dec_scale.getText().toString())) {
                showFailToast("工作目标 " + line_no.getText().toString().trim() + "  权重不能为空");
                removeData();
                return;
            }
            List<BusinessPerformanceData> datas = new ArrayList<>();
            BusinessPerformanceData businessPerformanceData = new BusinessPerformanceData();
            businessPerformanceData.setTable("dkpipost_03");
            BusinessPerformanceColumn businessPerformanceColumn = new BusinessPerformanceColumn();
            businessPerformanceColumn.setFlag_sts(flag_sts);
            businessPerformanceColumn.setFlag_psts("");
            businessPerformanceColumn.setDate_audit("1900-01-01 00:00:00");
            businessPerformanceColumn.setDate_eva(DateUtil.getCurrentTime(Constant.TIME_yyyy_MM_dd_HH_mm_ss));
//            String time = conclude_time_et + BusinessTimeUtils.getCurrentTime(Constant.NOWTIME2);
            businessPerformanceColumn.setDate_plan(conclude_time_et);
            businessPerformanceColumn.setDec_cmark(0.00);
            businessPerformanceColumn.setDec_dmark(0.00);
            businessPerformanceColumn.setDec_emark(0.00);
            businessPerformanceColumn.setDec_smark(0.00);
            businessPerformanceColumn.setDec_import(0.0000000);
            businessPerformanceColumn.setDec_stdmark(0.0000000);
            businessPerformanceColumn.setDec_mark(0.0);
            Double scale = Double.valueOf(input_dec_scale.getText().toString()) / 100.0;
            businessPerformanceColumn.setDec_scale(scale);
            gs_Widget.add(scale);
            businessPerformanceColumn.setId_audit("");
            businessPerformanceColumn.setId_flow("0601");
            businessPerformanceColumn.setId_cgroup(mainBean.getId_cgroup());
            businessPerformanceColumn.setId_clerk(mainBean.getId_clerk());
            businessPerformanceColumn.setId_dept(mainBean.getId_dept());
            businessPerformanceColumn.setId_evapsn("");
            businessPerformanceColumn.setId_kpicate("002");
            businessPerformanceColumn.setId_kpiperiod(Id_kpiperiod);
            businessPerformanceColumn.setId_kpiphase(mainBean.getId_kpiphase());
            businessPerformanceColumn.setId_kpigrade("");
            businessPerformanceColumn.setId_recorder(id_recorder);
            businessPerformanceColumn.setId_kpipost(input_id_kpipost.getText().toString());
            businessPerformanceColumn.setId_kpitype("001");
            businessPerformanceColumn.setId_post(mainBean.getId_post());
            businessPerformanceColumn.setId_supclerk(mainBean.getId_supclerk());
            businessPerformanceColumn.setId_table("");
            businessPerformanceColumn.setInt_year(Integer.valueOf(mainBean.getInt_year()));
            businessPerformanceColumn.setLine_no(Integer.valueOf(line_no.getText().toString()));
            businessPerformanceColumn.setOri_line_no(0);
            businessPerformanceColumn.setVar_comp("");
            businessPerformanceColumn.setVar_dremark("");
            businessPerformanceColumn.setVar_evastd(input_var_evastd.getText().toString());
            businessPerformanceColumn.setOri_no("");
            businessPerformanceColumn.setVar_evares("");
            businessPerformanceColumn.setVar_rejust(regect);
            businessPerformanceColumn.setVar_remark("");
            newLine_no.add(Integer.valueOf(line_no.getText().toString()));
//            infoHashSet.add(Integer.valueOf(item_line_id.getText().toString()));
            columns.add(businessPerformanceColumn);
            businessPerformanceData.setData(columns);
            datas.add(businessPerformanceData);
            businessInfo.setData(datas);
        }

        infoList.add(businessInfo);
        Log.v("show", "旧数据：" + businessInfo.getData().toString());
        computeSum(businessInfo, dealtype, buttonid);
    }

    /**
     * 判断之前保存的一条与现在新的区别
     */
    private void judgeDatas(BusinessPerformanceInfo bInfo, String dealtype, String buttonid) {
        if (infoList.size() > 1) {
            oldList.add(infoList.get(infoList.size() - 2));
            Log.v("show", "内容1:" + oldList.toString());
            for (int i = 0; i < oldList.size(); i++) {
                BusinessPerformanceInfo oldinfo = oldList.get(i);
                List<BusinessPerformanceData> olddata = oldinfo.getData();
                for (int a = 0; a < olddata.size(); a++) {
                    List<BusinessPerformanceColumn> oldcolumn = olddata.get(a).getData();
                    for (int b = 0; b < oldcolumn.size(); b++) {
                        infoHashSet.add(oldcolumn.get(b).getLine_no());
                        oldLine_no.add(oldcolumn.get(b).getLine_no());
                    }
                }
            }

            Iterator<Integer> iterator = infoHashSet.iterator();
            while (iterator.hasNext()) {
                int content = iterator.next();
                if (oldLine_no.contains(content)) {
                    if (newLine_no.contains(content)) {
                        uLine_no.add(content);
                        Log.v("show", "修改的内容:" + oldLine_no.toString() + ",,," + content + ",,," + newLine_no.toString());
                    } else {
                        dLine_no.add(content);
                        Log.v("show", "1删除的内容:" + oldLine_no.toString() + ",,," + content + ",," + newLine_no.toString());
                    }
                } else {
                    if (newLine_no.contains(content)) {
                        nLine_no.add(content);
                        Log.v("show", "新增的内容:" + oldLine_no.toString() + ",,," + content + ",," + newLine_no.toString());
                    }
                }
                Log.v("show", "内容3:" + oldLine_no.toString() + ",,," + content + ",,," + newLine_no.toString());
            }
        } else if (oldList.size() > 0) {

            oldList.add(infoList.get(infoList.size() - 1));
            Log.v("show", "内容1:" + oldList.toString());
            for (int i = 0; i < oldList.size(); i++) {
                BusinessPerformanceInfo oldinfo = oldList.get(i);
                List<BusinessPerformanceData> olddata = oldinfo.getData();
                for (int a = 0; a < olddata.size(); a++) {
                    List<BusinessPerformanceColumn> oldcolumn = olddata.get(a).getData();
                    for (int b = 0; b < oldcolumn.size(); b++) {
                        infoHashSet.add(oldcolumn.get(b).getLine_no());
                        oldLine_no.add(oldcolumn.get(b).getLine_no());
                    }
                }
            }

            Iterator<Integer> iterator = infoHashSet.iterator();
            while (iterator.hasNext()) {
                int content = iterator.next();
                if (oldLine_no.contains(content)) {
                    if (newLine_no.contains(content)) {
                        uLine_no.add(content);
                        Log.v("show", "修改的内容:" + oldLine_no.toString() + ",,," + content + ",,," + newLine_no.toString());
                    } else {
                        dLine_no.add(content);
                        Log.v("show", "删除的内容:" + oldLine_no.toString() + ",,," + content + ",," + newLine_no.toString());
                    }
                } else {
                    if (newLine_no.contains(content)) {
                        nLine_no.add(content);
                        Log.v("show", "新增的内容:" + oldLine_no.toString() + ",,," + content + ",," + newLine_no.toString());
                    }
                }
                Log.v("show", "内容3:" + oldLine_no.toString() + ",,," + content + ",,," + newLine_no.toString());
            }
        }
        setSubString(bInfo, dealtype, buttonid);
    }

    /**
     * 计算工作目标及关键绩效指标的各项总和
     */
    private void computeSum(BusinessPerformanceInfo bInfo, String dealtype, String buttonid) {
        if (kpi_Widget.size() > 0) {
            for (Double number : kpi_Widget) {
                widgetkpi_sum += number;
            }
            Log.v("show", "list_WdigetKPI:" + kpi_Widget.toString() + "和是：" + widgetkpi_sum);
            if (widgetkpi_sum != 1.0) {
                showFailToast("关键绩效指标(KPI)的权重不等于100%");
                removeData();
                if (infoList.size() > 0) {
                    infoList.remove(infoList.size() - 1);
                }
                if (kpi_Widget.size() > 0) {
                    kpi_Widget.remove(kpi_Widget.size() - 1);
                }
                return;
            }
        } else {
            Log.v("show", "list_WdigetKPI什么数据都没有");
        }

        if (gs_Widget.size() > 0) {
            for (Double number : gs_Widget) {
                widget_sum += number;
            }

            Log.v("show", "list_WdigetGS:" + gs_Widget.toString() + "和是：" + widget_sum);
            if (widget_sum != 1.0) {
                showFailToast("工作目标(GS)的权重不等于100%");
                removeData();
                if (infoList.size() > 0) {
                    infoList.remove(infoList.size() - 1);
                }
                if (gs_Widget.size() > 0) {
                    gs_Widget.remove(gs_Widget.size() - 1);
                }
                return;
            }
        } else {
            Log.v("show", "list_WdigetGS什么数据都没有");
        }

        judgeDatas(bInfo, dealtype, buttonid);
    }


    /**
     * 添加View
     */
    private void addLayoutView(final int viewType) {
        line_numb++;
        final View view = inflater.inflate(R.layout.performance_item, null);
        TextView prfItemLineTitle = (TextView) view.findViewById(R.id.prfItemLineTitle);
        TextView line_no = (TextView) view.findViewById(R.id.input_line_id);
        line_no.setText(String.valueOf(line_numb));
        TextView line_delete = (TextView) view.findViewById(R.id.input_line_delete);
        line_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewType) {
                    case KPIType:
                        deletelLayoutView(view, KPIType);
                        break;
                    case GSTYPE:
                        deletelLayoutView(view, GSTYPE);
                        break;
                }
            }
        });

        switch (viewType) {
            case KPIType:
                prfItemLineTitle.setText(getString(R.string.pfItem_Title_perfKPI));
                lay_kpiView.addView(view);
                kpiListView.add(view);
                break;
            case GSTYPE:
                prfItemLineTitle.setText(getString(R.string.pfItem_Title_perfGS));
                lay_gsView.addView(view);
                gsListView.add(view);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        regect = "";
        Constant.billsNo = "";
    }

    /**
     * 拼接字符串
     */
    private void setSubString(BusinessPerformanceInfo bInfo, String dealtype, String buttonid) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"dkpipost\",\"opr\":\"SS\",\"no\":\"" + bInfo.getNo() + "\",\"userid\":\"" + userID + "\",\"comid\":\"" + companyID + "\"," +
                "\"menuid\":\"" + Constant.ID_MENU + "\",\"dealtype\":\"" + dealtype + "\", \"buttonid\":\"" + buttonid + "\",\"data\":[");
        List<BusinessPerformanceData> bdataList = bInfo.getData();
        for (int i = 0; i < bdataList.size(); i++) {
            BusinessPerformanceData bdata = bdataList.get(i);
            List<BusinessPerformanceColumn> bcolumnList = bdata.getData();
            for (int a = 0; a < bcolumnList.size(); a++) {
                BusinessPerformanceColumn bcolumn = bcolumnList.get(a);
                if (uLine_no.contains(bcolumn.getLine_no())) {
                    stringBuffer.append("{\"table\":\"dkpipost_03\",\"where\":\" \",\"oprdetail\":\"" + "U" + "\",\"data\":[");
                } else if (nLine_no.contains(bcolumn.getLine_no())) {
                    stringBuffer.append("{\"table\": \"dkpipost_03\",\"where\":\" \",\"oprdetail\":\"" + "N" + "\",\"data\":[");
                } else {
                    stringBuffer.append("{\"table\": \"dkpipost_03\",\"where\":\" \",\"oprdetail\":\"" + "N" + "\",\"data\":[");
                }
                stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"" + bcolumn.getFlag_sts() + "\",\"datatype\":\"varchar\"},");
                stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"" + bcolumn.getId_flow() + "\",\"datatype\":\"varchar\"},");
                //审核时间
                stringBuffer.append("{\"column\":\"date_audit\",\"value\":\"" + bcolumn.getDate_audit() + "\",\"datatype\":\"datetime\"},");
                //评价日期
                stringBuffer.append("{\"column\":\"date_eva\",\"value\":\"" + bcolumn.getDate_eva() + "\",\"datatype\":\"datetime\"},");
                //计划签订日期
                stringBuffer.append("{\"column\":\"date_plan\",\"value\":\"" + bcolumn.getDate_plan() + "\",\"datatype\":\"datetime\"},");
                dataplan = bcolumn.getDate_plan();
                //综合得分
                stringBuffer.append("{\"column\":\"dec_cmark\",\"value\":\"" + bcolumn.getDec_cmark() + "\",\"datatype\":\"decimal\"},");
                //扣分
                stringBuffer.append("{\"column\":\"dec_dmark\",\"value\":\"" + bcolumn.getDec_dmark() + "\",\"datatype\":\"decimal\"},");
                //个人得分
                stringBuffer.append("{\"column\":\"dec_emark\",\"value\":\"" + bcolumn.getDec_emark() + "\",\"datatype\":\"decimal\"},");
                //个人总分
                stringBuffer.append("{\"column\":\"dec_smark\",\"value\":\"" + bcolumn.getDec_smark() + "\",\"datatype\":\"decimal\"},");
                //已导入值
                stringBuffer.append("{\"column\":\"dec_import\",\"value\":\"" + bcolumn.getDec_import() + "\",\"datatype\":\"decimal\"},");
                //标准分值
                stringBuffer.append("{\"column\":\"dec_stdmark\",\"value\":\"" + bcolumn.getDec_stdmark() + "\",\"datatype\":\"decimal\"},");
                //得分
                stringBuffer.append("{\"column\":\"dec_mark\",\"value\":\"" + bcolumn.getDec_mark() + "\",\"datatype\":\"int\"},");
                //权重
                stringBuffer.append("{\"column\":\"dec_scale\",\"value\":\"" + bcolumn.getDec_scale() + "\",\"datatype\":\"decimal\"},");
                //中止标志
                stringBuffer.append("{\"column\":\"flag_psts\",\"value\":\"" + bcolumn.getFlag_psts() + "\",\"datatype\":\"char\"},");
                //审核人
                stringBuffer.append("{\"column\":\"id_audit\",\"value\":\"" + bcolumn.getId_audit() + "\",\"datatype\":\"varchar\"},");
                //前流程
                stringBuffer.append("{\"column\":\"id_bflow\",\"value\":\"" + bcolumn.getId_flow() + "\",\"datatype\":\"varchar\"},");
                //绩效组  获取新的字段
                stringBuffer.append("{\"column\":\"id_cgroup\",\"value\":\"" + bcolumn.getId_cgroup() + "\",\"datatype\":\"varchar\"},");
                cgrgound = bcolumn.getId_cgroup();
                //被评人
                stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + bcolumn.getId_clerk() + "\",\"datatype\":\"varchar\"},");
                idclerk = bcolumn.getId_clerk();
                //部门
                stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + bcolumn.getId_dept() + "\",\"datatype\":\"varchar\"},");
                //评价人
                stringBuffer.append("{\"column\":\"id_evapsn\",\"value\":\"" + bcolumn.getId_evapsn() + "\",\"datatype\":\"varchar\"},");
                //指标分类
                stringBuffer.append("{\"column\":\"id_kpicate\",\"value\":\"" + bcolumn.getId_kpicate() + "\",\"datatype\":\"varchar\"},");
                idkpicate = bcolumn.getId_kpicate();
                //评价等级
                stringBuffer.append("{\"column\":\"id_kpigrade\",\"value\":\"" + bcolumn.getId_kpigrade() + "\",\"datatype\":\"varchar\"},");
                //评价周期
                stringBuffer.append("{\"column\":\"id_kpiperiod\",\"value\":\"" + bcolumn.getId_kpiperiod() + "\",\"datatype\":\"varchar\"},");
                idkpiperiod = bcolumn.getId_kpiperiod();
                //考评阶段
                stringBuffer.append("{\"column\":\"id_kpiphase\",\"value\":\"" + bcolumn.getId_kpiphase() + "\",\"datatype\":\"varchar\"},");
                //指标
                stringBuffer.append("{\"column\":\"id_kpipost\",\"value\":\"" + bcolumn.getId_kpipost() + "\",\"datatype\":\"varchar\"},");
                //录入人
                stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + bcolumn.getId_recorder() + "\",\"datatype\":\"varchar\"},");
                //考核类型
                stringBuffer.append("{\"column\":\"id_kpitype\",\"value\":\"" + bcolumn.getId_kpitype() + "\",\"datatype\":\"varchar\"},");
                //岗位
                stringBuffer.append("{\"column\":\"id_post\",\"value\":\"" + bcolumn.getId_post() + "\",\"datatype\":\"varchar\"},");
                //直接主管 改名称
                stringBuffer.append("{\"column\":\"id_supclerk\",\"value\":\"" + bcolumn.getId_supclerk() + "\",\"datatype\":\"varchar\"},");
                idsupclerk = bcolumn.getId_supclerk();
                //业务名
                stringBuffer.append("{\"column\":\"id_table\",\"value\":\"" + bcolumn.getId_table() + "\",\"datatype\":\"varchar\"},");
                //考核年
                stringBuffer.append("{\"column\":\"int_year\",\"value\":\"" + bcolumn.getInt_year() + "\",\"datatype\":\"int\"},");
                intyear = bcolumn.getInt_year();
                //行号1
                stringBuffer.append("{\"column\":\"line_no\",\"value\":\"" + bcolumn.getLine_no() + "\",\"datatype\":\"int\"},");
                //原始单行号
                stringBuffer.append("{\"column\":\"ori_line_no\",\"value\":\"" + bcolumn.getOri_line_no() + "\",\"datatype\":\"int\"},");
                //原始单号
                stringBuffer.append("{\"column\":\"ori_no\",\"value\":\"" + bcolumn.getOri_no() + "\",\"datatype\":\"varchar\"},");
                //完成情况
                stringBuffer.append("{\"column\":\"var_comp\",\"value\":\"" + bcolumn.getVar_comp() + "\",\"datatype\":\"varchar\"},");
                //备注
                stringBuffer.append("{\"column\":\"var_dremark\",\"value\":\"" + bcolumn.getVar_dremark() + "\",\"datatype\":\"varchar\"},");
                //评价意见
                stringBuffer.append("{\"column\":\"var_evares\",\"value\":\"" + bcolumn.getVar_evares() + "\",\"datatype\":\"varchar\"},");
                //评价标准
                stringBuffer.append("{\"column\":\"var_evastd\",\"value\":\"" + bcolumn.getVar_evastd() + "\",\"datatype\":\"varchar\"},");
                //驳回意见
                stringBuffer.append("{\"column\":\"var_rejust\",\"value\":\"" + bcolumn.getVar_rejust() + "\",\"datatype\":\"varchar\"},");
                //备注
                stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + bcolumn.getVar_remark() + "\",\"datatype\":\"varchar\"}]}");

                if (dLine_no.size() > 0) {
                    stringBuffer.append(",");
                } else if (a != bcolumnList.size() - 1) {
                    stringBuffer.append(",");
                }
            }
        }

        if (dLine_no.size() > 0) {
            for (int b = 0; b < dLine_no.size(); b++) {
                int line_no = dLine_no.get(b);
                stringBuffer.append("{\"table\": \"dkpipost_03\",\"where\":\" \",\"oprdetail\":\"" + "D" + "\",\"data\":[");
                stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"" + "L" + "\",\"datatype\":\"varchar\"},");
                stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"" + 0601 + "\",\"datatype\":\"varchar\"},");
                //审核时间
                stringBuffer.append("{\"column\":\"date_audit\",\"value\":\"" + "1900-01-01 00:00:00" + "\",\"datatype\":\"datetime\"},");
                //评价日期
                stringBuffer.append("{\"column\":\"date_eva\",\"value\":\"" + "" + "\",\"datatype\":\"datetime\"},");
                //计划签订日期
                stringBuffer.append("{\"column\":\"date_plan\",\"value\":\"" + dataplan + "\",\"datatype\":\"datetime\"},");
                //综合得分
                stringBuffer.append("{\"column\":\"dec_cmark\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //扣分
                stringBuffer.append("{\"column\":\"dec_dmark\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //个人得分
                stringBuffer.append("{\"column\":\"dec_emark\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //个人总分
                stringBuffer.append("{\"column\":\"dec_smark\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //已导入值
                stringBuffer.append("{\"column\":\"dec_import\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //标准分值
                stringBuffer.append("{\"column\":\"dec_stdmark\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //得分
                stringBuffer.append("{\"column\":\"dec_mark\",\"value\":\"" + "" + "\",\"datatype\":\"int\"},");
                //权重
                stringBuffer.append("{\"column\":\"dec_scale\",\"value\":\"" + "" + "\",\"datatype\":\"decimal\"},");
                //中止标志
                stringBuffer.append("{\"column\":\"flag_psts\",\"value\":\"" + "" + "\",\"datatype\":\"char\"},");
                //审核人
                stringBuffer.append("{\"column\":\"id_audit\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //前流程
                stringBuffer.append("{\"column\":\"id_bflow\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //绩效组  获取新的字段
                stringBuffer.append("{\"column\":\"id_cgroup\",\"value\":\"" + cgrgound + "\",\"datatype\":\"varchar\"},");
                //被评人
                stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + idclerk + "\",\"datatype\":\"varchar\"},");
                //部门
                stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //评价人
                stringBuffer.append("{\"column\":\"id_evapsn\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //指标分类
                stringBuffer.append("{\"column\":\"id_kpicate\",\"value\":\"" + idkpicate + "\",\"datatype\":\"varchar\"},");
                //评价等级
                stringBuffer.append("{\"column\":\"id_kpigrade\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //评价周期
                stringBuffer.append("{\"column\":\"id_kpiperiod\",\"value\":\"" + idkpiperiod + "\",\"datatype\":\"varchar\"},");
                //考评阶段
                stringBuffer.append("{\"column\":\"id_kpiphase\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //指标
                stringBuffer.append("{\"column\":\"id_kpipost\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //录入人
                stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + userID + "\",\"datatype\":\"varchar\"},");
                //考核类型
                stringBuffer.append("{\"column\":\"id_kpitype\",\"value\":\"" + "001" + "\",\"datatype\":\"varchar\"},");
                //岗位
                stringBuffer.append("{\"column\":\"id_post\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //直接主管 改名称
                stringBuffer.append("{\"column\":\"id_supclerk\",\"value\":\"" + idsupclerk + "\",\"datatype\":\"varchar\"},");
                //业务名
                stringBuffer.append("{\"column\":\"id_table\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //考核年
                stringBuffer.append("{\"column\":\"int_year\",\"value\":\"" + intyear + "\",\"datatype\":\"int\"},");
                //行号
                stringBuffer.append("{\"column\":\"line_no\",\"value\":\"" + line_no + "\",\"datatype\":\"int\"},");
                //原始单行号
                stringBuffer.append("{\"column\":\"ori_line_no\",\"value\":\"" + "" + "\",\"datatype\":\"int\"},");
                //原始单号
                stringBuffer.append("{\"column\":\"ori_no\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //完成情况
                stringBuffer.append("{\"column\":\"var_comp\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //备注
                stringBuffer.append("{\"column\":\"var_dremark\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //评价意见
                stringBuffer.append("{\"column\":\"var_evares\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //评价标准
                stringBuffer.append("{\"column\":\"var_evastd\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //驳回意见
                stringBuffer.append("{\"column\":\"var_rejust\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"},");
                //备注
                stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + "" + "\",\"datatype\":\"varchar\"}]}");
                if (b != dLine_no.size() - 1) {
                    stringBuffer.append(",");
                }
            }
        }

        stringBuffer.append("]}");
        String str = stringBuffer.toString();
        Log.v("show", "............" + str);
        getBusinessList(str, dealtype);
    }


    private void setMessage(int type, Object msg) {
        Message message = new Message();
        message.obj = msg;
        message.what = type;
        handler.sendMessage(message);
    }


    /**
     * 提交数据
     *
     * @param datas
     */
    private void getBusinessList(String datas, final String dealtype) {
//        OkGo.post("http://172.16.12.243:8085/hjmerp/servlet/DataUpdateServlet")
        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + "/servlet/DataUpdateServlet")
                .isMultipart(true)
                .params("datas", datas)
                .execute(new BFlagCallBack<BusinessFlag>() {
                    @Override
                    public void onSuccess(BusinessFlag businessFlag, Call call, Response response) {
                        String content = businessFlag.getMessage();
                        Constant.billsNo = businessFlag.getNo();
                        if (businessFlag.getFlag().equals("Y")) {
                            if (dealtype.equals(Constant.SAVE_DEALTYPE)) {
                                setMessage(SUCCEED_SAVE, content);
                            } else {
                                setMessage(SUCCEED_SEND, content);
                            }
                        } else {
                            setMessage(SUCCEED_ERROR, content);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (e instanceof OkGoException) {
                            setMessage(SUCCEED_ERROR, "服务器错误");
                        } else {
                            setMessage(SUCCEED_ERROR, "上传失败");
                        }
                    }
                });
    }

    private void waitThread() {
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    handler.sendEmptyMessage(SUCCEED_THREAD);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    /**
     * 清除数据
     */
    private void removeData() {
        waitDialog.dismiss();
        gs_Widget.clear();
        kpi_Widget.clear();
        widget_sum = 0.0;
        widgetkpi_sum = 0.0;
        conclude_time_et = "";
        oldLine_no.clear();
        newLine_no.clear();
        oldList.clear();

        infoHashSet.clear();
        uLine_no.clear();
        nLine_no.clear();
        dLine_no.clear();
        conclude_time_et = "";
    }
}
