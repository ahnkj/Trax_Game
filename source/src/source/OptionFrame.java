package source;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OptionFrame extends JFrame{
   Observer observer;
	
   JRadioButton guiButton1;
   JRadioButton guiButton2;
   JRadioButton guiButton3;
   ButtonGroup  guiButtonGroup;
   
   JRadioButton musicButton1;
   JRadioButton musicButton2;
   JRadioButton musicButton3;
   ButtonGroup  musicButtonGroup;
   
   OptionListener optionListner;
   
   
   static OptionFrame instance = null;
   
   private OptionFrame() {
      setTitle("Option");
      setLayout(null);
      setSize(250,300);
      setLocation(815, 0);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 해당 창만 종료됨
      
      observer=Observer.getInstance();
      optionListner=new OptionListener();

       guiButton1 = new JRadioButton("없음");
       guiButton1.setBounds(20,20,100,20);
       guiButton1.addActionListener(optionListner);
       //add(guiButton1);
      
       guiButton2 = new JRadioButton("마리오",true);
       guiButton2.setBounds(20,40,100,20);
       guiButton2.addActionListener(optionListner);
       add(guiButton2);
       
       guiButton3 = new JRadioButton("TRAX");
       guiButton3.setBounds(20,60,100,20);
       guiButton3.addActionListener(optionListner);
       add(guiButton3);
       
       guiButtonGroup= new ButtonGroup();
       guiButtonGroup.add(guiButton1);
       guiButtonGroup.add(guiButton2);
       guiButtonGroup.add(guiButton3);
       
       
       musicButton1 = new JRadioButton("없음", true);
       musicButton1.setBounds(20,120,100,20);
       musicButton1.addActionListener(optionListner);
       add(musicButton1);
       
       musicButton2 = new JRadioButton("마리오");
       musicButton2.setBounds(20,140,100,20);
       musicButton2.addActionListener(optionListner);
       add(musicButton2);
       
       musicButton3 = new JRadioButton("아이유-마음");
       musicButton3.setBounds(20,160,100,20);
       musicButton3.addActionListener(optionListner);
       add(musicButton3);
       
       musicButtonGroup= new ButtonGroup();
       musicButtonGroup.add(musicButton1);
       musicButtonGroup.add(musicButton2);
       musicButtonGroup.add(musicButton3);
       
       //리스너에 추가
    
       
       
   }
   public static OptionFrame getInstance() {
      if(instance == null) instance = new OptionFrame();
      return instance;
   }
    public void musicSelect() {
       setVisible(true);
    }

    public static void main(String [] args){
       new OptionFrame();
    }
    class OptionListener implements ActionListener { // 라디오 버튼을 누를 때 실행되는 리스너

      @Override
      public void actionPerformed(ActionEvent e) {
    	 Object obj=e.getSource();
         //JRadioButton radio = (JRadioButton)e.getSource();
    	 if(obj==(Object)guiButton1){
    		 observer.updataeGuiMode(null);
    	 }
    	 else if(obj==guiButton2){
    		 observer.updataeGuiMode("img\\marioPack\\");
    	 }
    	 else if(obj==guiButton3){
    		 observer.updataeGuiMode("img\\traxPack\\");
    	 }
    	 else if(obj==musicButton1) { 
            playMusic(0); // 리스너 호출한 라디오 버튼 라벨이 AAA...랑 같으면 1번 실행
         }
         else if(obj==musicButton2) {
            playMusic(1);
         }
         else if(obj==musicButton3) {
            playMusic(2);
         }
         
         
      }
       
    }

    public void playMusic(int select) {
       if(audio != null) // audio가 null이 아니라면 실행
          audio.myBgmEnd(); // 이전에 선택한 음악 정지
       if(select == 1)
          audio = new AudioPlay("sound/Land.wav"); // 오디오 플레이
       else if(select == 2)
         audio = new AudioPlay("sound/마음.wav"); // 오디오 플레이
    }
    AudioPlay audio;
}