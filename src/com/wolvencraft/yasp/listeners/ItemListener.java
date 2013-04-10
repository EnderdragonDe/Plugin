/*
 * ItemListener.java
 * 
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DatabaseTask;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.tables.Normal.MiscInfoPlayersTable;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.Util;

/**
 * Listens to any item changes on the server and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class ItemListener implements Listener {
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new instance of the Listener and registers it with the PluginManager
     * @param plugin StatsPlugin instance
     */
    public ItemListener(Statistics plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "item.pickup")) return;
        DatabaseTask.getSession(player).itemPickUp(player.getLocation(), event.getItem().getItemStack());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "item.drop")) return;
        DatabaseTask.getSession(player).itemDrop(player.getLocation(), event.getItemDrop().getItemStack());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFoodConsume(FoodLevelChangeEvent event) {
        if(Statistics.getPaused()) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(!Util.isTracked(player, "item.use")) return;
        OnlineSession session = DatabaseTask.getSession(player);
        session.itemUse(player.getLocation(), player.getItemInHand());
        session.addMiscValue(MiscInfoPlayersTable.FoodEaten);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemCraft(CraftItemEvent event) {
        if(Statistics.getPaused()) return;
        Player player = (Player) event.getWhoClicked();
        if(!Util.isTracked(player, "item.craft")) return;
        DatabaseTask.getSession(player).itemCraft(player.getLocation(), event.getCurrentItem());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemSmelt(FurnaceExtractEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "item.smelt")) return;
        DatabaseTask.getSession(player).itemSmelt(player.getLocation(), new ItemStack(event.getItemType()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onToolBreak(PlayerItemBreakEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getPlayer();
        if(!Util.isTracked(player, "item.break")) return;
        DatabaseTask.getSession(player).itemBreak(player.getLocation(), event.getBrokenItem());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemEnchant(EnchantItemEvent event) {
        if(Statistics.getPaused()) return;
        Player player = event.getEnchanter();
        if(!Util.isTracked(player, "item.enchant")) return;
        DatabaseTask.getSession(player).itemEnchant(player.getLocation(), new ItemStack(event.getItem().getType()));
    }
}
