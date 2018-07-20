package com.dxtnerp.util;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dxtnerp.business.bs_view.StartViewInfo;
import com.dxtnerp.business.bs_view.ViewClass;
import com.dxtnerp.business.bs_view.WidgetAttribute;
import com.dxtnerp.business.bs_view.WidgetClass;
import com.dxtnerp.business.bs_view.WidgetName;
import com.dxtnerp.model.md_business.BusinessParam;
import com.dxtnerp.widget.HJAttendance;
import com.dxtnerp.widget.HJBarChart;
import com.dxtnerp.widget.HJButton;
import com.dxtnerp.widget.HJCheckBox;
import com.dxtnerp.widget.HJComboBox;
import com.dxtnerp.widget.HJDatePicker;
import com.dxtnerp.widget.HJGridLayout;
import com.dxtnerp.widget.HJHorizontalBarChart;
import com.dxtnerp.widget.HJLabel;
import com.dxtnerp.widget.HJLine;
import com.dxtnerp.widget.HJLineChart;
import com.dxtnerp.widget.HJListLayout;
import com.dxtnerp.widget.HJLocation;
import com.dxtnerp.widget.HJPhotoView;
import com.dxtnerp.widget.HJPieChart;
import com.dxtnerp.widget.HJQrcode;
import com.dxtnerp.widget.HJRadioButton;
import com.dxtnerp.widget.HJSegmentControlOption;
import com.dxtnerp.widget.HJTextLabel;
import com.dxtnerp.widget.HJTextView;
import com.dxtnerp.widget.HJToolBar;
import com.dxtnerp.widget.HJViewMenu;

/**
 *1、首先判断控件visible是否为true，如为false则不显示该控件
 *
 *
 */
public class WidgetFactoryUtils {

	/**
	 * 
	 * @param items
	 * @param context
	 * @param currentviewClass
	 * @param startViewInfo
	 * @param bparam
	 * @param scrollView
	 * @param fragment
	 * @return
	 */
	public static View createView(WidgetClass items, Context context,
			ViewClass currentviewClass,
			StartViewInfo startViewInfo ,
			BusinessParam bparam , 
			ScrollView scrollView,
			View fragment) {
		
		if (items.widgetType.equals(WidgetName.HJ_LABEL)) {
			HJLabel textview = new HJLabel(context,items,currentviewClass,startViewInfo);
			textview.setAttribute(items ); 
			return textview;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_TEXTVIEW)) { 
			HJTextView edit = new HJTextView(context,items,currentviewClass,startViewInfo,bparam);
			edit.setAttribute(items);
			
			
			return edit;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_RADIOBUTTON)){ 
			HJRadioButton view = new HJRadioButton(context,items,currentviewClass,startViewInfo,bparam);
//			view.setAttribute(items);
			return view;
		}
		if (items.widgetType.equalsIgnoreCase(WidgetName.HJ_BUTTON)) { 
			HJButton view = new HJButton(context,items,currentviewClass,startViewInfo);
			view.setAttribute(items);
			return view;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_CHECKBOX)){ 
			HJCheckBox view = new HJCheckBox(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_LOCATION)){ 
			HJLocation view = new HJLocation(context,items,currentviewClass,startViewInfo,bparam);
			return view; 
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_PHOTOVIEW)){ 
			HJPhotoView view = new HJPhotoView(context,items,currentviewClass,startViewInfo,bparam );
			return view;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_TOOLBAR)){ 
			HJToolBar view = new HJToolBar(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_LIST)){
			Log.v("show", "WidgetFactoryUtils 100    执行:"+WidgetName.HJ_LIST);
			HJListLayout view = new HJListLayout(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_GRID)) { 
			HJGridLayout view = new HJGridLayout(context,items,currentviewClass,startViewInfo ,scrollView ,bparam);
           return view;
		}
		if(items.widgetType.equalsIgnoreCase(WidgetName.HJ_VIEWMENU)) {
			HJViewMenu view = new HJViewMenu(context,items,currentviewClass,startViewInfo,bparam ,fragment);
			return view;
		}
		if (WidgetName.HJ_LINE_CHART.equalsIgnoreCase(items.widgetType)) {
			HJLineChart view = new HJLineChart(context, items,
					currentviewClass, startViewInfo,bparam);
			return view;
		}
		if (WidgetName.HJ_BAR_CHART.equalsIgnoreCase(items.widgetType)) {
			HJBarChart view = new HJBarChart(context, items, currentviewClass,
					startViewInfo,bparam);
			return view;
		}
		if (WidgetName.HJ_HORIZONTAL_BAR_CHART
				.equalsIgnoreCase(items.widgetType)) {
			HJHorizontalBarChart view = new HJHorizontalBarChart(context,
					items, currentviewClass, startViewInfo,bparam);
			return view;
		}
		if(WidgetName.HJ_PIE_CHART.equalsIgnoreCase(items.widgetType)){
			HJPieChart view = new HJPieChart(context, items, currentviewClass, startViewInfo,bparam);
			return view;
		}
		if(WidgetName.HJ_SEGMENTCONTROL.equalsIgnoreCase(items.widgetType)){
			HJSegmentControlOption view = new HJSegmentControlOption(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(WidgetName.HJ_LINE.equalsIgnoreCase(items.widgetType)){
			HJLine view = new HJLine(context,items,currentviewClass,startViewInfo);
			return view;
		}
		if(WidgetName.HJ_QRCODE.equalsIgnoreCase(items.widgetType)){
			HJQrcode view = new HJQrcode(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(WidgetName.HJ_COMBOBOX.equalsIgnoreCase(items.widgetType)){
//			HJQrcode view = new HJQrcode(context,items,currentviewClass,startViewInfo,bparam);
			HJComboBox view = new HJComboBox(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(WidgetName.HJ_DATE_PICKER.equalsIgnoreCase(items.widgetType)){
			HJDatePicker view = new HJDatePicker(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		if(WidgetName.HJ_TEXTLABEL.equalsIgnoreCase(items.widgetType)){
			HJTextLabel view = new HJTextLabel(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		
		if(WidgetName.HJ_ATTENDANCE.equalsIgnoreCase(items.widgetType)){
			HJAttendance view = new HJAttendance(context,items,currentviewClass,startViewInfo,bparam);
			return view;
		}
		
		return null;
	}

	private static void setAttribute(WidgetClass items, TextView textview) {
		textview.setId(Integer.parseInt(items.id));
		textview.setText(items.name);
		setDetailAttribute(items.attribute, textview);
	}

	private static void setDetailAttribute(WidgetAttribute attribute,
			TextView textview) {
		TextPaint paint = textview.getPaint();
		paint.setFakeBoldText(attribute.bold);
		textview.setSingleLine(attribute.singleline);
		if("medium".equalsIgnoreCase(attribute.fontsize)){
			textview.setTextSize(18);
		}
		if("large".equalsIgnoreCase(attribute.fontsize)){
			textview.setTextSize(22);
		}
		if("small".equalsIgnoreCase(attribute.fontsize)){
			textview.setTextSize(14);
		}
	}

}
