package iandroid.club.heightlinedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import iandroid.club.heightlinedemo.entity.BaseHeight;
import iandroid.club.heightlinedemo.utils.ScreenUtils;
import iandroid.club.heightlinedemo.utils.FormatDataUtil;

/**
 * @Description: 身高曲线
 * @Author: 加荣
 * @Time: 2017/11/16
 */
public class GrouthHeightLineView extends View {

    //y轴的左侧纵线
    Paint paintYLineLeft = new Paint(Paint.ANTI_ALIAS_FLAG);
    //x轴的底部横线
    Paint paintXLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    //x轴底部的文字
    Paint paintXText = new Paint(Paint.ANTI_ALIAS_FLAG);
    //基线Paint
    Paint paintValueLine = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path mPath;// 路径对象

    int xPadding = 50;
    int startY = 50;
    int rectHeight = 300;//图形区域的高度
    int rectWidth = 500;//图形区域的宽度
    int xtextHeight = 0;//x轴文字距离x轴线的高度

    int xStepWidth;//x轴间距

    {
        xtextHeight = ScreenUtils.dp2px(getContext(), 20);
        xPadding = ScreenUtils.dp2px(getContext(), 30);//左右边距
        rectWidth = ScreenUtils.getScreenSize(getContext()).widthPixels - xPadding * 2;
        rectHeight = (int)(ScreenUtils.getScreenSize(getContext()).heightPixels *0.7);
        xStepWidth = ScreenUtils.dp2px(getContext(), 50);
    }

    private List<BaseHeight> datas;

    public GrouthHeightLineView(Context context) {
        super(context);
        init();
    }

    public GrouthHeightLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrouthHeightLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        datas = new ArrayList<>();
        //模拟数据
        initDatas();
        setPadding(xPadding, 0, xPadding, 0);

        mPath = new Path();
    }

    private void initDatas() {
        int size = 20;
        for (int i = 0; i < size; i+=2) {
            BaseHeight barEntity = new BaseHeight(i, 55 + i*2+"");
            datas.add(barEntity);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        /**
         * 1.左侧y轴画线
         */
        drawYLine(canvas);

        /**
         * 2.底部x轴画线
         */
        drawXLine(canvas);

        paintXText.setColor(Color.RED);
        paintXText.setTextSize(ScreenUtils.sp2px(getContext(), 12));
        int textY = startY + rectHeight + xtextHeight;//文字的y轴距

        paintValueLine.setColor(Color.GREEN);

        for (int i = 0; i < datas.size(); i++) {
            String mLabel = FormatDataUtil.getFormatXLabel(datas.get(i).getAge());
//            float mValue = datas.get(i).getHeight();

            int textWidth = ScreenUtils.calcTextWidth(paintXText, mLabel);
            int textX = xStepWidth*(i+1) -  textWidth/2;

            /**
             * 3.x轴文字
             */
            drawXText(canvas, mLabel, textX, textY);

            /**
             * 4.绘制身高基线
             */
//            drawHeightLine(canvas, mValue, barxPadding);
        }


    }



    /**
     * y轴画线
     *
     * @param canvas
     */
    private void drawYLine(Canvas canvas) {
        paintYLineLeft.setColor(Color.parseColor("#dddddd"));
        paintYLineLeft.setStrokeWidth(2);

        int stopYLineY = startY + rectHeight;
        canvas.drawLine(xPadding, startY, xPadding, stopYLineY, paintYLineLeft);
    }

    /**
     * x轴画线
     *
     * @param canvas
     */
    private void drawXLine(Canvas canvas) {
        paintXLine.setColor(Color.parseColor("#dddddd"));
        paintXLine.setStrokeWidth(2);

        int xLineStartY = startY + rectHeight;
        int xLineStopX = xPadding + rectWidth;
        canvas.drawLine(xPadding, xLineStartY, xLineStopX, xLineStartY, paintXLine);
    }

    /**
     * x轴文字
     *
     * @param canvas
     */
    private void drawXText(Canvas canvas, String mLabel, int textxPadding, int textY) {
        canvas.drawText(mLabel + "", textxPadding, textY, paintXText);
    }


}
