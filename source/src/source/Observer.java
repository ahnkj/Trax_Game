package source;

import java.awt.*;
import java.util.*;
import javax.swing.*;

//���ӿ� �־ �����Ǿ� �Ǵ� ��� ������ ��� �ִ� Ŭ����
//�����Ǵ� ������ ����Ǹ� �������� ������ �� ���� ������ �־�� ��
//�ʿ��� ������ ������ ��� �гο����� ���������� �Լ��� ��ü�� ��� ���� �ȴ�.
//��Ŭ������ getInstace �Լ��� ���� ��ü�� �޾� �� �� �ְ� ����� ���� �� ��ü�� �����ȴ�.
public class Observer {
   protected static Observer instance; //�����ϰ� �����ϴ� Observer ��ü�� ������� ��ü
   protected static Image[][] pipeImage; //���� ������ track �������ϴ� �̹����� ��� ��ü
   protected static Toolkit toolKit; // �̹����� �ε��� �� ���̴� ��ü

   boolean[][] blockPipeType; //�������� ������ ���� ����� ��ġ������ ������ �迭 
                        //ex)���ڰ� ����� 1010(��/������/�Ʒ�/����, 1�� �ʷ�������, 0�� ���� ������) 
   boolean turn,vflag;
   int gameState; //���� ������ ���¸� �����ϱ� ���� ����, ������ ������ ��Ÿ���� ���� ���� 
   int menuBlockType; // ���� �޴����� ���õ� �������� ������ ��� �ִ� ���� 
   int pipePicture = 1;
   String guiMode;
   //Frame frame; // 
   GamePanel gamePanel;//���� �г��� ��� ���� ����
                  //����� ������ �����гο� �����ϸ� �����гο��� �ʿ信 ���� �����÷��� �гη� �����ϵ��� ��
   GamePlayPanel gamePlayPanel;//������ ������ ����Ǵ� �г�
   MenuPanel menuPanel; // �޴��� ����� ����ϴ� �г�
   JTextArea textArea;
   HeaderPanel headerPanel;
   GameAi gameAi;

   
   
   protected Observer(){
      // ����� ������ ���� �� �ʱ�ȭ
      toolKit = Toolkit.getDefaultToolkit();
      guiMode="img\\marioPack\\";
      setPipeImage(); // ������ �׸� ����
      if(pipeImage[0][0]==null)System.out.println("images are not loaded");

      blockPipeType = new boolean[6][4];
      for(int i=0;i<6;++i)for(int j=0;j<4;++j)blockPipeType[i][j]=false;
      blockPipeType[0][1]=blockPipeType[0][2]=true; // false����, true���Ķ� 
      blockPipeType[3][3]=blockPipeType[3][0]=true; // 0~3 ������ �ð����
      blockPipeType[1][1]=blockPipeType[1][3]=true;
      blockPipeType[4][0]=blockPipeType[4][2]=true;
      blockPipeType[2][2]=blockPipeType[2][3]=true;
      blockPipeType[5][0]=blockPipeType[5][1]=true;
         
      gameState=0; // ���� ���� ����
      turn=true; // ������ ������
      vflag=false;
      menuBlockType=0; // ���� �޴����� ���õ� ������ ����
   }
   
   public boolean getVflag(){
	   return vflag;
   }
   public boolean getPipeType(int type, int direction){   
      return blockPipeType[type][direction];
   }
   //������ ��ü�� �ϳ��� �����ϰ� ���ִ� �Լ� // �̱��� ��ü
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
   public String getGuiMode(){ // �̹��� ���� 
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
   //������ ���� ��ư�� ������ �� ����� ������ ������ �޴��� �ݿ��ϱ� ���� �Լ�(���� ����)
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