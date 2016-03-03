package net.rocketeer.magma;

import net.rocketeer.magma.admin.BoundingBoxRegistry;
import net.rocketeer.magma.admin.NewArenaCommand;
import net.rocketeer.magma.admin.ResetCommand;
import net.rocketeer.magma.admin.SetSpawnCommand;
import net.rocketeer.magma.arena.ArenaStore;
import net.rocketeer.magma.message.FormatMessageStore;
import net.rocketeer.magma.weapon.SnowballHitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MagmaPlugin extends JavaPlugin
{
  public static MagmaPlugin instance;

  @Override
  public void onEnable()
  {
    instance = this;
    this.saveDefaultConfig();
    BoundingBoxRegistry bbRegistry = new BoundingBoxRegistry();
    ArenaStore as = new ArenaStore(this.getConfig());
    FormatMessageStore fs = new FormatMessageStore(this.getConfig());
    BaseCommand base = new BaseCommand();
    FormatMessageStore messageStore = new FormatMessageStore(this.getConfig());
    this.getCommand("mgm").setExecutor(base);
    base.registerCommand("pos1", bbRegistry.new Pos1Command());
    base.registerCommand("pos2", bbRegistry.new Pos2Command());
    base.registerCommand("newarena", new NewArenaCommand(this.getConfig(), bbRegistry));
    base.registerCommand("setspawn", new SetSpawnCommand(as));
    base.registerCommand("reset", new ResetCommand(as));
    base.registerCommand("start", new StartCommand(as, fs));
    Bukkit.getPluginManager().registerEvents(new SnowballHitListener(as), this);
  }

  @Override
  public void onDisable()
  {

  }
}
