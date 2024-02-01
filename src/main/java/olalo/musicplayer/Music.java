package olalo.musicplayer;

import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Music implements Runnable {
    boolean newMusic = true;
    AudioInputStream audioInput;
    Clip clip;
    boolean running = true;
    String song_path = "NULL";
    
    public void updateSongPath(String s){
        song_path = s;
    }
    
    
    @Override
    public void run() { //play music
        try{
            int i = 0;
            System.out.println("debug " + i);
            while(running){
                i += 1;
                File musicFile = new File(song_path);
                if (musicFile.exists()){
                    if (audioInput != null && !newMusic){ // add if changign music
                        clip.start();
                    }
                    else{
                        newMusic = false;
                        audioInput = AudioSystem.getAudioInputStream(musicFile);
                        clip = AudioSystem.getClip();
                        clip.open(audioInput);
                        clip.start();
                        System.out.println("called");
                    }
                }             
            }
            
            clip.stop(); // todo: add edgecase (what if musicfile false?)
        }
        catch(Exception x){
            x.printStackTrace();
        }
        
    }
    
     public void setVolume(int val){ // volume slider
        if (clip == null){
            return;
        }
        float volVal = (float)(-80 + (.8 * val));
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(volVal);
    }
    
    public void terminate(){
        running = false;
    }
    
    public void restart(){
        running = true;
    }
}
