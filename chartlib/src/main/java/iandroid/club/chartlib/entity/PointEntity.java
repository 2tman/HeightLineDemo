package iandroid.club.chartlib.entity;

/**
 * 落点
 */
public class PointEntity extends DataEntity{

    private int xLableValue;

    public PointEntity(float mValue, String mLabel) {
        super(mValue, mLabel);
    }

    public int getxLableValue() {
        return xLableValue;
    }

    public void setxLableValue(int xLableValue) {
        this.xLableValue = xLableValue;
    }

    public PointEntity(float mValue, String mLabel, int xLableValue) {
        super(mValue, mLabel);
        this.xLableValue = xLableValue;
    }
}