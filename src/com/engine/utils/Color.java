package com.engine.utils;

public class Color {
	public int r=0;
	public int g=0;
	public int b=0;
	public double a=0;
	/**
	 * Constructor.
	 * @name Color
	 * @class The Color class provides methods for manipulating colors.
	 * @param r red 0-255
	 * @param g green 0-255
	 * @param b blue 0-255
	 * @param a alpha 0-1
	 */
	public Color() {
		// TODO Auto-generated constructor stub
	}
	public Color(int r, int g, int b,double a)
	{
		this.r = r ;
		this.g = g ;
		this.b = b ;
		this.a = a ;
	}





	/**
	 * Returns the R+G+B combination currently in use by the color object.
	 */
	public String getRGB ()
	{
		return "rgb(' + this.r + ',' + this.g + ',' + this.b + ')";
	}

	/**
	 * Returns the R+G+B+A combination currently in use by the color object.
	 */
	public String getRGBA ()
	{
		return "rgb(' + this.r + ',' + this.g + ',' + this.b + ',' + this.a + ')";
	}

	/**
	 * Returns the HEX string currently in use by the color object.
	 */
	public String getHEX ()
	{
		String r = Integer.toHexString(this.r);
	    r = (r.length() == 1) ? ('0' + r) : r;
	    String g = Integer.toHexString(this.g);
	    g = (g.length() == 1) ? ('0' + g) : g;
	    String b = Integer.toHexString(this.b);
	    b = (b.length() == 1) ? ('0' + b) : b;
	    return r.toUpperCase() + g.toUpperCase() + b.toUpperCase();
	}

	/**
	 * Converts the color to grayscale mode.
	 */
	public void toGrayscale ()
	{
		int average = (int)(this.r * 0.3 + this.g * 0.59 + this.b * 0.11);
	   	this.r = this.g = this.b = average;
	}

	/**
	 * Converts the color to black and white mode.
	 */
	public void toBlackWhite ()
	{
		int threshold = 127;
		int average = (int)(this.r * 0.3 + this.g * 0.59 + this.b * 0.11);    
	    average = average < threshold ? 0 : 255;
		this.r = this.g = this.b = average;
	}
	public void toBlackWhite (int threshold)
	{
		int average = (int)(this.r * 0.3 + this.g * 0.59 + this.b * 0.11);    
	    average = average < threshold ? 0 : 255;
		this.r = this.g = this.b = average;
	}
}
