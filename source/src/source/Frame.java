package source;
import java.awt.*;
import javax.swing.JFrame;


//�� �гε��� �����ϱ� ���� Ŭ����
public class Frame extends JFrame {
   
      public Frame(){
      MenuPanel menuPanel;//�޴��� ����ϴ� ��ü
      GamePanel gamePanel;//������ ����ϴ� ��ü
      HeaderPanel headerPaenl;//����� ����ϴ� ��ü
      GameAi gameAi;
      Observer observer ;// �����Ǵ� ������ ����ϴ� ��ü 
      
      //observer ��ü ��������
      observer=Observer.getInstance();
      observer.initTurn();
      
      //����г� ���� �� ��ġ 
      headerPaenl =new HeaderPanel();
      headerPaenl.setBounds(0,0,800,124);
      this.getContentPane().add(headerPaenl);
      
      //�޴� ��� ���� �� ��ġ
      menuPanel = new MenuPanel();
      menuPanel.setBounds(0, 124, 200, 600);
      this.getContentPane().add(menuPanel);
      
      //���� �г� ���� �� ��ġ
      gamePanel = new GamePanel();
      gamePanel.setBounds(200,124,600,600);
      this.getContentPane().add(gamePanel);
      
      //���� �ΰ�����
      gameAi= new GameAi();
      

   
      }
   
   
}