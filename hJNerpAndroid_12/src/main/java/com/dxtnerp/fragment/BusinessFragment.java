package com.dxtnerp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxtnerp.business.bs_work.AddTerminalActivity;
import com.dxtnerp.business.bs_work.ConsumSpendeActivity;
import com.dxtnerp.business.bs_work.SalesOrderActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.dxtnerp.activity.SetActivity;
import com.dxtnerp.adapter.ad_biness.BusinessGridViewAdapter;
import com.dxtnerp.dao.BusinessQueryDao;
import com.dxtnerp.adapter.ad_ctlm.Ctlm1346Update;;
import com.dxtnerp.business.bs_activity.BusinessEJLocation;
import com.dxtnerp.business.bs_activity.BussinessHtmlActivity;
import com.dxtnerp.common.Constant;
import com.dxtnerp.common.EapApplication;
import com.dxtnerp.dao.BusinessBaseDao;
import com.dxtnerp.db.SQLiteWorker;
import com.dxtnerp.model.md_business.BusinessMenuResp;
import com.dxtnerp.model.md_business.BusinessTableCreateModel;
import com.dxtnerp.model.MenuContent;
import com.dxtnerp.model.NBusinessTableCreateModel;
import com.dxtnerp.net.HttpClientBuilder;
import com.dxtnerp.net.HttpClientManager;
import com.dxtnerp.net.HttpClientManager.HttpResponseHandler;
import com.dxtnerp.util.Command;
import com.dxtnerp.util.Log;
import com.dxtnerp.widget.MyToast;
import com.dxtnerp.widget.dialog.WaitDialogRectangle;
import com.dxtnerp.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@SuppressLint("ResourceAsColor")
public class BusinessFragment<Divider> extends CommonFragment {
    private static final String TAG = BusinessFragment.class.getSimpleName();

    private MyGridView gridView1;
    private MyGridView gridView2;
    private MyGridView gridView3;
    private MyGridView gridView4;
    private ArrayList<MenuContent> listCurrent;
    private ArrayList<MenuContent> listCurrent1;
    private ArrayList<MenuContent> listCurrent2;
    private ArrayList<MenuContent> listCurrent3;
    private ArrayList<MenuContent> listCurrent4;
    //	private BusinessExpandableListAdapter adapter;
    private BusinessGridViewAdapter adapter1;
    private BusinessGridViewAdapter adapter2;
    private BusinessGridViewAdapter adapter3;
    private BusinessGridViewAdapter adapter4;
    private Dialog noticeDialog;// 提示更新数据
    private MyHandler<Divider> myHandler;
    private static String clicked_id_model;// 用户想要进入的业务类型（用户点击了哪一个菜单）
    private static String clicked_xml_version;// 用户想要进入的业务类型的xml模板本地版本（用户点击的菜单的xml模板版本）
    private static String clicked_xml_nameModel;// 用户想要进入的业务类型的xml模板名称
    private static String CHECT_XML_EXIT = "check_xml_exit";
    private static String CHECT_XML_OK = "check_xml_ok";
    private static String CHECT_XML_OLD = "check_xml_OLD";
    private static String DOWNLOAD_XML_OK = "download_xml_ok";
    private static String DOWNLOAD_XML_ERROR = "download_xml_error";
    private static MenuContent menuContent;// 用户点击的菜单
    private static Context context;
    private Thread mThread;
    public static boolean isPoPoVisible;
    public static BusinessFragment businessFragment = null;
    private WaitDialogRectangle waitDialogRectangle;
    private LinearLayout visit_layout;
    private LinearLayout bus_error;
    private LinearLayout other_layout;
    private LinearLayout find_layout;
    private LinearLayout performance_layout;
    private static Intent intent = null;
    private BusinessGridViewAdapter adapter;

    private class MyHandler<Divider> extends Handler {
        ArrayList<MenuContent> listCurrent;
        BusinessFragment<Divider> fragment;

        public MyHandler(BusinessFragment<Divider> fragment,
                         ArrayList<MenuContent> listCurrent) {
            this.fragment = fragment;
            this.listCurrent = listCurrent;
        }

        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String flag = b.getString("flag");
            setMenuIntent(flag);
        }

        /**
         * 设置跳转界面
         */
        private void setMenuIntent(String flag) {
            if (fragment.waitDialogRectangle != null && fragment.waitDialogRectangle.isShowing()) {
                fragment.waitDialogRectangle.dismiss();
            }

            // 下载菜单成功
            if (flag.equals("getmenusok")) {
                listCurrent = BusinessBaseDao.queryBusinessMenus();
                fragment.listCurrent = listCurrent;
                fragment.refreshList(listCurrent);
                logShow("菜单的数据:" + listCurrent.toString());
            } else if (CHECT_XML_EXIT.equalsIgnoreCase(flag)) {


            } else if (CHECT_XML_OK.equalsIgnoreCase(flag)
                    || CHECT_XML_OLD.equalsIgnoreCase(flag)
                    || DOWNLOAD_XML_OK.equalsIgnoreCase(flag)) {

                if (clicked_id_model == null || "".equals(clicked_id_model)) {
                    return;
                }

                //界面跳转判断
                fragmentIntent(fragment);

            } else if (DOWNLOAD_XML_ERROR.equalsIgnoreCase(flag)) {
                if (clicked_id_model == null || "".equals(clicked_id_model)) {
                    myToastShow(context, "下载模板错误，请重新尝试。");
                    return;
                }
                logShow("模版下载出错！");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contextView = inflater.inflate(R.layout.fragment_business_ex, container, false);
        context = getActivity();
        businessFragment = this;

        // 从服务器获取显示菜单
        listCurrent = BusinessBaseDao.queryBusinessMenus();
        myHandler = new MyHandler<Divider>(this, listCurrent);
        initView(contextView);
        return contextView;
    }

    /**
     * 加载布局
     *
     * @param contextView
     */
    private void initView(View contextView) {
        listCurrent1 = new ArrayList<>();
        listCurrent2 = new ArrayList<>();
        listCurrent3 = new ArrayList<>();
        listCurrent4 = new ArrayList<>();

        gridView1 = (MyGridView) contextView.findViewById(R.id.gridview_businessmenu_ex1);
        gridView2 = (MyGridView) contextView.findViewById(R.id.gridview_businessmenu_ex2);
        gridView3 = (MyGridView) contextView.findViewById(R.id.gridview_businessmenu_ex3);
        gridView4 = (MyGridView) contextView.findViewById(R.id.gridview_businessmenu_ex4);
        visit_layout = (LinearLayout) contextView.findViewById(R.id.visit_layout);
        other_layout = (LinearLayout) contextView.findViewById(R.id.other_layout);
        find_layout = (LinearLayout) contextView.findViewById(R.id.find_layout);
        bus_error = (LinearLayout) contextView.findViewById(R.id.bus_error);
        performance_layout = (LinearLayout) contextView.findViewById(R.id.performance_layout);

        getBusinessMenus();
    }

    /**
     * 跳转界面
     *
     * @param fragment
     */
    private void fragmentIntent(Fragment fragment) {
        Class fromClass = null;
        //设置模版名称
        Constant.Clicked_xml_nameModel = clicked_xml_nameModel;

        logShow("XML模板名称：" + Constant.Clicked_xml_nameModel);

        if (!BusinessQueryDao.getUserInfo(context)) {
            intent = new Intent(fragment.getActivity(), SetActivity.class);
        } else
            //终端拜访
            if (clicked_id_model.equals(Constant.discard)) {

            } else
                //商务拜访
                if (clicked_id_model.equals(Constant.ddiscccard)) {

                } else
                    //市场检查
                    if (clicked_id_model.equals(Constant.ddischeck)) {

                    } else
                        //销售订单申请
                        if (clicked_id_model.equals(Constant.dsaordhtml)) {
                            fromClass = SalesOrderActivity.class;
                        } else
                            //新增终端
                            if (clicked_id_model.equals(Constant.nterminalhtml)) {
                                fromClass = AddTerminalActivity.class;
                            } else
                                //报销单
                                if (clicked_id_model.equals(Constant.dfeeothhtml)) {
                                    fromClass = ConsumSpendeActivity.class;
                                } else
                                    //终端修改
                                    if (clicked_id_model.equals(Constant.mterminalhtml)) {

                                    } else
                                        //运输单
                                        if (clicked_id_model.equals(Constant.dsatrans)) {

                                        } else
                                            //工作日志
                                            if (clicked_id_model.equals(Constant.ddissumaryhtml)) {

                                            } else
                                                //销量上报
                                                if (clicked_id_model.equals(Constant.dsarptp)) {

                                                } else
                                                    //考勤签到照片
                                                    if (clicked_id_model.equals(Constant.ddisplocatphohtml)) {
                                                        fromClass = BusinessEJLocation.class;
                                                    } else
                                                        //拜访业绩
                                                        if (clicked_id_model.equals(Constant.discardquery)) {

                                                        } else
                                                            //拜访情况
                                                            if (clicked_id_model.equals(Constant.cdiscard)) {

                                                            } else
                                                                //考勤记录
                                                                if (clicked_id_model.equals(Constant.ddisplocatquery)) {

                                                                } else
                                                                    //订单申请查询
                                                                    if (clicked_id_model.equals(Constant.dsaordzxqk)) {

                                                                    } else
                                                                        //待审单据查询
                                                                        if (clicked_id_model.equals(Constant.dflagsts)) {

                                                                        } else {
                                                                            fromClass = BussinessHtmlActivity.class;
                                                                            intent.putExtra("id_model", clicked_id_model);
                                                                        }

        //判断本地是否有模版
        if (fromClass != null) {
            intent = new Intent(fragment.getActivity(), fromClass);
            fragment.startActivity(intent);
        } else {
            myToastShow(context, "模版尚未开发");
        }
    }


    private void dogridview(final ArrayList<MenuContent> listCurrent, GridView gridView) {
        adapter = new BusinessGridViewAdapter(getActivity(), listCurrent);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
        final BusinessGridViewAdapter finalAdapter = adapter;

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuContent = finalAdapter.getItem(position);
                clicked_id_model = menuContent.getVarParm();
                Constant.ID_MENU = menuContent.getIdMenu();
                clicked_xml_nameModel = menuContent.getNameMenu();
                clicked_xml_version = listCurrent.get(position).getVarParm();
                if (checkSigleXMLModelexit(menuContent.getVarParm() + ".xml")) {// 检查当前模板是否存在
                    // TODO 1、检查xml版本，是否需要更新
                    String serverVersion = menuContent.getModelWindow();
                    String localVersion = BusinessBaseDao
                            .getTemlateVersion(menuContent.getVarParm());
                    if (serverVersion != null
                            && serverVersion.equalsIgnoreCase(localVersion)) {
                        // 不需要更新
                        sendToHandler(CHECT_XML_OK);
                    } else {
                        // 2、检查网络
                        if (hasInternetConnected(context)) {
                            updateModelThread();
                        } else {
                            // 网络无连接，用老版本
                            sendToHandler(CHECT_XML_OLD);
                        }
                    }
                } else {
//                     showNoticeDialog();
                    updateModelThread();
                }
            }
        });
    }

    /**
     * 添加菜单
     *
     * @param listCurrent
     */
    private void sortlist(ArrayList<MenuContent> listCurrent) {

        listCurrent1 = new ArrayList<>();
        listCurrent2 = new ArrayList<>();
        listCurrent3 = new ArrayList<>();
        listCurrent4 = new ArrayList<>();

        for (int i = 0; i < listCurrent.size(); i++) {
            if (listCurrent.get(i).getVarParm1().equalsIgnoreCase("001")) {
                listCurrent1.add(listCurrent.get(i));
            } else if (listCurrent.get(i).getVarParm1().equalsIgnoreCase("002")) {
                listCurrent2.add(listCurrent.get(i));
            } else if (listCurrent.get(i).getVarParm1().equalsIgnoreCase("003")) {
                listCurrent3.add(listCurrent.get(i));
            } else {
                listCurrent4.add(listCurrent.get(i));
            }
        }
        if (listCurrent1.size() > 0) {
            visit_layout.setVisibility(View.VISIBLE);
        } else {
            visit_layout.setVisibility(View.GONE);
        }
        if (listCurrent2.size() > 0) {
            other_layout.setVisibility(View.VISIBLE);
        } else {
            other_layout.setVisibility(View.GONE);
        }
        if (listCurrent3.size() > 0) {
            find_layout.setVisibility(View.VISIBLE);
        } else {
            find_layout.setVisibility(View.GONE);
        }
        if (listCurrent4.size() > 0) {
            performance_layout.setVisibility(View.VISIBLE);
        } else {
            performance_layout.setVisibility(View.GONE);
        }
        addlist(listCurrent1);
        addlist(listCurrent2);
        addlist(listCurrent3);
        addlist(listCurrent4);

        dogridview(listCurrent1, gridView1);
        dogridview(listCurrent2, gridView2);
        dogridview(listCurrent3, gridView3);
        dogridview(listCurrent4, gridView4);
    }

    private void addlist(ArrayList<MenuContent> listCurrent) {
        int a = listCurrent.size() % 4;
        if (a != 0 || listCurrent.size() == 0) {
            for (int i = 0; i < (4 - a); i++) {
                MenuContent menuContent = new MenuContent();
                menuContent.setPicpath("");
                menuContent.setModelWindow("0");
                listCurrent.add(menuContent);
            }
        }
    }

    /**
     * 获得菜单
     */
    private void getBusinessMenus() {
        HttpPost post = null;
        try {
            post = HttpClientBuilder
                    .createParam(Constant.BUSINESS_SERVICE_ADDRESS)
                    .addKeyValue(Constant.BM_ACTION_TYPE, Constant.BMTYPE_BUSINESS_MENU).getHttpPost();
        } catch (UnsupportedEncodingException e1) {
            Log.e(e1);
        }

        HttpClientManager.addTask(new HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    String msg = HttpClientManager.toStringContent(resp);
                    Gson gson = new Gson();
                    final BusinessMenuResp businessMenuResp = gson.fromJson(msg, BusinessMenuResp.class);
                    Log.d("businessresp", businessMenuResp.data.items.toString());
                    if ("result".equalsIgnoreCase(businessMenuResp.type)) {
                        if (businessMenuResp.data != null) {
                            SQLiteWorker.getSharedInstance().postDML(
                                    new SQLiteWorker.AbstractSQLable() {
                                        @Override
                                        public void onCompleted(Object event) {
                                            if (!(event instanceof Throwable)) {
                                                sendToHandler("getmenusok");
                                            }
                                        }

                                        @Override
                                        public Object doAysncSQL() {
                                            BusinessBaseDao.deleteBusinessMenus();// 插入新菜单前清除旧数据
                                            BusinessBaseDao.replaceBusinessMenus(businessMenuResp.data.items);
                                            return null;
                                        }
                                    });
                        } else {
                        }
                    }
                } catch (IOException e) {
                    onException(e);
                }
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        }, post);

        android.util.Log.e("show", post.toString());
    }

    /**
     * 刷新业务菜单
     */
    public void refreshList() {
        listCurrent = BusinessBaseDao.queryBusinessMenus();
        if (listCurrent.size() > 0) {
            refreshList(listCurrent);
        } else {
            getBusinessMenus();
        }
    }

    //刷新菜单
    private void refreshList(ArrayList<MenuContent> list) {
        if (list.size() > 0) {
            bus_error.setVisibility(View.GONE);
        } else {
            bus_error.setVisibility(View.VISIBLE);
        }

        sortlist(list);
        checkPaoPao();
    }

    private void sendToHandler(String msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putString("flag", msg);
        Msg.setData(b);
        myHandler.sendMessage(Msg);
    }

    // 检查xml文件是否全部存在
    boolean checkSigleXMLModelexit(String mfileName) {
        boolean flag = false;
        if (listCurrent == null || listCurrent.size() == 0) {
            Log.e(TAG, " listcurrent null");
            return false;
        }
        // 检查xml文件
        File file = EapApplication.getApplication().getFilesDir();
        if (file != null) {

            File[] files = file.listFiles();
            String[] filenames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                Log.i(TAG, "file name is " + files[i].getName());
                filenames[i] = files[i].getName();
                if (mfileName.equalsIgnoreCase(files[i].getName())) {
                    return true;
                }
            }

        } else {
            Log.e(TAG, "xmlmodel 为null");
            flag = false;
        }
        return flag;
    }

    // 检查xml文件是否全部存在
//    boolean checkXMLModelexit() {
//        boolean flag = true;
//        if (listCurrent == null || listCurrent.size() == 0) {
//            Log.e(TAG, " listcurrent null");
//            return false;
//        }
//        // 检查xml文件
//        File file = EapApplication.getApplication().getFilesDir();
//        if (file != null) {
//
//            File[] files = file.listFiles();
//            String[] filenames = new String[files.length];
//            for (int i = 0; i < files.length; i++) {
//                Log.i(TAG, "file name is " + files[i].getName());
//                filenames[i] = files[i].getName();
//            }
//
//            String[] menuids = new String[listCurrent.size()];
//            for (int i = 0; i < listCurrent.size(); i++) {
//                menuids[i] = listCurrent.get(i).getVarParm() + ".xml";
//            }
//
//            List<String> resuleList = StringUtil.exist2(filenames, menuids);
//            if (resuleList.size() > 0) {// xml文件不完全
//                for (int i = 0; i < resuleList.size(); i++) {
//                    Log.e(TAG, "缺少model " + resuleList.get(i));
//                }
//                flag = false;
//            }
//        } else {
//            Log.e(TAG, "xmlmodel 为null");
//            flag = false;
//        }
//        return flag;
//    }

    //    @Override
//    public void ons() {
//        super.onDestroy();
//        checkPaoPao();
//        refreshList(listCurrent);
//    }

    public void showNoticeDialog() {

        // noticeDialog = new Dialog(getActivity(), R.style.noticeDialogStyle);
        noticeDialog = new Dialog(getActivity(), R.style.noticeDialogStyle);
        // noticeDialog.setContentView(R.layout.dialog_notice_withcancel);
        noticeDialog.setContentView(R.layout.dialog_notice_nocancel);

        TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
        notice.setText("当前应用不存在，请到设置里更新应用");

        RelativeLayout dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_nc_confirm_rl);

        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // new Ctlm1346Update().action();//下载xml
                noticeDialog.dismiss();
            }
        });

        noticeDialog.show();

    }

    private String processBusinessCompress(String fileName) throws Exception {// 解压缩下载的同步数据
        File file = new File(getActivity().getExternalCacheDir(), fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry = zis.getNextEntry();
        if (entry == null) {
            new MyToast(getActivity(), "数据文件已损坏");
        }
        int len = -1;
        byte[] bytes = new byte[512];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = zis.read(bytes, 0, bytes.length)) > 0) {
            baos.write(bytes, 0, len);
        }
        String json = new String(baos.toByteArray(), HTTP.UTF_8);
        zis.close();
        return json;
    }

    private void procCompressJson(String json) {// 处理压缩包中的文本为json
        Gson gson = new Gson();
        List<BusinessTableCreateModel> btcms = gson.fromJson(json,
                new TypeToken<List<BusinessTableCreateModel>>() {
                }.getType());
        for (BusinessTableCreateModel btcm : btcms) {
            // btcm.build();
            btcm.create();
        }
        // BusinessBaseDao.replaceBusinessTableCreateModels(btcms);
        BusinessBaseDao.opBusinessTableCreateModels(btcms);
    }

    private void checkPaoPao() {
        String localVersion;
        for (int i = 0; i < listCurrent.size(); i++) {
            localVersion = BusinessBaseDao.getTemlateVersion(listCurrent.get(i)
                    .getVarParm());
            if (!localVersion.equals(listCurrent.get(i).getModelWindow())) {
                isPoPoVisible = true;
                break;
            } else {
                isPoPoVisible = false;
            }
        }
    }

    private void procCompressJson2(String json) {// 处理压缩包中的文本为json
        Gson gson = new Gson();
        NBusinessTableCreateModel model = gson.fromJson(json,
                NBusinessTableCreateModel.class);
        model.create();
        BusinessBaseDao.opNBusinessTableCreateModels(model);
    }

    public static boolean hasInternetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    private void updateModelThread() {
        if (!hasInternetConnected(getActivity())) {
        }
        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        waitDialogRectangle = new WaitDialogRectangle(context);
        waitDialogRectangle.setCanceledOnTouchOutside(false);
        waitDialogRectangle.show();

        mThread = new Thread() {
            @Override
            public void run() {
                new Ctlm1346Update(new Command.OnMultiResultListener() {
                    @Override
                    public void onResult(List<Boolean> successes) {
                        if (successes.contains(false)) {
                            Log.e(TAG, "Ctlm1346Update contains false");
                            waitDialogRectangle.dismiss();
                            sendToHandler(DOWNLOAD_XML_ERROR);
                        } else {
                            waitDialogRectangle.dismiss();
                            sendToHandler(DOWNLOAD_XML_OK);
                        }

                    }
                }).action();// 下载xml
            }
        };
        mThread.start();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        /*
         * @author haijian 检查是否显示泡泡
         */
        checkPaoPao();
        refreshList();
    }

    @Override
    public void onPause() {
        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        super.onPause();
    }

}