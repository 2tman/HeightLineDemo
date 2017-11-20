package iandroid.club.chartlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import iandroid.club.chartlib.entity.ChartPoint;
import iandroid.club.chartlib.entity.DataSet;
import iandroid.club.chartlib.entity.LineEntity;
import iandroid.club.chartlib.entity.PointEntity;
import iandroid.club.chartlib.util.ScreenUtils;
import iandroid.club.chartlib.util.Utils;


/**
 * 线形图
 * Created by 加荣 on 2017/7/27.
 */

public class LineChart extends BaseChart {

    //绘制文字
    private Paint mTextPaint;
    //绘制线条
    private Paint mLinePaint;
    //绘制线条虚线
    private Paint mLinePaintDash;
    //绘制圆点
    private Paint mPointPaint;
    //线条直接区域的背景
    private Paint mLineAreaPaint;
    //绘制线条的path
    private Path pathLine;
    //绘制背景渐变的path
    private Path pathShader;
    //线形值
    private List<DataSet> dataSets;
    //线条右侧的文字
    private List<String> rightLabels;
    //线形动画
    private AnimationSet animationSet;
    //线条的x y值
    private Map<String, List<ChartPoint>> pointsMap = new HashMap<>();

    //当前右侧文字x值
    private int currentRightTextX;
    private int pointWidth = 3;

    public LineChart(Context context) {
        super(context);
        initView();
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(Utils.sp2px(9));

        //线形图paint
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(Utils.dp2px(1));

        mLinePaintDash = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaintDash.setStyle(Paint.Style.STROKE);
        mLinePaintDash.setStrokeWidth(Utils.dp2px(1));
        mLinePaintDash.setPathEffect(new DashPathEffect(new float[]{
                5f, 5f
        }, 0f));

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.GREEN);

        mLineAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineAreaPaint.setStyle(Paint.Style.FILL);
        mLineAreaPaint.setAlpha(65);

        animationSet = new AnimationSet(true);
        pathLine = new Path();
        pathShader = new Path();


        currentRightTextX = getScreenWidth();

    }

    public void setRightLabels(List<String> rightLabels) {
        this.rightLabels = rightLabels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制线条
        drawLines(canvas);
        //绘制线条上部分的文字
        drawTextAboveLine(canvas, currentRightTextX);
        //绘制圆点
        drawCircles(canvas);
    }

    /**
     * 获取下一条线的path
     *
     * @param pointsList
     * @return
     */
    private Path getNextLinePath(List<ChartPoint> pointsList) {
        Path pathLineNext = new Path();
        ChartPoint previousePoint = null;
        for (int i = pointsList.size() - 1; i > -1; i--) {
            ChartPoint chartPoint = pointsList.get(i);
            if (i == pointsList.size() - 1) {
                previousePoint = chartPoint;
                pathLineNext.moveTo(chartPoint.getxValue(), chartPoint.getyValue());
            } else {
                pathLineNext.quadTo(previousePoint.getxValue(), previousePoint.getyValue(),
                        chartPoint.getxValue(), chartPoint.getyValue());
                previousePoint = chartPoint;
            }
        }
        return pathLineNext;
    }

    /**
     * 绘制线条
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        pointsMap.clear();
        if (dataSets != null && dataSets.size() > 0) {
            for (int k = 0; k < dataSets.size(); k++) {

                List<LineEntity> mLineValues = dataSets.get(k).getDataSet();

//                float[] animItem = animProgress.get(k);
                int mColor = dataSets.get(k).getmColor();

                mLinePaint.setColor(mColor);
                mLinePaintDash.setColor(mColor);

                pathLine.reset();
                pathShader.reset();

                List<ChartPoint> pointsList = new ArrayList<>();
                //获取坐标点
                pointsList.addAll(generatePoints(mLineValues));

                List<LineEntity> mLineValuesNext = null;
                List<ChartPoint> pointsListNext = null;
                if (k < dataSets.size() - 1) {
                    mLineValuesNext = dataSets.get(k + 1).getDataSet();
                    pointsListNext = new ArrayList<>();
                    pointsListNext.addAll(generatePoints(mLineValuesNext));
                }

                int index = 0;
                ChartPoint previousePoint = null;
                for (ChartPoint chartPoint : pointsList) {
                    if (index == 0) {
                        previousePoint = chartPoint;
                        pathLine.moveTo(chartPoint.getxValue(), chartPoint.getyValue());
                    } else {
                        pathLine.quadTo(previousePoint.getxValue(), previousePoint.getyValue(),
                                chartPoint.getxValue(), chartPoint.getyValue());
                        previousePoint = chartPoint;
                    }
                    index++;
                }

                pointsMap.put(rightLabels.get(k), pointsList);
                //绘制线条
                canvas.drawPath(pathLine, mLinePaint);

                //绘制背景色
                pathShader.addPath(pathLine);
                if (pointsListNext != null) {
                    pathShader.lineTo(pointsListNext.get(pointsListNext.size() - 1).getxValue(), //下一条线的x轴
                            pointsListNext.get(pointsListNext.size() - 1).getyValue());//下一条线的y轴

                    //下一条线的所有lines point, 连接下一条线的所有path
                    pathShader.addPath(getNextLinePath(pointsListNext));
                }
                //连接最终点
                pathShader.lineTo(pointsList.get(0).getxValue(), pointsList.get(0).getyValue());

                //最后一条不需要画背景
                if (k < dataSets.size() - 1) {
                    pathShader.close();
                    canvas.drawPath(pathShader, getShadeColorPaint(
                            previousePoint.getxValue(), previousePoint.getyValue(),
                            pointsList.get(0).getxValue(), pointsList.get(0).getyValue()
                    ));
                }

            }


        }
    }

    /**
     * 获取坐标点
     *
     * @param mLineValues
     */
    private List<ChartPoint> generatePoints(List<LineEntity> mLineValues) {
        List<ChartPoint> list = new ArrayList<>();
        for (int i = 0; i < mLineValues.size(); i++) {
            LineEntity lineEntityStart = mLineValues.get(i);

            float entityX = findFinalPointXByXLabel(lineEntityStart.getmLabel());
            float entityY =
//                            findFinalYByValue(animItem[i]);
                    findFinalYByValue(lineEntityStart.getmValue());
            ChartPoint chartPoint = new ChartPoint(entityX, entityY);
            list.add(chartPoint);
        }
        return list;
    }

    /**
     * 在线的右上角绘制文字
     * 文字始终在屏幕右侧，线条的上半部分
     *
     * @param canvas
     */
    private void drawTextAboveLine(Canvas canvas, int factScreenWidth) {
        if (rightLabels != null && rightLabels.size() > 0) {
            for (String rightLabel : rightLabels) {
                List<ChartPoint> points = pointsMap.get(rightLabel);
                ChartPoint targetPoint = null;
                for (ChartPoint point : points) {
                    if (point.getxValue() < factScreenWidth) {
                        targetPoint = point;
                    }
                }
                if (targetPoint != null) {
                    canvas.drawText(rightLabel, factScreenWidth - ScreenUtils.calcTextWidth(mTextPaint, rightLabel),
                            targetPoint.getyValue() - ScreenUtils.dp2px(getContext(), 10), mTextPaint);
                }
            }
        }
    }

    /**
     * 通知文字point的位置更新
     *
     * @param offset
     * @param left   false 右侧边距增大 ， true :右侧边距变小
     */
    public void notifyTextPointChange(int offset, boolean left) {
        currentRightTextX = getScreenWidth()+Math.abs(offset);
        invalidate();
    }


    //区域颜色
    int lineShaderColor = Color.parseColor("#abedfe");


    // 修改笔的颜色
    private Paint getShadeColorPaint(float startX, float startY, float stopX, float stopY) {
        Shader mShader = new LinearGradient(startX, startY, stopX, stopY,
                new int[]{lineShaderColor, lineShaderColor}, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        mLineAreaPaint.setShader(mShader);
        return mLineAreaPaint;
    }

    private List<PointEntity> points;
    /**
     * 落点数据
     * @param points
     */
    public void setPoints(List<PointEntity> points){
        this.points = points;
        invalidate();
    }

    /**
     * 绘制圆点
     * @param canvas
     */
    private void drawCircles(Canvas canvas){
        if(points!=null && points.size()>0){
            for (PointEntity point:points){
                //x轴文字
                int xLabelValue = point.getxLableValue();
                //y轴数据
                float mValue = point.getmValue();

                //x坐标
                float entityX = findFinalPointXByXValue(xLabelValue);
                //y坐标
                float entityY = findFinalYByValue(mValue);
                drawCircle(canvas, entityX, entityY);
            }
        }
    }

    /**
     * 画圆点
     *
     * @param canvas
     * @param circleX
     * @param circleY
     */
    private void drawCircle(Canvas canvas, float circleX, float circleY) {
        canvas.drawCircle(circleX, circleY, Utils.dp2px(pointWidth), mPointPaint);
    }

    /**
     * 设置数据源
     *
     * @param dataSets
     */
    public void setDataSets(List<DataSet> dataSets) {
        this.dataSets = dataSets;
        animProgress = new LinkedList<>();
        int duration = 1000;
        for (int i = 0; i < dataSets.size(); i++) {
            float[] aniItems = new float[dataSets.get(i).getDataSet().size()];
            animProgress.add(aniItems);

            HistogramAnimation lineAnimation = new HistogramAnimation();
            lineAnimation.setDuration(duration);
            animationSet.addAnimation(lineAnimation);

            duration += 200;
        }
    }


    private LinkedList<float[]> animProgress;

    public void animShow(int flag) {
        this.flag = flag;
        animationSet.setDuration(1000);
        startAnimation(animationSet);

    }

    /**
     * 集成animation的一个动画类
     *
     * @author
     */
    private class HistogramAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f && flag == 2) {
                for (int i = 0; i < dataSets.size(); i++) {
                    List<LineEntity> mLineValues = dataSets.get(i).getDataSet();
                    float[] aniItem = animProgress.get(i);
                    for (int j = 0; j < aniItem.length; j++) {
                        aniItem[j] = mLineValues.get(j).getmValue() * interpolatedTime;
                    }
                    animProgress.set(i, aniItem);
                }

            } else {
                for (int i = 0; i < dataSets.size(); i++) {
                    List<LineEntity> mLineValues = dataSets.get(i).getDataSet();
                    float[] aniItem = animProgress.get(i);
                    for (int j = 0; j < aniItem.length; j++) {
                        aniItem[j] = mLineValues.get(j).getmValue();
                    }
                    animProgress.set(i, aniItem);
                }
            }
            invalidate();
        }
    }

}
