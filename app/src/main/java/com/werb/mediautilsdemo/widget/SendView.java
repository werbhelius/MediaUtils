package com.werb.mediautilsdemo.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.werb.mediautilsdemo.R;
import com.werb.mediautilsdemo.Utils;

/**
 * Created by wanbo on 2017/1/20.
 */

public class SendView extends RelativeLayout {

    public RelativeLayout backLayout,selectLayout;

    public SendView(Context context) {
        super(context);
        init(context);
    }

    public SendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        RelativeLayout.LayoutParams params = new LayoutParams(Utils.getInstance(context).getWidthPixels(),Utils.getInstance(context).dp2px(180f));
        setLayoutParams(params);
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.widget_view_send_btn,null,false);
        layout.setLayoutParams(params);
        backLayout = (RelativeLayout) layout.findViewById(R.id.return_layout);
        selectLayout = (RelativeLayout) layout.findViewById(R.id.select_layout);
        addView(layout);
        setVisibility(GONE);
    }

    public void startAnim(){
        setVisibility(VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(backLayout,"translationX",0,-360),
                ObjectAnimator.ofFloat(selectLayout,"translationX",0,360)
        );
        set.setDuration(250).start();
    }

    public void stopAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(backLayout,"translationX",-360,0),
                ObjectAnimator.ofFloat(selectLayout,"translationX",360,0)
        );
        set.setDuration(250).start();
        setVisibility(GONE);
    }

}
