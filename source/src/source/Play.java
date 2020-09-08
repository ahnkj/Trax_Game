package source;

import java.awt.Dimension;

import javax.swing.JFrame;

//실행하기 위한 클래스 
public class Play {
	
	public static void main(String args[]){
		Frame frame;
		
		frame = new Frame();
		frame.setSize(new Dimension(815,760));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Dihan World");
		frame.setLayout(null);
		frame.setVisible(true);
		Play.frame=frame;
	}
	static Frame frame;
}
