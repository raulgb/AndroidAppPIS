package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

/**
 * A Quad-Tree data structure, a Tree that splits into
 * 4 more trees when too many nodes are added.
 */
public class QuadTree {

	public int MAX_OBJECTS = 8;
	public int MAX_LEVELS = 8;

	private static final int TOP_LEFT = 0;
	private static final int TOP_RIGHT = 1;
	private static final int BOT_LEFT = 2;
	private static final int BOT_RIGHT = 3;

	private int level;
	private Array<Body> objects;
	private Rectangle bounds;
	private QuadTree[] nodes;

	/**
	 * Construct a new quad tree
	 *
	 * @param pLevel Level inside a hierarchy
	 */
	public QuadTree(int pLevel, Rectangle quadBounds) {
		level = pLevel;
		objects = new Array<>(false, 8);
		bounds = quadBounds;
		nodes = new QuadTree[4];
	}

	public void clear() {
		objects.clear();
		for (int i = nodes.length - 1; i-- > 0; ) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	/** Splits the node into 4 subnodes. */
	private void split() {
		int subWidth = (int) (bounds.width / 2f);
		int subHeight = (int) (bounds.height / 2f);
		int x = (int) bounds.x;
		int y = (int) bounds.y;

		nodes[TOP_LEFT] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[TOP_RIGHT] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
		nodes[BOT_LEFT] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
		nodes[BOT_RIGHT] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
	}


	/**
	 * Determine which node the object belongs to. -1 means
	 * object cannot completely fit within a child node and is part
	 * of the parent node
	 */
	private int getIndex(Body body) {
		int index = -1;
		float[] aabb = body.bounds.aabb;
		float rx = aabb[0];
		float ry = aabb[1];
		float rw = aabb[2];
		float rh = aabb[3];

		float verticalMidLine = bounds.x + (bounds.width / 2f);
		float horizontalMidLine = bounds.x + (bounds.height / 2f);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (ry < horizontalMidLine && ry + rh < horizontalMidLine);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (ry > horizontalMidLine);

		// Object can completely fit within the left quadrants
		if (rx < verticalMidLine && rx + rw < verticalMidLine) {
			if (topQuadrant) {
				index = TOP_LEFT;
			} else if (bottomQuadrant) {
				index = BOT_LEFT;
			}
		}
		// Object can completely fit within the right quadrants
		else if (rx > verticalMidLine) {
			if (topQuadrant) {
				index = TOP_RIGHT;
			} else if (bottomQuadrant) {
				index = BOT_RIGHT;
			}
		}

		return index;
	}

	/**
	 * Insert the object into the quadtree. If the node
	 * exceeds the capacity, it will split and add all
	 * objects to their corresponding nodes.
	 */
	public void insert(Body body) {

		// If this tree is split, find where it would fit down the tree
		if (nodes[0] != null) {
			int index = getIndex(body);

			if (index != -1) {
				nodes[index].insert(body);
				return;
			}
		}

		objects.add(body);

		if (objects.size > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < objects.size) {
				int index = getIndex(objects.get(i));
				if (index != -1) {
					nodes[index].insert(objects.removeIndex(i));
				} else {
					i++;
				}
			}
		}
	}

	/** Return all objects that are in range of the given object */
	public Array retrieve(Array returnObjects, Body body) {
		returnObjects.clear();
		int index = getIndex(body);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, body);
		}

		returnObjects.addAll(objects);
		return returnObjects;
	}

	public void print(int level) {
		String tabs = "|";
		for (int i = 0; i < level; i++)
			tabs += "--";

		System.out.println(tabs + "Tree: " + bounds.toString());
		for (Body body : objects)
			System.out.println(tabs + "-" + body.userData + body.bounds.toString());

		for (QuadTree node : nodes) {
			if (node != null)
				node.print(level + 1);
		}
	}

}