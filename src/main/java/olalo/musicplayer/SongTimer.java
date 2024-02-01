package olalo.musicplayer;

public class SongTimer implements Runnable{
    boolean running = true;
    int i = 0;
    
    
    @Override
    public void run() {
        while (running){
            try{
                Thread.sleep(1000);
                System.out.println(++i);                
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
    
    public void resetCount(){
        i = 0;
    }
    
    public int getCount(){
        return i;
    }
    
    public void setCount(int i){
        this.i = i;
    }
    
}
