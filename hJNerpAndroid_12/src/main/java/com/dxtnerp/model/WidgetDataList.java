package com.dxtnerp.model;

import java.util.List; 

import com.dxtnerp.util.StringUtil;

public class WidgetDataList {
	public String UUID; 
	public  List<WidgetData>  Datalist ;
	  
    public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public List<WidgetData> getDatalist() {
		return Datalist;
	}
	public void setDatalist(List<WidgetData> datalist) {
		Datalist = datalist;
	}
	
	public void setUUID( ) {
		UUID= StringUtil.getMyUUID();
	} 
	
}
