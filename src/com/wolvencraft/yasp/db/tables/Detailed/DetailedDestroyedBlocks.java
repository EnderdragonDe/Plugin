package com.wolvencraft.yasp.db.tables.detailed;

public enum DetailedDestroyedBlocks implements _DetailedTable {
	
	TableName("detailed_destroyed_blocks"),
	
	EntryId("detailed_destroyed_blocks_id"),
	MaterialId("material_id"),
	MaterialData("material_data"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	DetailedDestroyedBlocks(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}