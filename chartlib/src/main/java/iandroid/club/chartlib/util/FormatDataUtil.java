package iandroid.club.chartlib.util;

/**
 * @Description: x轴文本格式化
 * @Author: 加荣
 * @Time: 2017/11/16
 */
public class FormatDataUtil {

    public static final int TYPE_THIRD_INT = 3;
    public static final int TYPE_FIFTH_INT = 50;
    public static final int TYPE_NIGHT_SEVEN_INT = 97;

    public static final String TYPE_THIRD = "3%";
    public static final String TYPE_FIFTH = "50%";
    public static final String TYPE_NIGHT_SEVEN = "97%";

    /**
     * 获取x轴format label
     *
     * @param xValue
     * @return
     */
    public static String getFormatXLabel(int xValue) {
        if (xValue == 0) {
            return "出生";
        } else if (xValue > 0 && xValue % 12 == 6) {
            int temp = xValue / 12;
            if (temp == 0) {
                return "半岁";
            }
            return temp + "岁半";
        } else if (xValue > 0 && xValue % 12 == 0) {
            return (xValue / 12)+"岁";
        }
        return xValue + "个月";
    }

}
