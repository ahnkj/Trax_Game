package source;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Save {
	Observer observer;
	GamePlayPanel gamePlayPanel;
	JFrame frame;
	static Save instance = null;
	JButton saveButton, loadButton, resetButton;
	ButtonListener buttonListener;
	
	private Save() {
		frame = new JFrame("저장/불러오기");
		frame.setSize(200, 100);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		//버튼 설정
		buttonListener = new ButtonListener(); 
		saveButton = new JButton("저장하기");
		saveButton.addActionListener(buttonListener);
		loadButton = new JButton("불러오기");
		loadButton.addActionListener(buttonListener);
		resetButton = new JButton("재시작하기");
		resetButton.addActionListener(buttonListener);
		//프레임에 추가
		frame.add(saveButton);
		frame.add(loadButton);
		frame.add(resetButton);
	}
	public static Save getInstance() {
		if(instance == null) instance = new Save();
		return instance;
	}
	public void riseWindow() {
		// 프레임 객체 생성은 생성자에서만 
		// 프레임 객체 하나만으로 계속 사용
		frame.setVisible(true);
	}
	public void init() { // 객체 받아오기
		//(게임리셋을 하기 때문에 한 번만 받아오면 안 됨)
		observer = Observer.getInstance();
		gamePlayPanel = observer.getGamePlayPanel();
	}
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton button = (JButton)e.getSource();
			
			if(button == saveButton) {
				saving();
			}
			else if(button == loadButton) {
				loading();
			}
			else if(button == resetButton) {
				new Restart();
			}
		}
		
	}
	public void saving() {
		saveBlockArrayList(); //배열리스트 저장
		saveTurn(); // 턴 저장
	}
	public void loading() {
		new Restart(); // 재시작
		loadBlockArrayList(); 
		loadTurn();
		gamePlayPanel.repaint(); // 게임플레이패널 리페인트
	}
	public void saveBlockArrayList() {
		init();
		ArrayList<PointState> blockArrayList = gamePlayPanel.getBlockArrayList();
		
		try {
			FileOutputStream fileOut = new FileOutputStream("save//BlockArrayList");
			ObjectOutputStream arrayListOut = new ObjectOutputStream(fileOut);
			// 배열 리스트를 파일에 기록
			arrayListOut.writeObject(blockArrayList);
			fileOut.close();
			arrayListOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveTurn() {
		init();
		int turn = observer.getTurn()?1:0;
		
		try {
			FileOutputStream fileOut = new FileOutputStream("save//Turn");
			DataOutputStream intOut = new DataOutputStream(fileOut);
			
			intOut.writeInt(turn); // turn 을 파일에 기록
			fileOut.close();
			intOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void loadBlockArrayList() {
		init();
		ArrayList<PointState> blockArrayList = null;
		
		try {
			FileInputStream fileIn = new FileInputStream("save//BlockArrayList");
			ObjectInputStream arrayListIn = new ObjectInputStream(fileIn);
			//arrayList를 파일에서 가져옴
			blockArrayList = (ArrayList<PointState>)arrayListIn.readObject(); 
			fileIn.close();
			arrayListIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		gamePlayPanel.setBlockArray(blockArrayList);
		gamePlayPanel.setBlockArrayListToBlockArray();
	}
	public void loadTurn() {
		init();
		int turn = -1;
		
		try {
			FileInputStream fileIn = new FileInputStream("save//Turn");
			DataInputStream intIn = new DataInputStream(fileIn);
			
			turn = intIn.readInt();
			fileIn.close();
			intIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		observer.turn = turn==1?true:false;
	}
}
