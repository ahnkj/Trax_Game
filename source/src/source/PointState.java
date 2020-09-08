package source;

import java.awt.*;
import java.io.Serializable;

import javax.swing.*;

//눌린 좌표와 놓이는 파이프의 종류를 저장하기 위해 만든 클래스
// 구조체와 같은 역할을 함
public class PointState implements Serializable {
	int x;
	int y;
	int state;
	
	public PointState(){
		x=y=0;
		state=6;
	}
	
	public PointState(int x, int y, int state){
		this.x=x;
		this.y=y;
		this.state= state;
	}

}
