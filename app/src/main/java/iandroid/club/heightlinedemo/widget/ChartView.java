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

import iandroid.club.chartlib.util.Utils;

/**
 * @Description: chartview重新绘制demo
 * @Author: 加荣
 * @Time: 2017/11/23
 */
public class ChartView extends View {

    /**
     * y 轴线 x
     */
    protected float mLeftX;

    /**
     * y轴刻度线文字的宽度
     */
    protected int xLabelWidth = 50;

    /**
     * y轴文本
     */
    private List<String> yLabels = new ArrayList<>();

    /**
     * 是否绘制y轴文本
     */
    protected boolean drawYText;


    /*=================== x轴用的 ===================*/
    /**
     * 左上角 和左下角 x
     * 最终需要根据数据进行计算
     */
    private int mLeftUpX = 0;

    /**
     * 左上角Y
     */
    protected int mLeftUpY = 50;

    /**
     * 左下角Y
     */
    protected int mLeftDownY = 400;

    /**
     * 右下角x
     * 最终需要根据数据进行计算
     */
    private int mRightDownX = 600;

    /**
     * 上下间隔
     */
    private int mYSpace = 50;

    /**
     * 左右间隔
     */
    private int mXSpace = 50;

    /**
     * x轴刻度线文字的高度
     */
    private int xLabelHeight = 50;

    /**
     * 高度
     */
    private int mHeight;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * x轴文本
     */
    private List<String> xLabels = new ArrayList<>();

    /**
     * 是否绘制x轴信息
     */
    protected boolean drawXInfo = true;


    private int minValue = 40;

    public ChartView(Context context) {
        super(context);
        initView();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public int getmXSpace() {
        return mXSpace;
    }

    public int getmYSpace() {
        return mYSpace;
    }

    public void setxLabels(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);

        mLeftDownY = mHeight - xLabelHeight;
        mRightDownX = mWidth;

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 界面初始化
     */
    private void initView() {
        mLeftUpX = 0;
        mLeftUpY = 0;

        //文本高度
        xLabelHeight = Utils.dp2px(50);
        //x轴刻度间距
        mXSpace = Utils.dp2px(50);
        //y轴刻度间距
        mYSpace = Utils.dp2px(50);

        //demo数据
        if (xLabels.size() == 0) {
            for (int i = 0; i < 30; i += 2) {
                xLabels.add(i + "");
            }
        }

        for (int i = 0; i < 20; i++) {
            yLabels.add(minValue + "");
            minValue += 10;
        }
    }

    /**
     * 画笔
     *
     * @return
     */
    public Paint getmPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        return mPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawXInfo) {
            //绘制x轴线
            drawXLine(canvas);
            //绘制x轴的刻度线
            drawXGridLine(canvas);
        }
        //绘制y轴的刻度线
        drawYGridLine(canvas);
    }

    /**
     * 绘制x 轴线
     * 都是从左下角往上 和 右边 画线
     */
    private void drawXLine(Canvas canvas) {
        getmPaint().setColor(Color.GREEN);
        getmPaint().setStrokeWidth(Utils.dp2px(0.5f));
        //x轴线
        canvas.drawLine(mLeftUpX, mLeftDownY, mRightDownX, mLeftDownY, getmPaint());
    }


    /**
     * 绘制x轴刻度线
     *
     * @param canvas
     */
    private void drawXGridLine(Canvas canvas) {
        //总共需要画多少条线
        float xDiff = mRightDownX - mLeftUpX;
        float lineCount = xDiff / getmXSpace();
        int factLineCount = (int) Math.ceil(lineCount);

        //设置线条画笔
        getmPaint().setColor(Color.LTGRAY);
        getmPaint().setStrokeWidth(Utils.dp2px(0.5f));
        //开始画线
        for (int i = 0; i < factLineCount; i++) {
            float startX = mLeftUpX + getmXSpace() * i;
            if (i > 0) {
                canvas.drawLine(startX, mLeftDownY, startX, mLeftUpY, getmPaint());
            }
            //绘制刻度文字
            drawXGridText(canvas, startX, i);
        }
    }

    /**
     * 绘制x轴刻度文字
     *
     * @param canvas
     * @param startX
     */
    private void drawXGridText(Canvas canvas, float startX, int position) {
        if ((xLabels.size() - 1) >= position) {
            getmPaint().setTextSize(Utils.sp2px(10));
            String xLabelText = xLabels.get(position);
            //文字的y轴
            float xTextY = mLeftDownY + Utils.dp2px(15);
            float xTextWidth = Utils.calcTextWidth(getmPaint(), xLabelText);
            if (position == 0) {
                canvas.drawText(xLabelText, startX, xTextY, getmPaint());
            } else {
                float factTextX = startX - xTextWidth / 2;
                canvas.drawText(xLabelText, factTextX, xTextY, getmPaint());
            }
        }
    }

    /**
     * 绘制y轴刻度线
     *
     * @param canvas
     */
    private void drawYGridLine(Canvas canvas) {
        //总共需要画多少条线
        float yDiff = mLeftDownY - mLeftUpY;
        float lineCount = yDiff / getmYSpace();
        int factLineCount = (int) Math.ceil(lineCount);

        //设置线条画笔
        getmPaint().setColor(Color.LTGRAY);
        getmPaint().setStrokeWidth(Utils.dp2px(0.5f));
        //开始画线
        for (int i = 0; i < factLineCount; i++) {
            float startY = mLeftDownY - getmYSpace() * i;
            if (drawXInfo) {
                if (i > 0) {
                    canvas.drawLine(mLeftUpX, startY, mRightDownX, startY, getmPaint());
                }
            }
            if (drawYText) {
                drawYGridText(canvas, startY, i);
            }
        }
    }


    /**
     * 绘制y轴刻度文字
     *
     * @param canvas
     */
    protected void drawYGridText(Canvas canvas, float startY, int position) {
        if ((yLabels.size() - 1) >= position) {
            getmPaint().setTextSize(Utils.sp2px(10));
            String yLabelText = yLabels.get(position);
            //文字的x轴
            float xTextX = mLeftX / 2;
            float xTextHeight = Utils.calcTextHeight(getmPaint(), yLabelText);

            float factTextY = startY;
            if (position > 0) {
                factTextY = startY + xTextHeight / 2;
            }
            canvas.drawText(yLabelText, xTextX, factTextY, getmPaint());
        }
    }
}
