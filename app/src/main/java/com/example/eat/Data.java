package com.example.eat;

public class Data {

    private final String itemName;
    private final String itemImage;
    private String movieNm;      // 영화이름
    private String audiAcc;      // 누적관객수
    private String openDt;       // 영화개봉일

    private boolean isChecked; // 체크 상태를 나타내는 변수

    public String getOpenDt() {
        return openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    public Data(String itemName, String itemImage, String openDt) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.openDt = openDt;
        this.isChecked = false;
    }

    public Data(String itemName, String itemImage) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.openDt = "";
        this.isChecked = false;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
