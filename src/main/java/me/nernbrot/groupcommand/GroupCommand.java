package me.nernbrot.groupcommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class GroupCommand extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("commands.yml", false);
        System.out.println("[Gc]已成功从服务器中加载该插件");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.saveConfig();
        System.out.println("[Gc]已成功从服务器中卸载该插件");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length ==0){
            if (sender instanceof ConsoleCommandSender){
                System.out.println("[Gc]插件主命令，输入Gc help查看帮助");
            }
            if (sender instanceof Player){
                Player player = ((Player) sender).getPlayer();
                player.sendMessage(ChatColor.YELLOW +"[Gc]插件主命令，输入Gc help查看帮助");
            }
        }else {
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("help")){
                    if (sender instanceof ConsoleCommandSender){
                        if (args.length >1){
                            System.out.println("[Gc]参数错误,查看帮助请输入gc help");
                        }else {
                            List<String> help = getConfig().getStringList("help");
                            for (String list_to_string : help){
                                System.out.println(list_to_string);
                            }
                        }
                    }
                    if (sender instanceof Player){
                        if (args.length >1){
                            Player player = ((Player) sender).getPlayer();
                            player.sendMessage("[Gc]参数错误,查看帮助请输入gc help");
                        }else {
                            List<String> help = getConfig().getStringList("help");
                            Player player = ((Player) sender).getPlayer();
                            for (String list_to_string : help){
                                player.sendMessage(ChatColor.YELLOW + list_to_string+".");
                            }
                        }
                    }
                }else {
                    if (args[0].equalsIgnoreCase("reload")){
                        if (sender instanceof ConsoleCommandSender){
                            if (args.length >1){
                                System.out.println("[Gc]参数错误,查看帮助请输入gc help");
                            }else {
                                reloadConfig();
                                System.out.println("[Gc]配置文件已重载");
                            }
                        }
                        if (sender instanceof Player){
                            if (args.length>1){
                                Player player = ((Player) sender).getPlayer();
                                player.sendMessage("[Gc]参数错误,查看帮助请输入gc help");
                            }else {
                                Player player = ((Player) sender).getPlayer();
                                reloadConfig();
                                player.sendMessage(ChatColor.YELLOW +"[Gc]配置文件已重载.");
                            }
                        }
                    }else {
                        if (args[0].equalsIgnoreCase("run")){  //asd
                            if (args.length <3){
                                if (sender instanceof ConsoleCommandSender){
                                    File commandfil = new File(this.getDataFolder(), "commands.yml");
                                    FileConfiguration commandconfig = YamlConfiguration.loadConfiguration(commandfil);
                                    if (args.length == 1){
                                        System.out.println("run <Group>");
                                    }
                                    if (args.length == 2){
                                        String key = "Group." + args[1];
                                        if (commandconfig.contains(key)){
                                            String consolekey = key + ".Console";
                                            List<String> consolelist = commandconfig.getStringList(consolekey);
                                            for (String list_to_string : consolelist){
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), list_to_string);
                                            }
                                            System.out.println("[Gc]成功执行" + args[1] + "命令组");
                                        } else {
                                            System.out.println("[Gc]没有此名称的命令组");
                                        }
                                    }
                                }else {
                                    if (sender instanceof  Player){
                                        File commandfil = new File(this.getDataFolder(), "commands.yml");
                                        FileConfiguration commandconfig = YamlConfiguration.loadConfiguration(commandfil);
                                        if (args.length == 1){
                                            Player player = ((Player) sender).getPlayer();
                                            player.sendMessage("run <Group>");
                                        }
                                        if (args.length == 2){
                                            String key = "Group." + args[1];
                                            if (commandconfig.contains(key)){
                                                String consolekey = key + ".Console";
                                                List<String> consolelist = commandconfig.getStringList(consolekey);
                                                for (String list_to_string : consolelist){
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), list_to_string);
                                                }
                                                Player player = ((Player) sender).getPlayer();
                                                player.sendMessage("[Gc]成功执行" + args[1] + "命令组");
                                            } else {
                                                Player player = ((Player) sender).getPlayer();
                                                player.sendMessage("[Gc]没有此名称的命令组");
                                            }
                                        }
                                    }
                                }
                            }else {
                                if (sender instanceof ConsoleCommandSender){
                                    System.out.println("[Gc]错误参数");
                                    System.out.println("[Gc]正确使用方法:gc run <Group>");
                                }
                                if (sender instanceof Player){
                                    Player player = ((Player) sender).getPlayer();
                                    player.sendMessage("[Gc]错误参数");
                                    player.sendMessage("[Gc]正确使用方法:gc run <Group>");
                                }
                            }
                        } else {
                            if (args[0].equalsIgnoreCase("list")){
                                if (sender instanceof ConsoleCommandSender){
                                    if (args.length == 1){
                                        System.out.println("[Gc]列出指定命令组的所有命令[gc list <Group>]");
                                    } else {
                                        if (args.length == 2){
                                            File commandfil = new File(this.getDataFolder(), "commands.yml");
                                            FileConfiguration commandconfig = YamlConfiguration.loadConfiguration(commandfil);
                                            String key = "Group." + args[1] + ".Console";
                                            if (commandconfig.contains(key)){
                                                List<String> consolelist = commandconfig.getStringList(key);
                                                System.out.println("============================");
                                                for (String list_to_string : consolelist){
                                                    System.out.println(list_to_string);
                                                }
                                                System.out.println("[Gc]成功列出" + args[1] + "命令组");
                                                System.out.println("============================");
                                            } else {
                                                System.out.println("[Gc]没有此名称的命令组");
                                            }
                                        } else {
                                            System.out.println("[Gc错误参数");
                                            System.out.println("[Gc]列出指定命令组的所有命令[gc list <Group>]");
                                        }
                                    }
                                }
                                if (sender instanceof Player){
                                    if (args.length == 1){
                                        Player player = ((Player) sender).getPlayer();
                                        player.sendMessage("[Gc]列出指定命令组的所有命令[gc list <Group>]");
                                    } else {
                                        if (args.length == 2){
                                            File commandfil = new File(this.getDataFolder(), "commands.yml");
                                            FileConfiguration commandconfig = YamlConfiguration.loadConfiguration(commandfil);
                                            String key = "Group." + args[1] + ".Console";
                                            Player player = ((Player) sender).getPlayer();
                                            if (commandconfig.contains(key)){
                                                List<String> consolelist = commandconfig.getStringList(key);
                                                player.sendMessage("============================");
                                                for (String list_to_string : consolelist){
                                                    player.sendMessage(list_to_string);
                                                }
                                                player.sendMessage("[Gc]成功列出" + args[1] + "命令组");
                                                player.sendMessage("============================");
                                            } else {
                                                player.sendMessage("[Gc]没有此名称的命令组");
                                            }
                                        } else {
                                            Player player = ((Player) sender).getPlayer();
                                            player.sendMessage("[Gc]错误参数");
                                            player.sendMessage("[Gc]列出指定命令组的所有命令[gc list <Group>]");
                                        }
                                    }
                                }
                            } else {
                                if (sender instanceof ConsoleCommandSender){
                                    System.out.println("[Gc]错误参数,查看帮助请输入gc help");
                                }
                                if (sender instanceof Player){
                                    Player player = ((Player) sender).getPlayer();
                                    player.sendMessage("[Gc]错误参数,查看帮助请输入gc help");
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> list = new ArrayList<>();
        if(args.length == 0){
            list.add("gc");
            list.add("Gc");
            list.add("Groupcommand");
            list.add("GroupCommand");
        }
        if (args.length == 1){
            list.add("reload");
            list.add("help");
            list.add("list");
            list.add("run");
        }
        return list.size() > 0 ? list : super.onTabComplete(sender, command, alias, args);
    }
}
