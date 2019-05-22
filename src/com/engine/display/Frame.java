package com.engine.display;

public class Frame {
	public DisplayObject disObj; //display object of the frame
	public String mode = ""; //label for the frame
	public int gotoFrame = 0; //can be either frameNumber or frameLabel
	public int pauseFrames = 0; //number of frames to pause
	public boolean stop = false; //whether stop after this frame
	public Frame(DisplayObject disObj, String mode, int gotoFrame, int pauseFrames, boolean stop)
	{
		this.disObj = disObj;
		this.mode = mode ; 
		this.gotoFrame = gotoFrame ;
		this.pauseFrames = pauseFrames; 
		this.stop = stop; 
	}

	public Frame(DisplayObject disObj) {
		this.disObj = disObj;
	}
}
