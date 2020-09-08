package source;

import java.awt.*;
import java.util.*;
import javax.swing.*;

//게임에 있어서 공유되야 되는 모든 정보를 담고 있는 클래스
//공유되는 정보가 변경되면 옵저버의 정보도 꼭 같이 변경해 주어야 함
//필요한 정보가 있으면 어느 패널에서든 옵저버에서 함수로 객체를 얻어 가면 된다.
//이클래스는 getInstace 함수를 통해 객체를 받아 올 수 있고 선언된 변수 및 객체는 공유된다.
public class Observer {
   protected static Observer instance; //유일하게 존재하는 Observer 객체를 담기위한 객체
   protected static Image[][] pipeImage; //원래 게임의 track 역할을하는 이미지를 담는 객체
   protected static Toolkit toolKit; // 이미지를 로딩할 때 쓰이는 객체

   boolean[][] blockPipeType; //파이프의 종류에 따라 모양을 수치적으로 저장한 배열 
                        //ex)십자가 모양일 1010(위/오른쪽/아래/왼쪽, 1은 초록파이프, 0은 빨간 파이프) 
   boolean turn,vflag;
   int gameState; //현재 게임의 상태를 저장하기 위한 변수, 누구의 턴이지 나타내기 위한 변수 
   int menuBlockType; // 현재 메누에서 선택된 파이프의 종류를 담고 있는 변수 
   int pipePicture = 1;
   String guiMode;
   //Frame frame; // 
   GamePanel gamePanel;//게임 패널을 담기 위한 변수
                  //변경된 정보를 게임패널에 전달하면 게임패널에서 필요에 따라 게임플레이 패널로 전달하도록 함
   GamePlayPanel gamePlayPanel;//게임의 진행이 진행되는 패널
   MenuPanel menuPanel; // 메뉴의 기능을 담당하는 패널
   JTextArea textArea;
   HeaderPanel headerPanel;
   GameAi gameAi;

   
   
   protected Observer(){
      // 선언된 변수들 생성 및 초기화
      toolKit = Toolkit.getDefaultToolkit();
      guiMode="img\\marioPack\\";
      setPipeImage(); // 파이프 그림 설정
      if(pipeImage[0][0]==null)System.out.println("images are not loaded");

      blockPipeType = new boolean[6][4];
      for(int i=0;i<6;++i)for(int j=0;j<4;++j)blockPipeType[i][j]=false;
      blockPipeType[0][1]=blockPipeType[0][2]=true; // false빨강, true은파랑 
      blockPipeType[3][3]=blockPipeType[3][0]=true; // 0~3 위부터 시계방향
      blockPipeType[1][1]=blockPipeType[1][3]=true;
      blockPipeType[4][0]=blockPipeType[4][2]=true;
      blockPipeType[2][2]=blockPipeType[2][3]=true;
      blockPipeType[5][0]=blockPipeType[5][1]=true;
         
      gameState=0; // 현재 게임 상태
      turn=true; // 누구의 턴인지
      vflag=false;
      menuBlockType=0; // 현재 메뉴에서 선택된 파이프 종류
   }
   
   public boolean getVflag(){
	   return vflag;
   }
   public boolean getPipeType(int type, int direction){   
      return blockPipeType[type][direction];
   }
   //옵저버 객체를 하나로 공유하게 해주는 함수 // 싱글톤 객체
   public static Observer getInstance(){
      if(instance==null)instance = new Observer();
      return instance;
   }
   public int getMenuBlockType(){
      return menuBlockType;
   }
   public Image[][] getPipeImage(){
      return pipeImage;
   }
   public JTextArea getTextArea() {
      return textArea;
   }
   
   public boolean getTurn(){
	   return turn;
   }
   
   public boolean getGamePalyEmpty()
   {
	   return gamePlayPanel.isEmpty();
   }
   public HeaderPanel getHeaderPanel() {
      return headerPanel;
   }
   public MenuPanel getMenuPanel() {
      return menuPanel;
   }
   public GamePlayPanel getGamePlayPanel() {
      return gamePlayPanel;
   }
   public String getGuiMode(){ // 이미지 폴더 
	   return guiMode;
   }
   public void initTurn()
   {
	   turn=true;
   }
   
   public void updateGameState(int x, int y,int gameState,int blockType)
   {
	   
	   menuPanel.printLog(x, y, gameState,blockType);
	   if(gameState<2)turn=!turn;
	      
   }
   public void updateGameAi(int[][] blockArray,ArrayList<PointState> mdfList)
   {
	   System.out.println("updateGameAi1");
	   gameAi.updateCaseList(blockArray, mdfList);
	   System.out.println("updateGameAi2");
   }
   //파이프 선택 버튼이 눌렸을 때 변경된 파이프 종류를 메뉴에 반영하기 위한 함수(삭제 예정)
   public void updateMenuBlockType(int type){
      menuBlockType=type;
      menuPanel.setBlockType(menuBlockType);
      menuPanel.repaint();
   }
   
   public void updataeGuiMode(String str){
	   this.guiMode=str;
	   setPipeImage();
	   headerPanel.setGUI();
	   menuPanel.setGUI();
	   gamePlayPanel.setGUI();
   }
   
   public void setVflag(boolean flag){
	   this.vflag=flag;
   }
   
   public void setPipeImage() {
  
        pipeImage = new Image[2][6];
        String str;
        for(int i=0;i<6;++i){
        str=guiMode+"pipe"+(i+1)+".jpg";
        System.out.println(str);
        pipeImage[0][i]=toolKit.getImage(str);
        str=guiMode+"pipe"+(i+7)+".jpg";
        pipeImage[1][i]=toolKit.getImage(str);
        }

   }
   
   public void setGameAi(GameAi gameAi)
   {
	   this.gameAi=gameAi;
   }
   
   public void setGamePanel(GamePanel gamePanel){
      this.gamePanel= gamePanel;
   }
   
   public void setGamePlayPanel(GamePlayPanel gamePlayPanel){
      this.gamePlayPanel=gamePlayPanel;
   }
   
   public void setMenuPanel(MenuPanel menuPanel){
      this.menuPanel= menuPanel;
   }
   public void setTextArea(JTextArea textArea) {
      this.textArea = textArea;
      
   }
   
   public void setTurn(boolean turn)
   {
	   this.turn=turn;
   }
   public void setHeaderPanel(HeaderPanel headerPanel) {
      this.headerPanel = headerPanel;
   }
   
   public void ai()
   {
	   PointState block;
	   int dg;
	   int[][] blockArray=gamePlayPanel.getBlockArray();
	   if(gameAi==null)gameAi=new GameAi();
	   block = gameAi.aiDecision(gamePlayPanel.getBlockArray(), turn);
	   System.out.println("block.x::"+block.x+"  ,block.y::"+block.y+"  state"+block.state);
	   
	  // for(int i=0;i<20;++i)System.out.println(""+block.x+"  "+block.y+"  "+block.state);
	   dg=gamePlayPanel.addBlock(block,true);
   }

   
}