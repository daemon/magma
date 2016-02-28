package net.rocketeer.magma.admin;

import net.rocketeer.magma.SubCommandExecutor;
import net.rocketeer.magma.arena.Arena;
import net.rocketeer.magma.arena.ArenaStore;
import net.rocketeer.magma.message.MessageAlertColor;
import net.rocketeer.magma.message.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.Set;

public class NewArenaCommand implements SubCommandExecutor
{
  private final ConfigurationSection _config;
  private final BoundingBoxRegistry _bbRegistry;
  private Set<String> _arenaNames;

  public NewArenaCommand(FileConfiguration config, BoundingBoxRegistry bbRegistry)
  {
    this._config = config.getConfigurationSection("arenas");
    this._bbRegistry = bbRegistry;
  }

  @Override
  public String getUsage()
  {
    return "/mgm newarena <arena name>";
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
    if (strings.length < 2)
      return false;
    Player p = (Player) commandSender;
    BoundingBox box;
    if ((box = this._bbRegistry.lookup(p)) == null)
    {
      p.sendMessage(MessageAlertColor.ERROR + Messages.SELECTION_BOX_REQUIRED);
      return true;
    }

    this._arenaNames = this._config.getKeys(false);
    if (this._arenaNames.contains(strings[1]))
    {
      p.sendMessage(MessageAlertColor.ERROR + "That name already exists!");
      return true;
    }

    Arena.Builder builder = new Arena.Builder();
    builder.boundingBox(box).name(strings[1]);
    ArenaStore.playerToArenaBuilder.put(p, builder);
    p.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "In-progress arena created and selected! Remember, it won't save until you register red and blue spawnpoints.");
    return true;
  }
}
