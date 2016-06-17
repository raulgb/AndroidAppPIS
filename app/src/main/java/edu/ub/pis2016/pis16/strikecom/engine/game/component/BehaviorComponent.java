package edu.ub.pis2016.pis16.strikecom.engine.game.component;

import edu.ub.pis2016.pis16.strikecom.engine.game.Component;
import edu.ub.pis2016.pis16.strikecom.engine.game.UpdateableComponent;

/**
 * A Component that defines how a GameObject will react to the stepping of the Game Simulation.
 * Extend this class and Override the {@code update(float delta)} method.
 *
 * @author German
 */
public abstract class BehaviorComponent extends Component implements UpdateableComponent {

	public abstract void update(float delta);

}
