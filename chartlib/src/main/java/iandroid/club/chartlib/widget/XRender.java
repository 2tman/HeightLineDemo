package iandroid.club.chartlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by gabriel on 2017/11/27.
 * 功能描述
 */

public class XRender extends BaseChart {
    public XRender(Context context) {
        super(context);
        initView();
    }

    public XRender(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public XRender(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        drawGridLine = false;
        drawXLine = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void animShow() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 30);

    }
}
