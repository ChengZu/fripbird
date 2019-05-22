package com.engine.geom;

public class Rectangle {
	public int x=0;
	public int y=0;
	public int width=0;
	public int height=0;
	public Rectangle (int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	//TODO: more manipulation functions.

	public Rectangle() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns whether this rectangle intersects the rectangle parameter.
	 * @param rect A Rectangle.
	 */
	public boolean intersects (Rectangle rect)
	{
		return (this.x <= rect.x + rect.width && rect.x <= this.x + this.width &&
				this.y <= rect.y + rect.height && rect.y <= this.y + this.height);
	};

	/**
	 * Computes the intersection of this rectangle and the rectangle parameter. 
	 * @param rect A Rectangle.
	 */
	public Rectangle intersection (Rectangle rect)
	{
		int x0 = Math.max(this.x, rect.x);
		int x1 = Math.min(this.x + this.width, rect.x + rect.width);

		if(x0 <= x1)
		{
			int y0 = Math.max(this.y, rect.y);
			int y1 = Math.min(this.y + this.height, rect.y + rect.height);

			if(y0 <= y1)
			{
				return new Rectangle(x0, y0, x1 - x0, y1 - y0);
			}
		}
		return null;
	};

	/**
	 * Checks this rectangle whether contains the point parameter.
	 */
	public boolean containsPoint (int x, int y)
	{
		return (this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height);
	}

	/**
	 * Creates a copy of this Rectangle object.
	 */
	public Rectangle clone ()
	{
		return new Rectangle(this.x, this.y, this.width, this.height);	
	}

	/**
	 * Returns a string that represent this Rectangle object.
	 */
	public String toString ()
	{
		return "(x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ")";	
	}
}
