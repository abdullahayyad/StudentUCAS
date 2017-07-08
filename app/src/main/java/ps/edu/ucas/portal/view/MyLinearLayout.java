package ps.edu.ucas.portal.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.rey.material.drawable.RippleDrawable;
import com.rey.material.widget.RippleManager;

/**
 * Created by Ayyad on 9/4/2015.
 */
public class MyLinearLayout extends LinearLayout {
    RippleManager mRippleManager;


    public MyLinearLayout(Context context) {
        super(context);
        getRippleManager().onCreate(this, context, (AttributeSet) null, 0, 0);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getRippleManager().onCreate(this, context, attrs  , defStyleAttr, defStyleRes);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getRippleManager().onCreate(this, context, attrs, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getRippleManager().onCreate(this, context, attrs, defStyleAttr, 0);
    }

    protected RippleManager getRippleManager(){
        if(mRippleManager == null){
            synchronized (RippleManager.class){
                if(mRippleManager == null)
                    mRippleManager = new RippleManager();
            }
        }

        return mRippleManager;
    }


    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        Drawable background = getBackground();
        if(background instanceof RippleDrawable && !(drawable instanceof RippleDrawable))
            ((RippleDrawable) background).setBackgroundDrawable(drawable);
        else
            super.setBackgroundDrawable(drawable);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        RippleManager rippleManager = getRippleManager();
        if(l == rippleManager)
            super.setOnClickListener(l);
        else{
            rippleManager.setOnClickListener(l);
            setOnClickListener(rippleManager);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        return  getRippleManager().onTouchEvent(this,event) || result;
    }



}
