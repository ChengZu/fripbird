package com.engine.display;

import java.awt.Font;
import java.awt.Graphics2D;

import com.engine.Engine;

public class Text extends DisplayObject{
	public String name = Engine.NameUtil.createUniqueName("Text");	
	public String text = "";
	public Font font=new Font("Arial",Font.BOLD,16);
		
	public Text(String str){
		this.text=str;
		this.mouseEnabled=false;
		this.width=this.text.length()*this.font.getSize();
		this.height=this.font.getSize();
	}

	public Text(String str, Font font){
		this.text=str;
		this.font=font;
		this.mouseEnabled=false;
		this.width=this.text.length()*this.font.getSize();
		this.height=this.font.getSize();
	}
	
	public void render(Graphics2D context)
	{
		Font savaFont=context.getFont();
		context.setFont(this.font);
		context.drawString(this.text, this.font.getSize(), this.font.getSize());
		context.setFont(savaFont);
	}
}
