package com.engine.display;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class DisplayObjectContainer extends DisplayObject {
	public ArrayList<DisplayObject> children=new ArrayList<DisplayObject>();
	public boolean mouseChildren=true;
	
	public DisplayObject addChildAt (DisplayObject child, int index)
	{
		if(index < 0) index = 0;
		else if(index > this.children.size()) index = this.children.size();
		
		int childIndex = this.getChildIndex(child);
		if(childIndex != -1)
		{
			if(childIndex == index) return child;
			this.children.remove(childIndex);
			index--;
		}else if(child.parent!=null)
		{
			((DisplayObjectContainer)child.parent).removeChild(child);
		}
		this.children.add(index, child);
		child.parent = this;
		return child;
	}
	/**
	 * Adds a child DisplayObject instance to this DisplayObjectContainer instance.
	 */
	public DisplayObject addChild (DisplayObject child)
	{	
		return this.addChildAt(child, this.children.size());
	}
	
	/**
	 * Removes a child DisplayObject from the specified index position in the child list of the DisplayObjectContainer.
	 */
	public boolean removeChildAt(int index)
	{
		if (index < 0 || index >= this.children.size()) return false;
		DisplayObject child = this.children.get(index);
		if (child != null) 
		{
			child.parent = null;
			child.stage = null;
		}
		this.children.remove(index);
		return true;
	}
	
	
	public boolean removeChild (DisplayObject child)
	{
		return this.removeChildAt(this.children.indexOf(child));
	}
	public boolean removeChildByName (String name)
	{	
		for(int i = 0, len = this.children.size(); i < len; i++)
		{
			if(this.children.get(i).name == name) 
			{
				return this.removeChildAt(i);
			}
		}
		return false;
	}
	/**
	 * Removes all children of the DisplayObjectContainer.
	 */
	public boolean removeAllChildren ()
	{
		while(this.children.size() > 0) this.removeChildAt(0);
		return true;
	}
	/**
	 * Returns the child display object that exists with the specified name.
	 */
	public DisplayObject getChildByName(String name)
	{
		for(int i = 0, len = this.children.size(); i < len; i++)
		{
			if(this.children.get(i).name == name) return this.children.get(i);
		}
		return null;
	}
	/**
	 * Returns the child display object instance that exists at the specified index.
	 */
	public DisplayObject getChildAt(int index)
	{
		if (index < 0 || index >= this.children.size()) return null;
		return this.children.get(index);
	}
	
	
	public int getChildIndex(DisplayObject child)
	{
		return this.children.indexOf(child);
	}
	
	/**
	 * Changes the position of an existing child in the display object container.
	 */
	public void setChildIndex(DisplayObject child, int index)
	{
		if(child.parent != this) return;
		int oldIndex = this.children.indexOf(child);
		if(index == oldIndex) return;
		this.children.remove(oldIndex);
		this.children.add(index,child);
	}

	/**
	 * Determines whether the specified display object is a child of the DisplayObjectContainer instance or the instance itself.
	 */
	public boolean contains(DisplayObject child)
	{
		return this.getChildIndex(child) != -1;
	}

	/**
	 * Returns the number of children of this object.
	 */
	public int getNumChildren()
	{
		return this.children.size();
	}
	
	/**
	 * Returns an array of objects that lie under the specified point and are children(or grandchildren, and so on) of this DisplayObjectContainer instance.
	 */
	public ArrayList<DisplayObject> getAllObjectUnderPoint (int x, int y, boolean usePixelCollision, int tolerance, boolean returnAll)
	{
		ArrayList<DisplayObject> result=new ArrayList<DisplayObject>();
		for(int i = this.children.size() - 1; i >= 0; i--)
		{
			DisplayObject child = this.children.get(i);
			if(child == null || !child.mouseEnabled || !child.visible || child.alpha <= 0) continue;
			if((child instanceof DisplayObjectContainer) && ((DisplayObjectContainer) child).mouseChildren && ((DisplayObjectContainer) child).getNumChildren() > 0)
			{	
				ArrayList<DisplayObject> obj = ((DisplayObjectContainer) child).getAllObjectUnderPoint(x, y, usePixelCollision, tolerance, returnAll);
				if(obj.size()>0)
				{	
					result.addAll(obj);
					if(!returnAll){
						result.clear();
						result.add(obj.get(0));
						break;
					}
				}else if(child.hitTestPoint(x, y, usePixelCollision, tolerance)){
					result.add(child);
					if(!returnAll) break;
				}	
			
			}else{
				if(child.hitTestPoint(x, y, usePixelCollision, tolerance)) 
				{
					result.add(child);
					if(!returnAll) break;
				}
			}
		}
		return result;
	}
	public DisplayObject getObjectUnderPoint (int x, int y, boolean usePixelCollision, int tolerance)
	{
		return this.getAllObjectUnderPoint(x, y, usePixelCollision, tolerance, false).size()>0?this.getAllObjectUnderPoint(x, y, usePixelCollision, tolerance, false).get(0) :null;
	}
	/**
	 * @private Renders all children of the DisplayObjectContainer onto specific context.
	 */
	public void render(Graphics2D context)
	{
		for(int i = 0; i < this.children.size(); i++)
		{
			this.children.get(i)._render(context,false,false);
		}
	}
	
}
