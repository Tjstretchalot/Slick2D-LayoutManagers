package me.timothy.utils;

import org.newdawn.slick.geom.Rectangle;

public class IRectangle {
	public int x;
	public int y;
	public int width;
	public int height;
	
	public IRectangle() {}
	
	public IRectangle(Rectangle rect) {
		x = (int) Math.round(rect.getX());
		y = (int) Math.round(rect.getY());
		width = (int) Math.round(rect.getWidth());
		height = (int) Math.round(rect.getHeight());
	}

	public boolean contains(int x2, int y2) {
		return 
				(
					x2 >= x && x2 <= x + width
				) &&
				(
					y2 >= y && y2 <= y + height
				);
	}

	public boolean set(int x2, int y2, int width2, int height2) {
		boolean res = false;
		if(x != x2) {
			x = x2;
			res = true;
		}
		if(y != y2) {
			y = y2;
			res = true;
		}
		if(width != width2) {
			width = width2;
			res = true;
		}
		if(height != height2) {
			height = height2;
			res = true;
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IRectangle [x=").append(x).append(", y=").append(y)
				.append(", width=").append(width).append(", height=")
				.append(height).append("]");
		return builder.toString();
	}

	public void set(Rectangle l) {
		set((int) Math.round(l.getX()), (int) Math.round(l.getY()), (int) Math.round(l.getWidth()), (int) Math.round(l.getHeight()));
	}
	
	
}
