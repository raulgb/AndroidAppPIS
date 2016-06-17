package edu.ub.pis2016.pis16.strikecom.engine.physics;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

/**
 * Created by Alexander Bevzenko
 * as for now - axis-bound rectangles only
 * TODO: include other shapes after testing this
 */
public class SpatialHashGrid {
	int cellsPerRow;
	int cellsPerCol;
	float cellSize;
	int[] cellIds = new int[4];

	private Array<Body>[] dynamicCells;
	private Array<Body>[] staticCells;
	private Array<Body> foundObjects = new Array<>();

	/**
	 * constructor takes world Width, Height and cell size and divides world into small cells, and initialises all lists
	 *
	 * @param worldWidth  world width
	 * @param worldHeight world height
	 * @param cellSize    size of a cell
	 */
	public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize) {
		this.cellSize = cellSize;
		this.cellsPerRow = (int) Math.ceil(worldWidth / cellSize);
		this.cellsPerCol = (int) Math.ceil(worldHeight / cellSize);
		int numCells = this.cellsPerRow * this.cellsPerCol;
		dynamicCells = new Array[numCells];
		staticCells = new Array[numCells];

		for (int i = 0; i < numCells; i++) {
			dynamicCells[i] = new Array<>(10);
			staticCells[i] = new Array<>(10);
		}
	}

	public void insertStaticObject(Body obj) {
		int[] cellIds = getCellIds(obj); // test
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			this.staticCells[cellId].add(obj);
		}
	}

	public void insertDynamicObject(Body body) {
		int[] cellIds = getCellIds(body); // test
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			this.dynamicCells[cellId].add(body);
		}
	}

	public void removeObject(Body obj) {
		int[] cellIds = getCellIds(obj); // test
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			this.staticCells[cellId].removeValue(obj);
			this.dynamicCells[cellId].removeValue(obj);
		}
	}

	/** is called each frame in order to update dynamic objects positions correctly */
	public void clearDynamicCells() {
		for (int i = 0; i < this.dynamicCells.length; i++) {
			this.dynamicCells[i].clear();
		}
	}

	public Array<Body> getPotentialColliders(Body body) {
		this.foundObjects.clear();

		int[] cellIds = getCellIds(body);

		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			int len = this.dynamicCells[cellId].size;
			for (int j = 0; j < len; j++) {
				//Log.d("SpatialHG", i + ", " + j + ", " + this.dynamicCells[cellId].size());
				Body collider = this.dynamicCells[cellId].get(j);
				if (!this.foundObjects.contains(collider)) {
					this.foundObjects.add(collider);
				}
			}
			len = this.staticCells[cellId].size;
			for (int j = 0; j < len; j++) {
				Body collider = this.staticCells[cellId].get(j);
				if (!this.foundObjects.contains(collider)) {
					this.foundObjects.add(collider);
				}
			}

		}
		return foundObjects;
	}

	public int[] getCellIds(Body body) {
		int x1 = (int) Math.floor(body.position.x / this.cellSize);
		int y1 = (int) Math.floor(body.position.y / this.cellSize);
		int x2 = (int) Math.floor((body.position.x + body.bounds.width) / this.cellSize);
		int y2 = (int) Math.floor((body.position.y + body.bounds.height) / this.cellSize);

		if (x1 == x2 && y1 == y2) {
			if (x1 >= 0 && x1 < this.cellsPerRow && y1 >= 0 && y1 < this.cellsPerCol) {
				this.cellIds[0] = x1 + y1 * this.cellsPerRow;
			} else {
				this.cellIds[0] = -1;
				this.cellIds[1] = -1;
				this.cellIds[2] = -1;
				this.cellIds[3] = -1;
			}

		} else if (x1 == x2) {
			int i = 0;
			if (x1 >= 0 && x1 < this.cellsPerRow) {
				if (y1 >= 0 && y1 < this.cellsPerCol) {
					this.cellIds[i++] = x1 + y1 * this.cellsPerRow;
				}
				if (y2 >= 0 && y2 < this.cellsPerCol) {
					this.cellIds[i++] = x1 + y1 * this.cellsPerRow;
				}
			}
			while (i < 4) this.cellIds[i++] = -1;
		} else if (y1 == y2) {
			int i = 0;
			if (y1 >= 0 && y1 < this.cellsPerCol) {
				if (x1 >= 0 && x1 < this.cellsPerRow) {
					this.cellIds[i++] = x1 + y1 * this.cellsPerRow;
				}
				if (x2 >= 0 && x2 < this.cellsPerRow) {
					this.cellIds[i++] = x1 + y1 * this.cellsPerRow;
				}
			}
			while (i < 4) this.cellIds[i++] = -1;
		} else {
			int i = 0;
			int y1CellsPerRow = y1 * cellsPerRow;
			int y2CellsPerRow = y2 * cellsPerRow;
			if (x1 >= 0 && x1 < this.cellsPerRow && y1 >= 0 && y1 < this.cellsPerCol) {
				this.cellIds[i++] = x1 + y1CellsPerRow;
			}
			if (x2 >= 0 && x2 < this.cellsPerRow && y1 >= 0 && y1 < this.cellsPerCol) {
				this.cellIds[i++] = x2 + y1CellsPerRow;
			}
			if (x2 >= 0 && x2 < this.cellsPerRow && y2 >= 0 && y2 < this.cellsPerCol) {
				this.cellIds[i++] = x2 + y2CellsPerRow;
			}
			if (x1 >= 0 && x1 < this.cellsPerRow && y2 >= 0 && y2 < this.cellsPerCol) {
				this.cellIds[i++] = x1 + y2CellsPerRow;
			}
			while (i < 4) this.cellIds[i++] = -1;
		}
		return this.cellIds;
	}

	public int cellCount() {
		return this.cellsPerCol * this.cellsPerRow;
	}
}
