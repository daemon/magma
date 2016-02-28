package net.rocketeer.magma.admin;

import net.rocketeer.magma.SubCommandExecutor;
import net.rocketeer.magma.arena.Arena;
import net.rocketeer.magma.arena.ArenaStore;
import net.rocketeer.magma.message.MessageAlertColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetSpawnCommand implements SubCommandExecutor
{
  private final ArenaStore _store;

  public SetSpawnCommand(ArenaStore store)
  {
    this._store = store;
  }

  @Override
  public String getUsage()
  {
    return "/mgm setspawn <red|blue>";
  }

  @Override
  public String getPermissionName()
  {
    return "mgm.admin";
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
  {
    if (strings.length < 2)
      return false;
    if (!(commandSender instanceof Player))
      return true;
    Player p = (Player) commandSender;
    boolean isRed = strings[1].equalsIgnoreCase("red");
    boolean isBlue = strings[1].equalsIgnoreCase("blue");
    if (!isRed && !isBlue)
      return false;
    if (!ArenaStore.playerToArenaBuilder.containsKey(p))
    {
      p.sendMessage(MessageAlertColor.ERROR + "You need to select an arena first.");
      return true;
    }

    Arena.Builder builder = ArenaStore.playerToArenaBuilder.get(p);
    if (isRed)
      builder.redSpawn(p.getLocation());
    else
      builder.blueSpawn(p.getLocation());

    if (isRed)
      p.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "Set red spawnpoint.");
    else
      p.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "Set blue spawnpoint.");
    if (builder.ready())
      try
      {
        Arena arena = builder.build();
        this._store.save(arena);
        this._store.register(arena);
        p.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "Created new arena " + ChatColor.GREEN + arena.name());
      } catch (IOException e)
      {
        e.printStackTrace();
        p.sendMessage(MessageAlertColor.ERROR + "Couldn't save arena to plugin folder! Do you have permission?");
      }
    return true;
  }
}
