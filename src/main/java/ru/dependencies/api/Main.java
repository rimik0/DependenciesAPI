package ru.dependencies.api;

import cn.nukkit.plugin.PluginBase;

import java.io.File;

public class Main extends PluginBase {
    public static Main instance;

    @Override
    public void onLoad() {
        instance = this;

        DependenciesAPI.pluginFolder = new File(this.getServer().getFilePath() + "/plugins");
        this.getDataFolder().mkdir();
    }
}
