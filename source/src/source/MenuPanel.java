package source;

import java.awt.*;
import javax.swing.*;
import java.util.Date;

// �޴��� ����ϴ� �г�
public class MenuPanel extends JPanel{
  // private JButton left,right;//�������� ������ �� ���̴� ��ư��
   private JButton play,help,option,exit;//���� ����� ����ϴ� ��ư ���� ��Ȯ�� ����� �������� ����
   private Image background; //����̹����� ������� ��ü
   private Image blockFrame; //���õ� �̹����� Ʋ�� ����� ���� �̹����� ��� ��ü
   private Image[][] pipeImage;//�������̹����� ������� ��ü
   private JTextArea messageArea; // ���� ���¿� ���õ� ������ ���� ���� ��ü
   private Observer observer;// ������ �����Ͽ� �����ϴ� ��ü
   private Toolkit toolKit; // �̹����� �ε��ϱ� ���� ��ü
   private MenuEventListener menuEventListener; //�޴����� �Ͼ�� �̺�Ʈ�� ó���ϱ� ���� ��ü
   private int blockType; // ���� ���õ� ����� ������ ��� �ִ� ��ü 
   private JScrollPane scrollPane;
   private int drawFlag = 1;
   private String guiMode;
   
   
   public  MenuPanel(){
   
   this.setLayout(null);// ��ġ�� ������� 
   
   observer=Observer.getInstance();
   observer.setMenuPanel(this);
   blockType=0;
   
   guiMode=observer.getGuiMode();
   toolKit = Toolkit.getDefaultToolkit();
  
   //�Ʒ��� �κе��� ��ü�� ������ ��ġ�� ���ϴ� ������ ��� ����
   menuEventListener= new MenuEventListener(this);
   this.addMouseListener(menuEventListener);
   
   /*
   //�޴� �г� ��ư ���� ����
   left = new JButton();
   left.setContentAreaFilled(false);// ����� ä��� ����
   left.setBorderPainted(false); //��� ��ĥ ����
   left.setBounds(10,25,50,50);
   left.addMouseListener(menuEventListener);
   add(left);

   right = new JButton();
   right.setContentAreaFilled(false);// ����� ä��� ����
   right.setBorderPainted(false); //��� ��ĥ ����
   right.setBounds(140,25,50,50);
   right.addMouseListener(menuEventListener);
   add(right);
   */

	   
   play = new JButton();
   play.setBounds(10,100,80,80);
   play.addMouseListener(menuEventListener);
   add(play);
	   

   option = new JButton();
   option.setBounds(110,100,80,80);
   option.addMouseListener(menuEventListener);
   add(option);

   help = new JButton();
   help.setBounds(10,200,80,80);
   help.addMouseListener(menuEventListener);
   add(help);

   exit = new JButton();
   exit.setBounds(110,200,80,80);
   exit.addMouseListener(menuEventListener);
   add(exit);
   
   setGUI();
   
   
   messageArea = new JTextArea();
   observer.setTextArea(messageArea);
   messageArea.setEditable(false);
   // �ؽ�Ʈ ����� ��ũ���� �߰�
   
   scrollPane = new JScrollPane();
   scrollPane.setBounds(10, 295, 180, 150);
   scrollPane.setViewportView(messageArea);
   scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
   add(scrollPane);
   
   

   }
   public void restoreAdd() {
      //add(left);
      //add(right);
      //add(play);
      //add(option);
      //add(help);
      //add(exit);
      add(scrollPane);
   }
   public void paintComponent(Graphics g){
      super.paintComponent(g);

      g.drawImage(background, 0, 0, 200, 600, this);//�޴��� ����̹����� �׷���
    //  g.drawImage(blockFrame, 65, 15, 70, 70, this);//���� ���õ� �̹����� ���δ� Ʋ�� �׷���
     //g.drawImage(pipeImage[0][blockType], 70,20,60,60, this);// ���� ���õ� ����� �׷��� 
   }
   
   // ����� ������ ���� �����̳� ������ �ϱ� ���� �Լ��� 
   //setter
   
   public void setGUI(){
	   
	   String guiMode=observer.getGuiMode();
	   background = toolKit.getImage(guiMode+"menuBackground.png");
	   blockFrame = toolKit.getImage(guiMode+"arrowFrame.png");
	   
	   /*
	   left.setIcon(new ImageIcon(guiMode+"left.png"));
	   left.setPressedIcon(new ImageIcon(guiMode+"pressedLeft.png"));
	   right.setIcon(new ImageIcon(guiMode+"right.png"));
	   right.setPressedIcon(new ImageIcon(guiMode+"pressedRight.png"));
	   */
	   play.setIcon(new ImageIcon(guiMode+"play.jpg"));
	   option.setIcon(new ImageIcon(guiMode+"option.jpg"));
	   help.setIcon(new ImageIcon(guiMode+"help.jpg"));
	   exit.setIcon(new ImageIcon(guiMode+"exit.jpg"));
	   
	   pipeImage=observer.getPipeImage();
	   
	   repaint();
	   
   }

   public void setDrawFlag(int flag) {
      drawFlag = flag;
   }
   // getter
   public void setBlockType(int type){
	   this.blockType=type;
   }
   public int getBlockType(){
      return blockType;
   }
   
   /*
   public JButton getLeftButton(){
      return left;
   }
   
   public JButton getRightButton(){
      return right;
   }
   */
   
   public JButton getOptionButton(){
      return option;
   }
   
   public JButton getPlayButton(){
      return play;
   }
   
   public JButton getHelpButton(){
      return help;
   }
   
   public JButton getExitButton(){
      return exit;
   }
   public Image getBackGround() {
      return  background;
   }
   
   public void printLog(int x, int y ,int gameState,int blockType){
	   
	   String turn1,turn2,str,bType;
	   Date date=new Date();
	   if(observer.getTurn()){turn1="Green(white)";turn2="Red(Black)";}
	   else {turn2="Green(White)";turn1="Red(Black)";}
	   str="";
	   bType="";
	   
	   if(blockType==0||blockType==3)bType="/";
	   else if(blockType==1||blockType==4)bType="+";
	   else if(blockType==2||blockType==5)bType="\\";
	   
	   if(gameState==1)str="("+(y+1)+","+(x+1)+")"+"�� "+bType+"�� �ξ����ϴ�.";
	   else if(gameState==2)str=turn1+"�� �¸� �Ͽ����ϴ�.";
	   else if(gameState==3)str=turn2+"�� �¸� �Ͽ����ϴ�.";
	   else if(gameState==4)str="�̹� ����� �����մϴ�.";
	   else if(gameState==5)str="�ֺ��� ����� �������� �ʽ��ϴ�";
	   else if(gameState==6)str="������ �� ���� ��� �����Դϴ�.";
	   else if(gameState==7)str="Illegal Turn �Դϴ�.";
	   
	   messageArea.append("["+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]"+turn1+"\n");
	   messageArea.append(str+"\n\n");
	   
   }
   

}