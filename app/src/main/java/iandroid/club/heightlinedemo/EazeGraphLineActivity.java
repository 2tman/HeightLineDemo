package iandroid.club.heightlinedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import iandroid.club.heightlinedemo.utils.FormatDataUtil;

public class EazeGraphLineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eaze_graph_line);
        initData();

    }

    private void initData(){
        ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        for (int i=0;i<20;i+=2){
            series.addPoint(new ValueLinePoint(FormatDataUtil.getFormatXLabel(i), 1.4f+i-0.1f));
        }

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
    }
}
