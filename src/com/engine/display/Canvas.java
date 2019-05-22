package com.engine.display;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class Canvas {
	private int width;
	private int height;
	public BufferedImage image;
	public Graphics2D context;

	public Canvas() {
		this.init(600, 480);
	}

	public Canvas(int width, int height) {
		this.init(width, height);
	}

	private void init(int width, int height) {
		this.width = width;
		this.height = height;
		this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		this.context = this.image.createGraphics();
		this.image = this.context.getDeviceConfiguration().createCompatibleImage(this.width, this.height, Transparency.TRANSLUCENT);		 
		this.context.dispose();
		this.context = this.image.createGraphics();
	}

	public void setWidth(int width) {
		this.init(width, this.height);
	}

	public void setHeight(int height) {
		this.init(this.width, height);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void clear() {
		this.init(this.width, this.height);
	}
}
