package com.github.cyberryan1.utils.yml;

public class YMLUtils {

    private static ConfigUtils configUtils = new ConfigUtils();
    private static DataUtils dataUtils = new DataUtils();

    public static ConfigUtils getConfig() { return configUtils; }

    public static DataUtils getData() { return dataUtils; }
    public static void saveData() { dataUtils.save(); }
}