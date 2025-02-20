package com.silvermoon.boxplusplus.common.loader;

import com.silvermoon.boxplusplus.common.items.BoxModuleResearchItem;

public class ItemRegister {

    public static BoxModuleResearchItem bmResearchItem = new BoxModuleResearchItem();

    public static void register() {
        bmResearchItem.registerItem();
    }
}
