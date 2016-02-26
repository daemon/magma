package net.rocketeer.magma.weapon;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SnowballHitListener implements Listener
{
  @EventHandler
  public void onProjectileHitEvent(ProjectileHitEvent event)
  {
    if (!event.getEntity().getType().equals(EntityType.SNOWBALL))
      return;
    (new Paintball(event.getEntity().getLocation(), Paintball.Color.RED)).explode();
  }
}
