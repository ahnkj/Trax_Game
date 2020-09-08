package source;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

//게임 플레이 패널과 JScrollPane을 종합하기 위한 패널
//옵저버에게 받은 정보를 게임패널에게 넘겨줌 
public class GamePanel extends JPanel {
	
	GamePlayPanel gamePlayPanel;//게임을 진행하는 패널
	JScrollPane scrollPane;//스클롤을 넣어주기 위한 객체
	Observer observer; // 정보를 종합하는 객체 
	
	public GamePanel() {
		this.setLayout(null);//레이아웃 배치는 원하는 대로 함
		
		//옵저버 객체 받아오며 자신을 참조할 수 있게 옵저버에게 전달
		observer=Observer.getInstance(); 
		observer.setGamePanel(this);
		
		//게임플레이 생성및 배치
		gamePlayPanel=new GamePlayPanel();
		gamePlayPanel.setBounds(0,0,600,600);
		gamePlayPanel.setVisible(true);
		
		//JScrollPane의 생성및 배치
		scrollPane= new JScrollPane();
		scrollPane.getViewport().add(gamePlayPanel);
		scrollPane.getHorizontalScrollBar().setValue(2875);//가로 스크롤 바 위치 설정
		scrollPane.getHorizontalScrollBar().setValue(2875);//가로 스크롤 바 위치 설정
		scrollPane.getVerticalScrollBar().setValue(2875);//세로 스크롤 바 위치 설정
		scrollPane.setBounds(0,0,600,600);
		add(scrollPane);
		
	
	}
	
	//단순히 선언된 객체를 전달하기 위한 함수 
	public GamePlayPanel getGamePlayPanel(){
		return gamePlayPanel;
	}
	
	//클리된 좌표와 상태를 주고 게임플레이패널이 추가할것인지를 결정하게 함

	
	//게임 플레이 패널에 있는 파이프 이미지를 설정
	public void setPipeImage(Image[][] pipeImage){
		gamePlayPanel.setPipeImage(pipeImage);
	}
	
	

}
