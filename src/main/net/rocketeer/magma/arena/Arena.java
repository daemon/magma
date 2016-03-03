package net.rocketeer.magma.arena;

import net.rocketeer.magma.admin.BoundingBox;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Arena
{
  private final String _name;
  private final BoundingBox _box;
  private final Location _redSpawn;
  private final Location _blueSpawn;
  private boolean _inProgress = false;
  private final Set<Player> _players = new HashSet<>();

  Arena(String name, BoundingBox box, Location redSpawn, Location blueSpawn)
  {
    this._name = name;
    this._box = box;
    this._redSpawn = redSpawn;
    this._blueSpawn = blueSpawn;
  }

  public World world()
  {
    return this._redSpawn.getWorld();
  }

  public BoundingBox boundingBox()
  {
    return this._box;
  }

  public String name()
  {
    return this._name;
  }

  public void setInProgress(boolean inProgress)
  {
    this._inProgress = inProgress;
  }

  public Location redSpawn()
  {
    return this._redSpawn;
  }

  public Location blueSpawn()
  {
    return this._blueSpawn;
  }

  public boolean isInProgress()
  {
    return this._inProgress;
  }

  BoundingBoxReader createReader() throws IOException
  {
    return new BoundingBoxReader(this._box, this._name);
  }

  BoundingBoxWriter createWriter() throws IOException
  {
    return new BoundingBoxWriter(this._box, this._name);
  }

  public Set<Player> players()
  {
    return this._players;
  }

  public static class Builder
  {
    private BoundingBox _bbox;
    private String _name;
    private Location _redSpawn;
    private Location _blueSpawn;

    public Builder boundingBox(BoundingBox box)
    {
      this._bbox = box;
      return this;
    }

    public Builder name(String name)
    {
      this._name = name;
      return this;
    }

    public Builder redSpawn(Location location)
    {
      this._redSpawn = location;
      return this;
    }

    public Builder blueSpawn(Location location)
    {
      this._blueSpawn = location;
      return this;
    }

    public boolean ready()
    {
      return _bbox != null && this._name != null && this._redSpawn != null && this._blueSpawn != null;
    }

    public Arena build() throws IOException
    {
      assert(this.ready());
      return new Arena(this._name, this._bbox, this._redSpawn, this._blueSpawn);
    }
  }
}
