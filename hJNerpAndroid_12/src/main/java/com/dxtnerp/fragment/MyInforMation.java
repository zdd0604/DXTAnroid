package com.dxtnerp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxtnerp.activity.SetActivity;
import com.dxtnerp.activity.ac_contact.FriendsActivity;
import com.dxtnerp.common.ActionBarWidgetActivity;
import com.dxtnerp.common.Constant;
import com.dxtnerp.common.EapApplication;
import com.dxtnerp.dao.QiXinBaseDao;
import com.dxtnerp.model.FriendInfo;
import com.dxtnerp.model.UserInfo;
import com.dxtnerp.net.ChatConstants;
import com.dxtnerp.net.ChatPacketHelper;
import com.dxtnerp.util.Log;
import com.dxtnerp.util.SharePreferenceUtil;
import com.dxtnerp.util.StringUtil;
import com.dxtnerp.util.bitmap.BitmapUtils;
import com.dxtnerp.R;
import com.itheima.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.Serializable;

public class MyInforMation extends Fragment implements View.OnClickListener {
    private TextView mysettingrel;
    private RelativeLayout myuserinfor;
    private View view;
    private TextView myusername;
    private TextView my_company;
    private RoundedImageView myImageView;
    private UserInfo myinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinformation, container, false);
        initView();
        return view;
    }

    private void initView() {
        mysettingrel = (TextView) view.findViewById(R.id.mysettingrel);
        myuserinfor = (RelativeLayout) view.findViewById(R.id.myuserinfor);
        myImageView = (RoundedImageView) view.findViewById(R.id.myInforphoto);
        myusername = (TextView) view.findViewById(R.id.myusername);
        my_company = (TextView) view.findViewById(R.id.my_company);
        mysettingrel.setOnClickListener(this);
        myuserinfor.setOnClickListener(this);
        setUserInfor();
    }

    private void setUserInfor() {
        myinfo = QiXinBaseDao.queryCurrentUserInfo();
        if (myinfo == null) {
            ActionBarWidgetActivity.showFailToast("请下载基础数据");
            return;
        }

        String url = myinfo.userImage;
        if (StringUtil.isStrTrue(url)) {
//            ImageLoaderHelper.displayImage(
//                    ChatPacketHelper.buildImageRequestURL(url,
//                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),myImageView);

            ImageLoader.getInstance().displayImage(
                    ChatPacketHelper.buildImageRequestURL(url,ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
                    myImageView,
                    new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    Log.e("show","加载失败");
                    myImageView.setImageBitmap(
                            BitmapUtils.convertViewToBitmap(
                                    ActionBarWidgetActivity.getPotoView(
                                            getActivity(), StringUtil.doubleName(myinfo.username))));
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                }
                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        } else {
            myImageView.setImageBitmap(
                    BitmapUtils.convertViewToBitmap(
                            ActionBarWidgetActivity.getPotoView(
                                    getActivity(), StringUtil.doubleName(myinfo.username))));
        }

        myusername.setText(myinfo.username);
        my_company.setText(myinfo.departmentName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mysettingrel:
                intentActivity(SetActivity.class);
                break;
            case R.id.myuserinfor:
                FriendInfo friendinfo = QiXinBaseDao.queryFriendInfo(
                        SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext())
                                .getMyUserId());
                if (friendinfo == null)
                    return;
                Intent intent = new Intent(this.getActivity(), FriendsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Constant.FRIEND_READ, (Serializable) friendinfo);
                intent.putExtras(mBundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfor();
    }


    private void intentActivity(Class c) {
        Intent intent = new Intent(this.getActivity(), c);
        startActivity(intent);
    }
}
