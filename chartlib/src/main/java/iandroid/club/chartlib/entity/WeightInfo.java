package iandroid.club.chartlib.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @Description: 体重基数
 * @Author: 加荣
 * @Time: 2017/11/16
 */

public class WeightInfo implements Comparable<WeightInfo>, Serializable {

    private int _id;

    //年龄
    private int age;

    //体重类型
    private int weightType;

    //体重
    private String weight;

    //性别
    private String sex;

    public WeightInfo(int age, String weight) {
        this.age = age;
        this.weight = weight;
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



    public String getWeight() {
        return this.weight;
    }


    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getWeightType() {
        return weightType;
    }

    public void setWeightType(int weightType) {
        this.weightType = weightType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public int compareTo(@NonNull WeightInfo o) {
        return o.getAge() < this.getAge() ? 1 : -1;
    }


}
