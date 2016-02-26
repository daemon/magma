package net.rocketeer.magma.admin;

import net.rocketeer.magma.MagmaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BoundingBox implements Iterable<Location>
{
  public final Location a;
  public final Location b;
  private static final Vector[] _normals = new Vector[6];
  private final Location[] _facePoints = new Location[6];

  static
  {
    // Intersections and bound-checking
    _normals[0] = new Vector(0, 1, 0);
    _normals[1] = new Vector(0, -1, 0);
    _normals[2] = new Vector(1, 0, 0);
    _normals[3] = new Vector(-1, 0, 0);
    _normals[4] = new Vector(0, 0, 1);
    _normals[5] = new Vector(0, 0, -1);
  }

  public BoundingBox(Location a, Location b)
  {
    this.a = a;
    this.b = b;
    if (this.a.getY() < this.b.getY())
    {
      this._facePoints[0] = a;
      this._facePoints[1] = b;
    }

    if (this.a.getX() < this.b.getX())
    {
      this._facePoints[2] = a;
      this._facePoints[3] = b;
    }

    if (this.a.getZ() < this.b.getZ())
    {
      this._facePoints[4] = a;
      this._facePoints[5] = b;
    }
  }

  @Override
  public Iterator iterator()
  {
    return new Iterator();
  }

  public boolean contains(Location location)
  {
    if (!location.getWorld().equals(a.getWorld()))
      return false;

    for (int i = 0; i < 6; ++i)
    {
      Location diff = location.clone().subtract(this._facePoints[i]);
      Vector vec = new Vector(diff.getX(), diff.getY(), diff.getZ());
      if (_normals[i].dot(vec) < 0)
        return false;
    }

    return true;
  }

  public String toString()
  {
    return this.a.toString() + ", " + this.b.toString();
  }

  public class Iterator implements java.util.Iterator<Location>
  {
    private Location current = a.clone();
    private double[] dVec = new double[3];
    private int x, y, z;

    public Iterator()
    {
      Location dLoc = b.clone().subtract(a);
      Vector vec = new Vector(dLoc.getX(), dLoc.getY(), dLoc.getZ());
      this.dVec[0] = vec.getX() / Math.abs(vec.getX());
      this.dVec[1] = vec.getY() / Math.abs(vec.getY());
      this.dVec[2] = vec.getZ() / Math.abs(vec.getZ());
    }

    @Override
    public boolean hasNext()
    {
      return b.distance(current) > 1;
    }

    @Override
    public Location next()
    {
      if ((int) a.getX() + this.x * this.dVec[0] == (int) b.getX() + this.dVec[0])
      {
        this.x = 0;
        ++this.y;
      } else if ((int) a.getY() + this.y * this.dVec[1] == (int) b.getY() + this.dVec[1])
      {
        this.x = 0;
        this.y = 0;
        ++this.z;
      }

      return (current = a.clone().add(this.x++ * this.dVec[0], this.y * this.dVec[1], this.z * this.dVec[2]));
    }
  }
}