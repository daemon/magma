package net.rocketeer.magma.arena;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.admin.BoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class ArenaStore
{
  public static final Map<Player, Arena.Builder> playerToArenaBuilder = new HashMap<>();
  private final ConfigurationSection _config;
  private Map<String, Arena> _nameToArenas = new HashMap<>();

  public ArenaStore(FileConfiguration config)
  {
    this._config = config.getConfigurationSection("arenas");
    this.load();
  }

  public boolean playing(Player player)
  {
    for (Arena arena : this._nameToArenas.values())
      if (arena.players().contains(player))
        return true;
    return false;
  }

  public boolean contains(Location loc, boolean inProgressArenasOnly)
  {
    for (Arena arena : this._nameToArenas.values())
    {
      if (inProgressArenasOnly && !arena.isInProgress())
        continue;
      if (arena.boundingBox().contains(loc))
        return true;
    }

    return false;
  }

  public void load()
  {
    Set<String> arenaNames = this._config.getKeys(false);
    for (String name : arenaNames)
    {
      try
      {
        ConfigurationSection config = this._config.getConfigurationSection(name);
        String worldName = config.getString("world");
        double x1 = config.getDouble("x1");
        double y1 = config.getDouble("y1");
        double z1 = config.getDouble("z1");
        double x2 = config.getDouble("x2");
        double y2 = config.getDouble("y2");
        double z2 = config.getDouble("z2");
        double redSpawnX = config.getDouble("redSpawnX");
        double redSpawnY = config.getDouble("redSpawnY");
        double redSpawnZ = config.getDouble("redSpawnZ");
        double blueSpawnX = config.getDouble("blueSpawnX");
        double blueSpawnY = config.getDouble("blueSpawnY");
        double blueSpawnZ = config.getDouble("blueSpawnZ");
        World world = Bukkit.getWorld(worldName);
        Location p1 = new Location(world, x1, y1, z1);
        Location p2 = new Location(world, x2, y2, z2);
        BoundingBox bbox = new BoundingBox(p1, p2);
        Location redSpawn = new Location(world, redSpawnX, redSpawnY, redSpawnZ);
        Location blueSpawn = new Location(world, blueSpawnX, blueSpawnY, blueSpawnZ);
        this._nameToArenas.put(name.toLowerCase(), new Arena(name, bbox, redSpawn, blueSpawn));
      } catch (Exception ignored) {
        MagmaPlugin.instance.getLogger().info("Couldn't load arena " + name);
      }
    }
  }

  public void register(Arena arena)
  {
    this._nameToArenas.put(arena.name().toLowerCase(), arena);
  }

  public Arena findByName(String name)
  {
    return this._nameToArenas.get(name.toLowerCase());
  }

  public void save(Arena arena) throws IOException
  {
    BoundingBox box = arena.boundingBox();
    BoundingBoxWriter writer = arena.createWriter();
    writer.write();
    Map<String, Object> map = new HashMap<>();
    map.put("world", box.a.getWorld().getName());
    map.put("x1", box.a.getX());
    map.put("y1", box.a.getY());
    map.put("z1", box.a.getZ());
    map.put("x2", box.b.getX());
    map.put("y2", box.b.getY());
    map.put("z2", box.b.getZ());
    map.put("redSpawnX", arena.redSpawn().getX());
    map.put("redSpawnY", arena.redSpawn().getY());
    map.put("redSpawnZ", arena.redSpawn().getZ());
    map.put("blueSpawnX", arena.blueSpawn().getX());
    map.put("blueSpawnY", arena.blueSpawn().getY());
    map.put("blueSpawnZ", arena.blueSpawn().getZ());
    this._config.set(arena.name(), null);
    this._config.createSection(arena.name(), map);
    MagmaPlugin.instance.saveConfig();
  }
}
