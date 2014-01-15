package com.xstrikers.ganmaquv2.model;

import java.io.Serializable;

/**
 * Created by LB on 14-1-15.
 */
public class Place implements Serializable {

  private int _id; // 数据库主键
  private static final long serialVersionUID = 1L;
  private String address;
  private int cost;
  private String detailType;
  private int id;
  private int routeId; // 路线的id
  private String mainType;
  private double lng;
  private double lat;
  private int rate;
  private String shopName;
  private String suitType;
  private String telNumber;
  private String time;
  private String routeType;
  private String picUrl;
  private int weight;

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public String getRouteType() {
    return routeType;
  }

  public void setRouteType(String routeType) {
    this.routeType = routeType;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public int getrouteId() {
    return routeId;
  }

  public void setrouteId(int routeId) {
    this.routeId = routeId;
  }

  public Place() {

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

  public void setId(int i) {
    this.id = i;
  }

  public String getMainType() {
    return mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  public double getlng() {
    return lng;
  }

  public void setlng(double lng) {
    this.lng = lng;
  }

  public double getlat() {
    return lat;
  }

  public void setlat(double lat) {
    this.lat = lat;
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

  public Place(String address, int cost, String detailType, int id,
      String mainType, double lng, double lat, int rate,
      String shopName, String suitType, String telNumber, String time) {
    super();
    this.address = address;
    this.cost = cost;
    this.detailType = detailType;
    this.id = id;
    this.mainType = mainType;
    this.lng = lng;
    this.lat = lat;
    this.rate = rate;
    this.shopName = shopName;
    this.suitType = suitType;
    this.telNumber = telNumber;
    this.time = time;
  }

  @Override
  public String toString()
  {
    return shopName;
  }


}
