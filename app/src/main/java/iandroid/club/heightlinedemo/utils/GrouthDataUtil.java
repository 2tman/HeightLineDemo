package iandroid.club.heightlinedemo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

import iandroid.club.heightlinedemo.AppContext;
import iandroid.club.heightlinedemo.R;
import iandroid.club.heightlinedemo.entity.BaseHeight;

/**
 * @Description:
 * @Author: 加荣
 * @Time: 2017/11/16
 */
public class GrouthDataUtil {

    /**
     * 获取身高数据
     * @return
     */
    public static List<BaseHeight> getData(){
        Gson gson = new Gson();
        String res = "";
        InputStream in = null;
        List<BaseHeight> datas = null;
        try {
            in = AppContext.getInstance().getApplicationContext().getResources().openRawResource(R.raw.tbgrouth);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null) {
                    in.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        try {
            datas = gson.fromJson(res,new TypeToken<List<BaseHeight>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }
}
