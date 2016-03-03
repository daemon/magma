package net.rocketeer.magma.weapon;

import net.rocketeer.magma.arena.ArenaStore;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class SnowballHitListener implements Listener
{
  private final ArenaStore _as;

  public SnowballHitListener(ArenaStore store)
  {
    this._as = store;
  }
  @EventHandler
  public void onProjectileHitEvent(ProjectileHitEvent event)
  {
    if (!event.getEntity().getType().equals(EntityType.SNOWBALL))
      return;
    if (!this._as.contains(event.getEntity().getLocation(), false)) // change to true
      return;
    (new Paintball(event.getEntity().getLocation(), Paintball.Color.RED)).explode();
  }
}
