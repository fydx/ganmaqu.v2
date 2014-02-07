package com.xstrikers.ganmaquv2.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * Created by LB on 14-1-15.
 */
public class Place implements Serializable {

  private int _id; // 数据库主键
  private static final long serialVersionUID = 1L;
  @Expose
  private String address;
  @Expose
  private int cost;
  @Expose
  private String detailType;
  @Expose
  private int id;
  @Expose
  private String mainType;
  // private double lng;
  // private double lat;
  @Expose
  private double pos_x;
  @Expose
  private double pos_y;
  @Expose
  private int rate;
  @Expose
  private String shopName;
  @Expose
  private String suitType;
  @Expose
  private String telNumber;
  @Expose
  private String time;
  @Expose
  private String picUrl;
  @Expose
  private int weight;
  @Expose
  private String info;

  public Place()
  {

  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public String getDetailType() {
    return detailType;
  }

  public void setDetailType(String detailType) {
    this.detailType = detailType;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getMainType() {
    return mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  public double getPos_x() {
    return pos_x;
  }

  public void setPos_x(double pos_x) {
    this.pos_x = pos_x;
  }

  public double getPos_y() {
    return pos_y;
  }

  public void setPos_y(double pos_y) {
    this.pos_y = pos_y;
  }

  public int getRate() {
    return rate;
  }

  public void setRate(int rate) {
    this.rate = rate;
  }

  public String getShopName() {
    return shopName;
  }

  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  public String getSuitType() {
    return suitType;
  }

  public void setSuitType(String suitType) {
    this.suitType = suitType;
  }

  public String getTelNumber() {
    return telNumber;
  }

  public void setTelNumber(String telNumber) {
    this.telNumber = telNumber;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public Place(String address, int cost, String detailType, int id,
      String mainType, double lng, double lat, int rate,
      String shopName, String suitType, String telNumber, String time) {
    super();
    this.address = address;
    this.cost = cost;
    this.detailType = detailType;
    this.id = id;
    this.mainType = mainType;
    this.pos_x = lng;
    this.pos_y = lat;
    this.rate = rate;
    this.shopName = shopName;
    this.suitType = suitType;
    this.telNumber = telNumber;
    this.time = time;
  }

  @Override
  public String toString() {
    return "Place{" +
        "_id=" + _id +
        ", address='" + address + '\'' +
        ", cost=" + cost +
        ", detailType='" + detailType + '\'' +
        ", id=" + id +
        ", mainType='" + mainType + '\'' +
        ", lng=" + pos_x +
        ", lat=" + pos_y +
        ", rate=" + rate +
        ", shopName='" + shopName + '\'' +
        ", suitType='" + suitType + '\'' +
        ", telNumber='" + telNumber + '\'' +
        ", time='" + time + '\'' +
        ", picUrl='" + picUrl + '\'' +
        ", weight=" + weight +
        '}';
  }
}
