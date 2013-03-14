package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.QueryUtils.QueryResult;
import com.wolvencraft.yasp.db.data.receive.ServerTotals;
import com.wolvencraft.yasp.db.data.sync.ServerStatistics;
import com.wolvencraft.yasp.db.data.sync.Settings;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

/**
 * Stores collected statistical data until it can be processed and sent to the database
 * @author bitWolfy
 *
 */
public class DataCollector implements Runnable {

	/**
	 * <b>Default constructor.</b><br />
	 * Initializes an empty list of LocalSessions
	 */
	public DataCollector() {
		sessions = new ArrayList<LocalSession>();
		serverStatistics = new ServerStatistics(StatsPlugin.getInstance());
		serverTotals = new ServerTotals();
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!Util.isExempt(player)) get(player);
		}
	}
	
	private static List<LocalSession> sessions;
	private static ServerStatistics serverStatistics;
	private static ServerTotals serverTotals;

	@Override
	public void run() {
		if(StatsPlugin.getPaused()) return;
		pushAllData();
		serverStatistics.fetchData();
		serverTotals.fetchData();
	}
	
	/**
	 * Pushes all data to the database.<br />
	 * This method is run periodically, as well as on plugin shutdown.
	 */
	public static void pushAllData() {
		Message.debug("Database synchronization in progress");
		for(LocalSession session : get()) {
			session.pushData();
			if(!session.isOnline()) remove(session);
		}
		
		serverStatistics.pushData();
	}
	
	public static void dumpAll() {
		for(LocalSession session : get()) {
			session.dump();
			remove(session);
		}
	}
	
	/**
	 * Returns all stored sessions
	 * @return List of stored player sessions
	 */
	public static List<LocalSession> get() {
		List<LocalSession> tempList = new ArrayList<LocalSession>();
		for(LocalSession session : sessions) tempList.add(session);
		return tempList;
	}
	
	/**
	 * Returns the LocalSession associated with the specified player.<br />
	 * If no session is found, it will be created.
	 * @param player Tracked player
	 * @return LocalSession associated with the player.
	 */
	public static LocalSession get(Player player) {
		for(LocalSession session : sessions) {
			if(session.getPlayerName().equals(player.getPlayerListName())) {
				return session;
			}
		}
		Message.debug("Creating a new user session for " + player.getPlayerListName());
		LocalSession newSession = new LocalSession(player);
		sessions.add(newSession);
		Message.send(player, Settings.getFirstJoinMessage(player));
		return newSession;
	}
	
	public static OfflineSession get(String playerName) {
		Message.debug("Fetching an offline session for " + playerName);
		return new OfflineSession(playerName);
	}
	
	/**
	 * Purges the stored sessions list of all data
	 */
	public static void clear() {
		sessions.clear();
	}
	
	/**
	 * Removes the specified session
	 * @param session Session to remove
	 */
	public static void remove(LocalSession session) {
		Message.debug("Removing a user session for " + session.getPlayerName());
		sessions.remove(session);
	}
	
	/**
	 * Returns the PlayerID corresponding with the specified username.<br />
	 * If the username is not in the database, a dummy entry is created, and an ID is assigned.<br />
	 * Unlike <i>getCachedPlayerId(String username)</i>, does not save the username-id pairs locally.
	 * @param player Player name to look up in the database
	 * @return <b>Integer</b> PlayerID corresponding to the specified username
	 */
	public static Integer getPlayerId(Player player) {
		String username = player.getPlayerListName();
		Message.debug("Retrieving a player ID for " + username);
		int playerId = -1;
		List<QueryResult> results;
		
		results = QueryUtils.select(PlayersTable.TableName.toString())
					.column(PlayersTable.PlayerId.toString())
					.column(PlayersTable.Name.toString())
					.condition(PlayersTable.Name.toString(), username)
					.select();
		
		if(results.isEmpty()) {
			QueryUtils.insert(PlayersTable.TableName.toString())
				.value(PlayersTable.Name.toString(), username)
				.insert();
			
			results = QueryUtils
				.select(PlayersTable.TableName.toString())
				.column(PlayersTable.PlayerId.toString())
				.column(PlayersTable.Name.toString())
				.condition(PlayersTable.Name.toString(), username)
				.select();
			playerId = results.get(0).getValueAsInteger(PlayersTable.PlayerId.toString());
		} else {
			playerId = results.get(0).getValueAsInteger(PlayersTable.PlayerId.toString());
		}
		
		Message.debug("User ID found: " + playerId);
		return playerId;
	}
	
	public static ServerStatistics global() {
		return serverStatistics;
	}
	
	public static ServerTotals serverTotals() {
		return serverTotals;
	}
}