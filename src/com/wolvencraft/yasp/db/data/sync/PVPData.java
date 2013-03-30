/*
 * PVPData.java
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

package com.wolvencraft.yasp.db.data.sync;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Detailed.PVPKills;
import com.wolvencraft.yasp.db.tables.Normal.TotalPVPKillsTable;
import com.wolvencraft.yasp.util.Util;

/**
 * Data collector that records all PVP statistics on the server for a specific player.
 * @author bitWolfy
 *
 */
public class PVPData implements DataStore {

    private int playerId;
    private List<TotalPVPEntry> normalData;
    private List<DetailedData> detailedData;
    
    /**
     * <b>Default constructor</b><br />
     * Creates an empty data store to save the statistics until database synchronization.
     */
    public PVPData(int playerId) {
        this.playerId = playerId;
        normalData = new ArrayList<TotalPVPEntry>();
        detailedData = new ArrayList<DetailedData>();
    }
    
    @Override
    public List<NormalData> getNormalData() {
        List<NormalData> temp = new ArrayList<NormalData>();
        for(NormalData value : normalData) temp.add(value);
        return temp;
    }
    
    @Override
    public List<DetailedData> getDetailedData() {
        List<DetailedData> temp = new ArrayList<DetailedData>();
        for(DetailedData value : detailedData) temp.add(value);
        return temp;
    }
    
    @Override
    public void sync() {
        for(NormalData entry : getNormalData()) {
            if(entry.pushData(playerId)) normalData.remove(entry);
        }
        
        for(DetailedData entry : getDetailedData()) {
            if(entry.pushData(playerId)) detailedData.remove(entry);
        }
    }
    
    @Override
    public void dump() {
        for(NormalData entry : getNormalData()) {
            normalData.remove(entry);
        }
        
        for(DetailedData entry : getDetailedData()) {
            detailedData.remove(entry);
        }
    }
    
    /**
     * Returns the specific entry from the data store.<br />
     * If the entry does not exist, it will be created.
     * @param victimId ID of the victim in a PVP event
     * @param weapon Weapon used in the event
     * @return Corresponding entry
     */
    public TotalPVPEntry getNormalData(int victimId, ItemStack weapon) {
        for(TotalPVPEntry entry : normalData) {
            if(entry.equals(victimId, weapon)) return entry;
        }
        TotalPVPEntry entry = new TotalPVPEntry(playerId, victimId, weapon);
        normalData.add(entry);
        return entry;
    }
    
    /**
     * Registers the player death in the data store
     * @param victim Player who was killed 
     * @param weapon Weapon used by killer
     */
    public void playerKilledPlayer(Player victim, ItemStack weapon) {
        int victimId = DataCollector.getPlayerId(victim);
        getNormalData(victimId, weapon).addTimes();
        detailedData.add(new DetailedPVPEntry(victim.getLocation(), victimId, weapon));
    }
    
    
    /**
     * Represents an entry in the PVP data store.
     * It is dynamic, i.e. it can be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class TotalPVPEntry implements NormalData {
        
        private int victimId;
        
        private int weaponType;
        private int weaponData;
        
        private int times;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new TotalPVP object based on the killer and victim in question
         * @param playerId Player who killed the victim
         * @param victimId Player who was killed
         * @param weapon Weapon used
         */
        public TotalPVPEntry(int playerId, int victimId, ItemStack weapon) {
            this.victimId = victimId;
            this.weaponType = weapon.getTypeId();
            this.weaponData = weapon.getData().getData();
            
            this.times = 0;
            
            fetchData(playerId);
        }
        
        @Override
        public void fetchData(int killerId) {
            QueryResult result = Query.table(TotalPVPKillsTable.TableName.toString())
                    .column(TotalPVPKillsTable.Times.toString())
                    .condition(TotalPVPKillsTable.PlayerId.toString(), killerId + "")
                    .condition(TotalPVPKillsTable.VictimId.toString(), victimId + "")
                    .condition(TotalPVPKillsTable.Material.toString(), Util.getBlockString(weaponType, weaponData))
                    .select();
            if(result == null) {
                Query.table(TotalPVPKillsTable.TableName.toString())
                    .value(TotalPVPKillsTable.PlayerId.toString(), killerId)
                    .value(TotalPVPKillsTable.VictimId.toString(), victimId)
                    .value(TotalPVPKillsTable.Material.toString(), Util.getBlockString(weaponType, weaponData))
                    .value(TotalPVPKillsTable.Times.toString(), times)
                    .insert();
            } else {
                times = result.getValueAsInteger(TotalPVPKillsTable.Times.toString());
            }
        }

        @Override
        public boolean pushData(int killerId) {
            boolean result = Query.table(TotalPVPKillsTable.TableName.toString())
                    .value(TotalPVPKillsTable.Times.toString(), times)
                    .condition(TotalPVPKillsTable.PlayerId.toString(), killerId + "")
                    .condition(TotalPVPKillsTable.VictimId.toString(), victimId + "")
                    .condition(TotalPVPKillsTable.Material.toString(), Util.getBlockString(weaponType, weaponData))
                    .update();
            if(Settings.LocalConfiguration.Cloud.asBoolean()) fetchData(killerId);
            return result;
        }
        
        /**
         * Matches data provided in the arguments with the one in the entry.
         * @param victimId ID of the victim
         * @param weapon Weapon used in the PVP event
         * @return <b>true</b> if the data matches, <b>false</b> otherwise.
         */
        public boolean equals(int victimId, ItemStack weapon) {
            int weaponType = weapon.getTypeId();
            if(Settings.ItemsWithMetadata.checkAgainst(weaponType)) {
                int weaponData = weapon.getData().getData();
                return this.victimId == victimId
                    && this.weaponType == weaponType
                    && this.weaponData == weaponData;
            }
            
            return this.victimId == victimId
                && this.weaponType == weaponType;
            
        }
        
        /**
         * Increments the number of times the victim was killed
         */
        public void addTimes() { times++; }
        
    }
    
    /**
     * Represents an entry in the Detailed data store.
     * It is static, i.e. it cannot be edited once it has been created.
     * @author bitWolfy
     *
     */
    public class DetailedPVPEntry implements DetailedData {
        
        private int victimId;
        
        private int weaponType;
        private int weaponData;
        
        private Location location;
        private long timestamp;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new DetailedPVPEntry based on the data provided
         * @param location
         * @param victimId
         * @param weapon
         */
        public DetailedPVPEntry(Location location, int victimId, ItemStack weapon) {
            this.victimId = victimId;
            this.weaponType = weapon.getTypeId();
            this.weaponData = weapon.getData().getData();
            
            this.location = location;
            this.timestamp = Util.getTimestamp();
        }
        
        @Override
        public boolean pushData(int killerId) {
            return Query.table(PVPKills.TableName.toString())
                    .value(PVPKills.KillerId.toString(), killerId)
                    .value(PVPKills.VictimId.toString(), victimId)
                    .value(PVPKills.Material.toString(), Util.getBlockString(weaponType, weaponData))
                    .value(PVPKills.World.toString(), location.getWorld().getName())
                    .value(PVPKills.XCoord.toString(), location.getBlockX())
                    .value(PVPKills.YCoord.toString(), location.getBlockY())
                    .value(PVPKills.ZCoord.toString(), location.getBlockZ())
                    .value(PVPKills.Timestamp.toString(), timestamp)
                    .insert();
        }

    }
    
}
