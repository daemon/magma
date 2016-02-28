package net.rocketeer.magma.arena;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.admin.BoundingBox;
import org.bukkit.block.Block;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class BoundingBoxReader
{
  private final BoundingBox _bbox;
  private final DataInputStream _in;
  private BoundingBox.Iterator _iterator;

  public BoundingBoxReader(BoundingBox bbox, String fileName) throws IOException
  {
    this._bbox = bbox;
    this._in = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(MagmaPlugin.instance.getDataFolder(), fileName))));
    this._iterator = this._bbox.iterator();
  }

  public int readIntoWorld(int nBlocks) throws IOException
  {
    if (!this._iterator.hasNext())
      return 0;

    int read = 0;
    while (this._iterator.hasNext() && read < nBlocks)
    {
      int typeId = this._in.readInt();
      int dv = this._in.readInt();
      Block block = this._iterator.next().getBlock();
      block.setTypeId(typeId);
      block.setData((byte) dv);
      ++read;
    }

    return read;
  }

  public void reset()
  {
    this._iterator = this._bbox.iterator();
    try {
      this._in.reset();
    } catch (IOException ignored) {}
  }
}
