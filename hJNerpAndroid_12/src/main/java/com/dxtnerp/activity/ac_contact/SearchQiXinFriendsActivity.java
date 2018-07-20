package com.dxtnerp.activity.ac_contact;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.activity.CaptureActivity;
import com.dxtnerp.business.ContactBusiness;
import com.dxtnerp.common.ActivitySupport;
import com.dxtnerp.common.Constant;
import com.dxtnerp.dao.QiXinBaseDao;
import com.dxtnerp.manager.HJWebSocketManager;
import com.dxtnerp.model.FriendInfo;
import com.dxtnerp.net.ChatPacketHelper;
import com.dxtnerp.net.IQ;
import com.dxtnerp.util.Log;
import com.dxtnerp.util.StringUtil;
import com.dxtnerp.util.ToastUtil;
import com.dxtnerp.widget.dialog.WaitDialogRectangle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 当前只搜索userId和PhoneNumber,这两个是唯一属性用户之间不会重复
 * 
 */

public class SearchQiXinFriendsActivity extends ActivitySupport {
	private String TAG = "SearchQiXinFriendsActivity";
	private RelativeLayout rl_search_phone;
	private RelativeLayout rl_saoyisao;
	private EditText input_et;
//	private Button search_btn;
	private String search_friend_id_phone;
	private FriendInfo friendInfo;
	private ArrayList<FriendInfo> friendInfoList ;
	private Dialog noticeDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addfriends);
//		mActionBar = getSupportActionBar();
//
////		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		mActionBar.setDisplayHomeAsUpEnabled(true);
//		mActionBar.setHomeButtonEnabled(true);
//		mActionBar.setTitle("添加联系人");
//		mActionBar.setBackgroundDrawable(new ColorDrawable(0xFFF7F7F7));
		getSupportActionBar().show();
		getSupportActionBar().setTitle("添加联系人");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		search_btn = (Button) findViewById(R.id.search_btn);
		input_et = (EditText) findViewById(R.id.input_et);
		input_et.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if(actionId == EditorInfo.IME_ACTION_SEARCH  || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
				{
					if (!ContactBusiness.checkFriends(search_friend_id_phone)) {
						searchUser();
					} else {
						 
						friendInfo = QiXinBaseDao.queryFriendInfo(search_friend_id_phone);
						jumpFriend();
					}
					return true;
				}
				return false;
			}
		});
//		search_btn.setOnClickListener(onClickListener);
		input_et.addTextChangedListener(textWatcher);
		rl_search_phone = (RelativeLayout) findViewById(R.id.rl_lianxiren);
		rl_saoyisao = (RelativeLayout)findViewById(R.id.rl_saoyisao);

		rl_search_phone.setOnClickListener(onClickListener);
		rl_saoyisao.setOnClickListener(onClickListener);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			search_friend_id_phone = input_et.getText().toString();
			if (search_friend_id_phone.length() > 0) {
//				search_btn.setEnabled(true);
			} else {
//				search_btn.setEnabled(false);
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_saoyisao:
			{
				Log.w(null, "saoyisao");
				///扫描二维码
				Intent intent = new Intent(); 
				intent.setClass(SearchQiXinFriendsActivity.this,CaptureActivity.class);
				startActivityForResult(intent,1001);
			}
					break;
			case R.id.rl_lianxiren:
				{
					Intent intent = new Intent();
					intent.setClass(SearchQiXinFriendsActivity.this,
							PhoneContactListActivity.class);
					startActivityForResult(intent,2001);
				}
				break; 

			case R.id.dialog_nc_confirm_rl:
				noticeDialog.dismiss();
				break;
			default:
				break;
			}

		}
	};

	private void searchUser(){ 
		searchUserTask  searchuser  = new searchUserTask(search_friend_id_phone);
		searchuser.execute(); 
	}

	
	private void jumpToAddingFriend() {
		Intent intent = new Intent();
		intent.setClass(this, AddingFriendActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Constant.FRIEND_READ, (Serializable) friendInfo);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	private void jumpFriend() {
		Intent intent = new Intent();
		intent.setClass(this, FriendsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Constant.FRIEND_READ, (Serializable) friendInfo);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (noticeDialog != null) {
			noticeDialog.dismiss();
		}
	}

	private void showNoticeDialog(String msg) {
		noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
		//noticeDialog.setCancelable(false);
		//noticeDialog.setCanceledOnTouchOutside(false);
		RelativeLayout dialog_confirm_rl;
		TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
		notice.setText(msg);
		dialog_confirm_rl = (RelativeLayout) noticeDialog
				.findViewById(R.id.dialog_nc_confirm_rl);
		dialog_confirm_rl.setOnClickListener(onClickListener);

		noticeDialog.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub 
		 super.onActivityResult(requestCode, resultCode, data);
		 if(data == null){
			 return;
		 }
			if( requestCode==1001) {
				   Bundle bundle = data.getExtras(); 
				    String str = bundle.getString("result"); 
				    if  (!StringUtil.isNullOrEmpty(str))
				    { 
				    	input_et.setText(str); 
				    }
			}
			if( requestCode == 2001){
				 Bundle bundle = data.getExtras(); 
				    String str = bundle.getString("result"); 
				    if  (!StringUtil.isNullOrEmpty(str))
				    {  
				    	input_et.setText(str); 
				    }
			}
			
			
			if (!ContactBusiness.checkFriendsByPhoneNumber(search_friend_id_phone)) {
				searchUser();
			} else {
				Log.w(TAG, "you have this friend already!");
				ToastUtil.ShowShort(SearchQiXinFriendsActivity.this, "已经是您的好友！");
				friendInfo = QiXinBaseDao.queryFriendInfoByPhoneNumber(search_friend_id_phone);
				jumpFriend();
			}
        
       
	}

	public class searchUserTask extends AsyncTask<String, Integer, Integer> {
	 
		private  WaitDialogRectangle waitDialogRectangle; 
		private String userId;
		private String errorMsg;
		private IQ iq ;
		public searchUserTask( String userid  ) {
			this.userId = userid; 
			this.waitDialogRectangle = getWaitDialogRectangle() ;
		}
		@Override
		protected void onPreExecute() { 
			waitDialogRectangle.show();
			waitDialogRectangle.setText( "正在搜索联系人...");  
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			return searchUser();
		}

		@Override
		protected void onProgressUpdate(Integer... values) { 
		
		}

		
		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
				case 1: //   
				waitDialogRectangle.dismiss();
			 
				friendInfoList = ChatPacketHelper.processSearchedUserInfo(iq);
				if (friendInfoList != null) 
				{ 
					friendInfo = friendInfoList.get(0);

					Intent intent = new Intent();
					intent.setClass(SearchQiXinFriendsActivity.this, SearchQiXinFriendsretActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable(Constant.FRIEND_READ, (Serializable) friendInfoList);
					intent.putExtras(mBundle);
					startActivity(intent); 
				 
				}
				break;
				case -1:
					waitDialogRectangle.dismiss();
					showNoticeDialog(getResources()
	     					.getString(
	     							R.string.user_not_exit));
			}
			super.onPostExecute(result);
		}
		
         public Integer searchUser()
         {
        	 iq =  HJWebSocketManager.getInstance().searchUser( userId);
     		 if (iq == null) return -1;
     			 
     		errorMsg  = ChatPacketHelper
     				.parseErrorCode(iq); 
     		if (errorMsg != null) { 
     			return  -1;
     		} 
        	 return 1;
         }
	}
		
}
