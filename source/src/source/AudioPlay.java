package source;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlay  extends Thread{
   private int bgmEnd;
   private String bgmName;
   public AudioPlay(String bgm){
      bgmEnd=1;
      bgmName=bgm;
      start();
   }
   public void myBgmEnd()
   {
      bgmEnd=0;
   }
   public void run()
   {
      int i=0;
        try
        {
            AudioInputStream bgm = AudioSystem.getAudioInputStream(new File(bgmName));//���� ���� BGM AudioInputStream ��ü�� �ֱ�
            Clip bgmClip = AudioSystem.getClip();//Ŭ���� �Ҵ����ֱ�
            bgmClip.open(bgm);//���� ����
            bgmClip.start();
           while(true)
           {
              i++;
               try{
                   sleep(10);
               }catch(InterruptedException e){}
               if(i==3000)//30�ʸ��� �뷡 ���� Ʋ��
             {
                  bgmClip.stop();//�뷡 ���߱�
                  bgmClip.close();//�뷡 ���� �ݱ�
                  bgm = AudioSystem.getAudioInputStream(new File(bgmName));//���� ���� BGM AudioInputStream ��ü�� �ֱ�
                  bgmClip = AudioSystem.getClip();//Ŭ���� �Ҵ����ֱ�
                  bgmClip.open(bgm);//���� ����
                  bgmClip.start();
                  i=0;
               }
               if(bgmEnd==0)
             {
                  bgmClip.stop();//�뷡 ���߱�
                  bgmClip.close();//�뷡 ���� �ݱ�
                  bgmEnd=1;
                  break;
                  }
               }
           }
        catch (Exception ex)
        {
        }
    }
}