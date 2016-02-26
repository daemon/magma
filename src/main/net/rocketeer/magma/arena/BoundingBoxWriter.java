package net.rocketeer.magma.arena;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.admin.BoundingBox;
import org.bukkit.Location;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class BoundingBoxWriter
{
  private final String _fileName;
  private final File _file;
  private final BoundingBox _bbox;

  public BoundingBoxWriter(BoundingBox bbox, String fileName)
  {
    this._fileName = fileName;
    this._bbox = bbox;
    this._file = new File(MagmaPlugin.instance.getDataFolder(), fileName);
  }

  public void write() throws IOException
  {
    GZIPOutputStream gz = null;
    try
    {
      try
      {
        gz = new GZIPOutputStream(new FileOutputStream(this._file));
      } catch (IOException e)
      {
        e.printStackTrace();
        return;
      }

      for (Location l : this._bbox)
      {
        gz.write(l.getBlock().getTypeId());
        gz.write(l.getBlock().getData());
      }
    } finally {
      if (gz != null)
        gz.close();
    }
  }
}
