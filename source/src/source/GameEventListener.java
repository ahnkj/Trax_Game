package source;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

//이 클래스는 게임플레이패널에서 일어나는 이벤트만을 처리함
public class GameEventListener implements MouseListener, MouseMotionListener{


	Observer observer;//옵저버 객체를 담기 위한 객체
	int type;
	ArrayList<Integer> pTypeList;
	GamePlayPanel gamePlayPanel;
	Point point,clickedPoint; // 클릭된 위치를 담기위한 포인트
	Point posPoint; //추후 마우스의 위치에 따라 블락이 놓일자리를 보여주기 위한 기능을 위한 포인트 
	
	public GameEventListener(GamePlayPanel gamePlayPanel){
	//각 객체들 선언및 초기화 
	observer = Observer.getInstance();
	if(observer==null) System.out.println("GameEventListener: observer is null");
	
	pTypeList = new ArrayList<Integer>();
	point = new Point();

	posPoint = new Point();
	this.gamePlayPanel=gamePlayPanel;

	}
	
	public void mouseClicked(MouseEvent e) {
		int gameState;
		//게임플레이패널에서 마우스가 클릭될 때
		point=e.getPoint(); // 포인트를 받아옴
		
		// 타일의 크기가 50이므로 좌표변환을 위해 50으로 나눠줌
		point.x=point.x/50; 
		point.y=point.y/50;
		
		 if(e.getButton() == 3)
		 {
			//클릭한 위치와 메뉴에서 선택된 블락의 종류를 게임플레이 패널에 전달 
			gameState=gamePlayPanel.addBlock(null,false);
			//observer.updateGameState(point.x, point.y, gameState);
			 
		 }
		 else
		 {
			 gamePlayPanel.setPBlock(point.x, point.y);
			 gamePlayPanel.repaint();
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

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		
	}

}
