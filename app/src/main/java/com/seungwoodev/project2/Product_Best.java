package com.seungwoodev.project2;

import android.widget.ImageView;

import java.util.ArrayList;

public class Product_Best {
    public String name;
    public int price;
    public int qty;
    public int hit;

    public Product_Best(){

    }

    //if header
    public Product_Best(String name, int price, int qty, int hit) {
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.hit = hit;
    }

//    public String getName(){
//        return name;
//    }
//    public int getPrice(){
//        return price;
//    }
//    public int getQty(){
//        return qty;
//    }
//    public int getHit(){
//        return hit;
//    }
    public static boolean compare(Product_Best arg0, Product_Best arg1){
        return arg0.hit >= arg1.hit;
    }
}
