package com.github.cyberryan1.utils.yml;

import com.github.cyberryan1.cybercore.managers.FileType;
import com.github.cyberryan1.cybercore.managers.YmlManager;
import com.github.cyberryan1.cybercore.utils.yml.YMLReadEditTemplate;

public class DataUtils extends YMLReadEditTemplate {

    public DataUtils() {
        setYMLManager( new YmlManager( FileType.DATA ) );
    }
}