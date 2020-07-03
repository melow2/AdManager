package com.hyeoksin.admanager.data;

public enum AdName {
    FACEBOOK("FACEBOOK", 2),
    ADMOB("ADMOB", 3),
    MANPLUS("MANPLUS", 5),
    CAULY("CAULY", 7),
    ADFIT("ADFIT", 11),
    HOUSE("HOUSE", 13);

    private final String name;
    private final int code;

    AdName(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
