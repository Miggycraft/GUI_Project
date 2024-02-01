package olalo.musicplayer.to_be_deleted;

import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class MusicPlayer {
    Music music = new Music();
    Thread t;
    String song_directory = "NULL";
    final String SUPPORTED_LISTS[] = {"wav"};
    
    public void stopMusic(){
        
    }
    
    public String getSongPath(JComboBox songList){
        return song_directory +"\\"+ songList.getSelectedItem().toString();
    }
    
    public boolean playMusic(JComboBox songList){
        File f = new File(getSongPath(songList));

        if (!f.exists())
            return false;
        music.updateSongPath(getSongPath(songList));
        t = new Thread(music);
        t.start();
        
        return true;
    }
    
    public boolean isValid(String s){
        for (int i = 0; i < SUPPORTED_LISTS.length; i++){
            if (s.equals(SUPPORTED_LISTS[i]))
                return true;
        }
        return false;
    }
    
    public void updateSonglist(String path, JComboBox songList){ //cursed.
        ArrayList<String> songs = new ArrayList();
        int len;
        File f = new File(path);
        song_directory = path;
        for (String s: f.list()){ // could add more guard clauses but this works for now
            len = s.length();
            String temp = s.substring(len - 3);
            if (isValid(temp)){
                songs.add(s);
            }
        }
        songList.setModel(new DefaultComboBoxModel(songs.toArray()));
    }
}
