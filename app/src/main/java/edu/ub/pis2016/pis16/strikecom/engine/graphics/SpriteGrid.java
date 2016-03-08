package edu.ub.pis2016.pis16.strikecom.engine.graphics;

public class SpriteGrid {


//	private final int X = 0, Y = 1, W = 2, H = 3;
//	/** Size of individual tiles in pixels */
//	private int tilew, tileh;
//	/** Size of grid in tiles */
//	private int gridw, gridh;
//	/** Defines what index is drawn in each region */
//	private int[][] region;
//
//	private final Vector2 coords = new Vector2();
//
//	/** Creates a new sprite grid, will be rendered in one call. */
//	public SpriteGrid(Pixmap sheet, int sizeX, int sizeY, int tileW, int tileH) {
//		this.pixmap = sheet;
//		this.width = sizeX * tileW;
//		this.height = sizeY * tileW;
//		this.scaleX = 1;
//		this.scaleY = 1;
//		this.ox = 0;
//		this.oy = 0;
//		this.angle = 0;
//		this.dirty = true;
//
//		// Region size is num_rows = size Y * num_cols = size X
//		region = new int[sizeY][sizeX];
//		this.gridw = sizeX;
//		this.gridh = sizeY;
//		this.tilew = tileW;
//		this.tileh = tileH;
//
//		// Set all regions at 0, later will load from file or something
//		for (int r = 0; r < gridh; r++)
//			for (int c = 0; c < gridh; c++)
//				region[r][c] = MathUtils.random(1);
//
//	}
//
//	private int c, r;
//	private float offx, offy;
//
//	public void draw(Graphics g) {
//		this.updateProjectionMatrix();
//
//		g.saveTransformation();
//		g.applyTransformation(projection);
//
//		for (r = 0; r < gridh; r++)
//			for (c = 0; c < gridw; c++) {
//				//getFromIndex(region[r][c]);
//				offx = x + c * tilew;
//				offy = y + r * tileh;
//				srcX = region[r][c] % gridw * tilew;
//				srcY = region[r][c] / gridw * tileh;
//				//g.drawPixmap(pixmap, offx, offy, srcX, srcY, tilew+1, tileh+1);
//			}
//
//		g.restoreTransformation();
//	}
//
//	int srcX, srcY;
//
//	protected void getFromIndex(int index) {
//		srcX = index % gridw * tilew;
//		srcY = index / gridw * tileh;
//	}

}
