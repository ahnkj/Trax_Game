package source;

public class AiType {
int type;
double value;

public AiType()
{
	type=2;
	value=0;
}

public AiType(int type)
{
	this.type=type;
	value=0;
}

public AiType(int type, double val)
{
	this.type = type;
	this.value=val;
}
}
