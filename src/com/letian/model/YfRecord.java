package com.letian.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-24
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class YfRecord extends Model {

    public boolean result;
    public String reason;

    public String louge;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLouge_bh() {
        return louge_bh;
    }

    public void setLouge_bh(String louge_bh) {
        this.louge_bh = louge_bh;
    }

    public String louge_bh;
    public String danyuan;
    public String huxing;
    public String fangjianleixing;
    public String shoulouduixiang;
    public String shoulouxiangmu;

    public Context context;

    public String getDanyuan_id() {
        return danyuan_id;
    }

    public void setDanyuan_id(String danyuan_id) {
        this.danyuan_id = danyuan_id;
    }

    public String getHuxing_id() {
        return huxing_id;
    }

    public void setHuxing_id(String huxing_id) {
        this.huxing_id = huxing_id;
    }

    public String getFangjianleixing_id() {
        return fangjianleixing_id;
    }

    public void setFangjianleixing_id(String fangjianleixing_id) {
        this.fangjianleixing_id = fangjianleixing_id;
    }

    public String getShoulouduixiang_id() {
        return shoulouduixiang_id;
    }

    public void setShoulouduixiang_id(String shoulouduixiang_id) {
        this.shoulouduixiang_id = shoulouduixiang_id;
    }

    public String getShoulouxiangmu_id() {
        return shoulouxiangmu_id;
    }

    public void setShoulouxiangmu_id(String shoulouxiangmu_id) {
        this.shoulouxiangmu_id = shoulouxiangmu_id;
    }

    public String danyuan_id;
    public String huxing_id;
    public String fangjianleixing_id;
    public String shoulouduixiang_id;
    public String shoulouxiangmu_id;

    public YfRecord(Context context) {
        this.context = context;
    }

    public String getDanyuan() {
        return danyuan;
    }

    public void setDanyuan(String danyuan) {
        this.danyuan = danyuan;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

    public String getFangjianleixing() {
        return fangjianleixing;
    }

    public void setFangjianleixing(String fangjianleixing) {
        this.fangjianleixing = fangjianleixing;
    }

    public String getShoulouduixiang() {
        return shoulouduixiang;
    }

    public void setShoulouduixiang(String shoulouduixiang) {
        this.shoulouduixiang = shoulouduixiang;
    }

    public String getShoulouxiangmu() {
        return shoulouxiangmu;
    }

    public void setShoulouxiangmu(String shoulouxiangmu) {
        this.shoulouxiangmu = shoulouxiangmu;
    }

    public String getLouge() {
        return louge;
    }

    public void setLouge(String louge) {
        this.louge = louge;
    }


}
