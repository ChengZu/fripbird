package com.engine.display;

import com.engine.event.StageEvent;

public class Button extends MovieClip{
	//default state
	
	public static String UP="up";
	public static String OVER="over";
	public static String DOWN="down";
	public static String DISABLED="disable";
	public String state = Button.UP;

	public boolean enabled = true;
	//disable mouse children, so the button act as one object, and get fast mouse target tracing.
	public boolean mouseChildren = false;
	//use hand cursor default
	public boolean useHandCursor = true;
	private DisplayObject upState;
	private DisplayObject overState;
	private DisplayObject downState;
	private DisplayObject disabledState;
	public Button(){
		
	}
	public Button(DisplayObject up, DisplayObject over, DisplayObject down, DisplayObject disabled) {
		this.setUpState(up);
		this.setOverState(over);
		this.setDownState(down);
		this.setDisabledState(disabled);
		this.stop();
	}
	/**
	 * Sets display object for the normal state.
	 */
	public Button setUpState(DisplayObject upState)
	{
		Frame frame = new Frame(upState, null ,0,0,false);
		this.addFrameAt(frame, 0);
		this.upState = upState;
		return this;
	}

	/**
	 * Sets display object for the mouse over state.
	 */
	public Button setOverState(DisplayObject overState)
	{
		Frame frame = new Frame(overState, null ,0,0,false);
		this.addFrameAt(frame, 1);
		this.overState = overState;
		return this;
	}

	/**
	 * Sets display object for the mouse down state.
	 */
	public Button setDownState(DisplayObject downState)
	{
		Frame frame = new Frame(downState, null ,0,0,false);
		this.addFrameAt(frame, 2);
		this.downState = downState;
		return this;
	}

	/**
	 * Sets display object for the disbaled state.
	 */
	public Button setDisabledState(DisplayObject disabledState)
	{
		Frame frame = new Frame(disabledState, null ,0,0,false);
		this.addFrameAt(frame, 3);
		this.disabledState = disabledState;
		return this;
	}

	/**
	 * Enables or disables the button.
	 */
	public Button setEnabled(boolean enabled)
	{
		if(this.enabled == enabled) return this;
		this.mouseEnabled = this.enabled = enabled;	 
		if(!enabled)
		{
			if(this.disabledState!=null) this.gotoAndStop(4);
			else this.gotoAndStop(1);
		}else
		{
			if(this.currentFrame == 4) this.gotoAndStop(1);
		}
		return this;
	}

	/**
	 * Sets the button's state name. Developers can use it to change button state manually.
	 */

	 public void  setState(String state)
	{
		if(this.state == state) return;
		this.state = state;
		if(!this.enabled) this.mouseEnabled = this.enabled = true;
		if(state.equals(Button.UP)){
			this.gotoAndStop(1);
		}else if(state.equals(Button.OVER)){
			this.gotoAndStop(2);
		}else if(state.equals(Button.DOWN)){
			this.gotoAndStop(3);
		}else if(state.equals(Button.DISABLED)){
			this.setEnabled(false);
		}
		
	}

	/**
	 * Internal handler for mouse events. Normally developers should not modify it.
	 * @private
	 */

	public void onMouseEvent(StageEvent e) {
		
		if (!this.enabled)
			return;

		if (e.type.equals("mousemove")) {
			if (this.state != Button.OVER) {
				e.type = "mouseover";
				this.onMouseOver(e);
			} else if (this.state == Button.OVER) {
				this.onMouseMove(e);
			}
			if (this.overState != null && this.state == Button.UP)
				this.setState(Button.OVER);

		} else if (e.type.equals("mouseout")) {
			if (this.upState != null)
				this.setState(Button.UP);
			e.type = "mouseout";
			this.onMouseOut(e);

		} else if (e.type.equals("mousedown")) {
			if (this.downState != null)
				this.setState(Button.DOWN);
			this.onMouseDown(e);

		} else if (e.type.equals("mouseup")) {
			if (this.overState != null)
				this.setState(Button.OVER);
			else
				this.setState(Button.UP);
			this.onMouseUp(e);

		}
	}

	/**
	 * A Handler for mouse move event. Default is null.
	 * @function
	 * @param event
	 */
	public void onMouseMove  (StageEvent e){}

	/**
	 * A Handler for mouse over event. Default is null.
	 * @function
	 * @default null
	 * @param event
	 */
	public void onMouseOver (StageEvent e){}
	/**
	 * A Handler for mouse out event. Default is null.
	 * @function
	 * @param event
	 */

	public void onMouseOut (StageEvent e){}
	/**
	 * A Handler for mouse down event. Default is null.
	 * @function
	 * @param event
	 */

	public void onMouseDown (StageEvent e){}
	/**
	 * A Handler for mouse up event. Default is null.
	 * @function
	 * @param event
	 */
	public void onMouseUp (StageEvent e){}

}
