package iandroid.club.chartlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import iandroid.club.chartlib.util.Utils;


/**
 * Y轴
 */
public class YRender extends View {

    private Paint yLabelPaint;// 绘制Y轴文本的画笔
    private Paint yLinePaint;//y轴线

    /**
     * y轴左侧距离
     */
    private int yWidthDefault = 50;
    /**
     * y轴上侧距离
     */
    private float yTopOffSet;

    private int maxValue;
    private int minValue;
    private int hPerHeight;
    private int vPerValue;
    private int height;
    List<Integer> mValues = new ArrayList<>();

    private int showLableCount = 6;

    public YRender(Context context) {
        super(context);
        init(context);
    }

    public YRender(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YRender(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        yLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yLinePaint.setColor(Color.BLACK);
        yLinePaint.setStrokeWidth(Utils.dp2px(1));

        yLabelPaint = new Paint();
        yLabelPaint.setColor(Color.BLACK);

    }


    public List<Integer> getmValues() {
        return mValues;
    }

    public int getyWidthDefault() {
        return yWidthDefault;
    }

    public void setyWidthDefault(int yWidthDefault) {
        this.yWidthDefault = yWidthDefault;
    }

    public void setyTopOffSet(float yTopOffSet) {
        this.yTopOffSet = yTopOffSet;
    }

    public float getyTopOffSet() {
        return yTopOffSet + Utils.dp2px(30);
    }

    public int getShowLableCount() {
        return showLableCount;
    }

    public void setShowLableCount(int showLableCount) {
        this.showLableCount = showLableCount;
    }

    public int gethPerHeight() {
        return hPerHeight;
    }

    public void sethPerHeight(int hPerHeight) {
        this.hPerHeight = hPerHeight;
    }

    public int getvPerValue() {
        return vPerValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(yWidthDefault, measuredHeight);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = getHeight() - Utils.dp2px(50);//减去x坐标文字高度

        hPerHeight = height / showLableCount - 1;// 分成四部分

        //y轴线
        drawYLine(canvas);

        // 设置左部的数字
        drawYLables(canvas, hPerHeight);


    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void generateValues() {
        mValues.clear();
        if (maxValue > minValue) {
            vPerValue = (maxValue - minValue) / (showLableCount - 1);
            for (int i = 0; i < showLableCount; i++) {
                int tempValue = minValue + vPerValue * i;
                mValues.add(tempValue);
            }
        }
        Collections.sort(mValues, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        postInvalidate();
    }

    /**
     * 绘制Y轴Lables
     */
    private void drawYLables(Canvas canvas, int hPerHeight) {
        yLabelPaint.setTextAlign(Align.CENTER);
        yLabelPaint.setTextSize(Utils.sp2px(10));
        yLabelPaint.setAntiAlias(true);
        yLabelPaint.setStyle(Paint.Style.FILL);

        generateValues();

        for (int i = 0; i < mValues.size(); i++) {
            String yLabelText = mValues.get(i) + "";
            int textHeight = Utils.calcTextHeight(yLabelPaint, yLabelText);
            int textWidth = Utils.calcTextWidth(yLabelPaint, yLabelText);
            canvas.drawText(yLabelText, getyWidthDefault() - textWidth,
                    textHeight / 2 + i * hPerHeight + yTopOffSet ,
                    yLabelPaint);
        }
    }


    /**
     * y轴线
     *
     * @param canvas
     */
    private void drawYLine(Canvas canvas) {
        canvas.drawLine(getyWidthDefault(), yTopOffSet-Utils.dp2px(20),
                getyWidthDefault(), getZeroLineHeight()-Utils.dp2px(5)-Utils.dp2px(30), yLinePaint);
    }


    /**
     * 获取0坐标的位置
     */
    public int getZeroLineHeight() {
        int perHeight = gethPerHeight();
        float count = ((float) getMaxValue() - 0) / (float) getvPerValue();
        int zeroHeight = Math.round(count * (float) perHeight  + getyTopOffSet());
        return zeroHeight;
    }

}