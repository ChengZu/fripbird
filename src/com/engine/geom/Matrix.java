package com.engine.geom;

public class Matrix {
	public double a=1;
	public double b=0;
	public double c=0;
	public double d=1;
	public double tx=0;
	public double ty=0;
	public Matrix(){}
	
	public Matrix(double a, double b, double c, double d, double tx, double ty){
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
		this.tx=tx;
		this.ty=ty;
	}
	
	/**
	 * Concatenates a matrix with the current matrix, effectively combining the geometric effects of the two.
	 */
	public void concat(Matrix mtx){
		double a = this.a;
		double c = this.c;
		double tx = this.tx;
		
		this.a = a * mtx.a + this.b * mtx.c;
		this.b = a * mtx.b + this.b * mtx.d;
		this.c = c * mtx.a + this.d * mtx.c;
		this.d = c * mtx.b + this.d * mtx.d;
		this.tx = tx * mtx.a + this.ty * mtx.c + mtx.tx;
		this.ty = tx * mtx.b + this.ty * mtx.d + mtx.ty;
	}
	
	/**
	 * Concatenates a transformation with the current matrix, effectively combining the geometric effects of the two.
	 */
	public void concatTransform (double x, double y, double scaleX, double scaleY, double rotation, double regX, double regY)
	{
		double cos = 1;
		double sin = 0;
		if(rotation%360!=0)
		{
			double r = rotation * Math.PI / 180;
			cos = Math.cos(r);
			sin = Math.sin(r);
		}
		
		if(regX != 0) this.tx -= regX; 
		if(regY != 0) this.ty -= regY;
		this.concat(new Matrix(cos*scaleX, sin*scaleX, -sin*scaleY, cos*scaleY, x, y));
	}
	
	public void rotate (double angle)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		double a = this.a;
		double c = this.c;
		double tx = this.tx;
		
		this.a = a * cos - this.b * sin;
		this.b = a * sin + this.b * cos;
		this.c = c * cos - this.d * sin;
		this.d = c * sin + this.d * cos;
		this.tx = tx * cos - this.ty * sin;
		this.ty = tx * sin + this.ty * cos;
	}

	/**
	 * Applies a scaling transformation to the matrix.
	 */
	public void scale (double sx, double sy)
	{
		this.a *= sx;
		this.d *= sy;
		this.tx *= sx;
		this.ty *= sy;
	}

	/**
	 * Translates the matrix along the x and y axes, as specified by the dx and dy parameters.
	 */
	public void translate (double dx, double dy)
	{
		this.tx += dx;
		this.ty += dy;
	}

	/**
	 * Sets each matrix property to a value that causes a null transformation.
	 */
	public void identity ()
	{
		this.a = this.d = 1;
		this.b = this.c = this.tx = this.ty = 0;
	}

	/**
	 * Performs the opposite transformation of the original matrix.
	 */
	public void invert ()
	{
		double a = this.a;
		double b = this.b;
		double c = this.c;
		double d = this.d;
		double tx = this.tx;
		double i = a * d - b * c;
		
		this.a = d / i;
		this.b = -b / i;
		this.c = -c / i;
		this.d = a / i;
		this.tx = (c * this.ty - d * tx) / i;
		this.ty = -(a * this.ty - b * tx) / i;
	}
	/**
	 * Returns a new Matrix object that is a clone of this matrix, with an exact copy of the contained object.
	 */	
	public Matrix clone ()
	{
		return new Matrix(this.a, this.b, this.c, this.d, this.tx, this.ty);
	}

	/**
	 * Returns a String value listing the properties of the Matrix object.
	 */
	public String toString ()
	{
		return "(a="+this.a+", b="+this.b+", c="+this.c+", d="+this.d+", tx="+this.tx+", ty="+this.ty+")";
	}
}
