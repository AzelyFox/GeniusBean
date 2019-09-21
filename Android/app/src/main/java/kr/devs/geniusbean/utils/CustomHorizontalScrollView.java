package kr.devs.geniusbean.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {

    protected OnEdgeTouchListener onEdgeTouchListener;


    public static enum DIRECTION { LEFT, RIGHT, NONE };

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnEdgeTouchListener(OnEdgeTouchListener l){
        this.onEdgeTouchListener = l;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(onEdgeTouchListener != null){
            if(getScrollX()==0){
                onEdgeTouchListener.onEdgeTouch(DIRECTION.LEFT);
            } else if((getScrollX() + getWidth())==computeHorizontalScrollRange()){
                onEdgeTouchListener.onEdgeTouch(DIRECTION.RIGHT);
            } else {
                onEdgeTouchListener.onEdgeTouch(DIRECTION.NONE);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


    public interface OnEdgeTouchListener {
        void onEdgeTouch(DIRECTION direction);
    }
}
