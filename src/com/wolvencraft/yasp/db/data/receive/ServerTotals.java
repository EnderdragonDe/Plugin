/*
 * ServerTotals.java
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

package com.wolvencraft.yasp.db.data.receive;

import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.yasp.DatabaseTask;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.*;

/**
 * Generic Server information used on DisplaySigns and books.
 * @author bitWolfy
 *
 */
public class ServerTotals {
    
    /**
     * <b>Default Constructor</b><br />
     * Sets up the default values for the data holder.
     */
    public ServerTotals() {
        blocksBroken = 0;
        blocksPlaced = 0;
        distance = 0;
        toolsBroken = 0;
        itemsCrafted = 0;
        snacksEaten = 0;
        
        pvpKills = 0;
        pveKills = 0;
        otherKills = 0;
    }
    
    private int blocksBroken;
    private int blocksPlaced;
    private double distance;
    private int toolsBroken;
    private int itemsCrafted;
    private int snacksEaten;
    
    private int pvpKills;
    private int pveKills;
    private int pveDeaths;
    private int otherKills;
    
    /**
     * Fetches the data from the remote database.<br />
     * Automatically calculates values from the contents of corresponding tables.
     */
    public void fetchData() {
        blocksBroken = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Destroyed).sum();
        blocksPlaced = (int) Query.table(TotalBlocksTable.TableName).column(TotalBlocksTable.Placed).sum();
        distance = Query.table(DistancePlayersTable.TableName).column(DistancePlayersTable.Foot).sum();
        toolsBroken = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Broken).sum();
        itemsCrafted = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Crafted).sum();
        snacksEaten = (int) Query.table(TotalItemsTable.TableName).column(TotalItemsTable.Used).sum();
        
        pvpKills = (int) Query.table(TotalPVPKillsTable.TableName).column(TotalPVPKillsTable.Times).sum();
        pveKills = (int) Query.table(TotalPVEKillsTable.TableName).column(TotalPVEKillsTable.CreatureKilled).sum();
        pveDeaths = (int) Query.table(TotalPVEKillsTable.TableName).column(TotalPVEKillsTable.PlayerKilled).sum();
        otherKills = (int) Query.table(TotalDeathPlayersTable.TableName).column(TotalDeathPlayersTable.Times).sum();
    }
    
    /**
     * Bundles up the values into one Map for ease of access.
     * @return Map of values
     */
    public Map<String, Object> getValues() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("blBroken", blocksBroken);
        values.put("blPlaced", blocksPlaced);
        values.put("distance", distance);
        values.put("itBroken", toolsBroken);
        values.put("itCrafted", itemsCrafted);
        values.put("itEaten", snacksEaten);
        values.put("pvpKills", pvpKills);
        values.put("pveKills", pveKills);
        values.put("pveDeaths", pveDeaths);
        values.put("othKills", otherKills);
        values.putAll(DatabaseTask.getStats().getValueMap());
        return values;
    }
    
}
