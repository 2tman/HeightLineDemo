package iandroid.club.chartlib.entity;

import android.support.annotation.NonNull;


import java.io.Serializable;

/**
 * @Description: 身高基数
 * @Author: 加荣
 * @Time: 2017/11/16
 */

public class HeightInfo implements Comparable<HeightInfo> ,Serializable {

    private int _id;

    //年龄
    private int age;

    //身高基数
    private int heightType;

    //身高
    private String height;

    //性别
    private String sex;



    public HeightInfo(int age, String height) {
        this.age = age;
        this.height = height;
    }


    public int get_id() {
        return this._id;
    }


    public void set_id(int _id) {
        this._id = _id;
    }


    public int getAge() {
        return this.age;
    }


    public void setAge(int age) {
        this.age = age;
    }


    public int getHeightType() {
        return this.heightType;
    }


    public void setHeightType(int heightType) {
        this.heightType = heightType;
    }


    public String getHeight() {
        return this.height;
    }


    public void setHeight(String height) {
        this.height = height;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public int compareTo(@NonNull HeightInfo o) {
        return o.getAge()<this.getAge()?1:-1;
    }


}
