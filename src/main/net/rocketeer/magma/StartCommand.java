package net.rocketeer.magma;

import net.rocketeer.magma.arena.Arena;
import net.rocketeer.magma.arena.ArenaStore;
import net.rocketeer.magma.message.FormatMessageStore;
import net.rocketeer.magma.message.MessageAlertColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StartCommand implements SubCommandExecutor
{
  private final ArenaStore _as;
  private final FormatMessageStore _fs;

  public StartCommand(ArenaStore store, FormatMessageStore fs)
  {
    this._as = store;
    this._fs = fs;
  }

  @Override
  public String getUsage()
  {
    return "/mgm start <arena name>";
  }

  @Override
  public String getPermissionName()
  {
    return "magma.cmd.start";
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
  {
    if (strings.length < 2)
      return false;
    String arenaName = strings[1];
    Arena a = this._as.findByName(arenaName.toLowerCase());
    if (a == null)
    {
      commandSender.sendMessage(MessageAlertColor.ERROR + "That arena couldn't be found!");
      return true;
    }

    if (a.isInProgress())
    {
      commandSender.sendMessage(MessageAlertColor.ERROR + "That arena is currently in progress.");
      return true;
    }
    a.setInProgress(true);
    Bukkit.getServer().broadcastMessage(this._fs.lookup("game-beginning-msg").colorFormat().format("arena_name", arenaName.toLowerCase()).toString());
    return true;
  }
}
