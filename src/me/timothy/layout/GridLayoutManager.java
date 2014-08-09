package me.timothy.layout;

import java.util.List;

import me.timothy.utils.IRectangle;

public class GridLayoutManager implements LayoutManager {
	private int wide;
	private int tall;
	private boolean leftRight;
	private boolean padX;
	private boolean padY;
	
	public GridLayoutManager(int wide, int tall, boolean leftRight, boolean padLeftRight, boolean padTopDown) {
		if(wide < 0 || tall < 0)
			throw new IllegalArgumentException("wide or tall < 0?");
		if(wide == 0 && tall == 0)
			throw new IllegalArgumentException("wide and tall == 0?");
		this.wide = wide;
		this.tall = tall;
		this.leftRight = leftRight;
		this.padX = padLeftRight;
		this.padY = padTopDown;
	}
	
	@Override
	public void layoutContainer(LayoutContainer container) {
		List<LayoutComponent> components = container.getComponents();
		if(wide != 0 && tall != 0 && components.size() > wide * tall)
			throw new IllegalArgumentException(String.format("Too many components (%d) for a %dx%d GridLayout!", components.size(), wide, tall));
		IRectangle rect = container.getLocation();
		
		int realwide = (wide != 0 ? wide : (components.size() / tall) + (components.size() % tall != 0 ? 1 : 0));
		int realtall = (tall != 0 ? tall : (components.size() / wide) + (components.size() % wide != 0 ? 1 : 0));
		int xind = 0;
		int yind = 0;
		int widthEach = rect.width / realwide;
		int heightEach = rect.height / realtall;
		for(LayoutComponent lc : components) {
			if(lc.getMinimumSize().getWidth() > widthEach || lc.getMinimumSize().getHeight() > heightEach) {
				throw new IllegalArgumentException(lc + 
						String.format(" can't fit in this grid (requires %dx%d, but have %dx%d)", 
								lc.getMinimumSize().getWidth(), lc.getMinimumSize().getHeight(), widthEach, heightEach));
			}
			int x = widthEach * xind;
			int y = heightEach * yind;
			int w = widthEach;
			int h = heightEach;
			
			if(padX) {
				x += widthEach / 10;
				w = (widthEach * 8) / 10;
			}
			
			if(padY) {
				y += heightEach / 10;
				h = (heightEach * 8) / 10;
			}
			lc.getLocation().set(x, y, w, h);
			
			if(leftRight) {
				xind++;
				if(xind >= realwide) {
					xind = 0;
					yind++;
				}
			}else {
				yind++;
				if(yind >= realtall) {
					yind = 0;
					xind++;
				}
			}
		}
	}
}
