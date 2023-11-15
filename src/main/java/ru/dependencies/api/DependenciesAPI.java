package ru.dependencies.api;

import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Logger;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class DependenciesAPI {
    protected static File pluginFolder;

    private final Main plugin = Main.instance;

    private final PluginManager pluginManager = plugin.getServer().getPluginManager();

    private final Logger logger = plugin.getLogger();

    protected List<Dependence> dependenceList = new ArrayList<Dependence>();

    @Nullable
    private File downloadFile(URL url, File file) {
        String fileName = file.getName();

        try {
            URLConnection connection = url.openConnection();

            ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();

            logger.info("File upload started under name:" + fileName);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("Failed to install a dependency named: "+ fileName);

            return null;
        } finally {
            logger.info(TextFormat.GREEN + "The file was successfully installed: " + fileName);
            pluginManager.loadPlugin(file);
        }

        return file;
    }

    public File getResourceFile(InputStream stream) {
        try (stream) {
            if (stream != null) {
                File temp = new File(plugin.getDataFolder(), "temp.yml");
                temp.delete();

                Utils.writeFile(temp, stream);

                return temp;
            }
        } catch (IOException e) {
            logger.error("error loading dependency config file: " + e.getMessage());
        }

        return null;
    }

    public DependenciesAPI addDependence(String urlString, String name, String version) {
        try {
            URL url = new URL(urlString);

            dependenceList.add(new Dependence(url, name, version));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public DependenciesAPI loadDependenciesFromFile(InputStream stream) {
        File configFile = getResourceFile(stream);

        if (configFile == null) {
            logger.error("Failed to load the dependency configuration file.");
            return this;
        }

        Config config = new Config(configFile);

        for (String key : config.getKeys(false)) {
            String version = config.getString(key + ".version");
            String urlString = config.getString(key + ".url");

            logger.info(version + " " + urlString + " " + key);

            addDependence(urlString, key, version);
        }

        return this;
    }

    public void checkUpdate(Dependence dependence) {
        File file = dependence.file;

        if (!file.exists()) return;

        PluginLoader pluginLoader = plugin.getPluginLoader();

        PluginDescription pluginDescription = pluginLoader.getPluginDescription(file);

        int version = dependence.getVersionInt();
        int currentVersion= Dependence.VersionToInt(pluginDescription.getVersion());

        if (version > currentVersion) {
            logger.info("For the plugin " + dependence.name + " new version required");
        }
    }

    public void build() {
        for (Dependence dependence : dependenceList) {
            String name = dependence.name;
            URL url = dependence.url;

            File file = dependence.file;

            if (!file.exists()) {
                file = downloadFile(url, file);
            }

            if (file == null) {
                logger.error("Failed to download file: " + name);
                return;
            }

            checkUpdate(dependence);
        }
    }

    /*public String getFileNameFromURL(String urlString) {
        String fileName = null;

        try {
            URL url = new URL(urlString);

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000);

            String fieldValue = connection.getHeaderField("Content-Disposition");

            if (fieldValue == null || ! fieldValue.contains("filename=\"")) {
                fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
            } else {
                fileName = fieldValue.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return fileName;
    }
     */
}
