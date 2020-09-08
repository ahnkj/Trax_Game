package source;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpFrame //extends JFrame
{
   static public int mode=0;//
   Button left ;    //버튼 왼쪽 오른쪽 만들기
   Button right ;
   Panel bgp;
   Image image;
   Toolkit toolkit;
   int rowPictureNum = 1, loopPictureNum = 1;
   Graphics gTemp;
   Timer rowTimer, loopTimer;
   
   public HelpFrame()
   {


      JFrame helpframe = new JFrame("HELP");   //프레임이 만들어지고 이름이 정해진다
      helpframe.setSize(1000,500);  //프레임 사이즈 설정
      helpframe.setLocation(815,0);
      toolkit = Toolkit.getDefaultToolkit();
      
      left = new Button("이전");
      right = new Button("이후");

      bgp = new Panel()  //익명클래스로 패널 생성
            {
         public void paint(Graphics g)
         {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image img = tk.getImage("img\\helpBackground.jpg");//이미지 읽기
            g.drawImage(img, 0, 0, this);//그림 출력
            System.out.println("paint");
            //게임방식
            if(HelpFrame.getMode()==0)
            {

               g.setFont(new Font("돋움체", Font.PLAIN, 15));       //GOTHIC
               g.drawString("< 게임방법 >", 450, 100);
               g.drawString("왼쪽 상단에 보이는 타일이미지에서", 350, 150);
               g.drawString("타일을 선택해서 원하는 위치에 타일을 두어", 350, 200);
               g.drawString("먼저 루프를 만들거나", 350, 250);
               g.drawString("상대 플레이어가 8개 이상의 라인을 만들면 승리한다", 350, 300);

            }
            else if(HelpFrame.getMode()==1)
            {

               g.setFont(new Font("돋움체", Font.PLAIN, 15));       //GOTHIC
               g.drawString("< 게임규칙 >", 450, 100);
               g.drawString("- 화이트 플레이어를 시작으로 각 플레이어는 자신의 차례가 되면 타일을 둔다.", 50, 150);
               g.drawString("- 처음 타일을 두고 난 후부터는 같은색의 모서리끼리 연결된 타일 중에 하나를 둘 수 있다.", 50, 200);
               g.drawString("- 플레이어는 하나 또는 두개의 색상으로 게임을 할 수 있다.", 50, 250);
               g.drawString("- 새로운 타일이 추가 될 시 인접한 빈 공간 중 맞닿은 2개의 경로의 색이 같은 경우가 있는지 확인하여 있다면 알맞은 타일을 추가된다.", 50, 300);
               g.drawString("- 새로운 타일이 추가 될 시  빈 공간 중 맞닿은 3개의 경로의 색이 같은 경우가 있는지 확인하여 있다면 그 턴을 다시 하도록 한다.", 50, 350);

            }
            else if(HelpFrame.getMode()==2) {
               image = toolkit.getImage("img\\guide.PNG");
               g.drawImage(image, 0, 0, 1000, 500, bgp);
            }
            else if(HelpFrame.getMode()==3) {
               if(rowPictureNum > 13) rowPictureNum = 1;
               image = toolkit.getImage("img\\8행\\"+rowPictureNum+".jpg");
               g.drawImage(image, 282,167, this);
             
               if(rowTimer == null)
                  rowTimer = new Timer(400, rowListener);
               rowTimer.start();
            }
            else if(HelpFrame.getMode()==4) {
               if(loopPictureNum > 5) loopPictureNum = 1;
               image = toolkit.getImage("img\\루프\\"+loopPictureNum+".jpg");
               g.drawImage(image,394,192, this);
               
               if(loopTimer == null)
                  loopTimer = new Timer(400, loopListener);
               loopTimer.start();
            }
         }
            };

            bgp.setSize(1000,500); //판넬 사이즈 설정
            bgp.setLocation(0,0);  //판넬 위치 설정
            helpframe.add(bgp);  //프레임에 판넬 적용시키기

            //Button left = new Button("left");    //버튼 왼쪽 오른쪽 만들기
            //Button right = new Button("이후");

            left.setLocation(500,500);   //버튼 위치시키기
            right.setLocation(540,500);
            bgp.add(left);                   //버튼 판넬에 적용
            bgp.add(right);


            ClickListener2 listener = new ClickListener2();   //버튼 이벤트 처리

            left.addActionListener(listener);
            right.addActionListener(listener);


            helpframe.setLayout(null);   //프레임에 기본설정된 레이아웃을 없앤다.안쓰면 화면전체에 꽉참
            helpframe.setVisible(true);  //프레임 보이기
            helpframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //x 클릭시 해당 창만 종료됨
   }
   static private int getMode(){ // 안에서만 쓰일 것이므로 private으로 바꿈
      return mode;
   }
   class RowListener implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         if(HelpFrame.getMode() != 3 && rowTimer != null) {
            rowTimer.stop(); return; 
         }
         rowPictureNum++;
         bgp.repaint();
      }
   }
   class LoopListener implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         if(HelpFrame.getMode() != 4 && loopTimer != null) {
            loopTimer.stop(); return; 
         }
         loopPictureNum++;
         bgp.repaint();
      }
   }
   RowListener rowListener = new RowListener();
   LoopListener loopListener = new LoopListener();
   
   public class ClickListener2 implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {   Object obj;
      obj=event.getSource();
      System.out.println(obj);
      if(obj==left)
      {
         mode--;
         if(mode < 0) mode++;
         bgp.repaint();
      }
      else
      {
         mode++;
         if(mode > 4) mode--;
         bgp.repaint();
      }

      }
   }
   
   public static void main(String[] args)
   {
      new HelpFrame();
   }
}