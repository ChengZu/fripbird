package com.engine.display;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import com.engine.Engine;
import com.engine.event.StageEvent;
import com.engine.geom.Point;

public class Stage extends DisplayObjectContainer {
	public String name = Engine.NameUtil.createUniqueName("Stage");

	public Graphics2D context = null;
	public Canvas canvas = null;
	public CPanel panel = null;
	public int mouseX = 0;
	public int mouseY = 0;
	public double scaleX = 1;
	public double scaleY = 1;
	// determine whether trace mouse target
	public boolean traceMouseTarget = true;

	// determine whether use pixel collision while tracing mouse target
	public boolean usePixelTrace = true;
	// refer to current mouse target if set traceMouseTarget to true
	public DisplayObject mouseTarget = null;
	// refer to current dragging object
	public DisplayObject dragTarget = null;
	// restrict dragging area
	// public int dragBounds = null;
	// original mouse position for dragging object
	public int _dragMouseX = 0;
	public int _dragMouseY = 0;

	// @protected
	protected int _frameRate = 0;
	protected boolean _paused = false;
	protected boolean _pauseInNextFrame = false;

	// @private internal use
	private Thread __intervalID = null;

	public Stage(CPanel panel) {
		this.panel = panel;
		this.canvas = this.panel.canvas;
		this.context = this.panel.canvas.context;
		this.setFrameRate(60);
		this.addListener();
	}

	public void setPaused(boolean pause, boolean pauseInNextFrame) {
		if (this._paused == pause)
			return;
		this._paused = pause;
		// sometimes we need to pause after rendering current frame
		this._pauseInNextFrame = pauseInNextFrame || false;
	}

	public boolean getPaused() {
		return this._paused;
	}

	public int getFrameRate() {
		return this._frameRate;
	}

	public void setFrameRate(int frameRate) {
		if (this._frameRate == frameRate)
			return;
		this._frameRate = frameRate;
		if (this.__intervalID != null)
			this.__intervalID = null;
		final Stage stage = this;

		this.__intervalID = new Thread(new Runnable() {
			public void run() {
				while (true) {
					long startTime = new Date().getTime();
					stage.__enterFrame();

					stage.panel.repaint();
					long endTime = new Date().getTime();
					int sleepTime = (1000 / stage._frameRate)
							- (int) (endTime - startTime);
					try {
						Thread.sleep(sleepTime > 0 ? sleepTime : 0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.__intervalID.start();

	}

	private void __mouseHandler(MouseEvent event, String type) {
		this.mouseX = this.doubleRounded((event.getX()/this.panel.scaleX));
		this.mouseY = this.doubleRounded((event.getY()/this.panel.scaleY));
		// trace mouse target if traceMouseTarget=true
		if (this.traceMouseTarget && type == "mousemove") {
			// know issue: it can get mouse target only start with mousemove
			// it won't work properly if the mouse doesn't move but click at
			// inital time
			this.__getMouseTarget(event);
		}

		// stage event
		StageEvent e = new StageEvent();
		e.target = e.currentTarget = (this.mouseTarget != null ? this.mouseTarget
				: this);
		e.type = type;
		e.mouseX = this.mouseX;
		e.mouseY = this.mouseY;

		// if onMouseEvent is defined for mouseTarget, trigger it...
		if (this.mouseTarget != null) {
			this.mouseTarget.onMouseEvent(e);
			this.mouseTarget.dispatchEvent(e);

		}
		// change cursor by useHandCursor property
		// this.setCursor((this.mouseTarget && this.mouseTarget.useHandCursor) ?
		// "pointer" : "");

		// dispatch event
		// this.dispatchEvent(e);

		// disable text selection on the canvas, works like a charm.
		// event.preventDefault();

		// event.stopPropagation();
	}

	private void __getMouseTarget(MouseEvent event) {
		DisplayObject obj = this.getObjectUnderPoint(this.mouseX, this.mouseY,
				this.usePixelTrace, 100);

		DisplayObject oldObj = this.mouseTarget;

		this.mouseTarget = obj;
		if (oldObj != null && oldObj != obj) {

			StageEvent e = new StageEvent();
			e.type = "mouseout";
			e.target = e.currentTarget = oldObj;
			e.mouseX = this.mouseX;
			e.mouseY = this.mouseY;
			oldObj.onMouseEvent(e);
		}
	}

	public void __enterFrame() {
		if (this._paused && !this._pauseInNextFrame)
			return;
		this.dispatchEvent(new StageEvent(StageEvent.ENTER_FRAME));
		// check if paused once more, because it may be changed in ENTER_FRAME
		// handler
		if (!this._paused || this._pauseInNextFrame)
			this._render(this.context, true, false);
		if (this._frameRate <= 0) {
			// stop rendering if frameRate equal 0

			this.__intervalID = null;
		}

	}

	public void render(Graphics2D context) {

		if(context==null) context = this.context;
		this.clear();
		if (this.dragTarget != null) {
			// handle dragging target
			this.dragTarget.x = this.mouseX - this._dragMouseX;
			this.dragTarget.y = this.mouseY - this._dragMouseY;
		}

		super.render(context);
		if (this._pauseInNextFrame) {
			this._paused = true;
			this._pauseInNextFrame = false;
		}

	}

	public void startDrag(DisplayObject target, boolean bounds) {
		this.dragTarget = target;
		Point p = this.dragTarget.globalToLocal(this.mouseX, this.mouseY);
		Point p2=this.dragTarget.localToGlobal(p.x, p.y);
		Point p3=this.localToGlobal(this.dragTarget.x, this.dragTarget.y);
		this._dragMouseX = p2.x-p3.x;
		this._dragMouseY = p2.y-p3.y;
		
		// this.setCursor("pointer");
		// this.dragBounds = bounds;
	}

	public void stopDrag() {
		this.dragTarget = null;
		// this.dragBounds = null;
		// this.setCursor("");
	}

	public void clear() {
		this.context.clearRect(0, 0, this.canvas.image.getWidth(),this.canvas.image.getHeight());
		
	}

	public void addListener() {
		final Stage stage = this;
		this.panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				stage.__mouseHandler(e, "mouseclik");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				stage.__mouseHandler(e, "mouseover");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				stage.__mouseHandler(e, "mouseout");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				stage.__mouseHandler(e, "mousedown");
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				stage.__mouseHandler(e, "mouseup");
			}

		});
		this.panel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				stage.__mouseHandler(e, "mousedrag");
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				stage.__mouseHandler(e, "mousemove");
			}

		});
		this.panel.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
	}
}
