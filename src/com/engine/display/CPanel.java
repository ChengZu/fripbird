package com.engine.display;

import java.awt.Graphics;

import javax.swing.JPanel;

public class CPanel extends JPanel {

	private static final long serialVersionUID = 5000689981240775853L;
	public Canvas canvas = new Canvas();
	public boolean fullPanel = false;
	public double scaleX = 1;
	public double scaleY = 1;

	public CPanel() {
	}

	public CPanel(int width, int height) {
		this.canvas = new Canvas(width, height);
	}

	public void restoreScale() {
		this.scaleX = (double) this.getWidth()
				/ (double) this.canvas.getWidth();
		this.scaleY = (double) this.getHeight()
				/ (double) this.canvas.getHeight();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (fullPanel)
			this.restoreScale();
		g.drawImage(this.canvas.image, 0, 0,
				(int) (this.canvas.getWidth() * this.scaleX),
				(int) (this.canvas.getHeight() * this.scaleY), null);

	}
}
