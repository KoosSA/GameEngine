package engine.io;

import com.koossa.logger.Log;

public interface IInputHandler {
	
	default void registerInputHandler() {
		Log.info(getClass(), "Register as input handler.");
		GameInput.inputHandlers.add(this);
	}
	
	void handleInput(float delta, GameInput input);

}
