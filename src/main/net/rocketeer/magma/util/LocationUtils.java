package net.rocketeer.magma.util;

import net.rocketeer.magma.MagmaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class LocationUtils
{
  public static Location from(FileConfiguration config, String worldKey, String xKey, String yKey, String zKey)
  {
    double x = config.getDouble(xKey);
    double y = config.getDouble(yKey);
    double z = config.getDouble(zKey);
    World world = Bukkit.getWorld(config.getString(worldKey));
    if (world == null)
      return null;
    return new Location(world, x, y, z);
  }

  public static void save(ConfigurationSection config, String worldKey, String xKey, String yKey, String zKey, Location location)
  {
    config.set(worldKey, location.getWorld().getName());
    config.set(xKey, location.getX());
    config.set(yKey, location.getY());
    config.set(zKey, location.getZ());
    MagmaPlugin.instance.saveConfig();
  }
}
