package com.silvermoon.boxplusplus.common.loader;

public class ServerInitLoader {

    public static void initOnPlayerJoinedSever() {
        new RecipeLoader().run();
    }
}
