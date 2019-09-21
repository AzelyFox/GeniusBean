package kr.devs.geniusbean.utils;

import android.util.*;
import android.view.*;
import android.content.*;
import java.io.*;
import android.support.v4.view.*;

public class GameViewPager extends ViewPager {

	boolean enabled = true;
	OnSwipeOutListener mListener;
	
	public GameViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		return super.onTouchEvent(ev);
	}
	
	public interface OnSwipeOutListener {
		public void onSwipeOutAtEnd();
	}
} 
