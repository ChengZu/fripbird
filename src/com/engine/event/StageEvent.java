package com.engine.event;

public class StageEvent extends EventBase{
	public int mouseX;
	public int mouseY;
	public final static String ENTER_FRAME = "enterframe";
	public final static String MOUSE_DOWN = "mousedown";
	public final static String MOUSE_UP = "mouseup";
	public final static String MOUSE_MOVE = "mousemove";
	public final static String MOUSE_OVER = "mouseover";
	public final static String MOUSE_OUT = "mouseout";
	
	public StageEvent() {
	}
	
	public StageEvent(String type) {
		this.type=type;
	}
	
	public StageEvent(String type, boolean bubbles, boolean cancelable) {
		super(type, bubbles, cancelable);
		this.mouseX=0;
		this.mouseY=0;
	}

	public String toString(){
		return "[StageEvent type=" + this.type + ", mouseX=" + this.mouseX + ", mouseY=" + this.mouseY + "]";
	}
}
