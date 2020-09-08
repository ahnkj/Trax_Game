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
            AudioInputStream bgm = AudioSystem.getAudioInputStream(new File(bgmName));//게임 실행 BGM AudioInputStream 객체에 주기
            Clip bgmClip = AudioSystem.getClip();//클립에 할당해주기
            bgmClip.open(bgm);//파일 열기
            bgmClip.start();
           while(true)
           {
              i++;
               try{
                   sleep(10);
               }catch(InterruptedException e){}
               if(i==3000)//30초마다 노래 새로 틀기
             {
                  bgmClip.stop();//노래 멈추기
                  bgmClip.close();//노래 파일 닫기
                  bgm = AudioSystem.getAudioInputStream(new File(bgmName));//게임 실행 BGM AudioInputStream 객체에 주기
                  bgmClip = AudioSystem.getClip();//클립에 할당해주기
                  bgmClip.open(bgm);//파일 열기
                  bgmClip.start();
                  i=0;
               }
               if(bgmEnd==0)
             {
                  bgmClip.stop();//노래 멈추기
                  bgmClip.close();//노래 파일 닫기
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