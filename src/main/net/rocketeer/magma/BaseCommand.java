package net.rocketeer.magma;

import net.rocketeer.magma.message.HelpPagesBuilder;
import net.rocketeer.magma.message.MessageAlertColor;
import net.rocketeer.magma.message.Messages;
import net.rocketeer.magma.message.Pages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class BaseCommand implements CommandExecutor
{
  private Pages _helpPages;
  private Map<String, SubCommandExecutor> _subcommandToExecutor = new HashMap<>();

  public BaseCommand()
  {
    HelpPagesBuilder builder = new HelpPagesBuilder();
    builder.addHelp("mgm start", "Start a game.");
    builder.addHelp("mgm stop", "Stop a game.");
    builder.addHelp("mgm join", "Join a game.");
    builder.addHelp("mgm listarena", "Lists all available arenas.");
    builder.addHelp("mgm pos1", "Sets one corner of a selection box.");
    builder.addHelp("mgm pos2", "Sets other corner of a selection box.");
    builder.addHelp("mgm newarena", "Creates an arena from the selection box.");
    builder.addHelp("mgm reset", "Reset an arena to its original state.");
    builder.addHelp("mgm delete", "Delete an arena.");
    builder.addHelp("mgm setspawn", "Sets a team's spawnpoint.");
    this._helpPages = builder.toPages();
  }

  public SubCommandExecutor getCommandExecutor(String subCommand)
  {
    return this._subcommandToExecutor.get(subCommand);
  }

  public void registerCommand(String subCommand, SubCommandExecutor executor)
  {
    this._subcommandToExecutor.put(subCommand.toLowerCase(), executor);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    boolean isPageNo = false;
    int pageNo = 0;

    if (args.length > 0)
      try
      {
        pageNo = Integer.parseInt(args[0]) - 1;
        isPageNo = true;
      } catch (NumberFormatException ignored) {
      }
    if (args.length == 0 || isPageNo)
    {
      this._helpPages.printTo(sender, pageNo);
      return true;
    }

    String cmd = args[0].toLowerCase();
    SubCommandExecutor executor = this._subcommandToExecutor.get(cmd);
    if (executor == null)
      return false;

    if (!sender.hasPermission(executor.getPermissionName()))
    {
      sender.sendMessage(MessageAlertColor.ERROR + Messages.NO_PERMISSION);
      return true;
    }

    if (!executor.onCommand(sender, command, label, args))
      sender.sendMessage(MessageAlertColor.ERROR + "Usage: " + executor.getUsage());
    return true;
  }
}
