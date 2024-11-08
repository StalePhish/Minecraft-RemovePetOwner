package org.candrews.removePetOwner;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public final class RemovePetOwner extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityClick (PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();

        // Player holding shears in their main hand right-clicks a tameable mob
        if (event.getHand() == EquipmentSlot.HAND &&
                player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS) &&
                event.getRightClicked() instanceof Tameable pet)
        {
            // Get the pet's name
            String name = pet.getName();

            // Does the pet have an owner?
            if (pet.getOwner() != null)
            {
                // Player owns the pet, and it's not wearing wolf armor
                if (player.getName().equals((pet.getOwner().getName())) &&
                        (pet.getEquipment() == null ||
                                !pet.getEquipment().getItem(EquipmentSlot.BODY).getType().equals(Material.WOLF_ARMOR)))
                {
                    // Remove the owner
                    pet.setOwner(null);
                    player.sendMessage(name + " has been released");

                    // Play the shear sound effect when successful
                    player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);

                    // Make the animal stand up since it has no owner
                    if (pet instanceof Sittable sit)
                    {
                        sit.setSitting(false);
                    }

                    // Cancel other PlayerInteractEntityEvent events on this mob to prevent conflicting outcomes
                    event.setCancelled((true));
                }
                else
                {
                    player.sendMessage(name + " is owned by " + pet.getOwner().getName());
                }
            }
            else
            {
                player.sendMessage(name + " has no owner");
            }
        }
    }
}