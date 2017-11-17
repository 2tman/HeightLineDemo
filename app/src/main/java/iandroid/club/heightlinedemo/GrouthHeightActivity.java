package iandroid.club.heightlinedemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import iandroid.club.chartlib.entity.BaseHeight;
import iandroid.club.chartlib.entity.DataSet;
import iandroid.club.chartlib.entity.LineEntity;
import iandroid.club.chartlib.util.FormatDataUtil;
import iandroid.club.chartlib.util.LogUtils;
import iandroid.club.chartlib.widget.ComboChart;
import iandroid.club.chartlib.widget.YRender;
import iandroid.club.heightlinedemo.utils.GrouthDataUtil;
import iandroid.club.heightlinedemo.utils.RxUtils;

/**
 * 身高曲线
 */
public class GrouthHeightActivity extends AppCompatActivity {

    private ComboChart comboChart;
    private List<String> xLables = new ArrayList<>();
    //3%的基数
    private List<LineEntity> lineEntitiesThird = new ArrayList<>();
    //50%的基数
    private List<LineEntity> lineEntitiesFifth = new ArrayList<>();
    //97%的基数
    private List<LineEntity> lineEntitiesNightySeven = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouth_height);

        comboChart = (ComboChart) findViewById(R.id.mComboChart);

        queryDataSource();
        addCombEvent();
    }



    private List<BaseHeight> baseHeights = new ArrayList<>();

    /**
     * json数据解析
     */
    private void queryDataSource(){
        RxUtils.createObserable(this, new RxUtils.OnRequestListener() {
            @Override
            public Object doInBackground() {
                return GrouthDataUtil.getData();
            }

            @Override
            public void onResultBack(Object object) {
                List<BaseHeight> newList = (List<BaseHeight>)object;
                baseHeights.clear();
                baseHeights.addAll(newList);
                initDataCombo(comboChart.getyRender());
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    private float maxValue = 0;

    /**
     * 获取y轴最大值
     */
    private void generateMaxValue(){
        if(baseHeights!=null && baseHeights.size()>0){
            for (BaseHeight item:baseHeights){
                float height = Float.parseFloat(item.getHeight());
                if(maxValue<height){
                    maxValue = height;
                }
            }
        }
    }

    private Comparator<BaseHeight> COMPARATOR = new Comparator<BaseHeight>() {
        public int compare(BaseHeight o1, BaseHeight o2) {
            return o1.compareTo(o2);
        }
    };

    /**
     * 设置数据源
     * @param yRender
     */
    public void initDataCombo(YRender yRender) {
        generateMaxValue();

        int yMaxValue = (int)Math.ceil(maxValue);
        yRender.setMinValue(0);
        yRender.setMaxValue(yMaxValue);
        yRender.generateValues();
        lineEntitiesThird.clear();
        lineEntitiesFifth.clear();
        lineEntitiesNightySeven.clear();
        xLables.clear();

        List<BaseHeight> thirdData = new ArrayList<>();
        List<BaseHeight> fifThData = new ArrayList<>();
        List<BaseHeight> nightSevenData = new ArrayList<>();

        Collections.sort(baseHeights, COMPARATOR);

        for (int i = 0; i < baseHeights.size(); i++) {
            BaseHeight baseHeight = baseHeights.get(i);

            //3%的数据
            if(baseHeight.getHeightType() == FormatDataUtil.TYPE_THIRD_INT) {
                thirdData.add(baseHeight);

            }else if(baseHeight.getHeightType() == FormatDataUtil.TYPE_FIFTH_INT) {
                //50%的数据
                fifThData.add(baseHeight);
            }else if(baseHeight.getHeightType() == FormatDataUtil.TYPE_NIGHT_SEVEN_INT) {
                //97%的数据
                nightSevenData.add(baseHeight);
            }
        }

        Collections.sort(thirdData, COMPARATOR);

        Collections.sort(fifThData, COMPARATOR);

        Collections.sort(nightSevenData, COMPARATOR);

        /**
         * 3%的数据
         */
        for (int i = 0; i < thirdData.size(); i++) {
            BaseHeight baseHeight = thirdData.get(i);
            String label = FormatDataUtil.getFormatXLabel(baseHeight.getAge());
            LineEntity lineEntity = new LineEntity(Float.parseFloat(baseHeight.getHeight()), label);
            lineEntitiesThird.add(lineEntity);

            if(!xLables.contains(label)){
                xLables.add(label);
            }
        }

        /**
         * 50%的数据
         */
        for (int i = 0; i < fifThData.size(); i++) {
            BaseHeight baseHeight = fifThData.get(i);
            String label = FormatDataUtil.getFormatXLabel(baseHeight.getAge());
            LineEntity lineEntity = new LineEntity(Float.parseFloat(baseHeight.getHeight()), label);
            lineEntitiesFifth.add(lineEntity);
        }

        /**
         * 97%的数据
         */
        for (int i = 0; i < nightSevenData.size(); i++) {
            BaseHeight baseHeight = nightSevenData.get(i);
            String label = FormatDataUtil.getFormatXLabel(baseHeight.getAge());
            LineEntity lineEntity = new LineEntity(Float.parseFloat(baseHeight.getHeight()), label);
            lineEntitiesNightySeven.add(lineEntity);
        }


        List<DataSet> dataSets = new ArrayList<>();

        DataSet dataSet3 = new DataSet();
        dataSet3.setDataSet(lineEntitiesThird);
        dataSet3.setmColor(Color.parseColor("#abedfe"));

        DataSet dataSet50 = new DataSet();
        dataSet50.setDataSet(lineEntitiesFifth);
        dataSet50.setmColor(Color.parseColor("#abedfe"));

        DataSet dataSet97 = new DataSet();
        dataSet97.setDataSet(lineEntitiesNightySeven);
        dataSet97.setmColor(Color.parseColor("#abedfe"));

        /**
         * 右侧文字
         */
        List<String> rightLables = new ArrayList<>();
        rightLables.add("97%");
        rightLables.add("50%");
        rightLables.add("3%");

        dataSets.add(dataSet97);
        dataSets.add(dataSet50);
        dataSets.add(dataSet3);


        comboChart.getBaseBarChart().setRightLabels(rightLables);
        comboChart.getBaseBarChart().setxLabels(xLables);
        comboChart.getBaseBarChart().setDataSets(dataSets);

        comboChart.animShow();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    private void addCombEvent() {
        comboChart.setOnDragListener(new ComboChart.OnDragListener() {
            @Override
            public void onDragToLeft(int position) {
                LogUtils.log("滑到了最左边");
            }

            @Override
            public void onDragToRight(int position) {
                LogUtils.log("滑到了最右边");
            }
        });

    }
}
