package com.xstrikers.ganmaquv2.model;

import java.util.List;

/**
 * Created by LB on 14-2-7.
 */
public class Route {
    private String placesJSON;
    private int  id; //远程数据库Route id
    private int _id; //本地数据库主键
    private int userId;
    private String date;
    private int like; //点赞次数
    private String comment;
    private List<String> picUrls; //用户上传图片的url

    public Route()
    {

    }

    public String getPlacesJSON() {
        return placesJSON;
    }

    public void setplacesJSON(String placeList) {
        this.placesJSON = placeList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }
}
