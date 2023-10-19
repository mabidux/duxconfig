package dev.abidux.duxconfig;

import dev.abidux.duxconfig.annotation.Config;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private final Plugin plugin;
    public PluginLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    public <T> T loadConfig(Class<T> cls) {
        if (!cls.isAnnotationPresent(Config.class)) return null;
        String fileName = cls.getAnnotation(Config.class).value();

        try (JarFile jar = new JarFile(URLDecoder.decode(new File(cls.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath(), "UTF-8"))) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().equals(fileName)) continue;
                File file = new File(plugin.getDataFolder(), fileName);
                if (!file.exists()) {
                    if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
                    try (BufferedInputStream input = new BufferedInputStream(jar.getInputStream(entry))) {
                        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
                            byte[] buff = new byte[4096];
                            int n;
                            while ((n = input.read(buff)) > 0) {
                                output.write(buff, 0, n);
                            }
                        }
                    }
                }

                T instance = cls.newInstance();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                for (Field field : cls.getFields()) {
                    field.setAccessible(true);
                    String fieldType = field.getType().getSimpleName();
                    switch (fieldType) {
                        case "String":
                            field.set(instance, config.getString(field.getName().toLowerCase()));
                            break;
                        case "int":
                        case "Integer":
                            field.set(instance, config.getInt(field.getName().toLowerCase()));
                            break;
                        case "double":
                        case "Double":
                            field.set(instance, config.getDouble(field.getName().toLowerCase()));
                            break;
                        case "boolean":
                        case "Boolean":
                            field.set(instance, config.getBoolean(field.getName().toLowerCase()));
                            break;
                    }
                }
                return instance;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}