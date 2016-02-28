package net.rocketeer.magma.admin;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.SubCommandExecutor;
import net.rocketeer.magma.arena.Arena;
import net.rocketeer.magma.arena.ArenaResetTask;
import net.rocketeer.magma.arena.ArenaStore;
import net.rocketeer.magma.message.MessageAlertColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class ResetCommand implements SubCommandExecutor
{
  private final ArenaStore _as;

  public ResetCommand(ArenaStore store)
  {
    this._as = store;
  }

  @Override
  public String getUsage()
  {
    return "/mgm reset <arena name>";
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
    String arenaName = strings[1];
    Arena arena = this._as.findByName(arenaName.toLowerCase());
    if (arena == null)
    {
      commandSender.sendMessage(MessageAlertColor.ERROR + "Couldn't find that arena!");
      return true;
    }

    try
    {
      commandSender.sendMessage(MessageAlertColor.NOTIFY_AGNOSTIC + "Beginning reset on arena " + arena.name() + "...");
      Bukkit.getScheduler().runTaskAsynchronously(MagmaPlugin.instance, new ArenaResetTask(commandSender, arena));
    } catch (IOException e)
    {
      commandSender.sendMessage(MessageAlertColor.ERROR + "Couldn't find arena file in plugins folder!");
    }
    return true;
  }
}
