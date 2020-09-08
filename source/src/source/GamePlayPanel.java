package source;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

//게임을 진행을 담당하는 패널
public class GamePlayPanel extends JPanel {
	Image background;
	GameEventListener gameEventListener;
	private ArrayList<PointState> blockArrayList; 
	private ArrayList<PointState> resultArrayList;
	private ArrayList<PointState> victoryList;
	private ArrayList<Integer> pTypeList;
	int[][] blockArray;
	int pType;
	Point clickedPoint;
	PointState pBlock;
	private Image[][] pipeImage;
	private Toolkit toolKit; 
	private Observer observer; 
	private int reverseTrunFlag;

	private ArrayList<Integer> boundaryIndex;
	
	public GamePlayPanel()
	{
		
		toolKit = Toolkit.getDefaultToolkit();
		
		this.setPreferredSize(new Dimension(6400,6400));
		
		observer=Observer.getInstance();
		observer.setGamePlayPanel(this);
		
		
		gameEventListener=new GameEventListener(this);
		this.addMouseListener(gameEventListener);
		
		blockArrayList = new ArrayList<PointState>();
		resultArrayList = new ArrayList<PointState>();
		victoryList = new ArrayList<PointState>();
		pTypeList= new ArrayList<Integer>();
		
		pType=0;
		reverseTrunFlag=0;
		clickedPoint=new Point(-1,-1);
		pBlock= null;
		
		
		blockArray= new int[128][128];
		for(int i=0;i<128;++i)
			for(int j=0;j<128;++j)
				blockArray[i][j]=6;
		
		//numTurn = -1; // -1로 초기화
		boundaryIndex = new ArrayList<Integer>();
		setGUI();
	}
	
	protected void paintComponent(Graphics g){
		//패널에 그리는 것을 담당하는 부분
		PointState pointState= new PointState();
		
		g.drawImage(background, 0, 0,6400,6400, this); //배경을 그려줌
		//System.out.println("paintComponent:"+blockArrayList.size());
		
		/*
		 blockArrayList.clear();
		for(int i=0;i<128;++i)
		{
			for(int h=0;h<128;++h)
			{
				if(blockArray[i][h]!=6)blockArrayList.add(new PointState(i,h,blockArray[i][h]));
			}
		}
		*/
		for(int i=0;i<blockArrayList.size();++i){// 저장된 파이프 종류와 위치를 가져와 패널에 그림
			pointState=blockArrayList.get(i);
			g.drawImage(pipeImage[0][pointState.state], pointState.x*50, pointState.y*50, 50, 50, this);
		}
		
		if(pBlock!=null)g.drawImage(pipeImage[0][pBlock.state], pBlock.x*50, pBlock.y*50, 50, 50, this);
		
		if(victoryList.size()>0)System.out.println("victory Draw"+victoryList.size());
		
		for(int i=0;i<victoryList.size();++i){
			pointState=victoryList.get(i);
			g.drawImage(pipeImage[1][pointState.state], pointState.x*50, pointState.y*50, 50, 50, this);
		}
	}
	public void increseStep() {

		if(blockArrayList.isEmpty()) return;
		else if(boundaryIndex.isEmpty()) boundaryIndex.add(blockArrayList.size()-1); 
		else if(boundaryIndex.get(boundaryIndex.size()-1) < blockArrayList.size()-1)
			boundaryIndex.add(blockArrayList.size()-1);
		System.out.println("increaseStep: blockArrayList of Size:"+blockArrayList.size()+" boundaryIndex of size:"+boundaryIndex.size());
		reverseTrunFlag=0;
	}
	public void restoreOneStep() {
		PointState point;
		int indexEnd=0, indexStart=0;

		if(boundaryIndex.size()==0) return;
		else if(boundaryIndex.size()==1) { 
			indexEnd = boundaryIndex.get(boundaryIndex.size()-1);
			for(int i=indexEnd; i>=0; i--) {
				point = blockArrayList.get(i);
				blockArray[point.x][point.y] = 6;
				blockArrayList.remove(i);
			}
			
		}
		else { // 2회이상 돌리기
			indexEnd = boundaryIndex.get(boundaryIndex.size()-1);
			indexStart = boundaryIndex.get(boundaryIndex.size()-2);
			for(int i=indexEnd; i>indexStart; i--) {
				point = blockArrayList.get(i);
				blockArray[point.x][point.y] = 6;
				blockArrayList.remove(i);
			}
			
		}
		reverseTrunFlag++;
		if(reverseTrunFlag%2==0)observer.setTurn(observer.getTurn());
		boundaryIndex.remove(boundaryIndex.size()-1);
		victoryList.clear();
		System.out.println("restoreOneStep: indexEnd"+indexEnd+"indexStart"+indexStart);
		repaint();
	}
	public int addBlock(PointState block,boolean aiFlag){

		boolean result, vFlag , vColor;
		int oblRes,gameState;
		Date date;
		PointState oblPoint;
		ArrayList<PointState> mdfList;
		
		
		vFlag=false;
		vColor=false;
		result=true;
		mdfList= new ArrayList<PointState>(); 
		gameState=oblRes=1;
		if(!aiFlag)block=pBlock;

		
		if(pBlock==null&&aiFlag==false)result=false;
		
		if(result)System.out.println("block.x::"+block.x+"  ,block.y::"+block.y);
		
		if(result)result=blockArray[block.x][block.y]==6; 
		if(!result&&gameState!=1)gameState=4;
		
		if(result)result&=isAdjecent(block.x, block.y);
		if(!result&&gameState==1)gameState=5;
		if(blockArrayList.size()==0)gameState=1;
		
		if(result)result&=canConected(block.x, block.y, block.state); 
		if(!result&&gameState==1)gameState=6;
		
		if(result)mdfList=obligation(block.x, block.y, block.state); 
		
		
		
		if(mdfList.size()!=0)result&=true; 
		else if(gameState==1)gameState=7;
	
		victoryList=isEnd(mdfList, observer.getTurn()); 
		if(victoryList.size()>0) gameState=2;
		if(victoryList.size()==0)victoryList=isEnd(mdfList, !observer.getTurn()); 
		if(victoryList.size()>0&&gameState!=2) gameState=3;
		
		
		if(mdfList.size()>0)
		{
			observer.updateGameAi(blockArray, mdfList);
			increseStep();
			
		}
		
		observer.updateGameState(block.x, block.y, gameState, block.state);
		repaint();
		
		
		return gameState;
	}
	
	public boolean canConected(int x, int y, int state){
		
		boolean result;
		int type,offset;
		int xPos,yPos;
		result=true;
		offset=-1;
		
		for(int i=0;i<4;++i){
			xPos=x+offset/10; yPos=y+offset%10;
			
			if(-1<xPos&&xPos<128 && -1<yPos && yPos<128){
				type=blockArray[x+offset/10][y+offset%10];
				if(type<6) result&=(observer.getPipeType(type,(i+2)%4)==observer.getPipeType(state, i));
				else result&=true;
			}
			
			if(offset<10 && offset>-10) offset*=-10;
			else offset/=10;
		}
		
		if(result)System.out.println(  "("+x+", "+y+")"+state +" is possible block type" );
		return result;
	}
	

	
	public boolean isAdjecent(int x, int y){
		boolean res;
		int offset, xPos,yPos;
		int type;
		
		res=false;
		offset=-1;
		
		if(isEmpty())res=true;
		
		for(int i=0;i<4;++i){ 
			xPos=x+offset/10; yPos=y+offset%10;
			
			
			if(xPos<128 && yPos<128 && xPos>-1 && yPos>-1){
				type=blockArray[xPos][yPos];
				if(type<6) res|=true; // 타일이 존재하면 true
				else res|=false;
			}
			
			if(offset<10 && offset>-10) offset*=-10;
			else offset/=10;
		}
		
		return res;
	}
	
	public ArrayList<PointState> isEnd(ArrayList<PointState> changeList,boolean color){
		
		int direction;
		boolean cFlag,vFlag;
		PointState sBlock,tempBlock;
		int xMax,xMin,yMax,yMin;
		PointState rBlock,lBlock;
		ArrayList<PointState> victoryPath;
		
		
		victoryPath = new ArrayList<PointState>();
		vFlag=false; 
		
		for(int i=0; i<changeList.size(); ++i){ 
			sBlock= changeList.get(i);
			xMax=xMin=sBlock.x;
			yMax=yMin=sBlock.y;
			
			victoryPath.add(sBlock); 
			direction=0; 
		
			lBlock=rBlock=tempBlock=sBlock; 
			while(true){
				int xPos,yPos;
				direction=isEnd_lFind(tempBlock, direction, color);
				
				xPos=tempBlock.x+direction/10; 
				yPos=tempBlock.y+direction%10;
				
				tempBlock=new PointState(xPos,yPos,blockArray[xPos][yPos]);
				
				if(tempBlock.state>5||isSamePointState(sBlock,tempBlock))break;
				else{
					
					if(xPos>=xMax)xMax=xPos;
					else if(xPos<=xMin)xMin=xPos;
					
					if(yPos>=yMax)yMax=yPos;
					else if(yPos<=yMin)yMin=yPos;
					
					lBlock=tempBlock;
					
					victoryPath.add(tempBlock);
				}
			}
			
			direction=0;
			if(isSamePointState(sBlock,tempBlock))cFlag=true;
			else cFlag=false; 
			if(cFlag)break;
			
			tempBlock=sBlock;
			
			while(true){ 
				int xPos,yPos;
				direction=isEnd_rFind(tempBlock, direction, color);
				
				xPos=tempBlock.x+direction/10;
				yPos=tempBlock.y+direction%10;
				
				tempBlock=new PointState(xPos,yPos,blockArray[xPos][yPos]);
				
				if(tempBlock.state>5||isSamePointState(sBlock,tempBlock))break;
				else{
					
					if(xPos>=xMax)xMax=xPos;
					else if(xPos<=xMin)xMin=xPos;
					
					if(yPos>=yMax)yMax=yPos;
					else if(yPos<=yMin)yMin=yPos;
					
					rBlock=tempBlock;
					
					victoryPath.add(tempBlock);
				}
			}
			
			
			if(isSamePointState(sBlock,tempBlock))cFlag=true;
			else cFlag=false;
			
			if(cFlag)vFlag=true; // 루프이면 승리 플래그 true
			if(vFlag)break;
			
			if(rBlock.state==6||lBlock.state==6)System.out.println("할당오류");
			
			if(xMax-xMin>6){
				boolean maxColor,minColor,res;
				PointState big,small;
				
				if(lBlock.x>rBlock.x)
				{
					big=lBlock;
					small=rBlock;
				}
				else
				{
					small=lBlock;
					big=rBlock;
				}
				
				res=true;
				res&=(xMax==big.x);
				res&=(xMin==small.x);
				
				maxColor=observer.getPipeType(big.state, 1);
				minColor=observer.getPipeType(small.state, 3);
				if(minColor==color&&maxColor==color&&res)vFlag=true;
				
				System.out.println("wrong direc:("+big.x+","+small.x+"):");
				
			}
			
			if(yMax-yMin>6){ 
				boolean maxColor,minColor,res;
				PointState big,small;
				
				if(lBlock.y>rBlock.y)
				{
					big=lBlock;
					small=rBlock;
				}
				else
				{
					small=lBlock;
					big=rBlock;
				}
				
				res=true;
				res&=(yMax==big.y);
				res&=(yMin==small.y);
				
				maxColor=observer.getPipeType(big.state, 2);
				minColor=observer.getPipeType(small.state, 0);
				if(minColor==color&&maxColor==color&&res)vFlag=true;
				
				System.out.println("YInfo::"+big.y+","+small.y+"):");
				
			}
			
			
			if(vFlag) 
			{
			System.out.println("Vitorry??");
			break;
			}
			else victoryPath.clear();
		}
		
		return victoryPath;
	}
	
	public int isEnd_lFind(PointState source,int start,boolean color){
		
		int offset,type,direction;
		int xPos, yPos;
		
		type=source.state;
		offset=-10;
		
		System.out.println("isEnd_lFind source type"+type);
		
		for(int i=3;i>=0;--i){
			xPos=source.x+offset/10; yPos=source.y+offset%10;
			
			if(color==observer.getPipeType(type, i)&&-offset!=start)break;
			
			if(offset<10 && offset>-10) offset*=10;
			else offset/=-10;
		}
		
		return offset;
		
	}
	
	public int isEnd_rFind(PointState source,int start, boolean color)
	{
		
		int offset,type,direction;
		int xPos, yPos;
		
		type=source.state;
		offset=-1;
		
		for(int i=0;i<4;++i){
			xPos=source.x+offset/10; yPos=source.y+offset%10;
		
			if(color==observer.getPipeType(type, i)&&-offset!=start)break;
			
			if(offset<10 && offset>-10) offset*=-10;
			else offset/=10;
		}
		
		return offset;
		
	}
	public ArrayList<PointState> getBlockArrayList() {
		return blockArrayList;
	}
	
	public int[][] getBlockArray() {
		return blockArray;
	}
	
	public ArrayList<Integer> getPTypeList(int x, int y)
	{
		ArrayList<Integer> pTypeList= new ArrayList<Integer>();
		for(int i=0; i<6;++i)
		{
			if(canConected(x, y, i))pTypeList.add(new Integer(i));
		}
		
		return pTypeList;
	}
	
	public int getType(int x, int y)
	{
		if(-1<x&&x<128&&-1<y&&y<128)return blockArray[x][y];
		else return 8;
	}
	public boolean isEmpty(){
		return blockArrayList.isEmpty();
	}
	
	public ArrayList<PointState> obligation(int x, int y, int state){
		int offset,blockTemp;
		PointState psTemp;
		ArrayList<PointState> oblList,blockListTemp;
		
		offset=-1;
		blockTemp=6;

		psTemp = new PointState();
		oblList= new ArrayList<PointState>();
		oblList.add(new PointState(x,y,state));
		blockListTemp = new ArrayList<PointState>();
		
		while(!oblList.isEmpty()){ 
			psTemp=oblList.get(oblList.size()-1);
			blockListTemp.add(psTemp);
			blockArray[psTemp.x][psTemp.y]=psTemp.state;//temp
			oblList.remove(oblList.size()-1);
			
			for(int i=0;i<4;++i)
			{
				if(blockArray[psTemp.x+offset/10][psTemp.y+offset%10]>5)//temp
				{
					blockTemp=obligationCheckPart(psTemp.x+offset/10, psTemp.y+offset%10);
					
					if(blockTemp<6) oblList.add(new PointState(psTemp.x+offset/10, psTemp.y+offset%10,blockTemp));
					
					else if(blockTemp==7)break; 
				}
		
				if(offset<10 && offset>-10) offset*=-10;
				else offset/=10;
			}
			
			if(blockTemp==7) break; 
		}
		
		
		if(blockTemp<7) 
		{
			for(int i=0;i<blockListTemp.size();i++){
				PointState bTemp=blockListTemp.get(i);
				blockArrayList.add(new PointState(bTemp.x,bTemp.y,bTemp.state));
			}
			
		}
		else
		{	
			for(int i=0;i<blockListTemp.size();i++)
			{
				PointState st= blockListTemp.get(i);
				blockArray[st.x][st.y]=6;
			}
		
			blockListTemp.clear(); 
		}
		
		return blockListTemp;
		
	}
	
	public int obligationCheckPart(int x, int y)
	{
		int offset,redCnt,greenCnt;
		int blockType;
		
		offset=-1;
		redCnt=0;
		greenCnt=0;
		blockType=6; 
	
		for(int i=0;i<4;++i)
		{
			int type;
			type=blockArray[x+offset/10][y+offset%10];
			
			if(type<6){
				
				if(observer.getPipeType(type, (i+2)%4))greenCnt++;
				
				else redCnt++;
			}
			
			if(-10<offset&& offset<10 ) offset*=-10;
			else offset/=10;
		}
		
		if(greenCnt>1||redCnt>1){ 
			for(int i=0;i<6;++i)
			{
				if(canConected(x, y,i))
				{
					blockType=i; 
					break;
				}
			
			}
			
			if(blockType==6)blockType=7; 
		}
		
		return blockType;
		
	}
	
	public void setPBlock( int x, int y)
	{
		
		if(blockArrayList.size()>0)
		{
			if(x==clickedPoint.x&&y==clickedPoint.y)
			{
				if(++pType>=pTypeList.size())pType=0;
			}
			else 
			{
				clickedPoint.x=x;
				clickedPoint.y=y;
				pTypeList= getPTypeList(x, y);
				pType=0;
			}
		}
		else
		{
			clickedPoint.x=x;
			clickedPoint.y=y;
			if(pType>3)pType=3;
			else pType=4;
		}
		
		if(getType(x, y)==6&&pTypeList.size()>0&&isAdjecent(x, y)) 
			pBlock= new PointState(x,y,pTypeList.get(pType).intValue());
		else
			pBlock= null;
		
		if(getType(clickedPoint.x,clickedPoint.y)==6&blockArrayList.size()==0)
			pBlock= new PointState(63,63,pType);
		
		
	}
	

	//아래 함수들은 각 변수를 설정해주는 함수들
	public void setBlockArray(ArrayList<PointState> blockArray){
		this.blockArrayList= blockArray;
	}
	
	public void setBackground(Image img){
		background=img;
	}
	
	public void setClickedPoint(int x, int y)
	{
		clickedPoint.x=x;
		clickedPoint.y=y;
	}
	
	public void setGUI(){
		background = toolKit.getImage(observer.getGuiMode()+"gameBackground3.jpg");
		pipeImage=observer.getPipeImage();
		repaint();
	}
	
	public void setPipeImage(Image[][] pipeImage){
		this.pipeImage=pipeImage;
	}
	public void setBlockArrayListToBlockArray() {
		PointState point;
		for(int i=0; i<blockArrayList.size(); i++) {
			point = blockArrayList.get(i);
			blockArray[point.x][point.y] = point.state;
		}
	}
	public boolean isSamePointState(PointState op1,PointState op2){
		
		return (op1.x==op2.x&&op1.y==op2.y&&op1.state==op2.state);
	}
	
	public Point checkNEWS(int x, int y, int step) {
		Point point = new Point();
		switch(step) {
		case 0: point.x = x; point.y = y - 1; break;
		case 1: point.x = x + 1; point.y = y; break;
		case 2: point.x = x; point.y = y + 1; break;
		case 3: point.x = x - 1; point.y = y; break;
		}
		return point;
	}
}
