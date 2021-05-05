package com.example.duan1.Model;

import androidx.fragment.app.Fragment;

public class MenuItem {
    private String label;
    private Fragment fragment;
    private int drawable;

    public MenuItem(String label, Fragment fragment, int drawable) {
        this.label = label;
        this.fragment = fragment;
        this.drawable = drawable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
