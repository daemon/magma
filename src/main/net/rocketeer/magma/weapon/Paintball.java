package net.rocketeer.magma.weapon;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.admin.BoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Paintball
{
  private final Location _location;
  private final BoundingBox _box;
  private final Color _color;

  enum Color
  {
    RED(Material.STAINED_CLAY, (byte) 14),
    ORANGE(Material.STAINED_CLAY, (byte) 1),
    YELLOW(Material.STAINED_CLAY, (byte) 4),
    WHITE(Material.STAINED_CLAY, (byte) 0),
    BLUE(Material.STAINED_CLAY, (byte) 11);
    public final Material material;
    public final byte data;

    Color(Material mat, byte data)
    {
      this.material = mat;
      this.data = data;
    }
  }

  public Paintball(Location location, Color color)
  {
    this._location = location;
    this._box = new BoundingBox(location.clone().add(4, 4, 4), location.clone().subtract(4, 4, 4));
    this._color = color;
  }

  public void explode()
  {
    final Map<Location, Double> locToHeat = new HashMap<>();

    for (Location l : this._box)
      if (!locToHeat.containsKey(l))
        locToHeat.put(l, 0.0);
    locToHeat.put(this._location, 25000.0);
    final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(MagmaPlugin.instance, () -> {
      Map<Location, Double> locToHeat2 = new HashMap<>();
      locToHeat.forEach((loc, heat) -> {
        double a = 0.12;
        if (loc.getBlock().getType() == Material.AIR)
          a /= 2;
        for (int i = -1; i <= 1; i += 2)
          for (int j = -1; j <= 1; j += 2)
            for (int k = -1; k <= 1; k += 2)
              if (locToHeat.get(loc.clone().add(i, j, k)) == null)
                return;
        // Central difference
        Location next = loc.clone().add(1, 0, 0);
        double heat1 = locToHeat.get(next);
        next = loc.clone().add(-1, 0, 0);
        double heat2 = locToHeat.get(next);
        double ddx = heat1 + heat2 - 2 * heat;

        next = loc.clone().add(0, 1, 0);
        heat1 = locToHeat.get(next);
        next = loc.clone().add(0, -1, 0);
        heat2 = locToHeat.get(next);
        double ddy = heat1 + heat2 - 2 * heat;

        next = loc.clone().add(0, 0, 1);
        heat1 = locToHeat.get(next);
        next = loc.clone().add(0, 0, -1);
        heat2 = locToHeat.get(next);
        double ddz = heat1 + heat2 - 2 * heat;
        double newHeat = heat + a * (ddx + ddy + ddz);
        locToHeat2.put(loc, newHeat);
      });

      locToHeat2.forEach(locToHeat::put);
      locToHeat.forEach((loc, heat) -> {
        if (heat > 20 && !loc.getBlock().isEmpty())
        {
          loc.getBlock().setType(this._color.material);
          loc.getBlock().setData(this._color.data);
        }
      });
    }, 0, 3);

    Bukkit.getScheduler().runTaskLater(MagmaPlugin.instance, () -> Bukkit.getScheduler().cancelTask(id), 20);
  }
}
