package com.cnm.bottomnavigation;

import androidx.annotation.DrawableRes;

public class CNMBottomNavigationItem {

    private String title;
    private @DrawableRes int iconRes;
    private @DrawableRes int activeIconRes;
    private @DrawableRes int inactiveIconRes;

    public CNMBottomNavigationItem() {

    }

    public CNMBottomNavigationItem(String title, @DrawableRes int activeIconRes, @DrawableRes int inactiveIconRes) {
        setTitle(title);
        setIconRes(0);
        setActiveIconRes(activeIconRes);
        setInactiveIconRes(inactiveIconRes);
    }

    public CNMBottomNavigationItem(String title, @DrawableRes int iconRes) {
        setTitle(title);
        setIconRes(iconRes);
        setActiveIconRes(0);
        setInactiveIconRes(0);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getActiveIconRes() {
        return activeIconRes;
    }

    public void setActiveIconRes(int activeIconRes) {
        this.activeIconRes = activeIconRes;
    }

    public int getInactiveIconRes() {
        return inactiveIconRes;
    }

    public void setInactiveIconRes(int inactiveIconRes) {
        this.inactiveIconRes = inactiveIconRes;
    }
}
