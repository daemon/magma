package net.rocketeer.magma.admin;

import net.rocketeer.magma.SubCommandExecutor;
import net.rocketeer.magma.message.MessageAlertColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BoundingBoxRegistry
{
  private final Map<Player, BoundingBox> _playersToBoundingBoxes = new HashMap<>();
  private final Map<Player, Location> _playersToFirstPos = new HashMap<>();
  private final Map<Player, Location> _playersToSecondPos = new HashMap<>();

  public BoundingBox lookup(Player player)
  {
    return this._playersToBoundingBoxes.get(player);
  }

  public class Pos1Command implements SubCommandExecutor
  {
    @Override
    public String getUsage()
    {
      return "/mgm pos1";
    }

    @Override
    public String getPermissionName()
    {
      return "mgm.admin";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
      if (!(commandSender instanceof Player))
        return true;
      Player p = (Player) commandSender;
      p.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "Selected corner 1");
      _playersToFirstPos.put(p, p.getLocation());
      Location other = _playersToSecondPos.get(p);
      if (other != null && other.getWorld().equals(p.getWorld()))
        _playersToBoundingBoxes.put(p, new BoundingBox(other, p.getLocation()));
      return true;
    }
  }

  public class Pos2Command implements SubCommandExecutor
  {
    @Override
    public String getUsage()
    {
      return "/mgm pos2";
    }

    @Override
    public String getPermissionName()
    {
      return "mgm.admin";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
      if (!(commandSender instanceof Player))
        return true;
      Player p = (Player) commandSender;
      p.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "Selected corner 2");
      _playersToSecondPos.put(p, p.getLocation());
      Location other = _playersToFirstPos.get(p);
      if (other != null && other.getWorld().equals(p.getWorld()))
        _playersToBoundingBoxes.put(p, new BoundingBox(other, p.getLocation()));
      return true;
    }
  }
}
