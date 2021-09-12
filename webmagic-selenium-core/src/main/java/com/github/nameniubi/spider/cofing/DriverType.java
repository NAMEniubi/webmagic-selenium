package com.github.nameniubi.spider.cofing;


public enum DriverType {
    Chrome(0),
    Firefox(1),
    Edge(2),
    Opera(3),
    Safari(4);
    private final int key;

    DriverType(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
