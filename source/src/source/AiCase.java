package source;

public class AiCase {
	int state;
	int type;
	int val;
	
	public AiCase()
	{
		val=state=type=-1;
	}
	
	public AiCase(int t ,int s, int v)
	{
		state=s;
		type=t;
		val=v;
	}
}
