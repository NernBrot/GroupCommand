package me.nernbrot.groupcommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class GroupCommand extends JavaPlugin {
    private static Plugin plugin;
    BukkitTask bukkitTask2;

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        saveResource("commands.yml", false);
        System.out.println("[Gc]已成功从服务器中加载该插件");
        saveDefaultConfig();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.saveConfig();
        System.out.println("[Gc]已成功从服务器中卸载该插件");
        bukkitTask2.cancel();
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
                        if (args[0].equalsIgnoreCase("run")){
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
                                            System.out.println("[Gc]成功执行" + args[1] + "命令组");
                                            for (String list_to_string : consolelist){
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), list_to_string);
                                            }
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
                                            player.sendMessage("[Gc]run <Group>");
                                        }
                                        if (args.length == 2){
                                            String key = "Group." + args[1];
                                            if (commandconfig.contains(key)){
                                                String consolekey = key + ".Console";
                                                List<String> consolelist = commandconfig.getStringList(consolekey);
                                                Player player = ((Player) sender).getPlayer();
                                                player.sendMessage("[Gc]成功执行" + args[1] + "命令组");
                                                for (String list_to_string : consolelist){
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),list_to_string);
                                                }
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
                                if (args[0].equalsIgnoreCase("wait")){
                                    if (args.length>= 3){
                                        if (sender instanceof ConsoleCommandSender){
                                            if (args.length == 1){
                                                System.out.println("[Gc]延迟命令");
                                            }else {
                                                if (args.length == 3){
                                                    String wait1 = args[1];
                                                    String command1 = args[2];
                                                    int a = Integer.parseInt(wait1);
                                                    BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                    bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                        }
                                                    },a);
                                                } else {
                                                    if (args.length==4){
                                                        String wait1 = args[1];
                                                        String command1 = args[2] +" " +args[3];
                                                        int a = Integer.parseInt(wait1);
                                                        reloadConfig();
                                                        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                        bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {

                                                            @Override
                                                            public void run() {
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                            }
                                                        },a);
                                                    } else {
                                                        if (args.length ==5){
                                                            String wait1 = args[1];
                                                            String command1 = args[2] +" " +args[3] + " "+args[4];
                                                            int a = Integer.parseInt(wait1);
                                                            reloadConfig();
                                                            BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                            bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                }
                                                            },a);
                                                        } else {
                                                            if (args.length == 6 ){
                                                                String wait1 = args[1];
                                                                String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5];
                                                                int a = Integer.parseInt(wait1);
                                                                reloadConfig();
                                                                BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                    }
                                                                },a);
                                                            } else {
                                                                if (args.length == 7){
                                                                    String wait1 = args[1];
                                                                    String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                            " " + args[6];
                                                                    int a = Integer.parseInt(wait1);
                                                                    reloadConfig();
                                                                    BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                    bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                        }
                                                                    },a);
                                                                } else {
                                                                    if (args.length == 8 ){
                                                                        String wait1 = args[1];
                                                                        String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                " " + args[6] +" " + args[7];
                                                                        int a = Integer.parseInt(wait1);
                                                                        reloadConfig();
                                                                        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                        bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                            }
                                                                        },a);
                                                                    } else {
                                                                        if (args.length == 9){
                                                                            String wait1 = args[1];
                                                                            String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                    " " + args[6] +" " + args[7] + " " + args[8];
                                                                            int a = Integer.parseInt(wait1);
                                                                            reloadConfig();
                                                                            BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                            bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                }
                                                                            },a);
                                                                        } else {
                                                                            if (args.length ==10){
                                                                                String wait1 = args[1];
                                                                                String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                        " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9];
                                                                                int a = Integer.parseInt(wait1);
                                                                                reloadConfig();
                                                                                BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                    }
                                                                                },a);
                                                                            } else {
                                                                                if (args.length == 11){
                                                                                    String wait1 = args[1];
                                                                                    String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                            " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10];
                                                                                    int a = Integer.parseInt(wait1);
                                                                                    reloadConfig();
                                                                                    BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                    bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                        }
                                                                                    },a);
                                                                                } else {
                                                                                    if (args.length==12){
                                                                                        String wait1 = args[1];
                                                                                        String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                " "+args[11];
                                                                                        int a = Integer.parseInt(wait1);
                                                                                        reloadConfig();
                                                                                        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                        bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                            @Override
                                                                                            public void run() {
                                                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                            }
                                                                                        },a);
                                                                                    } else {
                                                                                        if (args.length == 13){
                                                                                            String wait1 = args[1];
                                                                                            String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                    " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                    " "+args[11] +" "+args[12];
                                                                                            int a = Integer.parseInt(wait1);
                                                                                            reloadConfig();
                                                                                            BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                            bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                }
                                                                                            },a);
                                                                                        } else {
                                                                                            if (args.length == 14){
                                                                                                String wait1 = args[1];
                                                                                                String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                        " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                        " "+args[11] +" "+args[12]+" "+args[13];
                                                                                                int a = Integer.parseInt(wait1);
                                                                                                reloadConfig();
                                                                                                BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                    @Override
                                                                                                    public void run() {
                                                                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                    }
                                                                                                },a);
                                                                                            } else {
                                                                                                if (args.length == 15){
                                                                                                    String wait1 = args[1];
                                                                                                    String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                            " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                            " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14];
                                                                                                    int a = Integer.parseInt(wait1);
                                                                                                    reloadConfig();
                                                                                                    BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                    bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                        @Override
                                                                                                        public void run() {
                                                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                        }
                                                                                                    },a);
                                                                                                } else {
                                                                                                    if (args.length ==16){
                                                                                                        String wait1 = args[1];
                                                                                                        String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                                " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                                " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14] +" "+args[15];
                                                                                                        int a = Integer.parseInt(wait1);
                                                                                                        reloadConfig();
                                                                                                        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                        bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                            }
                                                                                                        },a);
                                                                                                    } else {
                                                                                                        if (args.length == 17){
                                                                                                            String wait1 = args[1];
                                                                                                            String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                                    " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                                    " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14] +" "+args[15] +
                                                                                                                    " "+args[16];
                                                                                                            int a = Integer.parseInt(wait1);
                                                                                                            reloadConfig();
                                                                                                            BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                            bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                                @Override
                                                                                                                public void run() {
                                                                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                                }
                                                                                                            },a);
                                                                                                        } else {
                                                                                                            if (args.length == 18){
                                                                                                                String wait1 = args[1];
                                                                                                                String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                                        " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                                        " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14] +" "+args[15] +"" +
                                                                                                                        " "+args[16]+" "+args[17];
                                                                                                                int a = Integer.parseInt(wait1);
                                                                                                                reloadConfig();
                                                                                                                BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                                bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                                    @Override
                                                                                                                    public void run() {
                                                                                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                                    }
                                                                                                                },a);
                                                                                                            } else {
                                                                                                                if (args.length == 19){
                                                                                                                    String wait1 = args[1];
                                                                                                                    String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                                            " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                                            " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14] +" "+args[15] +
                                                                                                                            " "+args[16]+" "+args[17] +" "+ args[18];
                                                                                                                    int a = Integer.parseInt(wait1);
                                                                                                                    reloadConfig();
                                                                                                                    BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                                    bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                                        @Override
                                                                                                                        public void run() {
                                                                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                                        }
                                                                                                                    },a);
                                                                                                                }
                                                                                                                if (args.length == 20){
                                                                                                                    String wait1 = args[1];
                                                                                                                    String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                                            " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                                            " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14] +" "+args[15] +
                                                                                                                            " "+args[16]+" "+args[17] +" "+ args[18] +" "+args[19];
                                                                                                                    int a = Integer.parseInt(wait1);
                                                                                                                    reloadConfig();
                                                                                                                    BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                                    bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                                        @Override
                                                                                                                        public void run() {
                                                                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                                        }
                                                                                                                    },a);
                                                                                                                } else {
                                                                                                                    if (args.length == 21){
                                                                                                                        String wait1 = args[1];
                                                                                                                        String command1 = args[2] +" " +args[3] + " "+args[4] + " "+ args[5] +
                                                                                                                                " " + args[6] +" " + args[7] + " " + args[8]+" "+ args[9]+" "+args[10] +
                                                                                                                                " "+args[11] +" "+args[12]+" "+args[13]+" "+args[14] +" "+args[15] +
                                                                                                                                " "+args[16]+" "+args[17] +" "+ args[18] +" "+args[19]+" "+args[20];
                                                                                                                        int a = Integer.parseInt(wait1);
                                                                                                                        reloadConfig();
                                                                                                                        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
                                                                                                                        bukkitTask2 = bukkitScheduler.runTaskLater(this, new Runnable() {
                                                                                                                            @Override
                                                                                                                            public void run() {
                                                                                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command1);
                                                                                                                            }
                                                                                                                        },a);
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (sender instanceof Player){
                                                Player player = ((Player) sender).getPlayer();
                                                player.sendMessage("[Gc]该命令只允许控制台驶入");
                                        }
                                    } else {
                                        System.out.println("错误参数");
                                    }
                                }else {
                                    if (sender instanceof ConsoleCommandSender){
                                        System.out.println("[Gc]参数错误");
                                    }
                                    if (sender instanceof Player){
                                        Player player = ((Player) sender).getPlayer();
                                        player.sendMessage("[Gc]参数错误");
                                    }
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
