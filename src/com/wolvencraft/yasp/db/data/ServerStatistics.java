package com.wolvencraft.yasp.db.data;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.ServerStatisticsTable;
import com.wolvencraft.yasp.util.Util;

public class ServerStatistics {
	
	public ServerStatistics(StatsPlugin plugin) {
		lastSyncTime = Util.getTimestamp();
		
		firstStartupTime = 0;
		startupTime = Util.getTimestamp();
		currentUptime = 0;
		totalUptime = 0;
		maxPlayersOnline = 0;
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
		maxPlayersAllowed = Bukkit.getMaxPlayers();
		bukkitVersion = Bukkit.getBukkitVersion();
		serverIP = Bukkit.getIp();
		serverPort = Bukkit.getPort();
		serverMOTD = Bukkit.getMotd();
		
		Runtime runtime = Runtime.getRuntime();
		totalMemory = runtime.totalMemory();
		freeMemory = runtime.freeMemory();
		
		World mainWorld = Bukkit.getWorlds().get(0);
		
		weather = mainWorld.hasStorm();
		weatherDuration = mainWorld.getWeatherDuration();
		serverTime = mainWorld.getFullTime();
		
		entities = 0;
		for(World world : Bukkit.getServer().getWorlds()) {
			entities += world.getEntities().size();
		}
		
		fetchData();
		pushStaticData();
	}
	
	private long lastSyncTime;
	
	private long firstStartupTime;
	private long startupTime;
	private long currentUptime;
	private long totalUptime;
	private int maxPlayersOnline;
	private long maxPlayersOnlineTime;
	private int plugins;
	private String bukkitVersion;
	
	private String serverIP;
	private int serverPort;
	private String serverMOTD;
	
	private int maxPlayersAllowed;
	
	private long totalMemory;
	private long freeMemory;
	
	private boolean weather;
	private int weatherDuration;
	private long serverTime;
	
	private int entities;
	
	public void fetchData() {
		List<QueryResult> entries = QueryUtils.select(ServerStatisticsTable.TableName.toString(), new String[] {"*"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("first_startup")) firstStartupTime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("total_uptime")) totalUptime = entry.getValueAsLong("value");
			else if(entry.getValue("key").equalsIgnoreCase("max_players_online")) maxPlayersOnline = entry.getValueAsInteger("value");
		}
		
		if(firstStartupTime == 0) firstStartupTime = Util.getTimestamp();
	}

	public boolean pushData() {
		long curTime = Util.getTimestamp();
		currentUptime = curTime - startupTime;
		totalUptime += (curTime - lastSyncTime);
		lastSyncTime = curTime;
		serverTime = Bukkit.getWorlds().get(0).getFullTime();
		entities = 0;
		for(World world : Bukkit.getServer().getWorlds()) {
			entities += world.getEntities().size();
		}
		
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", currentUptime + "", new String[] {"key", "current_uptime"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", totalUptime + "", new String[] {"key", "total_uptime"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", maxPlayersOnline + "", new String[] {"key", "max_players_online"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", maxPlayersOnlineTime + "", new String[] {"key", "max_players_online_time"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", totalMemory + "", new String[] {"key", "total_memory"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", freeMemory + "", new String[] {"key", "free_memory"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverTime + "", new String[] {"key", "server_time"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weather + "", new String[] {"key", "weather"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weatherDuration + "", new String[] {"key", "weather_duration"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", entities + "", new String[] {"key", "entities_count"} );
		return true;
	}
	
	public void pushStaticData() {
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", firstStartupTime + "", new String[] {"key", "first_startup"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", startupTime + "", new String[] {"key", "last_startup"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", plugins + "", new String[] {"key", "plugins"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", bukkitVersion, new String[] {"key", "bukkit_version"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverIP, new String[] {"key", "server_ip"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverPort + "", new String[] {"key", "server_port"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", serverMOTD, new String[] {"key", "server_motd"} );
	}
	
	/**
	 * Indicates that the plugin is shutting down and registers the current shutdown time.
	 */
	public void pluginShutdown() {
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", Util.getTimestamp() + "", new String[] {"key", "last_shutdown"} );
	}
	
	/**
	 * Updates the maximum online players count.
	 * @param players Maximum players online
	 */
	public void playerLogin(int playersOnline) {
		if(playersOnline > maxPlayersOnline) {
			this.maxPlayersOnline = playersOnline;
			this.maxPlayersOnlineTime = Util.getTimestamp();
		}
		
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", maxPlayersAllowed + "", new String[] {"key", "players_allowed"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", playersOnline + "", new String[] {"key", "players_online"} );
	}
	
	public void weatherChange(boolean isStorming, int duration) {
		weather = isStorming;
		weatherDuration = duration;
		
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weather + "", new String[] {"key", "weather"} );
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", weatherDuration + "", new String[] {"key", "weather_duration"} );
	}
	
	public void pluginNumberChange() {
		plugins = Bukkit.getServer().getPluginManager().getPlugins().length;
		QueryUtils.update( ServerStatisticsTable.TableName.toString(), "value", plugins + "", new String[] {"key", "plugins"} );
	}
}
