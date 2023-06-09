package com.silvermoon.boxplusplus.common.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static String greeting = "Hel lo WoIAAAWILLFFFREEE...These violent delights have violent end";

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
