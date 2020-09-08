package source;

import java.awt.event.*;
import javax.swing.*;

//�޴����� �Ͼ�� �̺�Ʈ�� ó���ϱ� ���� Ŭ���� 
public class MenuEventListener implements MouseListener {
   
   Observer observer;//������ �����ϴ� ��ü
   private MenuPanel menuPanel;//�޴��� ����ϴ� ��ü
   OptionFrame optioinFrame;
   Save save; // ���� �ҷ����� �����ϱ�
   GamePlayPanel gamePlayPanel;
   
   public MenuEventListener(MenuPanel menuPanel){
      //����� ��ü ����
      observer = Observer.getInstance();
      optioinFrame = OptionFrame.getInstance();
      save = Save.getInstance();
      this.menuPanel=menuPanel;
      gamePlayPanel = observer.getGamePlayPanel();
   }
   //���� ��ư�� ���� ���� ���õ� �������� �������ִ� �Լ� 
   public void mouseClicked(MouseEvent e) {
      Object obj= new Object();
      obj=e.getSource();
      int type=menuPanel.getBlockType();
      	 /*
         if(obj==menuPanel.getLeftButton()){
            observer.updateMenuBlockType((type+5)%6);
         }
         else if(obj==menuPanel.getRightButton()){
            observer.updateMenuBlockType((type+1)%6);
         }
         */
         if(obj==menuPanel.getPlayButton()){
        	 if(e.getButton() == 3)new Restart();
        	 else save.riseWindow();
         }
         else if(obj==menuPanel.getHelpButton()){
            //new HelpFrame();
        	 observer.ai();
         }
         else if(obj==menuPanel.getOptionButton()) {
        	 optioinFrame.musicSelect();
         }
         else if(obj==menuPanel.getExitButton()) {
        	 gamePlayPanel = observer.getGamePlayPanel();
        	 observer.setTurn(!observer.getTurn());
        	 gamePlayPanel.restoreOneStep();
         }

   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mouseExited(MouseEvent e) {
   }

   public void mousePressed(MouseEvent e) {
   }

   public void mouseReleased(MouseEvent e) {
   }

}