package net.rocketeer.magma.arena;

import net.rocketeer.magma.MagmaPlugin;
import net.rocketeer.magma.message.MessageAlertColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class ArenaResetTask implements Runnable
{
  private final Arena _arena;
  private final CommandSender _sender;
  private final BoundingBoxReader _reader;
  private int _id;

  public ArenaResetTask(CommandSender sender, Arena arena) throws IOException
  {
    this._arena = arena;
    this._sender = sender;
    this._reader = this._arena.createReader();
  }

  @Override
  public void run()
  {
    this._id = Bukkit.getScheduler().scheduleSyncRepeatingTask(MagmaPlugin.instance, () -> {
      try
      {
        if (this._reader.readIntoWorld(200) == 0)
        {
          Bukkit.getScheduler().cancelTask(this._id);
          this._sender.sendMessage(MessageAlertColor.NOTIFY_SUCCESS + "Reset arena successfully.");
        }
      } catch (IOException ignored)
      {
        Bukkit.getScheduler().cancelTask(this._id);
        this._sender.sendMessage(MessageAlertColor.ERROR + "Couldn't reset arena properly.");
      }
    }, 0, 5);
  }
}
