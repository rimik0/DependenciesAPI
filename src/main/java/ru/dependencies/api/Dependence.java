package ru.dependencies.api;

import java.io.File;
import java.net.URL;

public class Dependence {
    public URL url;
    public String version;
    public String name;

    public File file;

    public Dependence(URL url, String name, String version) {
        this.version = version;
        this.name = name;
        this.url = url;

        file = new File(DependenciesAPI.pluginFolder, name + ".jar");
    }

    public int getVersionInt() {
        return Integer.parseInt(version.replaceAll("[^0-9]", ""));
    }

    public static int VersionToInt(String version) {
        return Integer.parseInt(version.replaceAll("[^0-9]", ""));
    }
}
