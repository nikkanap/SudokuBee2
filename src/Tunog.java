import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// audio. self explanatory lol
public class Tunog{
	private Clip sample;
	private AudioInputStream audioInputStream;

	public Tunog(String fname) {
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(fname).getAbsoluteFile());
			sample = AudioSystem.getClip();
			sample.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  
	public void play() {
		sample.stop();
		sample.start();
	}

	public void stop(){
		sample.stop();
	}

	public void loop(){
		sample.stop();
		sample.loop(Clip.LOOP_CONTINUOUSLY);
	}
}