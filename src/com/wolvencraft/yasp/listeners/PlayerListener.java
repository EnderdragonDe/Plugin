package com.wolvencraft.yasp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class PlayerListener implements Listener {

	public PlayerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		DataCollector.updateMaxPlayersOnline(Bukkit.getOnlinePlayers().length);
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).login();
		Message.send(player, Settings.getWelcomeMessage(player));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).logout();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		double distance = player.getLocation().distance(event.getTo());
		if(player.isInsideVehicle()) {
			Vehicle vehicle = (Vehicle) player.getVehicle();
			if(vehicle.getType().equals(EntityType.MINECART)) {
				DataCollector.get(player).addDistanceMinecart(distance);
			} else if(vehicle.getType().equals(EntityType.BOAT)) {
				DataCollector.get(player).addDistanceBoat(distance);
			} else if(vehicle.getType().equals(EntityType.PIG)) {
				DataCollector.get(player).addDistancePig(distance);
			}
		} else {
			DataCollector.get(player).addDistanceFoot(distance);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemPickUp(event.getItem().getItemStack());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemDrop(event.getItemDrop().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerItemUse(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemUse(player.getItemInHand());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCraft(CraftItemEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemCraft(event.getCurrentItem());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerSmelt(FurnaceExtractEvent event) {
		Player player = event.getPlayer();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemSmelt(new ItemStack(event.getItemType()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerEnchant(EnchantItemEvent event) {
		Player player = event.getEnchanter();
		if(Util.isExempt(player)) return;
		DataCollector.get(player).itemEnchant(new ItemStack(event.getItem().getType()));
	}
}
