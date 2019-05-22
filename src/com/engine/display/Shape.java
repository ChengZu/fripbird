package com.engine.display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.engine.Engine;

public class Shape extends DisplayObject {
	public String name = Engine.NameUtil.createUniqueName("Shape");
	public BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
	
	public Graphics2D graphics = this.image.createGraphics();

	public void Shap() {
		this.mouseEnabled = false;
		this.x=149;
		this.y=110;
		this.width=100;
		this.height=100;
	}

	public void render(Graphics2D context) {
		context.drawImage(this.image, this.x, this.y, null);
	}
}
