package com.engine.event;

public abstract class EventBase {
	public Object type;
	public Object target;
	public Object currentTarget;
	public Object params;
	
	public boolean bubbles=false;
	public boolean cancelable=false;
	public EventBase(String type ,boolean bubbles, boolean cancelable){
		this.type=type;
		this.target=null;
		this.currentTarget=null;
		this.params=null;
		this.bubbles=bubbles;
		this.cancelable=cancelable;
	}
	
	public EventBase() {
		// TODO Auto-generated constructor stub
	}

	public void stopPropagation(){
		//TODO
	}
	
	public void preventDefault(){
		//TODO
	}
	
	public Object clone(){
		return this;
	}
	
	public void dispose(){
		this.type=null;
		this.target=null;
		this.currentTarget=null;
		this.params=null;
	}
	
	public String toString(){
		return "[EventBase type=" + this.type + "]";
	}
}
