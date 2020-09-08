package source;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpFrame //extends JFrame
{
   static public int mode=0;//
   Button left ;    //��ư ���� ������ �����
   Button right ;
   Panel bgp;
   Image image;
   Toolkit toolkit;
   int rowPictureNum = 1, loopPictureNum = 1;
   Graphics gTemp;
   Timer rowTimer, loopTimer;
   
   public HelpFrame()
   {


      JFrame helpframe = new JFrame("HELP");   //�������� ��������� �̸��� ��������
      helpframe.setSize(1000,500);  //������ ������ ����
      helpframe.setLocation(815,0);
      toolkit = Toolkit.getDefaultToolkit();
      
      left = new Button("����");
      right = new Button("����");

      bgp = new Panel()  //�͸�Ŭ������ �г� ����
            {
         public void paint(Graphics g)
         {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image img = tk.getImage("img\\helpBackground.jpg");//�̹��� �б�
            g.drawImage(img, 0, 0, this);//�׸� ���
            System.out.println("paint");
            //���ӹ��
            if(HelpFrame.getMode()==0)
            {

               g.setFont(new Font("����ü", Font.PLAIN, 15));       //GOTHIC
               g.drawString("< ���ӹ�� >", 450, 100);
               g.drawString("���� ��ܿ� ���̴� Ÿ���̹�������", 350, 150);
               g.drawString("Ÿ���� �����ؼ� ���ϴ� ��ġ�� Ÿ���� �ξ�", 350, 200);
               g.drawString("���� ������ ����ų�", 350, 250);
               g.drawString("��� �÷��̾ 8�� �̻��� ������ ����� �¸��Ѵ�", 350, 300);

            }
            else if(HelpFrame.getMode()==1)
            {

               g.setFont(new Font("����ü", Font.PLAIN, 15));       //GOTHIC
               g.drawString("< ���ӱ�Ģ >", 450, 100);
               g.drawString("- ȭ��Ʈ �÷��̾ �������� �� �÷��̾�� �ڽ��� ���ʰ� �Ǹ� Ÿ���� �д�.", 50, 150);
               g.drawString("- ó�� Ÿ���� �ΰ� �� �ĺ��ʹ� �������� �𼭸����� ����� Ÿ�� �߿� �ϳ��� �� �� �ִ�.", 50, 200);
               g.drawString("- �÷��̾�� �ϳ� �Ǵ� �ΰ��� �������� ������ �� �� �ִ�.", 50, 250);
               g.drawString("- ���ο� Ÿ���� �߰� �� �� ������ �� ���� �� �´��� 2���� ����� ���� ���� ��찡 �ִ��� Ȯ���Ͽ� �ִٸ� �˸��� Ÿ���� �߰��ȴ�.", 50, 300);
               g.drawString("- ���ο� Ÿ���� �߰� �� ��  �� ���� �� �´��� 3���� ����� ���� ���� ��찡 �ִ��� Ȯ���Ͽ� �ִٸ� �� ���� �ٽ� �ϵ��� �Ѵ�.", 50, 350);

            }
            else if(HelpFrame.getMode()==2) {
               image = toolkit.getImage("img\\guide.PNG");
               g.drawImage(image, 0, 0, 1000, 500, bgp);
            }
            else if(HelpFrame.getMode()==3) {
               if(rowPictureNum > 13) rowPictureNum = 1;
               image = toolkit.getImage("img\\8��\\"+rowPictureNum+".jpg");
               g.drawImage(image, 282,167, this);
             
               if(rowTimer == null)
                  rowTimer = new Timer(400, rowListener);
               rowTimer.start();
            }
            else if(HelpFrame.getMode()==4) {
               if(loopPictureNum > 5) loopPictureNum = 1;
               image = toolkit.getImage("img\\����\\"+loopPictureNum+".jpg");
               g.drawImage(image,394,192, this);
               
               if(loopTimer == null)
                  loopTimer = new Timer(400, loopListener);
               loopTimer.start();
            }
         }
            };

            bgp.setSize(1000,500); //�ǳ� ������ ����
            bgp.setLocation(0,0);  //�ǳ� ��ġ ����
            helpframe.add(bgp);  //�����ӿ� �ǳ� �����Ű��

            //Button left = new Button("left");    //��ư ���� ������ �����
            //Button right = new Button("����");

            left.setLocation(500,500);   //��ư ��ġ��Ű��
            right.setLocation(540,500);
            bgp.add(left);                   //��ư �ǳڿ� ����
            bgp.add(right);


            ClickListener2 listener = new ClickListener2();   //��ư �̺�Ʈ ó��

            left.addActionListener(listener);
            right.addActionListener(listener);


            helpframe.setLayout(null);   //�����ӿ� �⺻������ ���̾ƿ��� ���ش�.�Ⱦ��� ȭ����ü�� ����
            helpframe.setVisible(true);  //������ ���̱�
            helpframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //x Ŭ���� �ش� â�� �����
   }
   static private int getMode(){ // �ȿ����� ���� ���̹Ƿ� private���� �ٲ�
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