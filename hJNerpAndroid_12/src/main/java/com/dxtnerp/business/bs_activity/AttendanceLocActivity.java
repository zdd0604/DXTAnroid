package com.dxtnerp.business.bs_activity;

import android.os.Bundle;

import com.dxtnerp.R;
import com.dxtnerp.common.ActivitySupport;

public class AttendanceLocActivity extends ActivitySupport {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("位置信息");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.layout_hjattendance_info_activity); 
	}

		
}
