package source;

import java.awt.*;
import java.io.Serializable;

import javax.swing.*;

//���� ��ǥ�� ���̴� �������� ������ �����ϱ� ���� ���� Ŭ����
// ����ü�� ���� ������ ��
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
