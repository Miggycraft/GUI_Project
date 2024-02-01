package olalo.musicplayer;

import javax.swing.JSlider;

public class SongTimer implements Runnable{
    JSlider audioSlider;
    boolean running = true;
    int i = 0;
    
    public SongTimer(JSlider audioSlider){
        this.audioSlider = audioSlider;
    }
    
    @Override
    public void run() {
        running = true;
        while (running){
            try{
                Thread.sleep(1000);
                i++;
                updateSlider();
            }
            catch(Exception x){
                System.out.println(x);
            }
        }
    }
    
    public void stopCount(){
        running = false;
    }
    public void startCount(){
        running = true;
    }
    
    public void updateSlider(){
        audioSlider.setValue(i);
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
