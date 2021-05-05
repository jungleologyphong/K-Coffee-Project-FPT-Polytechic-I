package com.example.duan1.Model;

public class ChartItem {
    private String label;
    private int count;
    private long total;

    public ChartItem(String label, int count, long total) {
        this.label = label;
        this.count = count;
        this.total = total;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
