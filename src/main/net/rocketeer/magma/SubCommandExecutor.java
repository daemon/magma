package net.rocketeer.magma;

import org.bukkit.command.CommandExecutor;

public interface SubCommandExecutor extends CommandExecutor
{
  public String getUsage();
  public String getPermissionName();
}
