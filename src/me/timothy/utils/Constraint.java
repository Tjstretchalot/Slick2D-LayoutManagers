package me.timothy.utils;

/*
 * padded signifies a 10% padding from the left and right or top and bottom. 
 */
public class Constraint {
	public static final Constraint CENTER_X = new Constraint(true, false, false, false, "CENTER_X");
	public static final Constraint CENTER_Y = new Constraint(false, true, false, false, "CENTER_Y");
	public static final Constraint PADDED_X = new Constraint(true, false, true, false, "PADDED_X");
	public static final Constraint PADDED_Y = new Constraint(false, true, false, true, "PADDED_Y");
	public static final Constraint FILL_X = new Constraint(true, false, true, false, "FILL_WIDTH");
	public static final Constraint FILL_Y = new Constraint(false, true, false, true, "FILL_HEIGHT");
	public static final Constraint LEFT_ALIGN = new Constraint(true, false, false, false, "LEFT_ALIGN");
	public static final Constraint RIGHT_ALIGN = new Constraint(true, false, false, false, "RIGHT_ALIGN");
	public static final Constraint TOP_ALIGN = new Constraint(false, true, false, false, "TOP_ALIGN");
	public static final Constraint BOTTOM_ALIGN = new Constraint(false, true, false, false, "BOTTOM_ALIGN");
	public static final Constraint TWNTYPRC_PADDED_X = new Constraint(true, false, true, false, "20PRC_PADDED_X");
	public static final Constraint TWNTYPRC_PADDED_Y = new Constraint(false, true, false, true, "20PRC_PADDED_Y");
	
	public final boolean x;
	public final boolean y;
	public final boolean width;
	public final boolean height;
	private String name;
	
	Constraint(boolean x, boolean y, boolean width, boolean height, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	
	public boolean conflictsWith(int skipIndex, Constraint... list) {
		for(int i = 0; i < list.length; i++) {
			if(i != skipIndex) {
				Constraint fr = list[i];
				if((fr.x && x) || (fr.y && y) || (fr.width && width) || (fr.height && height))
					return true;
			}
		}
		return false;
	}
	
	public String name() {
		return name != null ? name : "null";
	}
	
	@Override
	public String toString() {
		return name();
	}
}
