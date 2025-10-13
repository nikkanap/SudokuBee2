import java.applet.AudioClip;
import java.net.MalformedURLException;

public class Tunog{
	private AudioClip sample;
	Tunog(String fname){
		try{
			sample = java.applet.Applet.newAudioClip(new java.net.URL("file:"+fname));
		    }
		 catch(MalformedURLException e){
			 }
		}
	public void play(){
		sample.stop();
		sample.play();
		}
	public void stop(){
		sample.stop();
		}
	public void loop(){
		sample.stop();
		sample.loop();
		}
	}