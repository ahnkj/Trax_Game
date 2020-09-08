package source;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

//���� �÷��� �гΰ� JScrollPane�� �����ϱ� ���� �г�
//���������� ���� ������ �����гο��� �Ѱ��� 
public class GamePanel extends JPanel {
	
	GamePlayPanel gamePlayPanel;//������ �����ϴ� �г�
	JScrollPane scrollPane;//��Ŭ���� �־��ֱ� ���� ��ü
	Observer observer; // ������ �����ϴ� ��ü 
	
	public GamePanel() {
		this.setLayout(null);//���̾ƿ� ��ġ�� ���ϴ� ��� ��
		
		//������ ��ü �޾ƿ��� �ڽ��� ������ �� �ְ� ���������� ����
		observer=Observer.getInstance(); 
		observer.setGamePanel(this);
		
		//�����÷��� ������ ��ġ
		gamePlayPanel=new GamePlayPanel();
		gamePlayPanel.setBounds(0,0,600,600);
		gamePlayPanel.setVisible(true);
		
		//JScrollPane�� ������ ��ġ
		scrollPane= new JScrollPane();
		scrollPane.getViewport().add(gamePlayPanel);
		scrollPane.getHorizontalScrollBar().setValue(2875);//���� ��ũ�� �� ��ġ ����
		scrollPane.getHorizontalScrollBar().setValue(2875);//���� ��ũ�� �� ��ġ ����
		scrollPane.getVerticalScrollBar().setValue(2875);//���� ��ũ�� �� ��ġ ����
		scrollPane.setBounds(0,0,600,600);
		add(scrollPane);
		
	
	}
	
	//�ܼ��� ����� ��ü�� �����ϱ� ���� �Լ� 
	public GamePlayPanel getGamePlayPanel(){
		return gamePlayPanel;
	}
	
	//Ŭ���� ��ǥ�� ���¸� �ְ� �����÷����г��� �߰��Ұ������� �����ϰ� ��

	
	//���� �÷��� �гο� �ִ� ������ �̹����� ����
	public void setPipeImage(Image[][] pipeImage){
		gamePlayPanel.setPipeImage(pipeImage);
	}
	
	

}
