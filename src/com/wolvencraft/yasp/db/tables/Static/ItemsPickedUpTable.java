package com.wolvencraft.yasp.db.tables.Static;

public enum ItemsPickedUpTable implements StaticTable {

	TableName("detailed_items_pickup"),
	DestroyedBlockId("ipickupID"),
	MaterialId("material_id"),
	PlayerId("player_id"),
	World("world"),
	XCoord("x"),
	YCoord("y"),
	ZCoord("z"),
	Timestamp("time");
	
	ItemsPickedUpTable(String columnName) {
		this.columnName = columnName;
	}
	
	private String columnName;
	
	@Override
	public String toString() { return columnName; }
}