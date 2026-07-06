package com.example.purepawapp.ui.admin;

public class StatusStyle {
    private final String label;
    private final int bgColorRes;
    private final int textColorRes;

    public StatusStyle(String label, int bgColorRes, int textColorRes) {
        this.label = label;
        this.bgColorRes = bgColorRes;
        this.textColorRes = textColorRes;
    }

    public String getLabel() {
        return label;
    }

    public int getBgColorRes() {
        return bgColorRes;
    }

    public int getTextColorRes() {
        return textColorRes;
    }
}
