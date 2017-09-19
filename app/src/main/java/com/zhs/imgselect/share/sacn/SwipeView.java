package com.zhs.imgselect.share.sacn;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhs.imgselect.share.adapter.DensityUtil;

/**
 * Created by Administrator on 2017/9/18.
 */

public class SwipeView extends RelativeLayout {
    private View decorView;
    public SwipeView(Context context) {
        super(context);
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        decorView=findViewById(android.R.id.content);
}

    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        decorView=findViewById(android.R.id.content);
    }

    private int startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getRawX();
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) ev.getRawX();
                if(decorView.getX()<0){
                    return super.onInterceptTouchEvent(ev);
                }
                decorView.setX(currentX);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        Log.d("wwq","onTouchEvent///");
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//            case MotionEvent.ACTION_UP:
//                startAnim();
//                break;
//        }
//        return false;
//    }

    private void startAnim() {
        int width=getResources().getDisplayMetrics().widthPixels;
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(getX(),width).setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();
                setX(value);
            }
        });
        valueAnimator.start();
    }


}
