package com.engine.utils;

import com.engine.display.DisplayObject;
import com.engine.display.Stage;

public class NameUtil {
	public int _counter=0;
	public int getUID(){
		return _counter++;
	}
	public String createUniqueName(String name){
		//if end with a digit, then append an underscore before appending
		char charCode = name.charAt(name.length() - 1);
	    if (charCode >= 48 && charCode <= 57) name += "_";
	    return name + getUID();
	}
	
	/**
	 * Returns a string, such as "Stage0.scene1.buttonContainer2.Sprite3", for a DisplayObject object that indicates its position in the ierarchy of DisplayObject objects in an application.
	 */
	
	public String displayObjectToString (DisplayObject displayObject)
	{
		String result="";
		for(DisplayObject o = displayObject; o != null; o = o.parent)
		{		
			//prefer id over name if specified
	        String s = o.id != -1? o.id+"" : o.name;
	        result = result == "" ? s : (s + "." + result);
	        if (o == o.parent || (o.parent instanceof Stage)) break;
		}
		return result;
	}
}
