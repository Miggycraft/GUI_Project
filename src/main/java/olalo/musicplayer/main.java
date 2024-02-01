/*
 * 
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package olalo.musicplayer;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;


/**
 *
 * @author Miggy Olalo
 * plz note because i use the default audio library the only it support are
 * wav format :(
 */
public class main extends javax.swing.JFrame {
    final String SUPPORTED_LISTS[] = {"wav"};
    String song_directory = "NULL";
    boolean isPaused = true;
    boolean newMusic = true;
    int lengthTextX;
    SongTimer st = new SongTimer();
    int musicFrame;
    AudioInputStream audioInput;
    Clip clip;
    Thread t;
    
    // initializing icons
    ImageIcon playIcon = getImage("/icons/play.png", 50, 50);
    ImageIcon leftIcon = getImage("/icons/left.png", 50, 50);
    ImageIcon rightIcon = getImage("/icons/right.png", 50, 50);
    ImageIcon pauseIcon = getImage("/icons/pause.png", 50, 50);
    ImageIcon stopIcon = getImage("/icons/stop.png", 25, 25);
    ImageIcon volumeMuteIcon = getImage("/icons/volume-mute.png", 25, 25);
    ImageIcon volumeLIcon = getImage("/icons/volume-low.png", 25, 25);
    ImageIcon volumeMIcon = getImage("/icons/volume-mid.png", 25, 25);
    ImageIcon volumeHIcon = getImage("/icons/volume-high.png", 25, 25);
    ImageIcon musicIcon = getImage("/icons/music-alt1.png", 100, 100);
    
    //
    
    public ImageIcon getImage(String p, int x, int y){ // just to clean this fuckign piece of shit image AAAAAAAAAAAAAAA!!!!
        ImageIcon temp = new ImageIcon(getClass().getResource(p));
        Image tempImage = temp.getImage();
        return new ImageIcon(tempImage.getScaledInstance(x, y, Image.SCALE_SMOOTH));
    }
    
    public void initIcons(){
//        Border empty = BorderFactory.createEmptyBorder(4,4,4,4);
//        
//        backButton.setBorder(empty);
//        backButton.setContentAreaFilled(false);
//        rightButton.setBorder(empty);
//        rightButton.setContentAreaFilled(false);
//        stopButton.setBorder(empty);
//        stopButton.setContentAreaFilled(false);
//        pauseButton.setBorder(empty);
//        pauseButton.setContentAreaFilled(false);
//        ugly
        
        backButton.setText("");
        backButton.setIcon(leftIcon);
        backButton.setFocusable(false);
        rightButton.setText("");
        rightButton.setIcon(rightIcon);
        rightButton.setFocusable(false);
        stopButton.setText("");
        stopButton.setIcon(stopIcon);
        stopButton.setFocusable(false);
        pauseButton.setText("");
        pauseButton.setIcon(playIcon);
        pauseButton.setFocusable(false);
        this.setIconImage(musicIcon.getImage());
        volumeLabel.setIcon(volumeHIcon);
    }
    
    /**
     * Creates new form main
     */
    public main() {
        initComponents();
        initIcons();
    }
    
    public String secondsToMinute(double i){
        int min = (int)(i / 60);
        int sec = (int)(i % 60);
        return min + ":" + sec;
    }
    
    public void updateVolumeIcon(){
        int currVol = volumeSlider.getValue();
        if (currVol > 67){
            volumeLabel.setIcon(volumeHIcon);
        }
        else if (currVol > 34){
            volumeLabel.setIcon(volumeMIcon);
        }
        else if (currVol > 1){
            volumeLabel.setIcon(volumeLIcon);
        }
        else{ 
            volumeLabel.setIcon(volumeMuteIcon);
        }
    }
    
    public void updateMaxLength(){
        String music_path = song_directory + "\\" + songList.getSelectedItem().toString();
        try {
            File musicFile = new File(music_path);
            
            if (musicFile.exists()){
                    audioInput = AudioSystem.getAudioInputStream(musicFile);
                    long frames = audioInput.getFrameLength();
                    AudioFormat format = audioInput.getFormat();
                    double seconds = (frames+0.0) / format.getFrameRate();
                    maxText.setText(secondsToMinute(seconds));
                    audioSlide.setMaximum((int)(seconds));
                }
        }
        catch(Exception x){
                    // meow
        }
    }
    
    public void pauseMusic(){
        String music_path = song_directory + "\\" + songList.getSelectedItem().toString();
        try {
            File musicFile = new File(music_path);
                
            if (musicFile.exists()){
                musicFrame = clip.getFramePosition();
                clip.stop();
                
            }
        }
        catch(Exception x){
            JOptionPane.showMessageDialog(null, "ERROR PAUSE!");
        }
    }
    
    public void setVolume(){
        if (clip == null){
            return;
        }
        float volVal = (float)(-80 + (.8 * volumeSlider.getValue()));
        FloatControl testLang = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        testLang.setValue(volVal);
    }
    
    public void playMusic(){
        String music_path = song_directory + "\\" + songList.getSelectedItem().toString();
        try {
            File musicFile = new File(music_path);
            
            if (musicFile.exists()){
                if (audioInput != null && !newMusic){ // add if changign music
                    clip.setFramePosition(musicFrame);
                    clip.start();
                }
                else{
                    newMusic = false;
                    audioInput = AudioSystem.getAudioInputStream(musicFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    setVolume();
                    clip.start();
                }
            }
        }
        catch(UnsupportedAudioFileException x){
            JOptionPane.showMessageDialog(null, "Unsupported file, WAV only :(");
        }
        catch(Exception x){
            JOptionPane.showMessageDialog(null, "ERROR PLAY!");
        }
    }
    
    public boolean isValid(String s){
        for (int i = 0; i < SUPPORTED_LISTS.length; i++){
            if (s.equals(SUPPORTED_LISTS[i]))
                return true;
        }
        return false;
    }
    
    public void moveList(boolean toRight){
        int select = songList.getSelectedIndex();
        int maxSize = songList.getModel().getSize();
        
        if (!newMusic)
            clip.stop();
        
        if (maxSize == 0)
            return;
        
        if (toRight){ // +1
            if (select+1 < maxSize){
                select += 1;
            }
            else{ // reset to 0
                select = 0;
            }
        }
        else{ // -1
            if (select > 0)
                select -= 1;
            else
                select = maxSize -1;
        }
        
        songList.setSelectedIndex(select);
    }
    
    public void resetPauseButton(){
        newMusic = true;
        isPaused = true;
        pauseButton.setText(""); //play
        pauseButton.setIcon(playIcon);
        String music_path = song_directory + "\\" + songList.getSelectedItem().toString();
        try{
            /*
            the first clip.stop() stops whatever the current clip is located to
            then it opens the new clip via path directory and then stops it as well
            what the fuck?  
            */
            clip.stop();
            File musicFile = new File(music_path);
            if (musicFile.exists()){
                audioInput = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.stop();
            }
        }
        catch(Exception x){ // meow
            System.out.println(x);
        }
    }
    
    public void updatePauseButton(){    
        File f = new File(song_directory);
        if (!f.exists())
            return;
        
        if (isPaused){ 
            isPaused = false;
            playMusic();
            pauseButton.setText(""); // pause
            pauseButton.setIcon(pauseIcon);
            st.stopCount();
        }
        else{
            isPaused = true;
            pauseMusic();
            pauseButton.setText(""); // play
            pauseButton.setIcon(playIcon);
            t = new Thread(st);
            t.run();
        }
    }
    
    public void moveAudioSection(){
        clip.setFramePosition(musicFrame);
        clip.start();
    }
    
    public void updateSonglist(String path){
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        audioSlide = new javax.swing.JSlider();
        minText = new javax.swing.JLabel();
        maxText = new javax.swing.JLabel();
        lengthText = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        songList = new javax.swing.JComboBox<>();
        midPanel = new javax.swing.JPanel();
        pauseButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        rightButton = new javax.swing.JButton();
        rightPanel = new javax.swing.JPanel();
        volumeSlider = new javax.swing.JSlider();
        volumeLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        directoryItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Music Player App");
        setResizable(false);

        audioSlide.setPaintLabels(true);
        audioSlide.setPaintTicks(true);
        audioSlide.setToolTipText("");
        audioSlide.setValue(0);
        audioSlide.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                audioSlideStateChanged(evt);
            }
        });

        minText.setText("0:00");

        maxText.setText("0:00");

        lengthText.setText("0:00");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(audioSlide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(topPanelLayout.createSequentialGroup()
                                .addComponent(lengthText)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addComponent(minText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(maxText)
                        .addGap(21, 21, 21))))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topPanelLayout.createSequentialGroup()
                .addContainerGap(177, Short.MAX_VALUE)
                .addComponent(lengthText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(audioSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxText)
                    .addComponent(minText))
                .addContainerGap())
        );

        songList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                songListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(songList, 0, 130, Short.MAX_VALUE)
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(songList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 78, Short.MAX_VALUE))
        );

        pauseButton.setText("play");
        pauseButton.setMaximumSize(new java.awt.Dimension(100, 100));
        pauseButton.setMinimumSize(new java.awt.Dimension(100, 100));
        pauseButton.setPreferredSize(new java.awt.Dimension(100, 100));
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        backButton.setText("back");
        backButton.setMaximumSize(new java.awt.Dimension(100, 100));
        backButton.setMinimumSize(new java.awt.Dimension(100, 100));
        backButton.setPreferredSize(new java.awt.Dimension(100, 100));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        rightButton.setText("right");
        rightButton.setMaximumSize(new java.awt.Dimension(100, 100));
        rightButton.setMinimumSize(new java.awt.Dimension(100, 100));
        rightButton.setPreferredSize(new java.awt.Dimension(100, 100));
        rightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout midPanelLayout = new javax.swing.GroupLayout(midPanel);
        midPanel.setLayout(midPanelLayout);
        midPanelLayout.setHorizontalGroup(
            midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(midPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        midPanelLayout.setVerticalGroup(
            midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(midPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rightButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        volumeSlider.setPaintLabels(true);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setValue(100);
        volumeSlider.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                volumeSliderAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeSliderStateChanged(evt);
            }
        });

        volumeLabel.setText("100");
        volumeLabel.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                volumeLabelAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        stopButton.setText("stop");
        stopButton.setMaximumSize(new java.awt.Dimension(75, 75));
        stopButton.setMinimumSize(new java.awt.Dimension(75, 75));
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(volumeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(volumeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(midPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        fileMenu.setText("File");

        directoryItem.setText("Open Directory");
        directoryItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directoryItemActionPerformed(evt);
            }
        });
        fileMenu.add(directoryItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Help");

        aboutItem.setText("About");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        // TODO add your handling code here:
        System.out.println(newMusic);
        updatePauseButton();
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void volumeLabelAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_volumeLabelAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_volumeLabelAncestorMoved

    private void volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeSliderStateChanged
        // TODO add your handling code here:
        setVolume();
        volumeLabel.setText(volumeSlider.getValue() + "");
        updateVolumeIcon();
    }//GEN-LAST:event_volumeSliderStateChanged

    private void volumeSliderAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_volumeSliderAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_volumeSliderAncestorMoved

    private void songListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_songListActionPerformed
        // TODO add your handling code here:
        resetPauseButton();
        updateMaxLength();
    }//GEN-LAST:event_songListActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
        resetPauseButton();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void rightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightButtonActionPerformed
        // TODO add your handling code here:
        moveList(true);
    }//GEN-LAST:event_rightButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        moveList(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutItemActionPerformed
        // TODO add your handling code here:
        String message = 
                "Written By: Manuel A. Olalo Jr.\n"
                + "CJ Narvaez\n"
                + "Leusor Ethan Dulzo\n"
                + "Mary Joyce Gloria";
        JOptionPane.showMessageDialog(this, message, "About Us", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutItemActionPerformed

    private void directoryItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directoryItemActionPerformed
        // TODO add your handling code here:
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.showOpenDialog(null);
        updateSonglist(f.getSelectedFile().toString());
        updateMaxLength();
    }//GEN-LAST:event_directoryItemActionPerformed

    private void audioSlideStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_audioSlideStateChanged
        // TODO add your handling code here:
        try{
            AudioFormat format = audioInput.getFormat();
            int frames = (int)(audioSlide.getValue() * (format.getFrameRate()));
            lengthText.setText(secondsToMinute(audioSlide.getValue()));
            musicFrame = frames;
            moveAudioSection(); // TODO: FIX SHIT!
        }
        catch(Exception x){
            // meow
        }
    }//GEN-LAST:event_audioSlideStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                System.out.println(info.getName());
//                if ("Metal".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        UIManager.setLookAndFeel(new FlatLightLaf());
        }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
            catch (Exception x){
                    // im tired chief...
          }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JSlider audioSlide;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JMenuItem directoryItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel lengthText;
    private javax.swing.JLabel maxText;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel midPanel;
    private javax.swing.JLabel minText;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton rightButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JComboBox<String> songList;
    private javax.swing.JButton stopButton;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel volumeLabel;
    private javax.swing.JSlider volumeSlider;
    // End of variables declaration//GEN-END:variables
}
