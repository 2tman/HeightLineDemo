package iandroid.club.heightlinedemo.entity;

/**
 * @Description:
 * @Author: 加荣
 * @Time: 2017/11/17
 */
public class ChartPoint {
    private float xValue;
    private float yValue;

    public float getxValue() {
        return xValue;
    }

    public void setxValue(float xValue) {
        this.xValue = xValue;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }

    public ChartPoint(float xValue, float yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }
}
