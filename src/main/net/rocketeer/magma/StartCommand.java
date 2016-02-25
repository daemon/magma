package net.rocketeer.magma;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StartCommand implements SubCommandExecutor
{
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
    return false;
  }
}
