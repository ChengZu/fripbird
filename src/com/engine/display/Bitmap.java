package com.engine.display;

import java.awt.Graphics2D;
import java.awt.Image;

import com.engine.Engine;

public class Bitmap  extends DisplayObject{

	public String name = Engine.NameUtil.createUniqueName("Bitmap");
	
	public Image image;	
	public int[] frame={0,0,0,0};
	
	public Bitmap(Image image){
		this.init(image, x, y, image.getWidth(null), image.getHeight(null));		
	}
	public Bitmap(Image image, int x, int y, int width, int height){
		this.init(image, x, y, width, height);
	}
	private void init(Image image, int x, int y, int width, int height){
		this.mouseEnabled = false;
		this.image=image;
		this.frame[0]=x;
		this.frame[1]=y;
		this.width=frame[2]=width;
		this.height=frame[3]=height;

	}
	public void render(Graphics2D context)
	{
		context.drawImage(this.image, 0, 0, frame[2], frame[3], frame[0], frame[1], frame[0]+frame[2], frame[1]+frame[3],null);
	}
}
