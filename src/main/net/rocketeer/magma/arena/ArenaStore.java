package net.rocketeer.magma.arena;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.admin.BoundingBox;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArenaStore
{
  public static final Map<Player, Arena.Builder> playerToArenaBuilder = new HashMap<>();
  private final Map<World, List<Arena>> _worldToArenas = new HashMap<>();
  private final ConfigurationSection _config;

  public ArenaStore(FileConfiguration config)
  {
    this._config = config.getConfigurationSection("arenas");
  }

  public void add(Arena arena)
  {
    if (this._worldToArenas.get(arena.world()) == null)
      this._worldToArenas.put(arena.world(), new LinkedList<>());
    this._worldToArenas.get(arena.world()).add(arena);
  }

  public void save(Arena arena)
  {
    Map<String, Object> map = new HashMap<>();
    BoundingBox box = arena.boundingBox();
    map.put("world", box.a.getWorld().getName());
    map.put("x1", box.a.getX());
    map.put("y1", box.a.getY());
    map.put("z1", box.a.getZ());
    map.put("x2", box.b.getX());
    map.put("y2", box.b.getY());
    map.put("z2", box.b.getZ());
    map.put("redSpawnX", box.b.getY());
    map.put("redSpawnY", box.b.getZ());
    map.put("redSpawnZ", box.b.getZ());
    this._config.set(arena.name(), null);
    this._config.createSection(arena.name(), map);
    MagmaPlugin.instance.saveConfig();
  }
}
