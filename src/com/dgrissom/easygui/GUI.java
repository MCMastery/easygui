package com.dgrissom.easygui;

import com.dgrissom.osbu.main.Book;
import com.dgrissom.osbu.main.PlayerSelector;
import com.dgrissom.osbu.main.utilities.InventoryUtility;
import com.dgrissom.osbu.main.utilities.StringUtility;
import com.dgrissom.osbu.main.utilities.YamlUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUI {
    private InventoryUtility inventory;
    private Map<Integer, GUICommand> slotCommands;
    private String name;

    public GUI(String name) {
        this.inventory = new InventoryUtility(this.inventory);
        this.slotCommands = new HashMap<>();
        this.name = name;
    }

    public InventoryUtility getInventory() {
        return this.inventory;
    }
    public GUI setInventory(InventoryUtility inventory) {
        this.inventory = inventory;
        return this;
    }

    public GUI addClickHandler() {
        // don't allow moving items around and stuff.
        this.inventory.addClickHandler(evt -> {
            GUICommand cmd = this.slotCommands.get(evt.getClickedSlot());
            if (cmd != null)
                cmd.execute(evt);
            evt.setCancelled(true);
        });
        return this;
    }
    public GUI addEditClickHandler() {
        // active when /easygui edit is used on this
        this.inventory.addClickHandler(evt -> {
            if (evt.getClickType() == ClickType.SHIFT_RIGHT) {
                evt.getPlayer().closeInventory();
            }
        });
        return this;
    }
    public GUI addEditCloseHandler() {
        this.inventory.addCloseHandler(evt -> {
            setInventory(evt.getInventory());
            save();
        });
        return this;
    }

    public String getName() {
        return this.name;
    }
    public GUI setName(String name) {
        this.name = name;
        return this;
    }

    public Map<Integer, GUICommand> getSlotCommands() {
        return this.slotCommands;
    }
    public GUICommand getSlotCommand(int slot) {
        return this.slotCommands.get(slot);
    }
    public GUI setSlotCommand(int slot, GUICommand command) {
        if (slot >= this.inventory.getSize())
            throw new IndexOutOfBoundsException("inventory does not contain the slot specified");
        this.slotCommands.put(slot, command);
        return this;
    }


    public static GUI load(String name) {
        try {
            YamlUtility config = new YamlUtility(EasyGUI.getInstance().getConfig());
            GUI gui = new GUI(name);
            gui.setInventory(config.getInventory(name + ".inventory"));
            for (String key : config.getObject().getConfigurationSection(name).getKeys(false)) {
                StringUtility string = new StringUtility(key);
                if (string.isInteger())
                    gui.setSlotCommand(string.parseInt(), GUICommand.deserialize(config.getObject().getString(name + "." + string.getObject())));
            }
            return gui;
        } catch (Exception e) {
            return null;
        }
    }
    public static Set<String> getGUINames() {
        return EasyGUI.getInstance().getConfig().getKeys(false);
    }


    public static Book generateGUIList() {
        Book book = new Book();
        //alternate colors every row
        int color = 0;
        for (String guiName : GUI.getGUINames()) {
            book.append("&l- " + ((color == 0) ? "&f" : "&7") + guiName);
            color = (color == 0) ? 1 : 0;
        }
        // 10 lines - includes "EasyGUI List" Headers
        book.generatePages(9);
        for (int pg = 0; pg < book.getPages(); pg++)
            book.insert(pg, 0, "&aEasyGUI List (" + (pg + 1) + "/" + book.getPages() + ")");
        return book;
    }



    public void save() {
        YamlUtility config = new YamlUtility(EasyGUI.getInstance().getConfig());
        config.saveInventory(this.inventory, this.name + ".inventory");
        for (int slot : this.slotCommands.keySet())
            config.set(this.name + "." + slot, this.slotCommands.get(slot).serialize());
        EasyGUI.getInstance().saveConfig();
    }
    public void delete() {
        YamlUtility config = new YamlUtility(EasyGUI.getInstance().getConfig());
        config.set(this.name, null);
        EasyGUI.getInstance().saveConfig();
    }
    public void resetSlotCommands() {
        YamlUtility config = new YamlUtility(EasyGUI.getInstance().getConfig());
        for (int slot : this.slotCommands.keySet())
            config.set(this.name + "." + slot, null);
        this.slotCommands = new HashMap<>();
        EasyGUI.getInstance().saveConfig();
    }
}
