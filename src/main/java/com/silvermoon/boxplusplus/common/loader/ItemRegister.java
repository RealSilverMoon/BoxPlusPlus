package com.silvermoon.boxplusplus.common.loader;

import com.silvermoon.boxplusplus.common.items.ItemMain;

public class ItemRegister {
    public static final ItemMain SpaceCore = new ItemMain("boxplusplus_SpaceCore","SpaceCore");
    public static final ItemMain GravitationalWaveDetector = new ItemMain("boxplusplus_GravitationalWaveDetector","GravitationalWaveDetector");
    public static final ItemMain Spring = new ItemMain("boxplusplus_Spring","Spring");
    public static void Register(){
        SpaceCore.registerItem();
        GravitationalWaveDetector.registerItem();
        Spring.registerItem();
    }
}
