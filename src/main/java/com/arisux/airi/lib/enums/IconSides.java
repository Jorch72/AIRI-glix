package com.arisux.airi.lib.enums;

public enum IconSides
{
	BOTTOM(0), TOP(1), BACK(2), FRONT(3), LEFT(4), RIGHT(5);
	
	public int side;
	
	IconSides(int side)
	{
		this.side = side;
	}
	
	public int getSide()
	{
		return side;
	}
	
	public static IconSides getSideFor(int side)
	{
		if (BOTTOM.side == side)
			return BOTTOM;
		if (TOP.side == side)
			return TOP;
		if (BACK.side == side)
			return BACK;
		if (FRONT.side == side)
			return FRONT;
		if (LEFT.side == side)
			return LEFT;
		if (RIGHT.side == side)
			return RIGHT;
		
		return null;
	}
}