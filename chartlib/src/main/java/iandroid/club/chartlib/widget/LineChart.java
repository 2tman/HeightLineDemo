package iandroid.club.chartlib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import iandroid.club.chartlib.R;
import iandroid.club.chartlib.entity.ChartPoint;
import iandroid.club.chartlib.entity.DataSet;
import iandroid.club.chartlib.entity.LineEntity;
import iandroid.club.chartlib.entity.PointEntity;
import iandroid.club.chartlib.util.ScreenUtils;
import iandroid.club.chartlib.util.Utils;


/**
 * @version 0.1
 * @Date 创建时间：2017/11/27
 * @Author jiarong
 * @Description 线形图
 */
public class LineChart extends BaseChart {

    //绘制文字
    private Paint mTextPaint;
    //绘制线条
    private Paint mLinePaint;
    //绘制圆点
    private Paint mPointPaint;
    //线条之间区域的背景
    private Paint mLineAreaPaint;
    //绘制线条的path
    private Path pathLine;
    //绘制背景渐变的path
    private Path pathShader;
    //线形值
    private List<DataSet> dataSets;
    //线条右侧的文字
    private List<String> rightLabels;
    //线条的x y值
    private Map<String, List<ChartPoint>> pointsMap = new HashMap<>();

    //当前右侧文字x值
    private int currentRightTextX;
    private int pointWidth = 3;

    //当前所在位置
    private int currentXValue;

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
        drawGridLine = true;

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(Utils.sp2px(9));

        //线形图paint
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.parseColor("#66acf2"));

        mLineAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineAreaPaint.setStyle(Paint.Style.FILL);
        mLineAreaPaint.setAlpha(65);

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
        //绘制x轴当前所在位置刻度线
        drawCurrentXValueLine(canvas);
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
            for (int i = 0; i < dataSets.size(); i++) {
                pathLine.reset();
                pathShader.reset();
                //单条线数据
                List<LineEntity> mLineValues = dataSets.get(i).getDataSet();
                //线条颜色
                int mColor = dataSets.get(i).getmColor();
                //线条宽度
                mLinePaint.setStrokeWidth(Utils.dp2px(1));
                //线条颜色
                mLinePaint.setColor(mColor);

                List<ChartPoint> pointsList = new ArrayList<>();
                //获取坐标点
                pointsList.addAll(generatePoints(mLineValues));

                List<LineEntity> mLineValuesNext = null;
                List<ChartPoint> pointsListNext = null;
                if (i < (dataSets.size() - 1)) {
                    mLineValuesNext = dataSets.get(i + 1).getDataSet();
                    pointsListNext = new ArrayList<>();
                    pointsListNext.addAll(generatePoints(mLineValuesNext));
                }

                int index = 0;
                ChartPoint previousePoint = null;
                for (ChartPoint chartPoint : pointsList) {
                    if (index == 0) {
                        pathLine.moveTo(chartPoint.getxValue(), chartPoint.getyValue());
                    } else {
                        pathLine.quadTo(previousePoint.getxValue(), previousePoint.getyValue(),
                                chartPoint.getxValue(), chartPoint.getyValue());
                    }
                    previousePoint = chartPoint;
                    index++;
                }

                if (rightLabels != null && rightLabels.size() > 0) {
                    pointsMap.put(rightLabels.get(i), pointsList);
                }
                //绘制线条
                canvas.drawPath(pathLine, mLinePaint);


                if (pointsListNext != null && pointsListNext.size()>1) {
                    //绘制背景色
                    pathShader.addPath(pathLine);

                    pathShader.lineTo(pointsListNext.get(pointsListNext.size() - 1).getxValue(), //下一条线的x轴
                            pointsListNext.get(pointsListNext.size() - 1).getyValue());//下一条线的y轴

                    //下一条线的所有lines point, 连接下一条线的所有path
                    pathShader.addPath(getNextLinePath(pointsListNext));

                    //连接最终点
                    pathShader.lineTo(pointsList.get(0).getxValue(), pointsList.get(0).getyValue());

                    //最后一条不需要画背景
                    if (i < dataSets.size() - 1) {
                        pathShader.close();
                        canvas.drawPath(pathShader, getShadeColorPaint(
                                previousePoint.getxValue(), previousePoint.getyValue(),
                                pointsList.get(0).getxValue(), pointsList.get(0).getyValue()
                        ));
                    }
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
            float entityY = findFinalYByValue(lineEntityStart.getmValue());
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
            if (pointsMap != null && pointsMap.size() > 0) {
                mTextPaint.setColor(Color.BLACK);
                for (String rightLabel : rightLabels) {
                    if (pointsMap.containsKey(rightLabel)) {
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
        }
    }

    /**
     * 通知文字point的位置更新
     *
     * @param offset
     * @param left   false 右侧边距增大 ， true :右侧边距变小
     */
    public void notifyTextPointChange(int offset, boolean left) {
        currentRightTextX = getScreenWidth() + Math.abs(offset);
        invalidate();
    }


    //区域颜色
    int lineShaderColor = Color.parseColor("#8fd9dc");


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
     *
     * @param points
     */
    public void setPoints(final List<PointEntity> points) {
        this.points = points;
//        invalidate();
    }

    /**
     * 绘制圆点
     *
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        pathLine.reset();
        if (points != null && points.size() > 0) {
            int index = 0;
            float entityXPre = 0;
            float entityYPre = 0;
            for (PointEntity point : points) {
                //x轴文字
                int xLabelValue = point.getxLableValue();
                //y轴数据
                float mValue = point.getmValue();

                //x坐标
                float entityX = findFinalPointXByXValue(xLabelValue);
                //y坐标
                float entityY = findFinalYByValue(mValue);
                drawCircle(canvas, entityX, entityY);

                //点和点之间连线
                if (index == 0) {
                    pathLine.moveTo(entityX, entityY);
                } else {
                    pathLine.quadTo(entityXPre, entityYPre,
                            entityX, entityY);
                }
                entityXPre = entityX;
                entityYPre = entityY;
                index++;
            }
            mLinePaint.setColor(Color.parseColor("#66acf2"));
            mLinePaint.setStrokeWidth(Utils.dp2px(2));
            canvas.drawPath(pathLine, mLinePaint);
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
    }

    /**
     * x轴当前所在位置
     *
     * @param currentXValue
     */
    public void setCurrentXValue(int currentXValue) {
        this.currentXValue = currentXValue;
        invalidate();
    }

    /**
     * 绘制x轴当前所在位置 刻度线
     *
     * @param canvas
     */
    private void drawCurrentXValueLine(Canvas canvas) {
        if (currentXValue != 0) {
            pathLine.reset();
            mLinePaint.setPathEffect(new DashPathEffect(new float[]{
                    15f, 15f
            }, 0f));
            mLinePaint.setStrokeWidth(Utils.dp2px(2));
            mLinePaint.setColor(Color.parseColor("#f69b3b"));
            //x坐标
            float entityX = findFinalPointXByXValue(currentXValue);
            pathLine.moveTo(entityX, getyRender().getZeroLineHeight());
            pathLine.lineTo(entityX, getyRender().getMaxYValuePoint());
            canvas.drawPath(pathLine, mLinePaint);
            mLinePaint.setPathEffect(null);
            pathLine.close();

            //绘制标注
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chengzhang_quxian_bg);
            int bitmapHeight = bitmap.getHeight();
            int bitmapWidth = bitmap.getWidth();
            float targetX = entityX + Utils.dp2px(2);
            float targetY = getyRender().getZeroLineHeight() - bitmapHeight - Utils.dp2px(20)
                    + yOffset;
            canvas.drawBitmap(bitmap, targetX, targetY, mLinePaint);

            //绘制标注文字
            String textLabel = "今天";
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setTextSize(Utils.sp2px(12));
            float textY = targetY + bitmapHeight / 2 + Utils.calcTextHeight(mTextPaint, textLabel) / 2;
            float textX = targetX + (bitmapWidth - Utils.calcTextWidth(mTextPaint, textLabel)) / 2;
            canvas.drawText(textLabel, textX + Utils.dp2px(2), textY - Utils.dp2px(1), mTextPaint);
        }
    }

    private float yOffset;
    private int offsetTop;

    /**
     * 进行移动
     *
     * @param xleft
     */
    public void scrollInvalidate(float xleft) {
        yOffset = Math.abs(xleft) / getCanvasWidth() * getyRender().getMaxYValuePoint();
        offsetTop = Math.round(yOffset);
        if (offsetTop < getyRender().getMaxYValueYPosition()) {
            offsetTop = Math.round(getyRender().getMaxYValueYPosition());
        }
        scrollTo(0, Math.round(yOffset));
//        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 进行移动
     *
     * @param xleft
     */
    public void scrollInvalidate(int targetX, float xleft) {
        yOffset = Math.abs(xleft) / getCanvasWidth() * getyRender().getMaxYValuePoint();
        offsetTop = Math.round(yOffset);
        if (offsetTop < getyRender().getMaxYValueYPosition()) {
            return;
        }
        scrollTo(targetX, Math.round(yOffset));
    }
}
