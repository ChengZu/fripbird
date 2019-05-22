package com.engine.geom;

public class Point {
	public int x=0;
	public int y=0;
	public Point(){
	}
	public Point(int x,int y){
		this.x = x;
		this.y = y;
	}
	//TODO: manipulation functions.
	
	/**
	 * Creates a copy of this Point object.
	 */
	public Point clone ()
	{
		return new Point(this.x, this.y);	
	}

	/**
	 * Returns a string that contains the values of the x and y coordinates.
	 */
	public String toString ()
	{
		return "(x=" + this.x + ", y=" + this.y + ")";	
	}
}
