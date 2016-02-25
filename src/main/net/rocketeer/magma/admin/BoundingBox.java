package net.rocketeer.magma.admin;

import org.bukkit.Location;
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
      if (Math.abs((a.getX() + x * dVec[0]) - b.getX()) < 1)
      {
        x = 0;
        ++y;
      } else if (Math.abs((a.getY() + y * dVec[1]) - b.getY()) < 1)
      {
        x = 0;
        y = 0;
        ++z;
      }

      return (current = a.clone().add(x++ * dVec[0], y * dVec[1], z * dVec[2]));
    }
  }
}