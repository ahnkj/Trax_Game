package source;

import java.awt.*;
import javax.swing.*;
import java.util.Date;

// 메뉴를 담당하는 패널
public class MenuPanel extends JPanel{
  // private JButton left,right;//파이프를 선택할 때 쓰이는 버튼들
   private JButton play,help,option,exit;//여러 기는을 담당하는 버튼 아직 정확한 기는을 정하지는 않음
   private Image background; //배경이미지를 담기위한 객체
   private Image blockFrame; //선택된 이미지에 틀을 씌우기 우한 이미지를 담는 객체
   private Image[][] pipeImage;//파이프이미지를 담기위한 객체
   private JTextArea messageArea; // 현재 상태와 관련된 정보를 띄우기 위한 객체
   private Observer observer;// 정보를 종합하여 관리하는 객체
   private Toolkit toolKit; // 이미지를 로딩하기 위한 객체
   private MenuEventListener menuEventListener; //메뉴에서 일어나는 이벤트를 처리하기 위한 객체
   private int blockType; // 현재 선택된 블락의 종류를 담고 있는 객체 
   private JScrollPane scrollPane;
   private int drawFlag = 1;
   private String guiMode;
   
   
   public  MenuPanel(){
   
   this.setLayout(null);// 배치를 마음대로 
   
   observer=Observer.getInstance();
   observer.setMenuPanel(this);
   blockType=0;
   
   guiMode=observer.getGuiMode();
   toolKit = Toolkit.getDefaultToolkit();
  
   //아래의 부분들은 객체를 생성및 위치를 정하는 내용을 담고 있음
   menuEventListener= new MenuEventListener(this);
   this.addMouseListener(menuEventListener);
   
   /*
   //메뉴 패널 버튼 각각 설정
   left = new JButton();
   left.setContentAreaFilled(false);// 빈공간 채우기 설정
   left.setBorderPainted(false); //경계 색칠 설정
   left.setBounds(10,25,50,50);
   left.addMouseListener(menuEventListener);
   add(left);

   right = new JButton();
   right.setContentAreaFilled(false);// 빈공간 채우기 설정
   right.setBorderPainted(false); //경계 색칠 설정
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
   // 텍스트 에리어에 스크롤팬 추가
   
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

      g.drawImage(background, 0, 0, 200, 600, this);//메뉴의 배경이미지를 그려줌
    //  g.drawImage(blockFrame, 65, 15, 70, 70, this);//현재 선택된 이미지를 감싸는 틀을 그려줌
     //g.drawImage(pipeImage[0][blockType], 70,20,60,60, this);// 현재 선택된 블락을 그려줌 
   }
   
   // 선언된 변수에 대한 설정이나 전달을 하기 위한 함수들 
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
	   
	   if(gameState==1)str="("+(y+1)+","+(x+1)+")"+"에 "+bType+"을 두었습니다.";
	   else if(gameState==2)str=turn1+"가 승리 하였습니다.";
	   else if(gameState==3)str=turn2+"가 승리 하였습니다.";
	   else if(gameState==4)str="이미 블록이 존재합니다.";
	   else if(gameState==5)str="주변에 블록이 존재하지 않습니다";
	   else if(gameState==6)str="연결할 수 없는 블록 종류입니다.";
	   else if(gameState==7)str="Illegal Turn 입니다.";
	   
	   messageArea.append("["+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]"+turn1+"\n");
	   messageArea.append(str+"\n\n");
	   
   }
   

}