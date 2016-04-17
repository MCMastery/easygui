package com.dgrissom.easygui;

import com.dgrissom.osbu.main.Book;
import com.dgrissom.osbu.main.OSBUCommand;
import com.dgrissom.osbu.main.utilities.ArrayUtility;
import com.dgrissom.osbu.main.utilities.InventoryUtility;
import com.dgrissom.osbu.main.utilities.PlayerUtility;
import com.dgrissom.osbu.main.utilities.StringUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EasyGUICommand extends OSBUCommand {
    public EasyGUICommand() {
        super("easygui", null, "/easygui", null);


        addSubCommand(new OSBUCommand("help", "easygui.help", "/easygui help [page]", "displays help") {
            @Override
            public void execute(PlayerUtility sender, String[] args) {
                // runs the CommandSender execute
                execute(sender.getObject(), args);
            }
            @Override
            public void execute(CommandSender sender, String[] args) {
                if (!hasPermission(sender)) {
                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                    return;
                }
                int page = 0;
                if (args.length > 0) {
                    try {
                        page = Integer.parseInt(args[0]) - 1;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(new StringUtility("&cInvalid page number!").format().toString());
                        return;
                    }
                }
                displayHelp(sender, page);
            }
        });

        addSubCommand(new OSBUCommand("display", "easygui.display", "/easygui display <guiname> [player]", "displays a GUI for a player") {
            @Override
            public void execute(PlayerUtility sender, String[] args) {
                // runs the CommandSender execute
                execute(sender.getObject(), args);
            }
            @Override
            public void execute(CommandSender sender, String[] args) {
                if (!hasPermission(sender)) {
                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                    return;
                }
                try {
                    String guiName = args[0];
                    PlayerUtility player;
                    if (args.length > 1) {
                        player = PlayerUtility.getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(new StringUtility("&cUnknown player!").format().toString());
                            return;
                        }
                    } else {
                        if (sender instanceof Player)
                            player = new PlayerUtility(sender);
                        else {
                            sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                            return;
                        }
                    }
                    GUI gui = GUI.load(guiName);
                    if (gui == null) {
                        sender.sendMessage(new StringUtility("&cUnknown GUI name!").format().toString());
                        return;
                    }
                    player.openInventory(gui.addClickHandler().getInventory().register(player));
                    if (!sender.getName().equals(player.getName()))
                        sender.sendMessage(new StringUtility("Displayed GUI " + guiName + " for player &f" + player.getName() + "&r&f.").format().toString());
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                }
            }
        });


        addSubCommand(new OSBUCommand("create", "easygui.create", "/easygui create <guiname> <rows> <guititle>", "creates a GUI") {
            @Override
            public void execute(PlayerUtility sender, String[] args) {
                // runs the CommandSender execute
                execute(sender.getObject(), args);
            }
            @Override
            public void execute(CommandSender sender, String[] args) {
                if (!hasPermission(sender)) {
                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                    return;
                }
                try {
                    String guiName = args[0];
                    int rows;
                    try {
                        rows = Integer.parseInt(args[1]);
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(new StringUtility("&cInvalid number of rows!").format().toString());
                        return;
                    }
                    if (rows < 1 || rows > InventoryUtility.MAX_ROWS) {
                        sender.sendMessage(new StringUtility("&cRows must be a value between 1 and 6!").format().toString());
                        return;
                    }
                    // convert multiple args to one (args are split by spaces - this allows spaces in the gui title)
                    String guiTitle = new ArrayUtility(args).subArray(2).join(" ");
                    GUI gui = new GUI(guiName);
                    gui.setInventory(new InventoryUtility(rows, guiTitle));
                    gui.save();
                    sender.sendMessage(new StringUtility("&aCreated GUI named " + guiName + ". &fUse &l/easygui display " + guiName + " &r&fto see it.").format().getObject());
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                }
            }
        });


        addSubCommand(new OSBUCommand("delete", "easygui.delete", "/easygui delete <guiname>", "deletes a GUI (no undo!)") {
            @Override
            public void execute(PlayerUtility sender, String[] args) {
                // runs the CommandSender execute
                execute(sender.getObject(), args);
            }
            @Override
            public void execute(CommandSender sender, String[] args) {
                if (!hasPermission(sender)) {
                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                    return;
                }
                try {
                    String guiName = args[0];
                    GUI gui = GUI.load(guiName);
                    if (gui == null) {
                        sender.sendMessage(new StringUtility("&cUnknown GUI name!").format().toString());
                        return;
                    }
                    gui.delete();
                    sender.sendMessage(new StringUtility("&aDeleted GUI named " + guiName + ".").format().getObject());
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                }
            }
        });



        addSubCommand(new OSBUCommand("list", "easygui.list", "/easygui list [page]", "displays a list of GUIs") {
            @Override
            public void execute(PlayerUtility sender, String[] args) {
                // runs the CommandSender execute
                execute(sender.getObject(), args);
            }
            @Override
            public void execute(CommandSender sender, String[] args) {
                if (!hasPermission(sender)) {
                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                    return;
                }
                try {
                    int page = 0;
                    if (args.length > 0) {
                        try {
                            page = Integer.parseInt(args[0]) - 1;
                        } catch (NumberFormatException e) {
                            sender.sendMessage(new StringUtility("&cInvalid page number!").format().toString());
                            return;
                        }
                    }
                    Book book = GUI.generateGUIList();
                    if (page >= book.getPages()) {
                        sender.sendMessage(new StringUtility("&cPage must be within 1 to " + book.getPages() + "!").format().toString());
                        return;
                    }
                    book.messagePage(sender, page);
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                }
            }
        });



        addSubCommand(new OSBUCommand("edit", "easygui.edit", "/easygui edit <guiname> | commands", null) {
            {
                addSubCommand(new OSBUCommand("commands", "easygui.edit.commands", "/easygui edit commands <set | reset>", null) {
                    {
                        addSubCommand(new OSBUCommand("set", "easygui.edit.commands.set", "/easygui edit commands set <guiname> <slot> <player | console> <command>",
                                "sets a GUI's slot's click command") {
                            @Override
                            public void execute(PlayerUtility sender, String[] args) {
                                // runs the CommandSender execute
                                execute(sender.getObject(), args);
                            }
                            @Override
                            public void execute(CommandSender sender, String[] args) {
                                if (!hasPermission(sender)) {
                                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                                    return;
                                }
                                try {
                                    String guiName = args[0];
                                    GUI gui = GUI.load(guiName);
                                    if (gui == null) {
                                        sender.sendMessage(new StringUtility("&cUnknown GUI name!").format().toString());
                                        return;
                                    }

                                    int slot;
                                    try {
                                        slot = Integer.parseInt(args[1]);
                                    }
                                    catch (NumberFormatException e) {
                                        sender.sendMessage(new StringUtility("&cInvalid slot number!").format().toString());
                                        return;
                                    }

                                    GUICommand.Executor executor;
                                    try {
                                        executor = GUICommand.Executor.valueOf(args[2].toUpperCase());
                                    } catch (Exception e) {
                                        sender.sendMessage(new StringUtility("&cExecutor must be either \"player\" or \"console\"!").format().toString());
                                        return;
                                    }
                                    String command = new ArrayUtility(args).subArray(3).join(" ");
                                    gui.setSlotCommand(slot, new GUICommand(command, executor));
                                    gui.save();
                                    sender.sendMessage(new StringUtility("&aUpdated slot command.").format().toString());
                                } catch (IndexOutOfBoundsException e) {
                                    sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                                }
                            }
                        });




                        addSubCommand(new OSBUCommand("reset", "easygui.edit.commands.reset", "/easygui edit commands reset <guiname>",
                                "resets a GUI's click commands") {
                            @Override
                            public void execute(PlayerUtility sender, String[] args) {
                                // runs the CommandSender execute
                                execute(sender.getObject(), args);
                            }
                            @Override
                            public void execute(CommandSender sender, String[] args) {
                                if (!hasPermission(sender)) {
                                    sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                                    return;
                                }
                                try {
                                    String guiName = args[0];
                                    GUI gui = GUI.load(guiName);
                                    if (gui == null) {
                                        sender.sendMessage(new StringUtility("&cUnknown GUI name!").format().toString());
                                        return;
                                    }
                                    gui.resetSlotCommands();
                                    sender.sendMessage(new StringUtility("&aReset slot commands.").format().toString());
                                } catch (IndexOutOfBoundsException e) {
                                    sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                                }
                            }
                        });
                    }
                    @Override
                    public void execute(PlayerUtility sender, String[] args) {
                        // runs the CommandSender execute
                        execute(sender.getObject(), args);
                    }
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        if (!hasPermission(sender)) {
                            sender.sendMessage(new StringUtility("&cYou don't have permission to run that command!").format().toString());
                            return;
                        }
                        sender.sendMessage(new StringUtility("&cCorrect usage: &f" + getUsage()).format().toString());
                    }
                });
            }
            @Override
            public void execute(PlayerUtility sender, String[] args) {
                if (!hasPermission(sender.getObject())) {
                    sender.sendFormattedMessage("&cYou don't have permission to run that command!");
                    return;
                }
                try {
                    String guiName = args[0];
                    GUI gui = GUI.load(guiName);
                    if (gui == null) {
                        sender.sendFormattedMessage("&cUnknown GUI name!");
                        return;
                    }
                    sender.openInventory(gui.addEditClickHandler().addEditCloseHandler().getInventory().register(sender));
                    sender.sendFormattedMessage("&fShift-Right-click items to add click commands. &aYour edits will be saved once the inventory is closed.");
                } catch (IndexOutOfBoundsException e) {
                    sender.sendFormattedMessage("&cCorrect usage: &f" + getUsage());
                }
            }
        });
    }

    @Override
    public void execute(PlayerUtility sender, String[] args) {
        // runs the CommandSender execute
        execute(sender.getObject(), args);
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        displayHelp(sender, 0);
    }
    public void displayHelp(CommandSender sender, int page) {
        Book help = generateHelp(sender, ChatColor.GRAY, ChatColor.WHITE, "&aEasyGUI List &f({page}/{pages})", true);
        if (page >= help.getPages()) {
            sender.sendMessage(new StringUtility("&cPage must be within 1 to " + help.getPages() + "!").format().toString());
            return;
        }
        help.messagePage(sender, page);
    }
}
