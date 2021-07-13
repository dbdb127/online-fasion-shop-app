package com.seungwoodev.project2;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PurchaseResult {

    private Integer code;
    private ArrayList<String> name;
    private ArrayList<Integer> qty;
    private ArrayList<String> time;

    public Integer getCode(){return code;}
    public ArrayList<String> getName() {
        return name;
    }
    public ArrayList<Integer> getQty() {return qty;}
    public ArrayList<String> getTimeStamp() {return time;}
}
