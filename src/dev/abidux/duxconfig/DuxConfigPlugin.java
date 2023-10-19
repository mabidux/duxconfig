package dev.abidux.duxconfig;

import dev.abidux.duxconfig.config.ExampleConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DuxConfigPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§a[DuxConfig] PLUGIN ENABLED");

        PluginLoader loader = new PluginLoader(this);
        ExampleConfig exampleConfig = loader.loadConfig(ExampleConfig.class);

        if (exampleConfig.SHOULD_SEND_MESSAGE) {
            Bukkit.getConsoleSender().sendMessage("§e[DuxConfig] VALORES DE example.yml");
            System.out.println(exampleConfig.EXAMPLE_MESSAGE);
            System.out.println(exampleConfig.SHOULD_SEND_MESSAGE);
            System.out.println(exampleConfig.EXAMPLE_INT);
            System.out.println(exampleConfig.EXAMPLE_DOUBLE);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c[DuxConfig] PLUGIN DISABLED");
    }
}