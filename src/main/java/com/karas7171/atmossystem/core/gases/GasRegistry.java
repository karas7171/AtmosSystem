package com.karas7171.atmossystem.core.gases;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.AtmosZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GasRegistry {
    private static final ArrayList<GasType> REGISTERED_GASES = new ArrayList<>();
    private static final Map<String, GasType> NAME_TO_GAS = new HashMap<>();

    public static void registerGas(GasType gas) {
        int id = REGISTERED_GASES.size();
        gas.assignId(id);

        REGISTERED_GASES.add(gas);
        NAME_TO_GAS.put(gas.getName(), gas);
    }

    public static int getGasesCount() {
        return REGISTERED_GASES.size();
    }
}
