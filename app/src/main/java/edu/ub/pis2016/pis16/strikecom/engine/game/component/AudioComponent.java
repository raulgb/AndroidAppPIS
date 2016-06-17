package edu.ub.pis2016.pis16.strikecom.engine.game.component;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.framework.audio.Sound;
import edu.ub.pis2016.pis16.strikecom.engine.game.Component;

public class AudioComponent extends Component {

	private HashMap<String, Sound> sounds;

	public void playSound(String soundName, float volume){
		sounds.get(soundName).play(volume);
	}

}
