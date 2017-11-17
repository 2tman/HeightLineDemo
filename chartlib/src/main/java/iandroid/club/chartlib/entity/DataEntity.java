package iandroid.club.chartlib.entity;

/**
 * Created by 加荣 on 2017/7/28.
 */

public class DataEntity {
    private float mValue;
    private String mLabel;
    private boolean drawDash;

    public float getmValue() {
        return mValue;
    }

    public void setmValue(float mValue) {
        this.mValue = mValue;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public boolean isDrawDash() {
        return drawDash;
    }

    public void setDrawDash(boolean drawDash) {
        this.drawDash = drawDash;
    }

    public DataEntity(float mValue, String mLabel) {
        this.mValue = mValue;
        this.mLabel = mLabel;
    }

}
