package com.g4bzz.linkurto.core.engines;

import lombok.Getter;

public abstract class Engine {
    @Getter
    protected int daysToExpire = 30;
    public abstract String getUniqueKey(String url);
}
