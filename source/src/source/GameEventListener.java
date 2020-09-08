package source;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

//�� Ŭ������ �����÷����гο��� �Ͼ�� �̺�Ʈ���� ó����
public class GameEventListener implements MouseListener, MouseMotionListener{


	Observer observer;//������ ��ü�� ��� ���� ��ü
	int type;
	ArrayList<Integer> pTypeList;
	GamePlayPanel gamePlayPanel;
	Point point,clickedPoint; // Ŭ���� ��ġ�� ������� ����Ʈ
	Point posPoint; //���� ���콺�� ��ġ�� ���� ����� �����ڸ��� �����ֱ� ���� ����� ���� ����Ʈ 
	
	public GameEventListener(GamePlayPanel gamePlayPanel){
	//�� ��ü�� ����� �ʱ�ȭ 
	observer = Observer.getInstance();
	if(observer==null) System.out.println("GameEventListener: observer is null");
	
	pTypeList = new ArrayList<Integer>();
	point = new Point();

	posPoint = new Point();
	this.gamePlayPanel=gamePlayPanel;

	}
	
	public void mouseClicked(MouseEvent e) {
		int gameState;
		//�����÷����гο��� ���콺�� Ŭ���� ��
		point=e.getPoint(); // ����Ʈ�� �޾ƿ�
		
		// Ÿ���� ũ�Ⱑ 50�̹Ƿ� ��ǥ��ȯ�� ���� 50���� ������
		point.x=point.x/50; 
		point.y=point.y/50;
		
		 if(e.getButton() == 3)
		 {
			//Ŭ���� ��ġ�� �޴����� ���õ� ����� ������ �����÷��� �гο� ���� 
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
