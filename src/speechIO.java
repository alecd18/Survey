import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class speechIO extends abstractIO {
	
	private VoiceManager manager;
	private String s;
	private String voiceName;

	public speechIO(String voiceName) {
		manager = VoiceManager.getInstance();
		this.voiceName = voiceName;
		s = "";
	}
	
	@Override
	public void outputMessage(String message) {
		s += message;
		speak();
	}

	public void speak() {
		if (s == null) {
			s = "";
		}
		
		Voice v = manager.getVoice(voiceName);
		v.allocate();
		v.speak(s);
		v.deallocate();
		
		s = "";
	}
}
