package com.jdplus.neo_shoopen.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class LayoutSize {

	Context m_Context;

	int m_DipWidth;
	int m_DipHeight;
	
	float m_Density;

	public LayoutSize(Context con) {
		m_Context = con;
		SettingSize();
	}

	public int GetDisWitdh() {
		if(m_DipWidth == 0)
			SettingSize();
		return m_DipWidth;
	}
	
	public int GetDisHeight() {
		if(m_DipHeight == 0)
			SettingSize();
		return m_DipHeight;
	}

	private void SettingSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) m_Context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);

		int tempWidth = metrics.widthPixels;
		int tempHeight = metrics.heightPixels;

//		Display display = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//
//		int tempWidth = display.getWidth();
//		int tempHeight = display.getHeight();
		
		if( tempWidth > tempHeight)
		{
			m_DipWidth = tempHeight;
			m_DipHeight = tempWidth;
		}
		else
		{
			m_DipWidth = tempWidth;
			m_DipHeight = tempHeight;
		}
	}

	public int DP_Convert_PX(int dp) {
		if(m_DipHeight == 0)
			SettingSize();

		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, m_Context.getResources().getDisplayMetrics());
	}

	private void Get_Density() {
		int density= m_Context.getResources().getDisplayMetrics().densityDpi;

		switch(density) {
			case DisplayMetrics.DENSITY_LOW:
//				Toast.makeText(context, "LDPI", Toast.LENGTH_SHORT).show();
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
//				Toast.makeText(context, "MDPI", Toast.LENGTH_SHORT).show();
				break;
			case DisplayMetrics.DENSITY_HIGH:
//				Toast.makeText(context, "HDPI", Toast.LENGTH_SHORT).show();
				break;
			case DisplayMetrics.DENSITY_XHIGH:
//				Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	private void screenSize() {
		int screenSize = m_Context.getResources().getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
//				Toast.makeText(this, "Large screen",Toast.LENGTH_LONG).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//				Toast.makeText(this, "Normal screen",Toast.LENGTH_LONG).show();
				break;
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
//				Toast.makeText(this, "Small screen",Toast.LENGTH_LONG).show();
				break;
			default:
//				Toast.makeText(this, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
		}
	}


}
