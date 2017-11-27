package iandroid.club.heightlinedemo.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import iandroid.club.chartlib.widget.ComboChart;

/**
 * Created by gabriel on 2017/11/27.
 * 功能描述 图标
 */

public class ChartGraph extends FrameLayout {

    private View chartView;


    public ChartGraph(Context context) {
        super(context);
        initView();
    }

    public ChartGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChartGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){

    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        chartView = getChildAt(0);
    }



}
