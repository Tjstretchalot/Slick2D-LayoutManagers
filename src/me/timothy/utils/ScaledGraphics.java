package me.timothy.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.timothy.scaling.SizeScaleSystem;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * This library takes in "real" coordinates (coordinates that are based on the
 * expected width and expected height from size scale system) and renders based
 * on pixel coordinates.
 * 
 * This class also offers some basic formatting
 * 
 * @author Timothy
 */
public class ScaledGraphics {
	public static Rectangle getLocation(final float ox, final float oy, final float ow,
			final float oh, final float iw, final float ih, Constraint... requirements) {
		return getLocation(ox, oy, ow, oh, iw, ih, Arrays.asList(requirements));
	}
	/**
	 * Applies the format requirements to the inside and returns the real
	 * top-left location for the inside.
	 * 
	 * 
	 * @param requirements
	 *            The restrictions for the formatting. No formats can conflict
	 *            (e.g. CENTER_X and LEFT_ALIGN). Defaults to LEFT_ALIGN if no
	 *            x-formatting options are given and TOP_ALIGN if no
	 *            y-formatting options are given
	 * @return
	 *            The real location and size to draw at. The width/height will
	 *            attempt to match the inside width and height, followed by the
	 *            outside width or height. This may change if the formatting
	 *            option affects width/height (e.g. PADDED_X or PADDED_Y) 
	 * @throws ConflictingFormatsException
	 *             If there is a conflict OR width / height information is not
	 *             given when it is required (e.g. CENTER_X)
	 * @throws InvalidOrderException
	 *             If the formatting requirements are not given such that there
	 *             are no x restrictions before width restrictions and no y restrictions
	 *             before height restrictions 
	 */
	public static Rectangle getLocation(final float ox, final float oy, final float ow,
			final float oh, final float iw, final float ih, final List<Constraint> requirements)
			throws ConflictingFormatsException, InvalidOrderException {
		final Rectangle result = new Rectangle(ox, oy, iw != Float.MIN_NORMAL ? iw : ow, ih != Float.MIN_NORMAL ? ih : oh);
		
		// ensure that width/height is before x + y
		boolean x = false, y = false, width = false, height = false;
		for(int i = 0; i < requirements.size(); i++) {
			if(requirements.get(i).x) {
				if(x)
					throw new ConflictingFormatsException();
				x = true;
			}
			if(requirements.get(i).y) {
				if(y)
					throw new ConflictingFormatsException();
				y = true;
			}
			if(requirements.get(i).width) {
				if(width)
					throw new ConflictingFormatsException();
				width = true;
			}
			if(requirements.get(i).height && !y) {
				if(height)
					throw new ConflictingFormatsException();
				height = true;
			}
			
			if((width && !x) || (height && !y))
				throw new InvalidOrderException();
		}
		
		for (int i = 0; i < requirements.size(); i++) {
			switch (requirements.get(i).name()) {
			case "CENTER_X":
				result.setX(ox + ow/2 - result.getWidth()/2);
				break;
			case "CENTER_Y":
				result.setY(oy + oh/2 - result.getHeight()/2);
				break;
			case "PADDED_X":
				result.setX(ox + ow/10);
				result.setWidth(ow * .8f);
				break;
			case "20PRC_PADDED_X":
				result.setX(ox + ow / 5);
				result.setWidth(ow * .6f);
				break;
			case "PADDED_Y":
				result.setY(oy + oh/10);
				result.setHeight(oh * .8f);
				break;
			case "20PRC_PADDED_Y":
				result.setY(oy + oh / 5);
				result.setHeight(oh * .6f);
				break;
			case "FILL_WIDTH":
				result.setX(ox);
				result.setWidth(ow);
				break;
			case "FILL_HEIGHT":
				result.setY(oy);
				result.setHeight(oh);
				break;
			case "LEFT_ALIGN":
				result.setX(ox);
				break;
			case "RIGHT_ALIGN":
				result.setX(ox + ow - result.getWidth());
				break;
			case "TOP_ALIGN":
				result.setY(oy);
				break;
			case "BOTTOM_ALIGN":
				result.setY(oy + oh - result.getHeight());
				break;
			default:
				throw new UnsupportedOperationException("Unknown formatting option: " + requirements.get(i).name());	
			}
		}
		return result;
	}
}
