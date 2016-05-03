package edu.ub.pis2016.pis16.strikecom.engine.game;

/**
 * A component is a container for a certain physical or non-physical set of properties and actions a {@link GameObject} can
 * perform. Each GameObject contains one or various of these components, and each component has a reference to the
 * GameObject they pertain. As such, Components may talk to each other, always making sure that the GameObject has
 * the other kind of component.
 * <p/>
 * For example, a GameObject might have a BehaviorComponent and a PhysicsComponent. The BehaviorComponent tells the PhysicsComponent
 * how it should move. For example, it tells the PhysicsComponent to move forward with a speed of 10 pixels/second.
 * After the PhysicsComponent has moved, a third component, a GraphicsComponent, pulls the position and rotation data from the
 * PhysicsComponent and uses it to draw the GameObject to the screen.
 *
 * @author German
 */
public abstract class Component {

	protected GameObject gameObject;

//	/** Called on the next frame of this component being on the GameObject. */
//	protected abstract void init();

	/** Called by the GameObject when it takes ownership. DO NOT USE THIS METHOD. */
	protected void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
}
