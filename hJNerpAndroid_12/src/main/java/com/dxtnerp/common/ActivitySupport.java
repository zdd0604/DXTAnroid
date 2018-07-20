package com.dxtnerp.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.core.PoiInfo;
import com.dxtnerp.activity.LoginActivity;
import com.dxtnerp.MainActivity;
import com.dxtnerp.db.SQLiteWorker;
import com.dxtnerp.model.LoginConfig;
import com.dxtnerp.net.ChatConstants;
import com.dxtnerp.net.HttpClientManager;
import com.dxtnerp.net.IQ;
import com.dxtnerp.service.IMChatService;
import com.dxtnerp.service.WebSocketService;
import com.dxtnerp.util.SharePreferenceUtil;
import com.dxtnerp.util.myscom.StringUtils;
import com.dxtnerp.widget.MyToast;
import com.dxtnerp.widget.dialog.WaitDialog;
import com.dxtnerp.widget.dialog.WaitDialogRectangle;
import com.dxtnerp.R;
import com.lzy.okgo.OkGo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdyy.utils.XPermissionListener;
import com.sdyy.utils.XPermissions;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Actity 工具支持类
 *
 * @author 李庆义
 */
public class ActivitySupport extends ActionBarActivity implements
        IActivitySupport {
    private static String TAG = "ActivitySupport";
    // public class ActivitySupport extends ActionBarActivity{
    protected Context context = null;
    protected EapApplication eapApplication;
    protected NotificationManager notificationManager;
    public static SharePreferenceUtil sputil = null;
    public Boolean isReceiver = true;

    protected MyActivitySupportReceiver receiver;
    protected IntentFilter filter;

    protected PoiInfo location;

    protected ActionBar mActionBar;
    protected ImageLoader mImageLoader;

    protected WaitDialog waitDialogSupport;
    protected WaitDialogRectangle waitDialogRectangle;

    protected SQLiteWorker worker = SQLiteWorker.getSharedInstance();
    private int saveMsgIndbFlag = 0; // 异步储存msg的返回值,0代表不能插入

    private static boolean isFromBackToFront = false;
    //是否授权
    public boolean isPsions = false;

    private int mLocationTime = 10000;//获取地址的时间间隔
    private LocationClient mLocationClient;
    private ActivitySupport.MyLocationListener myLocationListener;
    protected double mLatitude = 0.00;
    protected double mLongitude = 0.00;


    /**
     * @author haijian 增加变量判断键盘是否收回
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (sputil == null) {
            sputil = SharePreferenceUtil.getInstance(context);
        }
        eapApplication = (EapApplication) getApplicationContext();
        eapApplication.addActivity(this);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        waitDialogSupport = new WaitDialog(context);
        waitDialogRectangle = new WaitDialogRectangle(context);

//        getLocationInfo();

    }

    /**
     * 获取Editext内容
     *
     * @param editText
     * @return
     */
    public static String getEdVaule(EditText editText) {
        return editText.getText().toString().trim();
    }
    /**
     * TextView
     *
     * @param textView
     * @return
     */
    public static String getTvVaule(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 獲取定位
     */
    public void getLocationInfo() {
        mLocationClient = new LocationClient(this);
        myLocationListener = new ActivitySupport.MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(mLocationTime);
        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 地理位置信息返回结果
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mLocationClient != null)
            mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReceiver) {
            if (!isFromBackToFront) {
                isFromBackToFront = true;
                // checkNet();
            }
            receiver = new MyActivitySupportReceiver();
            filter = new IntentFilter();
            filter.addAction("com.hjnerp.service.websocket.action.IQ");
            registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onPause() {
        if (waitDialogSupport != null) {
            waitDialogSupport.dismiss();
        }
        if (isReceiver) {
            unregisterReceiver(receiver);

        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            // app 进入前台
            isFromBackToFront = false;
        }

        if (mLocationClient != null &&mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }


    @Override
    public void onDestroy() {
        eapApplication.removeActivity(this);
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
        //取消所有请求
        OkGo.getInstance().cancelAll();
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return false;
    }

    public void startService() {
        // 聊天服务
        Intent chatServer = new Intent(context, IMChatService.class);
        context.startService(chatServer);
        // // 自动恢复连接服务
        // Intent reConnectService = new Intent(context,
        // ReConnectService.class);
        // context.startService(reConnectService);

        HttpClientManager.open();

    }

    /**
     * 销毁服务.
     *
     * @author 李庆义
     * @update 2012-5-16 下午12:16:08
     */
    @Override
    public void stopService() {
        Log.i("info", "停止操作执行了");
        // 聊天服务
        Intent chatServer = new Intent(context, IMChatService.class);
        context.stopService(chatServer);
        // // 自动恢复连接服务
        // Intent reConnectService = new Intent(context,
        // ReConnectService.class);
        // context.stopService(reConnectService);
        // HttpClientManager.close();
    }

    @Override
    public void isExit() {

    }

    public void isForcedExit(String msg) {
        final Dialog noticeExitDialog = new Dialog(this,
                R.style.noticeDialogStyle);
        noticeExitDialog.setContentView(R.layout.dialog_notice_nocancel);
        noticeExitDialog.setCancelable(false);
        noticeExitDialog.setCanceledOnTouchOutside(false);
        RelativeLayout dialog_confirm_rl;
        TextView notice = (TextView) noticeExitDialog
                .findViewById(R.id.nc_notice);
        notice.setText(msg);
        dialog_confirm_rl = (RelativeLayout) noticeExitDialog
                .findViewById(R.id.dialog_nc_confirm_rl);

        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stopService();
                noticeExitDialog.dismiss();
                sputil.setForceExit(false);
                sputil.setMySessionId("");
                startActivity(new Intent(ActivitySupport.this,
                        LoginActivity.class));
                finish();
            }
        });

        noticeExitDialog.show();

    }

    public void isDeleteFromFriend(String friendName) {

        final Dialog noticeExitDialog = new Dialog(this,
                R.style.noticeDialogStyle);
        noticeExitDialog.setContentView(R.layout.dialog_notice_nocancel);
        noticeExitDialog.setCancelable(true);
        noticeExitDialog.setCanceledOnTouchOutside(true);
        RelativeLayout dialog_confirm_rl;
        TextView notice = (TextView) noticeExitDialog
                .findViewById(R.id.nc_notice);
        notice.setText("[" + friendName + "]" + " 把您从好友列表中移除");
        dialog_confirm_rl = (RelativeLayout) noticeExitDialog
                .findViewById(R.id.dialog_nc_confirm_rl);

        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeExitDialog.dismiss();
            }
        });

        noticeExitDialog.show();

    }

    @Override
    public boolean hasInternetConnected() {
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

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    @Override
    public boolean hasLocationGPS() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkMemoryCard() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage("请检查内存卡")
                    .setPositiveButton(R.string.menu_settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(
                                            Settings.ACTION_SETTINGS);
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton("退出",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    eapApplication.exit();
                                }
                            }).create().show();
        }
    }

    public void openWirelessSet() {
//         AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//         dialogBuilder
//         .setTitle(R.string.prompt)
//         .setMessage(context.getString(R.string.check_connection))
//         .setPositiveButton(R.string.menu_settings,
//         new DialogInterface.OnClickListener() {
//         @Override
//         public void onClick(DialogInterface dialog,
//         int which) {
//         dialog.cancel();
//         Intent intent = new Intent(
//         Settings.ACTION_WIRELESS_SETTINGS);
//         context.startActivity(intent);
//         }
//         })
//         .setNegativeButton(R.string.close,
//         new DialogInterface.OnClickListener() {
//         @Override
//         public void onClick(DialogInterface dialog,
//         int whichButton) {
//         dialog.cancel();
//         }
//         });
//         dialogBuilder.show();
    }

    /**
     * 关闭键盘事件
     *
     * @author 李庆义
     * @update 2012-7-4 下午2:34:34
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            // 启动activity时不自动弹出软键盘
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * 发出Notification的method.
     *
     * @param iconId       图标
     * @param contentTitle 标题
     * @param contentText  你内容
     * @param activity
     * @author 李庆义
     * @update 2012-5-14 下午12:01:55
     */
    public void setNotiType(int iconId, String contentTitle,
                            String contentText, Class activity, String from) {
        /*
         * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
        Intent notifyIntent = new Intent(this, activity);
        notifyIntent.putExtra("to", from);
        // notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		/* 创建PendingIntent作为设置递延运行的Activity */
        PendingIntent appIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, 0);

		/* 创建Notication，并设置相关参数 */
//        Notification myNoti = new Notification();
//		// 点击自动消失
//		myNoti.flags = Notification.FLAG_AUTO_CANCEL;
//		/* 设置statusbar显示的icon */
//		myNoti.icon = iconId;
//		/* 设置statusbar显示的文字信息 */
//		myNoti.tickerText = contentTitle;
//		/* 设置notification发生时同时发出默认声音 */
//		myNoti.defaults = Notification.DEFAULT_SOUND;
        /* 设置Notification留言条的参数 */
        Notification myNoti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(appIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(iconId)
                .setTicker(contentTitle)
                .setOngoing(true).build();


//		myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
        /* 送出Notification */
        notificationManager.notify(0, myNoti);
    }


//    /**
//     * 发送消息通知
//     * @param number
//     */
//    public void sendToOther(int number) {
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = null;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(alarmSound)
//                .setContentTitle("您有" + number + "条未读消息")
//                .setTicker("您有" + number + "条未读消息")
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.icon2)
//                .setDefaults(Notification.DEFAULT_LIGHTS)
//                .setContentIntent(pendingIntent); // 关联PendingIntent
//        notification = builder.build();
//        nm.notify(101010, notification);
////        editor2.putInt("isNoti", number);
////        editor2.commit();
//    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void saveLoginConfig(LoginConfig loginConfig) {
    }

    @Override
    public LoginConfig getLoginConfig() {
        LoginConfig loginConfig = new LoginConfig();
        return loginConfig;
    }

    @Override
    public EapApplication getEapApplication() {
        return eapApplication;
    }

    public String getCurrentChatFriendOrGroupId() {
        return EapApplication.EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID;
    }

    public void setCurrentChatFriendOrGroupId(String chatTargetId) {

        EapApplication.EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID = chatTargetId;

    }

    public void jumpToMain(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO refresh
    public void refreshIm() {
    }

    public void refreshImActionbarTab() {
    }

    public void refreshContact() {
    }

    public void refreshContactActionbarTab() {
    }

    public void refreshImAndActionBarTab() {
        refreshIm();
        refreshImActionbarTab();
    }

    public void refreshContactAndActionbarTab() {
        refreshContact();
        refreshContactActionbarTab();
    }

    public void closeInputMethodOnce(final Activity activity) {
        ((Handler) getEapApplication().getExtra(
                EapApplication.EXTRA_MAIN_HANDLER)).post(new Runnable() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View v = activity.getCurrentFocus();
                if (v != null)
                    inputMethodManager.hideSoftInputFromWindow(
                            v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void openInputMethodOnce(final View focusView) {
        focusView.requestFocus();
        ((Handler) getEapApplication().getExtra(
                EapApplication.EXTRA_MAIN_HANDLER)).post(new Runnable() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(focusView,
                        InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    // 有人解除了和我的好友 关系
    public void removeMeAsFriend(String friendId) {
    }

    // 群解散
    public void dropGroupChat(String groupId) {
    }

    // 群主移除了一个人
    public void removeUserFromGroup(String groupId, String userId) {
    }

    // 刷新聊天
    public void refalshContent(String groupId) {
    }

    // 群里有人退出
    public void quitUserFromGroup(String groupId) {
    }

    // 群里添加了成员
    public void addUserFromGroup(String groupId) {
    }

    public class MyActivitySupportReceiver extends BroadcastReceiver {

        @Override
        @SuppressWarnings("unchecked")
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (WebSocketService.ACTION_ON_IQ.equals(action)) {
                IQ iq = (IQ) intent.getSerializableExtra("data");
                Map<String, Object> dataMap = (Map<String, Object>) iq.data;

                if (ChatConstants.iq.FEATURE_CLINET_OFFLINE.equals(iq.feature)) {// 被踢下线
                    isForcedExit("您的账号在另一台设备登陆！");
                }
                if (ChatConstants.iq.FEATURE_OP_GROUP_CHAT.equals(iq.feature)) {
                    String type = (String) dataMap
                            .get(ChatConstants.iq.DATA_KEY_TYPE);
                    if (ChatConstants.iq.DATA_VALUE_ADD.equals(type)
                            || ChatConstants.iq.DATA_VALUE_EXIT.equals(type)
                            || ChatConstants.iq.DATA_VALUE_REMOVE.equals(type)) {// 有成员退出

                        refalshContent("");
                    }

                }
                if (ChatConstants.iq.FEATURE_REMOVE_CONTACT.equals(iq.feature)) {// 有人删除我
                    // String userId = iq.from; // 这个人把你删了
                    // if( userId.equalsIgnoreCase((String)
                    // EapApplication.getApplication().getExtra(EapApplication.EXTRA_USER_ID))){
                    // return;//我把别人删除了
                    // }
                    // FriendInfo friendInfo =
                    // QiXinBaseDao.queryFriendInfoall(userId);
                    // if(friendInfo != null){
                    // isDeleteFromFriend(friendInfo.getFriendname());
                    // }else{
                    // isDeleteFromFriend(userId);
                    // }

                }

            }

        }

    }

    private void sendToMyHandler(String msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putString("flag", msg);
        Msg.setData(b);

        mySupportHandler.sendMessage(Msg);
    }

    final Handler mySupportHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle b = msg.getData();
            String mmsg = b.getString("flag");
            Log.i(TAG, "mmsg is " + mmsg);
            if (mmsg.equals("refreshImAndActionBarTab")) {
                refreshImAndActionBarTab();
            } else if (mmsg.equals("agree_add_me_asfriend")) {
                refreshContactAndActionbarTab();

            } else if (mmsg.equals("searchgroup_when_getnew_groupchatmsg_")) {

            }

        }

        ;
    };

    @Override
    public WaitDialogRectangle getWaitDialogRectangle() {
        // TODO Auto-generated method stub
        return waitDialogRectangle;
    }

    // 检查应用是否在后台运行
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    // 检查手机网络状况
    public void checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isAvailable()) {
            handleWebsocketConnect();
        }
    }

    // 检查websocket状态并处理
    public void handleWebsocketConnect() {

        if (sputil.isForceExit()
                || StringUtils.isBlank(sputil.getMySessionId())) {
            return;
        }

        // try {
        // // if
        // (HJWebSocketManager.getInstance().getConnection().isConnecting())
        // return ;
        // HJWebSocketClient hjWebSockectClient =
        // HJWebSocketManager.getInstance().getConnection();
        // if (hjWebSockectClient == null) {
        // HJWebSocketManager.getInstance().connect(URL);
        // }
        // else{
        // if (HJWebSocketManager.getInstance().getConnection().isOpen())
        // return ;//webxocket连接正常
        // if (HJWebSocketManager.getInstance().getConnection().isConnecting())
        // return ;//webxocket连接正常
        // HJWebSocketManager.getInstance().connect(URL);
        // }
        //
        // String errorText = HJWebSocketManager.getInstance().autoLogin();
        // if ("".equalsIgnoreCase(errorText)) {
        // this.startService();
        // HJWebSocketManager.getInstance().readyPresence();
        //
        // }else{
        // Intent intent = new Intent();
        // intent.setClass(this, LoginActivity.class);
        // startActivity(intent);
        // }
        //
        // } catch (Exception e) {
        // // ToastUtil.ShowShort(this, "连接错误");
        // return ;
        // }

    }

    /**
     * @author haijian 增加点击edittext区域外，收起软键盘功能
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    /**
     * 判断是否授权
     *
     * @param psions
     * @return
     */
    public boolean isPermissions(String[] psions) {
        XPermissions.getPermissions(psions, (Activity) context, new XPermissionListener() {
            @Override
            public void onAcceptAccredit() {
                isPsions = true;
            }

            @Override
            public void onRefuseAccredit(String[] results) {
                isPsions = false;
            }
        });
        return isPsions;
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > right
                    && event.getY() > top && event.getY() < bottom) {//如果是输入框右边的部分就保留
                return false;
            }
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //判断手机格式是否正确
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 小数点后两位
     *
     * @param editText
     */
    public static void setEditextPoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }



    /**
     * 长toast
     *
     * @param content
     */
    public void toastLONG(String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 短toast
     *
     * @param content
     */
    public void toastSHORT(String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }


    /**
     * 自定义toast
     *
     * @param content
     */
    public void myToastShow(String content) {
        new MyToast(context, content);
    }

    /**
     * 打印日志
     *
     * @param content
     */
    public void logShow(String content) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        if (Constant.IS_LOG_SHOW_VIEW)
            Log.e("MZ", "文件名称："+getClass().getSimpleName()
                    + "，行号：" + ste.getLineNumber()
                    + "，内容：" + content);
    }


    /**
     * 获取输入框的内容
     *
     * @param editText
     * @return
     */
    protected String getEditextCt(EditText editText) {
        return editText.getText().toString().trim();
    }



}
