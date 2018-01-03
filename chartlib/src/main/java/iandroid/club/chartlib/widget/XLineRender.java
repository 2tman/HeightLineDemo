package iandroid.club.chartlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import iandroid.club.chartlib.util.ScreenUtils;
import iandroid.club.chartlib.util.Utils;

/**
 * @version 0.1
 * @Date 创建时间：2017/11/27
 * @Author jiarong
 * @Description x轴
 */
public class XLineRender extends View {

    private LineChart lineChart;

    /**
     * 绘制X轴文本的画笔
     */
    private Paint xLabelPaint;
    // 坐标轴 轴线 画笔
    private Paint xLinePaint;

    public XLineRender(Context context) {
        super(context);
        initView();
    }

    public XLineRender(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public XLineRender(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xLabelPaint.setColor(Color.BLACK);

        xLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 给画笔设置颜色
        xLinePaint.setColor(Color.BLACK);
        xLinePaint.setStrokeWidth(Utils.dp2px(1));
    }

    public void setLineChart(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int factWidth = lineChart.getCanvasWidth();
        int height = Utils.dp2px(50);
        setMeasuredDimension(factWidth, height);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.topMargin = lineChart.getyRender().getZeroLineHeight();
        layoutParams.height = Utils.dp2px(50);
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //0坐标的高度
//        int bottom = lineChart.getyRender().getZeroLineHeight();
        int bottom = 0;
        int lineWidth = lineChart.getCanvasWidth();
        //间隔宽度
        int barStep = lineChart.getXWidthStep();
        int columCount = lineChart.getxLabels().size();

        // 绘制zero的线条
        drawZeroLine(canvas, bottom, lineWidth);
        /**
         * 绘制x轴文字
         */
        if (lineChart.getxLabels() != null && lineChart.getxLabels().size() > 0) {
            // 设置底部的文字
            int xTextHeight = bottom + Utils.dp2px(10);
            drawXLables(canvas, barStep, xTextHeight, columCount);
        }
    }

    /**
     * 绘制x轴0坐标的线条
     *
     * @param canvas
     */
    private void drawZeroLine(Canvas canvas, int height, int width) {
        //判断是否已经有0坐标

        xLinePaint.setStrokeWidth(Utils.dp2px(0.5f));
        canvas.drawLine(lineChart.getyRender().getyWidthDefault(),
                height, width,
                height
                , xLinePaint);

    }

    /**
     * 绘制X轴Labels
     */
    private void drawXLables(Canvas canvas, int step, int height, int columCount) {
        xLabelPaint.setTextAlign(Paint.Align.CENTER);
        xLabelPaint.setTextSize(Utils.sp2px(9));
        xLabelPaint.setAntiAlias(true);
        xLabelPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < columCount; i++) {
            int left = step * i + lineChart.getyRender().getyWidthDefault();
            if (i == 0) {
                int textWidth = ScreenUtils.calcTextWidth(xLabelPaint, lineChart.getxLabels().get(i));
                left += textWidth / 2;
            }
            canvas.drawText(lineChart.getxLabels().get(i), left, height, xLabelPaint);
        }
    }

    public void animShow() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 30);

    }
}
