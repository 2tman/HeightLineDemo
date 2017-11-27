package iandroid.club.chartlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import iandroid.club.chartlib.util.LogUtils;
import iandroid.club.chartlib.util.Utils;


/**
 * 复合图 线形图
 */
public class ComboChart extends FrameLayout {

    /**
     * 拖拽效果
     */
    private ViewDragHelper mViewDragHelper;

    /**
     * y轴
     */
    private YRender yRender;

    /**
     * 折线图
     */
    private LineChart lineChart;

    /**
     * x轴
     */
    private XRender xRender;

    /**
     * 右侧最大宽度
     */
    private int maxRightWidth;

    /**
     * y轴顶部默认的文字
     */
    private String showText = "身高(cm)";

    /**
     * y轴距离左侧的距离
     */
    private int yLeftOffset = 40;

    /**
     * y轴距离头部的距离
     */
    private int yTopOffset = 20;

    /**
     * 绘制Y轴文本的画笔
     */
    private Paint textPaint;

    /**
     * 图标区域左侧y轴起点
     */
    private float leftStartY;

    public ComboChart(Context context) {
        super(context);
        initView();
    }

    public ComboChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ComboChart(Context context,
                      AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }


    public LineChart getBaseBarChart() {
        return lineChart;
    }

    public YRender getyRender() {
        return yRender;
    }

    public void animShow() {
        lineChart.animShow();
        xRender.animShow();
    }

    public void setXLabels(List<String> xLabels){
        lineChart.setxLabels(xLabels);
        xRender.setxLabels(xLabels);
    }

    public int getyLeftOffset() {
        return Utils.dp2px(yLeftOffset);
    }

    public int getyTopOffset() {
        return Utils.dp2px(yTopOffset);
    }

    /**
     * 初始化
     */
    private void initView() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(Utils.sp2px(12));

        mViewDragHelper = ViewDragHelper.create(this, callback);
        //拖动的方向
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //顶部文字宽度
        float showTextWidth = Utils.calcTextWidth(textPaint, showText);
        //顶部文字高度
        float showTextHeight = Utils.calcTextHeight(textPaint, showText);
        //左侧y轴起点坐标值
        leftStartY = getyTopOffset() + showTextHeight + Utils.dp2px(5);

        //这种上侧起点
        yRender.setyTopOffSet(leftStartY);

        super.onDraw(canvas);

        //绘制y轴最上方的文字显示
        canvas.drawText(showText, getyLeftOffset() - showTextWidth / 2, getyTopOffset(), textPaint);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        xRender = (XRender)getChildAt(0);
        yRender = (YRender) getChildAt(2);
        lineChart = (LineChart) getChildAt(1);
        //设置左侧起点
        yRender.setyWidthDefault(getyLeftOffset());
        lineChart.setyRender(yRender);
        xRender.setyRender(yRender);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxRightWidth = lineChart.getCanvasWidth()-lineChart.getScreenWidth();
    }

    /****************************************************************
     * 一但返回True（代表事件在当前的viewGroup中会被处理）
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            /**
             * 比如当你的手指在屏幕上拖动一个listView或者一个ScrollView而不是去按上面的按钮时会触发这个事件
             */
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_DOWN:
                mViewDragHelper.cancel(); // 类似onTouch的ACTION_UP事件,清除
                break;
        }

        /**
         * 是否应该打断该MotionEvent的传递
         */
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 处理拦截到的事件 这个方法会在返回前分发事件
         */
        mViewDragHelper.processTouchEvent(event);
        /**
         * return true 表示消费了事件。
         */
        return true;
    }


    private int currentLeft = 0;

    private ViewDragHelper.Callback callback =
            new ViewDragHelper.Callback() {

                private boolean isLeftLimit;
                private boolean isRightLimit;

                // 何时开始检测触摸事件
                @Override
                public boolean tryCaptureView(View child, int pointerId) {
                    //如果当前触摸的child是mMainView时开始检测
                    return lineChart == child;
                }

                // 触摸到View后回调
                @Override
                public void onViewCaptured(View capturedChild,
                                           int activePointerId) {
                    super.onViewCaptured(capturedChild, activePointerId);
                }

                // 当拖拽状态改变，比如idle，dragging
                @Override
                public void onViewDragStateChanged(int state) {
                    switch (state) {
                        case ViewDragHelper.STATE_DRAGGING: // 正在被拖动
                            break;
                        case ViewDragHelper.STATE_IDLE: // view没有被拖拽
                            break;
                        case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                            break;
                    }
                    super.onViewDragStateChanged(state);
                }

                // 当位置改变的时候调用,常用与滑动时更改scale等
                @Override
                public void onViewPositionChanged(View changedView,
                                                  int left, int top, int dx, int dy) {
                    super.onViewPositionChanged(changedView, left, top, dx, dy);
                }

                private int currentTop = 0;

                // 处理垂直滑动
                @Override
                public int clampViewPositionVertical(View child, int top, int dy) {
                    return currentTop;
                }

                /********************************************************************
                 * 这两个方法分别用来处理x方向和y方向的拖动的，返回值该child现在的位置。
                 * @param  child 被拖动到view
                 * @param  left 移动到达的x轴的距离
                 * @param  dx 建议的移动的x距离
                 * clampViewPositionHorizontal的第二个参数是指当前拖动子view应该到达的x坐标。
                 * 所以按照常理这个方法原封返回第二个参数就可以了，但为了让被拖动的view遇到边界之后就不在拖动，对返回的值做了更多的考虑
                 */
                @Override
                public int clampViewPositionHorizontal(View child, int left, int dx) {

                    if (left > 0) {
                        isLeftLimit = true;
                        left = 0;
                    } else if (left < -maxRightWidth) {
                        isRightLimit = true;
                        left = -maxRightWidth;
                    }
                    isRightLimit = false;
                    isLeftLimit = false;
                    if (currentLeft != 0 && currentLeft != -maxRightWidth) {//不是在边界
                        if (currentLeft > left) {//left越来越小，则是往右滑动
                            lineChart.notifyTextPointChange(left, false);

                        } else if (currentLeft < left) {//left越来越大，则是往左滑动
                            lineChart.notifyTextPointChange(left, true);
                        }
                    }
                    currentLeft = left;
                    xRender.scrollTo(Math.abs(left), 0);
                    yRender.scrollInvalidate(lineChart.getCanvasWidth(), left);
                    lineChart.scrollInvalidate(left);
                    return left;
                }

                // 拖动结束后调用
                @Override
                public void onViewReleased(View releasedChild, float xvel, float yvel) {
                    super.onViewReleased(releasedChild, xvel, yvel);
                    if (isLeftLimit) {//到了最左边
                        if (onDragListener != null) {
                            onDragListener.onDragToLeft(0);
                            lineChart.notifyTextPointChange(0, true);
                        }
                    } else if (isRightLimit) {//到了最右边
                        if (onDragListener != null) {
                            onDragListener.onDragToRight(0);
                        }
                    }

                    // 返回true， 说明还没有移动到指定位置。需要重绘界面
                    ViewCompat
                            .postInvalidateOnAnimation(ComboChart.this);

                }
            };


    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * =======================================event======================
     */
    private OnDragListener onDragListener;

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    public interface OnDragListener {
        void onDragToLeft(int position);

        void onDragToRight(int position);
    }

    /**
     * x轴 居中到 xLable位置
     *
     * @param xLabel
     */
    public void centerToXLabels(String xLabel) {
        int targetX = lineChart.findFinalXByXValue(xLabel);
        int centerX = lineChart.findCenterX();
        mViewDragHelper.smoothSlideViewTo(lineChart, -(targetX - centerX) - lineChart.getXWidthStep() / 2, 0);
        ViewCompat.postInvalidateOnAnimation(ComboChart.this);
    }
}