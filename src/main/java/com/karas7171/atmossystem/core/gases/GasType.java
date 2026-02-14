package com.karas7171.atmossystem.core.gases;

import com.karas7171.atmossystem.core.logic.AtmosProgressListener;

public class GasType {
    private final String name;
    private final int color;
    private final float density;
    private int id = -1;

    public GasType(String name, int color, float density) {
        this.name = name;
        this.color = color;
        this.density = density;
    }

    public void assignId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
