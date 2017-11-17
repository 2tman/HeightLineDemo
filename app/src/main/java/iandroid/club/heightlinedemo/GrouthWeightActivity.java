package iandroid.club.heightlinedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import iandroid.club.heightlinedemo.widget.BrokenLineChartView;

/**
 * 体重曲线
 */
public class GrouthWeightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouth_weight);

        BrokenLineChartView linechart = (BrokenLineChartView)findViewById(R.id.linechart);
        linechart.drawBrokenLine();
    }
}
