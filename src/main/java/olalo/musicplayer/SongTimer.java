package olalo.musicplayer;

import javax.swing.JLabel;
import javax.swing.JSlider;

public class SongTimer implements Runnable{
    JSlider audioSlider;
    JLabel text;
    boolean running = true;
    int i = 0;
    
    public SongTimer(JSlider audioSlider, JLabel text){
        this.audioSlider = audioSlider;
        this.text = text;
    }
    
    @Override
    public void run() {
        running = true;
        while (running){
            try{
                updateSlider();
                Thread.sleep(1000);
                i++;
            }
            catch(Exception x){
                System.out.println(x);
            }
        }
    }
    
    public String secondsToMinute(int x){
        int min = (int)(x / 60);
        int sec = (int)(x % 60);
        if (sec < 10){
            return min + ":0" + sec;
        }
        return min + ":" + sec;
    }
    
    public void stopCount(){
        running = false;
    }
    public void startCount(){
        running = true;
    }
    
    public void updateSlider(){
        audioSlider.setValue(i);
        text.setText(secondsToMinute(i));
    }
    
    public void resetCount(){
        i = 0;
        running = false;
    }
    
    public int getCount(){
        return i;
    }
    
    public void setCount(int i){
        this.i = i;
    }
    
}
