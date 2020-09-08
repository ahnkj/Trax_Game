package source;
import java.awt.*;
import javax.swing.JFrame;


//각 패널들을 종합하기 위한 클래스
public class Frame extends JFrame {
   
      public Frame(){
      MenuPanel menuPanel;//메뉴를 담당하는 객체
      GamePanel gamePanel;//게임을 담당하는 객체
      HeaderPanel headerPaenl;//헤더를 담당하는 객체
      GameAi gameAi;
      Observer observer ;// 공유되는 정보를 담당하는 객체 
      
      //observer 객체 가져오기
      observer=Observer.getInstance();
      observer.initTurn();
      
      //헤더패널 생성 및 배치 
      headerPaenl =new HeaderPanel();
      headerPaenl.setBounds(0,0,800,124);
      this.getContentPane().add(headerPaenl);
      
      //메뉴 페널 생성 및 배치
      menuPanel = new MenuPanel();
      menuPanel.setBounds(0, 124, 200, 600);
      this.getContentPane().add(menuPanel);
      
      //게임 패널 생성 및 배치
      gamePanel = new GamePanel();
      gamePanel.setBounds(200,124,600,600);
      this.getContentPane().add(gamePanel);
      
      //게임 인공지능
      gameAi= new GameAi();
      

   
      }
   
   
}