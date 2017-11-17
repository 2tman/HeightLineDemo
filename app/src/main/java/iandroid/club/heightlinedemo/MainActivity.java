package iandroid.club.heightlinedemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        /**
         * 身高
         */
        findViewById(R.id.btn_height).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GrouthHeightActivity.class));
            }
        });

        /**
         * 体重
         */
        findViewById(R.id.btn_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GrouthWeightActivity.class));
            }
        });
    }
}
