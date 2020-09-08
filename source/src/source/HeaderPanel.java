package source;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// 단순히 제목이미지를 띄우기 위해 존재하는 패널
public class HeaderPanel extends JPanel {
   JLabel background;// 제목이지지를 담는 객체
   JFrame selectFrame;
   Observer observer;
  
   public HeaderPanel(){
      //선언된 객체 생성및 설정
      observer = Observer.getInstance();
      observer.setHeaderPanel(this); // 옵서버에 해당 객체 등록
      background= new JLabel();

      setGUI();
      
      background.setBounds(0, 0, 800, 124);
      this.add(background);
   }
   
   public void setGUI(){
	      background.setIcon(new ImageIcon(observer.getGuiMode()+"headerBackground2.png"));	 
	      repaint();
   }
   
   
}