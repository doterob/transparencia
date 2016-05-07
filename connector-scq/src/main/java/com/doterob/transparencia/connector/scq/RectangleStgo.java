package com.doterob.transparencia.connector.scq;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectangleStgo {

	final List<Integer> rows = new ArrayList<Integer>();
	final Map<Integer, List<Rectangle>> zones = new HashMap<Integer, List<Rectangle>>();

	public void add(int x, int y, int width, int height) {
		if (!this.zones.containsKey(y)) {
			this.zones.put(y, new ArrayList<Rectangle>());
			this.rows.add(y);
		}
		this.zones.get(y).add(new Rectangle(x, y, width, height));
	}

	public int getRows() {
		return this.zones.size();
	}

	public int getColumnsByIndex(int row) {
		return this.zones.get(this.rows.get(row)).size();
	}

	public Rectangle getZoneByIndex(int row, int column) {
		return this.zones.get(this.rows.get(row)).get(column);
	}

	public String getZoneName(int row, int column) {
		return "row(" + row + "," + column + ")";
	}

	private static volatile RectangleStgo instance;

	private RectangleStgo() {
	}

	public static RectangleStgo getInstance() {
		if (instance == null) {
			instance = new RectangleStgo();
		}
		return instance;
	}

}
