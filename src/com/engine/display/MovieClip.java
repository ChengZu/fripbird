package com.engine.display;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;

import com.engine.Engine;

public class MovieClip extends Sprite{

	public String name = Engine.NameUtil.createUniqueName("MovieClip");
	public ArrayList<Frame> frames=new  ArrayList<Frame>();
	public String mode = "";	
	public int currentFrame = 1; //starts from 1
	public DisplayObject frameDisObj = null;
	public int pauseFrames = 0;
	public boolean paused = false;
	public long time=200;
	private long pasttime=0;
	
	public MovieClip(){}

	public void addFrame(Frame frame){
		this.setFrame(this.frames.size(), frame);
	}
	
	public void addFrameAt(Frame frame, int frameNumber)
	{
		this.setFrame(frameNumber , frame);
	}
	
	public void setFrame (int frameNumber, Frame frame)
	{
		this.frames.add(frameNumber, frame);
		if(frame.pauseFrames!=0)this.pauseFrames=frame.pauseFrames;
		if(frameNumber == 1)
		{
			this.frameDisObj = frame.disObj;
			this.width = Math.max(this.width, this.frameDisObj.width);
			this.height = Math.max(this.height, this.frameDisObj.height);
		}
	}
	public int getFrameNumber(int num){
		return 0;
	}
	public void render(Graphics2D context)
	{
		Frame frame = this.getFrame(this.currentFrame);
		//remove display object of last frame
		//if(this._frameDisObj!=null && this._frameDisObj != frame.disObj) this.removeChild(this._frameDisObj);
		//add display object of current frame	
		//this.addChildAt(frame.disObj, 0);
		this.frameDisObj = frame.disObj;	
		this.width = Math.max(this.width, this.frameDisObj.width);
		this.height = Math.max(this.height, this.frameDisObj.height);
		if(frame.stop) this.stop();
		//render children
		this.frameDisObj._render(context, false, false);;


		//go to next frame
		if(!this.paused && context == this.getStage().context && this.canNextFrame()) this.nextFrame();
	}

	public void stop() {
		this.paused = true;
	}
	public  void gotoAndStop(int i) {
		this.currentFrame = i;
		this.paused = true;
			
	}
	private boolean canNextFrame(){
		long now= new Date().getTime();
		if((now-this.pasttime)>time){
			this.pasttime=now;
			return true;
		}
		return false;
	}
	private int nextFrame() {
		Frame frame=this.getFrame(this.currentFrame);
		//pause frames
		if(frame.pauseFrames!=0)
		{		
			if(frame.pauseFrames > this.pauseFrames) this.pauseFrames++;	
			else this.pauseFrames = 0;
		}
		
		//go to a specific frame
		if(frame.gotoFrame!=0) 
		{
			if(this.pauseFrames == 0 || frame.pauseFrames==0) 
			{
				return this.currentFrame = this.getFrameNumber(frame.gotoFrame);
			}
		}
		
		if(frame.pauseFrames!=0 && this.pauseFrames > 0) return this.currentFrame;
		else if(this.currentFrame >= this.frames.size()) return this.currentFrame = 1;
		else return ++this.currentFrame;
	}

	private Frame getFrame(int currentFrame) {
		return this.frames.get(currentFrame-1);
	}
}
