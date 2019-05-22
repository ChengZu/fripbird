package com.engine.display;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;

import com.engine.Engine;
import com.engine.event.EventDispatcher;
import com.engine.event.StageEvent;
import com.engine.geom.Matrix;
import com.engine.geom.Point;
import com.engine.geom.Rectangle;

public abstract class DisplayObject extends EventDispatcher{
	public String name=Engine.NameUtil.createUniqueName("DisplayObject");
	public int id=-1;
	public int x=0;
	public int y=0;
	public int width = 0;
	public int height = 0;
	public float alpha = 1;
	public double scaleX = 1;
	public double scaleY = 1;
	public int rotation = 0;
	public int regX = 0;
	public int regY = 0;
	public boolean visible = true;	
	public boolean mouseEnabled = true;
	public boolean useHandCursor = false;
	public DisplayObject parent = null;
	public Stage stage = null;	
	public int __hitTestTolerance = 100;
	/**
	 * Gets the scaled width of the display object currently, in pixels.
	 */
	public int getCurrentWidth ()
	{
		return this.doubleRounded(Math.abs(this.width * this.scaleX));
	}
	/**
	 * Gets the scaled height of the display object currently, in pixels.
	 */
	public int getCurrentHeight ()
	{
		return this.doubleRounded(Math.abs(this.height * this.scaleY));
	}
	/**
	 * Gets the stage reference, rather than using DisplayObject.stage, you'd better use DisplayObject.getStage().
	 */
	public Stage getStage()
	{
		if(this.stage!=null) return this.stage;
		DisplayObject p = this;
		while(p.parent!=null) p = p.parent;
		if(p instanceof Stage) return this.stage = (Stage)p;
		return null;
	}
	
	/**
	 * Converts the (x, y) point from the display object's (local) coordinates to the Stage (global) coordinates.
	 */
	public Point localToGlobal(int x, int y)
	{
		Matrix cm = this.getConcatenatedMatrix();
		if (cm == null) return new Point(0,0);
		Matrix m = new Matrix(1, 0, 0, 1, x, y);
		m.concat(cm);
		return new Point(this.doubleRounded(m.tx),this.doubleRounded(m.ty));
	}
	
	
	public Point globalToLocal (int x, int y) 
	{
		Matrix cm = this.getConcatenatedMatrix();
		if (cm == null) return new Point(0,0);
		cm.invert();
		Matrix m = new Matrix(1, 0, 0, 1, x, y);
		m.concat(cm);
		return new Point(this.doubleRounded(m.tx),this.doubleRounded(m.ty));
	}
	/**
	 * Converts the (x, y) point from the display object's (local) coordinates to the target object's coordinates.
	 */
	public Point localToTarget(int x, int y, DisplayObject target) 
	{
		Point p = this.localToGlobal(x, y);
		return target.globalToLocal(p.x, p.y);
	}
	
	/**
	 * @private
	 */
	private Matrix getConcatenatedMatrix () 
	{
		Matrix mtx = new Matrix();
		for (DisplayObject o = this; o != null; o = o.parent)
		{
			mtx.concatTransform(o.x, o.y, o.scaleX, o.scaleY, o.rotation, o.regX, o.regY);
			if(o instanceof Stage) break;
		}	
		return mtx;
	}
	/**
	 * @private
	 */
	private void _transform (Graphics2D context, boolean toGlobal)
	{	
		if(!toGlobal)
		{
			Point p = this.localToGlobal(0, 0);
			if(p.x != 0 || p.y != 0) context.translate(p.x, p.y);
		}else
		{
			context.translate(this.x, this.y);
		}
		context.translate(this.regX, this.regY);
		if(this.rotation%360 > 0) context.rotate((double)this.rotation%360/180*Math.PI);
		context.translate(-this.regX, -this.regY);
		if(this.scaleX != 1 || this.scaleY != 1) context.scale(this.scaleX, this.scaleY);
		//if(!toGlobal) context.translate(-this.regX, -this.regY);
		AlphaComposite alpha=(AlphaComposite) context.getComposite();
		
        context.setComposite(AlphaComposite.SrcOver.derive(alpha.getAlpha()*this.alpha));
		//context.globalAlpha *= this.alpha;
	}
	
	/**
	 * Entry of rendering, prepare and restore context.
	 * @param context The context render to
	 * @param noTransform Whether do transformation or not, default is do transformation.
	 * @param globalTransform Whether do global transformation or not
	 * @private
	 */
	public void _render (Graphics2D context, boolean noTransform, boolean globalTransform)
	{	
		if(!this.visible || this.alpha <= 0) return;
		//save context AffineTransform;
		Composite comp=context.getComposite();		
		AffineTransform tx=context.getTransform();
		if(!noTransform) this._transform(context, globalTransform);
		
		this.render(context);		
		//restore context AffineTransform;
		context.setTransform(tx);	
		context.setComposite(comp);
	}
	/**
	 * The real rendering workhorse, it should be overridden by subclasses.
	 */
	public void render (Graphics2D context) {
		
	}
	/**
	 * Evaluates the display object to see if it overlaps or intersects with the point specified by the x and y parameters.
	 */
	public boolean hitTestPoint (int x, int y, boolean usePixelCollision, int tolerance)
	{	
		Point p = this.globalToLocal(x, y);
		boolean result = p.x >= 0 && p.x <= this.width && p.y >= 0 && p.y <= this.height;
		if(!usePixelCollision)
		{	
			return result;		
		}
		if(result){
			Canvas canvas=new Canvas(1,1);
			Graphics2D context=canvas.context;
			context.translate(-x, -y);
			//render this displayobject to the hit testing context		
			this._render(context, false, true);
			
			int pixel=canvas.image.getRGB(0, 0);
			int apha=ColorModel.getRGBdefault().getAlpha(pixel);
			if(apha >= tolerance) return true;
		}
		return false;
	}
	public boolean hitTestPoint (int x, int y, boolean usePixelCollision)
	{	
		return this.hitTestPoint(x, y, usePixelCollision, this.__hitTestTolerance);
	}
	
	/**
	 * Evaluates the display object to see if it overlaps or intersects with the object parameter.
	 */
	public boolean hitTestObject(DisplayObject object, boolean usePixelCollision, int tolerance) {
		// note: to get right rectangle, pls make sure the objects' width/height
		// are set.		
		
		// default and fastest: compute intersection of two objects' rectangle
		
		if (!usePixelCollision)
			return this.doSATCheck(this.getBounds(), object.getBounds())?this.doSATCheck(object.getBounds(), this.getBounds()):false;
		Rectangle rect1 = this.getRect();
		Rectangle rect2 = object.getRect();
		Rectangle rect3 = rect1.intersection(rect2);
		
		if (rect3 != null && rect3.width > 0 && rect3.height > 0) {
			boolean result = false;

			// render the intersection of this object to the hit test context
			Canvas canvas1=new Canvas(rect3.width,rect3.height);
			Graphics2D context1=canvas1.context;
			context1.setTransform(new AffineTransform(1,0,0,1,-rect3.x, -rect3.y));
			this._render(context1, false, true);

			// get bitmap pixel data
			int[] pixelData1 = new int[rect3.width * rect3.height];
			canvas1.image.getRGB(0, 0, rect3.width, rect3.height, pixelData1, 0, rect3.width);
			// get the comparer object's pixel data

			Canvas canvas2=new Canvas(rect3.width,rect3.height);
			Graphics2D context2=canvas2.context;
			context2.setTransform(new AffineTransform(1,0,0,1,-rect3.x, -rect3.y));
			object._render(context2, false, true);
			
			int[] pixelData2 = new int[rect3.width * rect3.height];
			canvas2.image.getRGB(0, 0, rect3.width, rect3.height, pixelData2, 0, rect3.width);

			// compare the two pixel data to see if they have overlay pixel.
			int i = 0;
			ColorModel cm = ColorModel.getRGBdefault();
			while (i < pixelData1.length) {
				int red1 = cm.getRed(pixelData1[i]);
				int blue1 = cm.getBlue(pixelData1[i]);
				int green1 = cm.getGreen(pixelData1[i]);
				int apha1 = cm.getAlpha(pixelData1[i]);
				int red2 = cm.getRed(pixelData2[i]);
				int blue2 = cm.getBlue(pixelData2[i]);
				int green2 = cm.getGreen(pixelData2[i]);
				int apha2 = cm.getAlpha(pixelData2[i]);
				if ((red1 > 0 || blue1 > 0 || green1 > 0 || apha1 >= tolerance)
						&& (red2 > 0 || blue2 > 0 || green2 > 0 || apha2 >= tolerance)) {
					result = true;
					break;
				}
				i += 1;
			}
			return result;
		}
		return false;
	}
	public boolean hitTestObject(DisplayObject object, boolean usePixelCollision) {
		return this.hitTestObject(object, usePixelCollision, this.__hitTestTolerance);
	}
	
	/**
	 * Gets rectangle of this display object within specific target coordinate space.
	 * @function
	 * @param target
	 */
	public Rectangle getRect()
	{
		Point p1 = this.localToGlobal(0, 0);
		Point p2=this.localToGlobal(this.width, 0);
		Point p3=this.localToGlobal(this.width,this.height);
		Point p4=this.localToGlobal(0,this.height);
		int minX=Math.min(Math.min(Math.min(p1.x, p2.x),p3.x),p4.x);
		int maxX=Math.max(Math.max(Math.max(p1.x, p2.x),p3.x),p4.x);
		int minY=Math.min(Math.min(Math.min(p1.y, p2.y),p3.y),p4.y);
		int maxY=Math.max(Math.max(Math.max(p1.y, p2.y),p3.y),p4.y);
		return new Rectangle(minX, minY, maxX-minX, maxY-minY);
	}
	public Point[] getBounds()
	{
		Point p1 = this.localToGlobal(0, 0);
		Point p2=this.localToGlobal(this.width, 0);
		Point p3=this.localToGlobal(this.width,this.height);
		Point p4=this.localToGlobal(0,this.height);
		
		return new Point[]{p1,p2,p3,p4};
	}
	/**
	 * double number round to the nearest integer.
	 * @function
	 * @param double
	 */
	public int doubleRounded(double num){
		if(num>0){
			if(Math.abs(num%1.0)>=0.5)return (int)Math.ceil(num);
			return (int)Math.floor(num);
		}else{
			if(Math.abs(num%1.0)>=0.5)return (int)Math.floor(num);
			return (int)Math.ceil(num);
		}
	}
	

	public boolean doSATCheck(Point[] poly1, Point[] poly2) {
		double len1 = poly1.length, len2 = poly2.length, distance, min1, max1, min2, max2, dot, overlap = -1.0 / 0, normalx = 0, normaly = 0;
		Point currentPoint, nextPoint;
		for (int i = 0; i < len1; i++) {
			currentPoint = poly1[i];
			nextPoint = poly1[(i < len1 - 1 ? i + 1 : 0)];

			normalx = currentPoint.y - nextPoint.y;
			normaly = nextPoint.x - currentPoint.x;

			distance = Math.sqrt(normalx * normalx + normaly * normaly);
			normalx /= distance;
			normaly /= distance;

			min1 = max1 = poly1[0].x * normalx + poly1[0].y * normaly;
			for (int j = 1; j < len1; j++) {
				dot = poly1[j].x * normalx + poly1[j].y * normaly;
				if (dot > max1)
					max1 = dot;
				else if (dot < min1)
					min1 = dot;
			}

			min2 = max2 = poly2[0].x * normalx + poly2[0].y * normaly;
			for (int j = 1; j < len2; j++) {
				dot = poly2[j].x * normalx + poly2[j].y * normaly;
				if (dot > max2)
					max2 = dot;
				else if (dot < min2)
					min2 = dot;
			}

			if (min1 < min2) {
				overlap = min2 - max1;
				normalx = -normalx;
				normaly = -normaly;
			} else {
				overlap = min1 - max2;
			}

			if (overlap >= 0) {
				return false;
			}
		}
		return true;
	}


		
	/**
	 * A handler for mouse events. It only works when Stage.traceMouseTarget=true. Default is null.
	 * @function
	 * @param event
	 */
	
	public void onMouseEvent(StageEvent e){
	}

	/**
	 * Returns the string representation of the specified DisplayObject.
	 */
	public String toString ()
	{
		return Engine.NameUtil.displayObjectToString(this);
	}
	
}
