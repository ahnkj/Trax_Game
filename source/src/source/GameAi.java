package source;

import java.util.ArrayList;
import java.awt.*;

public class GameAi {
	
	Observer observer;
	AiCase caseArray[][][];
	ArrayList<PointState> vList;
	ArrayList<PointState> dList;
	ArrayList<PointState> fvList;
	ArrayList<PointState> fdList;
	ArrayList<PointState> attackList;
	ArrayList<PointState> randomList;
	
	
	
	public GameAi()
	{
		observer=Observer.getInstance();
		observer.setGameAi(this);
		
		randomList=new ArrayList<PointState>();
		vList=new ArrayList<PointState>();
		dList=new ArrayList<PointState>();
		fvList=new ArrayList<PointState>();
		fdList=new ArrayList<PointState>();
		attackList=new ArrayList<PointState>();
		
		caseArray = new AiCase[2][128][128];
		for(int x=0;x<128;x++)
		{
			for(int y=0;y<128;y++)
			{
				caseArray[0][x][y]=new AiCase();
				caseArray[1][x][y]=new AiCase();
			}
		}
	}

	public PointState aiDecision(int[][] blockArray,boolean turn)
	{
		ArrayList<PointState> caseList; //케이스
		ArrayList<PointState> bestList; //케이스
		
		ArrayList<Point> scope; //가능한 모든 포인트
		PointState best;
		AiType aiTypeTemp, aiTypeBestTemp;
		boolean endFlag;
		int idx,type; // caseArray 첨자 지정
		
		
		
		if(observer.getGamePalyEmpty())
		{
			best=null;
			int res =(int)(Math.random()*2+1);
			
			switch(res)
			{
			case 1:
				best=new PointState(63,63,3);
				break;
			case 2:
				best=new PointState(63,63,4);
				break;
			}
			
			return best;
		}
		
		
		
		//초기화
		best=null;
		bestList=new ArrayList<PointState>();
		caseList=new ArrayList<PointState>();
		
		randomList.clear();
		vList.clear();
		dList.clear();
		fvList.clear();
		fdList.clear();
		attackList.clear();
		
		int res=1;
		scope=setScope(blockArray);
		
		for(int i=0;i<scope.size();++i)
		{
			getBest(blockArray,scope.get(i), turn);
			if(vList.size()>0)best=vList.get(0);
			
			if(best!=null)break;
		}
		if(best!=null)return best;
		
		if(dList.size()>0)
		{
			best=dList.get(0);
			res=2;
		}
		else if(fvList.size()>0)
		{
			best=fvList.get(0);
			res=3;
		}
		else if(fdList.size()>0)
		{
			best=fdList.get(0);
			res=4;
		}
		else if(attackList.size()>0)
		{
			best=attackList.get((int)Math.random()*attackList.size());
			res=5;
		}
		else 
		{
			best=randomList.get((int)Math.random()*randomList.size());
			res=7;
		}
		
		System.out.println("aiDecision type::"+res);
		return best;
		
	}
	
	public AiType resultCase(int[][] pBlockArray,PointState block, boolean turn , boolean checkType, int limit)
	{
		int res, vCnt, dCnt;
		double val;
		ArrayList<PointState> mdfList,victory,pCase;
		AiType aiTypeTemp,aiTypeRes;
		boolean endFlag;
		int[][] blockArray;
		
		blockArray = new int[128][128];
		for(int i=0;i<128;++i)
		{
			for(int h=0;h<128;++h)
			{
				blockArray[i][h]=pBlockArray[i][h];
			}
		}
		vCnt=dCnt=0;
		val=0;
		aiTypeRes=new AiType(2);
		endFlag=false;
		
		
		// 수를 두고 오블리게이션까지 고려하여 배열을 수정
		mdfList=obligation(blockArray, block.x, block.y, block.state);
		for(int i=0;i<mdfList.size();++i)
		{
			blockArray[block.x][block.y]=block.state;
		}
		
		//illegal turn이 발생했을 경우
		if(mdfList.size()==0) return new AiType(5);
		
		//이겼을 경우
		victory=isEnd(blockArray,mdfList,checkType);
		
		if(victory.size()>0)
		{
			if(turn==checkType)return new AiType(1);
			else return new AiType(4);
		}
		
		//자기 자신이 지는 수를 두는 경우
		victory=isEnd(blockArray,mdfList,!checkType);
		if(victory.size()>0) return new AiType(5);
		
		//지정된 한계 만큼만 재귀를 하도록 하는 부분
		if(limit==0) return new AiType(2);
		
		
		//앞으로의 결과를 추측하는 부분
		//pCase=possibleCaseList(blockArray);
		pCase=new ArrayList<PointState>();
		
		for(int i=0; i<pCase.size();++i)
		{
			aiTypeTemp=resultCase(blockArray, pCase.get(i), turn, !checkType, limit-1);
			
			switch(aiTypeTemp.type)
			{
			case 1: 
				val+=1;
				vCnt++;
				if(vCnt>2)
				{
					endFlag=true;
					aiTypeRes=new AiType(3);
				}
				break;
			case 2:
				val+=aiTypeTemp.value;
				break;
				
			case 3:
				val+=1;
				vCnt++;
				if(vCnt>2)
				{
					endFlag=true;
					aiTypeRes=new AiType(3);
				}
				break;
				
			case 4: 
				val-=1;
				//dCnt++;
				//if(dCnt>2)
				//{
				endFlag=true;
				aiTypeRes=new AiType(4);
				//}
				break;
			case 5:
				pCase.remove(i);
				i--;
				break;
			}
			
			if(endFlag)break;
		}
		
		if(endFlag) return aiTypeRes;
		else return new AiType(2,val/2);
		
	}
	
	public boolean canConnected(int[][] blockArray,int x, int y, int state){
		
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
		
		return result;
	}
	
	public ArrayList<Point> getPossiblePoint(int[][] blockArray)
	{
		ArrayList<Point> pointList;
		
		pointList=new ArrayList<Point>();
		
		for(int i=0;i<128;i++)
		{
			for(int h=0;h<128;h++)
			{
				if(isAdjecent(blockArray, i, h))pointList.add(new Point(i,h));
			}
		}
		
		return pointList;
	}
	
	public boolean isAdjecent(int[][] blockArray, int x, int y){
		boolean res;
		int offset, xPos,yPos;
		int type;
		
		res=false;
		offset=-1;
		
		
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
	
	
	public ArrayList<PointState> isEnd(int[][] blockArray,ArrayList<PointState> changeList,boolean color){
		
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
				
				//System.out.println("wrong direc:("+big.x+","+big.y+"):");
				//System.out.println("wrong direc:("+small.x+","+small.y+"):");
				
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
				
				//System.out.println("Info"+big.y+","+small.y+"):");
				
			}
			
			
			if(vFlag) 
			{

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
		
		//System.out.println("isEnd_lFind source type"+type);
		
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
	public boolean isSamePointState(PointState op1,PointState op2){
		
		return (op1.x==op2.x&&op1.y==op2.y&&op1.state==op2.state);
	}
	
	public ArrayList<PointState> obligation(int[][] blockArray,int x, int y, int state){
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
					blockTemp=obligationCheckPart(blockArray,psTemp.x+offset/10, psTemp.y+offset%10);
					
					if(blockTemp<6) oblList.add(new PointState(psTemp.x+offset/10, psTemp.y+offset%10,blockTemp));
					
					else if(blockTemp==7)break; 
				}
		
				if(offset<10 && offset>-10) offset*=-10;
				else offset/=10;
			}
			
			if(blockTemp==7) break; 
		}
		
		if(blockTemp>6)blockListTemp.clear(); 
		
		
		return blockListTemp;
		
	}
	
	public int obligationCheckPart(int[][] blockArray,int x, int y)
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
			if(-1<x+offset/10&&x+offset/10<128&&-1<y+offset%10/10&&y+offset%10/10<128)
			type=blockArray[x+offset/10][y+offset%10];
			else type=7;
			
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
				if(canConnected(blockArray,x, y,i))
				{
					blockType=i; 
					break;
				}
			
			}
			
			if(blockType==6)blockType=7; 
		}
		
		return blockType;
		
	}

	public ArrayList<PointState> possibleCaseList(int[][]blockArray,Point point)
	{
		Point temp;
		ArrayList<PointState> pCase;
		
		pCase=new ArrayList<PointState>();
		
	
		temp=point;
		if(isAdjecent(blockArray, temp.x, temp.y)&&blockArray[temp.x][temp.y]==6)
		{
			for(int j=0;j<6;++j)
			{
				if(canConnected(blockArray,  temp.x, temp.y, j))
				{
					pCase.add(new PointState(temp.x,temp.y,j));
				}
			}
					
		}
		
		
		return pCase;
		
	}

	
	public ArrayList<PointState> possibleCaseList(int[][]blockArray,ArrayList<Point> pList)
	{
		Point temp;
		ArrayList<PointState> pCase;
		
		pCase=new ArrayList<PointState>();
		
		
		for(int i=0;i<pList.size();i++)
		{
			temp=pList.get(i);
				if(isAdjecent(blockArray, temp.x, temp.y)&&blockArray[temp.x][temp.y]==6)
				{
					for(int j=0;j<6;++j)
					{
						if(canConnected(blockArray,  temp.x, temp.y, j))
						{
							pCase.add(new PointState(temp.x,temp.y,j));
						}
					}
					
				}
		}
		
		return pCase;
		
	}
	
	
	public ArrayList<Point> setScope(int[][] blockArray)
	{
		ArrayList<Point> scope= new ArrayList<Point>();
		
		for(int x=0;x<128;x++)
		{
			for(int y=0;y<128;y++)
			{
				if(isAdjecent(blockArray, x, y)&&blockArray[x][y]==6)
				{
							scope.add(new Point(x,y));
				}
			}
		}
		
		return scope;
		
	}

	
	public ArrayList<Point> setScope(int[][] blockArray,ArrayList<PointState> mdfList,int range)
	{
		ArrayList<PointState> mdfTemp =new ArrayList<PointState>();
		ArrayList<Point> scope= new ArrayList<Point>();
		boolean[][] scopeTemp=new boolean[128][128];

		
		
		for(int x=0;x<128;++x)
			for(int y=0;y<128;++y)
				scopeTemp[x][y]=false;
		
		for(int i=0;i<mdfList.size();++i)mdfTemp.add(mdfList.get(i));
		
		for(int i=0;i<mdfList.size();++i)
		{
			mdfTemp.add(setScope_lEnd(blockArray, mdfList.get(i), false));
			mdfTemp.add(setScope_rEnd(blockArray, mdfList.get(i), false));
			mdfTemp.add(setScope_lEnd(blockArray, mdfList.get(i), true));
			mdfTemp.add(setScope_rEnd(blockArray, mdfList.get(i), true));
		}
		
		for(int i=0;i<mdfTemp.size();++i)
		{
			int xMax,yMax,xMin,yMin;
			xMax=xMin=mdfTemp.get(i).x;
			yMax=yMin=mdfTemp.get(i).y;
			
			if(xMax+range<=127)xMax=xMax+range;
			if(xMin-range>=0)xMin=xMin-range;
			if(yMax+range<=127)yMax=yMax+range;
			if(yMin-range>=0)yMin=yMin-range;
			
			for(int x=xMin;x<=xMax;x++)
				for(int y=yMin;y<=yMax;y++)
					scopeTemp[x][y]=true;
		}
		
		for(int x=0;x<128;++x)
			for(int y=0;y<128;++y)
				if(scopeTemp[x][y]==true)
					{
					
					scope.add(new Point(x,y));
			
					}
		
		return scope;
		
	}
	
	
	
	public PointState setScope_lEnd(int[][] blockArray, PointState block,boolean color)
	{
		PointState tempBlock=block;
		PointState postBlock=block;
		int xPos,yPos,direction=0;
		while(true){ 
			
			direction=isEnd_lFind(tempBlock, direction, color);
			
			xPos=tempBlock.x+direction/10;
			yPos=tempBlock.y+direction%10;
			
			postBlock=tempBlock;
			tempBlock=new PointState(xPos,yPos,blockArray[xPos][yPos]);
			
			if(tempBlock.state>5||isSamePointState(block,tempBlock))break;
		}
		
		
		return postBlock;
	}
	
	public PointState setScope_rEnd(int[][] blockArray, PointState block,boolean color)
	{
		PointState tempBlock=block;
		PointState postBlock=block;
		int xPos,yPos,direction=0;
		while(true){ 
			
			direction=isEnd_rFind(tempBlock, direction, color);
			
			xPos=tempBlock.x+direction/10;
			yPos=tempBlock.y+direction%10;
			
			postBlock=tempBlock;
			tempBlock=new PointState(xPos,yPos,blockArray[xPos][yPos]);
			
			if(tempBlock.state>5||isSamePointState(block,tempBlock))break;
		}
		
		
		return postBlock;
	}
	
	public void updateCaseList(int[][] blockArray,ArrayList<PointState> mdfList)
	{
		ArrayList<Point> scopeList;
		AiCase aiCase;
		
		scopeList=setScope(blockArray, mdfList,2);
		for(int i=0;i<scopeList.size();++i)
		{
			Point pTemp=scopeList.get(i);
			aiCase=getAiCase(blockArray, pTemp, false);
			caseArray[0][pTemp.x][pTemp.y]=aiCase;
			aiCase=getAiCase(blockArray, pTemp, true);
			caseArray[1][pTemp.x][pTemp.y]=aiCase;
		}
		
	}
	
	public AiCase getAiCase(int [][] blockArray, Point point,boolean color)
	{
		AiCase aiCase,aiCaseTemp;
		ArrayList<PointState> pCaseList=null;
		ArrayList<PointState> mdfList,vList;
		int[][] tempArray;
		
		tempArray=new int[128][128];
		aiCase=null;
		
		if(blockArray[point.x][point.y]<6)aiCase=new AiCase(-1,-1,-1);
		else if(!isAdjecent(blockArray, point.x, point.y))aiCase=new AiCase(-1,-1,-1);
		else
		{
			pCaseList=possibleCaseList(blockArray, point);
			
			for(int i=0;i<pCaseList.size();++i)
			{
				for(int x=0;x<128;x++)
				{
					for(int y=0;y<128;y++)
					{
						tempArray[x][y]=blockArray[x][y];
					}
				}
				
				PointState blockTemp=pCaseList.get(i);
				mdfList=obligation(tempArray, blockTemp.x, blockTemp.y, blockTemp.state);
				if(mdfList.isEmpty())
				{
					aiCase=new AiCase(-1,-1,-1);
					break;
				}
				
				vList=isEnd(tempArray, mdfList, color);
				if(!vList.isEmpty())
				{
					aiCase=new AiCase(1,blockTemp.state,-1);
					break;
				}
				
				vList=isEnd(tempArray, mdfList, !color);
				if(!vList.isEmpty())
				{
					aiCase=new AiCase(2,blockTemp.state,-1);
					break;
				}
				
				aiCaseTemp=getAiCase2(tempArray, mdfList, color);
				
				if(aiCase==null)aiCase=aiCaseTemp;
				else if(aiCase.type>aiCaseTemp.type)aiCase=aiCaseTemp;
				else if(aiCase.type==aiCaseTemp.type)
				{
					if(aiCase.type==3&aiCase.val<aiCaseTemp.val)
					{
						aiCase=aiCaseTemp;
					}
				}	
			}
		}
		return aiCase;
	}
	
	public AiCase getAiCase2(int [][] blockArray, ArrayList<PointState> cList,boolean color)
	{
		
		ArrayList<PointState> pCaseList=null;
		ArrayList<PointState> mdfList,vList;
		ArrayList<Point> scopeList;
		int[][] tempArray;
		int wCnt,dCnt;
		
		tempArray=new int[128][128];
		
		wCnt=dCnt=0;
		
		
		scopeList=setScope(blockArray, cList,2);
		pCaseList=possibleCaseList(blockArray, scopeList);
		
		for(int i=0;i<pCaseList.size();++i)
		{
			PointState blockTemp=pCaseList.get(i);
			
			for(int x=0;x<128;x++)
			{
				for(int y=0;y<128;y++)
				{
					tempArray[x][y]=blockArray[x][y];
				}
			}
			
			mdfList=obligation(tempArray, blockTemp.x, blockTemp.y, blockTemp.state);
			
			vList=isEnd(tempArray, mdfList, color);
			if(!vList.isEmpty())
			{
				wCnt++;
				continue;
			}
			
			vList=isEnd(tempArray, mdfList, !color);
			if(!vList.isEmpty())
			{
				dCnt++;
				break;
			}
			
		}
		
		if(dCnt>0) return new AiCase(3,cList.get(0).state,-1);
		else if(wCnt>0) return new AiCase(4,cList.get(0).state,wCnt);
		else return new AiCase(5,cList.get(0).state,-1);
	}
	
	public void getBest(int[][] blockArray, Point point,boolean trun)
	{
		ArrayList<PointState> caseList;
		PointState bTemp,bTemp2;
		boolean endFlag=false;
		
		caseList= possibleCaseList(blockArray, point);
		
		int type,type2;
		
		for(int i=0;i<caseList.size();i++)
		{
			bTemp=caseList.get(i);
			type=getCaseType(blockArray, caseList.get(i), trun);
			
			switch(type)
			{
			case -1: break;
			case 1:
				vList.add(new PointState(bTemp.x,bTemp.y,bTemp.state));
				endFlag=true;
			break;
			
			case 2:
				for(int j=0;j<caseList.size();j++)
				{
					bTemp2=caseList.get(j);
					type2=getCaseType(blockArray, caseList.get(j), trun);
					if(type2!=2&&type2!=4&&type2!=6)dList.add(new PointState(bTemp2.x,bTemp2.y,bTemp2.state));
				}
			break;
			
			case 3:
				fvList.add(new PointState(bTemp.x,bTemp.y,bTemp.state));
			break;
			
			case 4:
				for(int j=0;j<caseList.size();j++)
				{
					bTemp2=caseList.get(j);
					type2=getCaseType(blockArray, caseList.get(j), trun);
					if(type2!=2&&type2!=4&&type2!=6)fdList.add(new PointState(bTemp2.x,bTemp2.y,bTemp2.state));
				}
			break;
			
			case 5:
				attackList.add(new PointState(bTemp.x,bTemp.y,bTemp.state));
			break;
			
			case 6:break;
		
			case 7:
				randomList.add(new PointState(bTemp.x,bTemp.y,bTemp.state));
			break;
			}
			
			if(endFlag)break;
			
		}
		
	}
	
	public int getCaseType(int[][] blockArray, PointState block,boolean trun)
	{
		ArrayList<Point> scope;
		ArrayList<PointState> mdfList;
		ArrayList<PointState> vdList;
		ArrayList<PointState> futrueList;
		int[][] tempArray;
		
		tempArray=copyBlockArray(blockArray);
		mdfList=obligation(tempArray, block.x, block.y, block.state);
		
		if(mdfList.size()==0)return -1; //obligation
		
		vdList=isEnd(tempArray, mdfList, trun);
		if(vdList.size()>0)return 1;
		
		vdList=isEnd(tempArray, mdfList, !trun);
		if(vdList.size()>0)return 2;
		
		scope=setScope(tempArray, mdfList, 3);
		
		return getCaseType2(tempArray, scope, trun);
		
	}
	
	
	public int getCaseType2(int[][] blockArray, ArrayList<Point> scopeList,boolean trun)
	{
		ArrayList<PointState> pCaseList;
		ArrayList<PointState> mdfList;
		ArrayList<PointState> vdList;
		int[][] tempArray;
		int vCnt=0;
		int dCnt=0;
		
		pCaseList=possibleCaseList(blockArray, scopeList);
		
		for(int i=0;i<pCaseList.size();i++)
		{
			tempArray=copyBlockArray(blockArray);
			mdfList=obligation(tempArray, pCaseList.get(i).x, pCaseList.get(i).y,pCaseList.get(i).state);
			if(mdfList.size()==0)continue;
			
			vdList=isEnd(tempArray, mdfList, trun); // 이기는 경우
			if(vdList.size()>0)
			{
				vCnt++;
			}
			
			vdList=isEnd(tempArray, mdfList, !trun); //지는 경우
			if(vdList.size()>0)
			{
				dCnt++;
			}
		}
		
		if(dCnt>2) return 4;
		else if(dCnt>0) return 6;
		
		if(vCnt>2)return 3;
		else if(vCnt>0)return 5;
		else return 7;
		
	}
	
	public int[][] copyBlockArray(int[][] blockArray)
	{
		int[][] tempArray =new int[128][128];
		
		for(int x=0;x<128;x++)
		{
			for(int y=0;y<128;y++)
			{
				tempArray[x][y]=blockArray[x][y];
			}
		}
		
		return tempArray;
	}
	
	
	
}
