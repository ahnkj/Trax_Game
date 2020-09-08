package source;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// �ܼ��� �����̹����� ���� ���� �����ϴ� �г�
public class HeaderPanel extends JPanel {
   JLabel background;// ������������ ��� ��ü
   JFrame selectFrame;
   Observer observer;
  
   public HeaderPanel(){
      //����� ��ü ������ ����
      observer = Observer.getInstance();
      observer.setHeaderPanel(this); // �ɼ����� �ش� ��ü ���
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