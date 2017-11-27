package iandroid.club.heightlinedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import iandroid.club.chartlib.util.Utils;

/**
 * Created by gabriel on 2017/11/27.
 * 功能描述 y轴线
 */

public class ChartYView extends ChartView{




    public ChartYView(Context context) {
        super(context);
        initView();
    }

    public ChartYView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChartYView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        drawYText = true;
        drawXInfo = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);

        mLeftX = mWidth;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawYLine(canvas);
    }

    /**
     * 绘制y轴线
     * 都是从左下角往上 和 右边 画线
     */
    private void drawYLine(Canvas canvas) {
        getmPaint().setColor(Color.GREEN);
        getmPaint().setStrokeWidth(Utils.dp2px(0.5f));
        //y轴线
        canvas.drawLine(mLeftX, mLeftDownY, mLeftX, mLeftUpY, getmPaint());
    }


}
