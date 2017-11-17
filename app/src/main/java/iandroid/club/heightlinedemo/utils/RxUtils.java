package iandroid.club.heightlinedemo.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;

/**
 * @Description: rxjava 单线程 单次操作封装
 * @Author: 加荣
 * @Time: 2017/11/3
 */
public class RxUtils {


    private static final String TAG = RxUtils.class.getSimpleName();

    public interface OnRequestListener extends OnBaseRequestListener{
        /**
         * 后台多线程操作
         * @return
         */
        Object doInBackground();

    }

    public interface OnBaseRequestListener{

        /**
         * 结果返回
         * @param object
         */
        void onResultBack(Object object);

        /**
         * 请求结束
         */
        void onCompleted();

    }

    /**
     * 创建被观察者
     * @param activity
     * @param onRequestListener
     */
    public static void createObserable(final Context activity, final OnRequestListener onRequestListener) {
        //定义被观察者
        Observable<Object> observable = Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        //后台请求
                        Object object = onRequestListener.doInBackground();

                        subscriber.onNext(object);

                        subscriber.onCompleted();

                    }
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });
        Subscriber<Object> showsub = new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
                onRequestListener.onCompleted();

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
                Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                onRequestListener.onCompleted();
            }

            @Override
            public void onNext(Object object) {
                Log.i(TAG, "result-->>" + object);
                //请求返回
                onRequestListener.onResultBack(object);

            }
        };

        observable.subscribe(showsub);//关联被观察者

    }
}
