package com.dgrissom.easygui;

import com.dgrissom.osbu.main.events.InventoryClickSlotEvent;
import org.bukkit.Bukkit;

public class GUICommand {
    public enum Executor {
        PLAYER, CONSOLE
    }

    private String command;
    private Executor executor;


    public GUICommand(String command, Executor executor) {
        this.command = command;
        this.executor = executor;
    }

    public String getCommand() {
        return this.command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public Executor getExecutor() {
        return this.executor;
    }
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void execute(InventoryClickSlotEvent evt) {
        String cmd = this.command.replace("{name}", evt.getPlayer().getName())
                .replace("{displayname}", evt.getPlayer().getDisplayName());
        if (cmd.startsWith("/"))
            cmd = cmd.substring(1);
        if (this.executor == Executor.CONSOLE) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        } else
            Bukkit.dispatchCommand(evt.getPlayer().getObject(), cmd);
    }

    public String serialize() {
        return this.command + "`" + this.executor.name();
    }
    public static GUICommand deserialize(String s) {
        return new GUICommand(s.split("`")[0], Executor.valueOf(s.split("`")[1]));
    }
}
