package com.engine.event;

import java.util.ArrayList;

public class EventManager {
	public ArrayList<mapData> _maps=new  ArrayList<mapData>();
	
	public boolean addEventListener(EventDispatcher src, String type, EventListener listener) {
		mapData map=new mapData(src ,type, listener);
		if(!this._maps.contains(map))
		return this._maps.add(new mapData(src ,type, listener));
		return false;
	}
	public boolean removeEventListener(EventDispatcher src, String type, EventListener listener) {
		return this._maps.remove(new mapData(src ,type, listener));
	}
	public boolean removeEventListenerByType(EventDispatcher src, String type) {
		for(int i=0; i<this._maps.size(); i++){
			if(this._maps.get(i).src.equals(src) && this._maps.get(i).type.equals(type)){
				this._maps.remove(i);
				i--;
			}
		}
		return true;
	}
	public boolean removeAllEventListeners(EventDispatcher src) {
		for(int i=0; i<this._maps.size(); i++){
			if(this._maps.get(i).src.equals(src)){
				this._maps.remove(i);
				i--;
			}
		}
		return true;
	}
	public boolean dispatchEvent(EventDispatcher src, StageEvent event) {
		for(int i=0; i<this._maps.size(); i++){
			if((this._maps.get(i).src.equals(event.target)|| this._maps.get(i).type.equals(StageEvent.ENTER_FRAME)) && this._maps.get(i).type.equals(event.type)){	
				this._maps.get(i).listener.somethingDo(event);
			}
			//???
		}
		return true;
	}
	public boolean hasEventListener(EventDispatcher src, String type) {
		for(int i=0; i<this._maps.size(); i++){
			if(this._maps.get(i).src.equals(src) && this._maps.get(i).type.equals(type))
				return true;
		}
		return false;
	}
	class mapData{
		public EventDispatcher src;
		public String type;
		public EventListener listener;
		public mapData(EventDispatcher src, String type, EventListener listener){
			this.src=src;
			this.type=type;
			this.listener=listener;
		}
	}
}
