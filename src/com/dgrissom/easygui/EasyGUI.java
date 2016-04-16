package com.dgrissom.easygui;

import com.dgrissom.osbu.main.OSBU;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyGUI extends JavaPlugin {
    private static EasyGUI instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        OSBU.getInstance().getCommands().registerCommand(new EasyGUICommand());
    }

    public static EasyGUI getInstance() {
        return instance;
    }
}
