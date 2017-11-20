package iandroid.club.chartlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.List;

import iandroid.club.chartlib.util.ScreenUtils;
import iandroid.club.chartlib.util.Utils;


/**
 * 柱状图基类
 */
public class BaseChart extends View {

    private YRender yRender;

    // 坐标轴 轴线 画笔
    private Paint xLinePaint;
    // 坐标轴水平内部 虚线画笔
    private Paint hLinePaint;
    // 绘制X轴文本的画笔
    private Paint xLabelPaint;
    // 是否使用动画
    protected int flag;
    //每屏幕显示的最多的lable数量 奇数
    private int showLableCount = 5;
    //x轴的间距刻度
    private int xWidthStep;
    //x轴文本的高度
    private int xLabelHeight = 50;
    //x轴文字
    private List<String> xLabels;
    //x轴数据
    private List<Integer> xLabelValues;

    public BaseChart(Context context) {
        super(context);
        init(context);
    }

    public BaseChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (xLabels == null) {
            //演示数据
            xLabels = new ArrayList<>();
            xLabels.add("label1");
            xLabels.add("label2");
            xLabels.add("label3");
            xLabels.add("label4");
            xLabels.add("label5");
        }

        xWidthStep = ScreenUtils.dp2px(context, 80);

        xLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 给画笔设置颜色
        xLinePaint.setColor(Color.BLACK);
        xLinePaint.setStrokeWidth(Utils.dp2px(1));

        hLinePaint.setColor(Color.LTGRAY);
        xLabelPaint.setColor(Color.BLACK);

    }

    /**
     * x轴坐标文字
     *
     * @param xLabels
     */
    public void setxLabels(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    public void setxLabelValues(List<Integer> xLabelValues) {
        this.xLabelValues = xLabelValues;
    }

    public List<String> getxLabels() {
        return xLabels;
    }

    public List<Integer> getxLabelValues() {
        return xLabelValues;
    }

    public void setyRender(YRender yRender) {
        this.yRender = yRender;
    }

    public YRender getyRender() {
        return yRender;
    }

    public int getxLabelHeight() {
        return xLabelHeight;
    }

    public void setShowLableCount(int showLableCount) {
        this.showLableCount = showLableCount;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int factWidth = getCanvasWidth();
        setMeasuredDimension(factWidth, measuredHeight);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        //y轴间隔高度
        int hPerHeight = yRender.gethPerHeight();
        //y轴线
        hLinePaint.setTextAlign(Align.CENTER);
        //间隔宽度
        int barStep = getXWidthStep();
        //0坐标的高度
        int bottom = yRender.getZeroLineHeight();
        int lineWidth = getCanvasWidth();
        // 绘制grid横向线条
        drawToYLines(canvas, hPerHeight, lineWidth);
        // 绘制zero的线条
        drawZeroLine(canvas, bottom, lineWidth);

        /**
         * 绘制x轴文字
         */
        if (xLabels != null && xLabels.size() > 0) {
            // 设置底部的文字
            int columCount = xLabels.size();
            int xTextHeight = bottom+Utils.dp2px(10);
            drawXLables(canvas, barStep, xTextHeight, columCount);
            //绘制grid纵向线条
            drawToXLine(canvas, barStep, bottom, columCount);
        }
        canvas.restore();
    }


    /**
     * 绘制x轴0坐标的线条
     *
     * @param canvas
     */
    private void drawZeroLine(Canvas canvas, int height, int width) {
        //判断是否已经有0坐标
        if (!yRender.getmValues().contains(0)) {
            canvas.drawLine(yRender.getyWidthDefault(),
                    height, width,
                    height
                    , xLinePaint);

            //画zero的文字
        }

    }

    /**
     * 绘制X轴Labels
     */
    private void drawXLables(Canvas canvas, int step, int height, int columCount) {
        xLabelPaint.setTextAlign(Align.CENTER);
        xLabelPaint.setTextSize(Utils.sp2px(9));
        xLabelPaint.setAntiAlias(true);
        xLabelPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < columCount; i++) {
            int left = step * i + yRender.getyWidthDefault();
            if (i == 0) {
                int textWidth = ScreenUtils.calcTextWidth(xLabelPaint, xLabels.get(i));
                left += textWidth / 2;
            }
            canvas.drawText(xLabels.get(i), left, height, xLabelPaint);
        }
    }

    /**
     * 绘制纵向线条
     */
    private void drawToXLine(Canvas canvas, int step, int height, int columCount) {
        for (int i = 1; i < columCount; i++) {
            int left = step * i + yRender.getyWidthDefault();
            canvas.drawLine(left, yRender.getyTopOffSet()-Utils.dp2px(20),
                    left, height-Utils.dp2px(5), hLinePaint);

        }

    }


    /**
     * 绘制grid横向线条
     */
    private void drawToYLines(Canvas canvas, int hPerHeight, int width) {

        for (int i = 0; i < yRender.getShowLableCount(); i++) {
            canvas.drawLine(yRender.getyWidthDefault(), i * hPerHeight + yRender.getyTopOffSet(),
                    width, i * hPerHeight + yRender.getyTopOffSet(), hLinePaint);
        }
    }


    /**
     * 左侧的偏移量
     *
     * @return
     */
    public int getxLeftOffset() {
        return yRender.getyWidthDefault();
    }


    /**
     * 实际屏幕宽度
     *
     * @return
     */
    public int getCanvasWidth() {
        int factWidth = getScreenWidth() * getScreenCount();
        return factWidth;
    }

    /**
     * 屏幕宽度
     *
     * @return
     */
    public int getTotalViewWidth() {
        return getCanvasWidth() //保证居中
                - yRender.getyWidthDefault() * getScreenCount() * 2;
    }

    /**
     * 屏幕宽度
     *
     * @return
     */
    public int getScreenCount() {
        int count = xLabels.size() / showLableCount;
        if (xLabels.size() % showLableCount != 0) {
            count++;
        }
        return count;
    }

    /**
     * 屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return getScreenSize(getContext()).widthPixels;
    }

    /**
     * 每屏幕图标所在view的宽度
     *
     * @return
     */
    public int getViewWidth() {
        return getScreenWidth() //保证居中
                - yRender.getyWidthDefault() * 2 - getxLeftOffset() * 2;

    }


    /**
     * x轴间隔宽度
     *
     * @return
     */
    public int getXWidthStep() {
        return xWidthStep;
    }


    /**
     * 设置点击事件，是否显示数字
     */
    /*public boolean onTouchEvent(MotionEvent event) {
        int step = (getWidth() - dp2px(30)) / 8;
        int x = (int) event.getX();
        for (int i = 0; i < 7; i++) {
            if (x > (dp2px(15) + step * (i + 1) - dp2px(15))
                    && x < (dp2px(15) + step * (i + 1) + dp2px(15))) {
                text[i] = 1;
                for (int j = 0; j < 7; j++) {
                    if (i != j) {
                        text[j] = 0;
                    }
                }
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    invalidate();
                } else {
                    postInvalidate();
                }
            }
        }
        return super.onTouchEvent(event);
    }*/


    /**
     * 获取屏幕相关参数
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 根据xValue查找当前的x位置
     *
     * @param xValue
     * @return
     */
    public int findFinalXByXValue(String xValue) {
        int targetIndex = 0;
        if (xLabels != null && xLabels.size() > 0) {
            for (int i = 0; i < xLabels.size(); i++) {
                if (xLabels.get(i).equals(xValue)) {
                    targetIndex = i;
                    break;
                }
            }
        }
        //计算targetIndex的x轴位置
        int targetBarX = getXWidthStep() * targetIndex + getxLeftOffset();
        return targetBarX;
    }

    /**
     * 根据xLabel查找当前的x位置
     *
     * @param xValue
     * @return
     */
    public int findFinalPointXByXLabel(String xValue) {
        int targetIndex = 0;
        if (xLabels != null && xLabels.size() > 0) {
            for (int i = 0; i < xLabels.size(); i++) {
                if (xLabels.get(i).equals(xValue)) {
                    targetIndex = i;
                    break;
                }
            }
        }
        //计算targetIndex的x轴位置
        int targetBarX = getXWidthStep() * targetIndex + getxLeftOffset();
        return targetBarX;
    }

    /**
     * 根据xValue查找当前的x位置
     *
     * @param xValue
     * @return
     */
    public int findFinalPointXByXValue(int xValue) {
        int targetIndex = 0;
        int targetXValue = 0;
        if (xLabelValues != null && xLabelValues.size() > 0) {
            for (int i = 0; i < xLabelValues.size(); i++) {
                if (xLabelValues.get(i) >= xValue) {
                    targetXValue = xLabelValues.get(i);
                    targetIndex = i;
                    break;
                }
            }
        }
        //计算targetIndex的x轴位置
        int targetBarX = getXWidthStep() * targetIndex + getxLeftOffset();
        if (targetXValue == xValue) {
            return targetBarX;
        }
        int resultX = Math.round((float) targetBarX / (float) targetXValue * (float) xValue);
        return resultX;
    }

    /**
     * 根据yValue获得y坐标
     *
     * @param yValue
     * @return
     */
    public float findFinalYByValue(float yValue) {
        int bottom = yRender.getZeroLineHeight();
        float rh = bottom + -yValue * yRender.gethPerHeight() / yRender.getvPerValue();
        return rh;
    }

    /**
     * x轴居中的位置
     *
     * @return
     */
    public int findCenterX() {
        return (getScreenWidth() - yRender.getyWidthDefault() * 2) / 2;
    }

}