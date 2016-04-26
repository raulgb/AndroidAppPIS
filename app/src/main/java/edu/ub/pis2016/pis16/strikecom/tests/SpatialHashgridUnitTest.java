package edu.ub.pis2016.pis16.strikecom.tests;

import edu.ub.pis2016.pis16.strikecom.engine.physics.SpatialHashGrid;

/**
 * Created by Arbitro on 26/04/2016.
 */
public class SpatialHashgridUnitTest {

	public static void main(String... args) {

		SpatialHashGrid grid = new SpatialHashGrid(64, 64, 8);

		System.out.println(grid.cellCount());
	}
}
