package me.timothy.scaling;

import org.newdawn.slick.geom.Rectangle;


public class SizeScaleSystem {

	public static final float EXPECTED_WIDTH = 1920;
	public static final float EXPECTED_HEIGHT = 1080;
	
	public static final Rectangle EXPECTED_RECT = new Rectangle(0, 0, EXPECTED_WIDTH, EXPECTED_HEIGHT);
	
	private static float realWidth;
	private static float realHeight;

	public static void setRealWidth(float f) {
		realWidth = f;
	}
	public static void setRealHeight(float f) {
		realHeight = f;
	}
	public static float getRealWidth() {
		return realWidth;
	}
	public static float getRealHeight() {
		return realHeight;
	}

	public static int adjRealToPixelX(int x) {
		return (int) (x * ((float) realWidth / EXPECTED_WIDTH));
	}
	public static int adjRealToPixelY(int y) {
		return (int) (y * ((float) realHeight / EXPECTED_HEIGHT));
	}
	
	public static int adjPixelToRealX(int x) {
		return (int) (x * ((float) EXPECTED_WIDTH / realWidth));
	}
	
	public static int adjPixelToRealY(int y) {
		return (int) (y * ((float) EXPECTED_HEIGHT / realHeight));
	}
}
