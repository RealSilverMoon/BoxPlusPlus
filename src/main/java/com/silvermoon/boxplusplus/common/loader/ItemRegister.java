package com.silvermoon.boxplusplus.common.loader;

import com.silvermoon.boxplusplus.common.items.tierDrone;

public class ItemRegister {

    public static final tierDrone drone = new tierDrone("maintainingDrone");

    public static void Register() {
        drone.registerItem();
    }
}
