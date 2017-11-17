package iandroid.club.chartlib.entity;

import java.util.List;

/**
 * Created by 加荣 on 2017/7/28.
 */

public class DataSet {
    private List<LineEntity> dataSet;
    private int mColor;

    public List<LineEntity> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<LineEntity> dataSet) {
        this.dataSet = dataSet;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }
}
