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
		frame = new JFrame("����/�ҷ�����");
		frame.setSize(200, 100);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		//��ư ����
		buttonListener = new ButtonListener(); 
		saveButton = new JButton("�����ϱ�");
		saveButton.addActionListener(buttonListener);
		loadButton = new JButton("�ҷ�����");
		loadButton.addActionListener(buttonListener);
		resetButton = new JButton("������ϱ�");
		resetButton.addActionListener(buttonListener);
		//�����ӿ� �߰�
		frame.add(saveButton);
		frame.add(loadButton);
		frame.add(resetButton);
	}
	public static Save getInstance() {
		if(instance == null) instance = new Save();
		return instance;
	}
	public void riseWindow() {
		// ������ ��ü ������ �����ڿ����� 
		// ������ ��ü �ϳ������� ��� ���
		frame.setVisible(true);
	}
	public void init() { // ��ü �޾ƿ���
		//(���Ӹ����� �ϱ� ������ �� ���� �޾ƿ��� �� ��)
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
		saveBlockArrayList(); //�迭����Ʈ ����
		saveTurn(); // �� ����
	}
	public void loading() {
		new Restart(); // �����
		loadBlockArrayList(); 
		loadTurn();
		gamePlayPanel.repaint(); // �����÷����г� ������Ʈ
	}
	public void saveBlockArrayList() {
		init();
		ArrayList<PointState> blockArrayList = gamePlayPanel.getBlockArrayList();
		
		try {
			FileOutputStream fileOut = new FileOutputStream("save//BlockArrayList");
			ObjectOutputStream arrayListOut = new ObjectOutputStream(fileOut);
			// �迭 ����Ʈ�� ���Ͽ� ���
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
			
			intOut.writeInt(turn); // turn �� ���Ͽ� ���
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
			//arrayList�� ���Ͽ��� ������
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
