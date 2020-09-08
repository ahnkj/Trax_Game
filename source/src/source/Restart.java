package source;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Restart {
   public Restart() {
      Frame frame = Play.frame;
      frame.removeAll(); // add했던 것들 모두 삭제
      frame.setVisible(false);
      // 새 프레임 세팅
      frame = new Frame(); 
      Play.frame = frame; // 이 것도 필요함
      frame.setSize(new Dimension(815,760)); 
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("Dihan World");
      frame.setLayout(null); 
      frame.setVisible(true); 
   }
}