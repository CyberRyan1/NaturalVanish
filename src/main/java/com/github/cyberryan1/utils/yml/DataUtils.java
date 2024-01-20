package com.github.cyberryan1.utils.yml;

import com.github.cyberryan1.cybercore.spigot.config.FileType;
import com.github.cyberryan1.cybercore.spigot.config.YmlEditor;
import com.github.cyberryan1.cybercore.spigot.config.YmlLoader;

public class DataUtils extends YmlEditor {

    public DataUtils() {
        super( new YmlLoader( FileType.DATA ) );
    }
}