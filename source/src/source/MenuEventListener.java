package source;

import java.awt.event.*;
import javax.swing.*;

//메뉴에서 일어나는 이벤트를 처리하기 위한 클래스 
public class MenuEventListener implements MouseListener {
   
   Observer observer;//정보를 종합하는 객체
   private MenuPanel menuPanel;//메뉴를 담다하는 객체
   OptionFrame optioinFrame;
   Save save; // 저장 불러오기 리셋하기
   GamePlayPanel gamePlayPanel;
   
   public MenuEventListener(MenuPanel menuPanel){
      //선언된 객체 생성
      observer = Observer.getInstance();
      optioinFrame = OptionFrame.getInstance();
      save = Save.getInstance();
      this.menuPanel=menuPanel;
      gamePlayPanel = observer.getGamePlayPanel();
   }
   //눌린 버튼에 따라 현재 선택된 파이프를 변경해주는 함수 
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