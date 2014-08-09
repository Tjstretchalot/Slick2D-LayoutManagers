package me.timothy.layout;

import java.util.List;

import me.timothy.utils.IRectangle;

import org.lwjgl.util.Dimension;

public class FlowLayoutManager implements LayoutManager {
	public static final boolean VERTICAL_LAYOUT = true;
	public static final boolean HORIZONTAL_LAYOUT = false;
	
	private boolean dir;
	
	public FlowLayoutManager(boolean direction) {
		dir = direction;
	}
	
	@Override
	public void layoutContainer(LayoutContainer container) {
		List<LayoutComponent> components = container.getComponents();
		IRectangle rect = container.getLocation();

		if(dir == VERTICAL_LAYOUT) {
			layoutVertical(components, rect);
		}else {
			layoutHorizontal(components, rect);
		}
	}
	
	private void layoutVertical(List<LayoutComponent> components,
			IRectangle rect) {
		int maxWidth = 0;
		int height = 0;
		for(LayoutComponent lc : components) {
			if(lc.getPreferredSize().getWidth() > maxWidth)
				maxWidth = lc.getPreferredSize().getWidth();
			
			height += lc.getPreferredSize().getHeight();
		}
		boolean minimumSizes = false;
		
		if(height > rect.height) {
			minimumSizes = true;
			maxWidth = 0;
			height = 0;
			for(LayoutComponent lc : components) {
				if(lc.getMinimumSize() != null) {
					if(lc.getMinimumSize().getWidth() > maxWidth)
						maxWidth = lc.getMinimumSize().getWidth();
					
					height += lc.getMinimumSize().getHeight();
				}else {
					if(lc.getPreferredSize().getWidth() > maxWidth)
						maxWidth = lc.getPreferredSize().getWidth();
					
					height += lc.getPreferredSize().getHeight();
				}
			}
			
			if(height > rect.height)
				throw new IllegalArgumentException("Components are too tall for this location!");
		}
		final int sX = rect.width/2 - maxWidth/2;
		final int sY = rect.height/2 - height/2;
		
		int y = sY;
		
		for(LayoutComponent lc : components) {
			Dimension size = minimumSizes ? (lc.getMinimumSize() != null ? lc.getMinimumSize() : lc.getPreferredSize()) : lc.getPreferredSize();
			
			lc.getLocation().set(sX + maxWidth/2 - size.getWidth()/2, y, size.getWidth(), size.getHeight());
			
			y += size.getHeight();
		}
	}

	private void layoutHorizontal(List<LayoutComponent> components,
			IRectangle rect) {
		int maxHeight = 0;
		int width = 0;
		for(LayoutComponent lc : components) {
			if(lc.getPreferredSize().getHeight() > maxHeight)
				maxHeight = lc.getPreferredSize().getHeight();
			
			width += lc.getPreferredSize().getWidth();
		}
		boolean minimumSizes = false;
		
		if(width > rect.width) {
			minimumSizes = true;
			maxHeight = 0;
			width = 0;
			for(LayoutComponent lc : components) {
				if(lc.getMinimumSize() != null) {
					if(lc.getMinimumSize().getHeight() > maxHeight)
						maxHeight = lc.getMinimumSize().getHeight();
					
					width += lc.getMinimumSize().getWidth();
				}else {
					if(lc.getPreferredSize().getHeight() > maxHeight)
						maxHeight = lc.getPreferredSize().getHeight();
					
					width += lc.getPreferredSize().getWidth();
				}
			}
			
			if(width > rect.width)
				throw new IllegalArgumentException("Components are too wide for this location!");
		}
		final int sX = rect.width/2 - width/2;
		final int sY = rect.height/2 - maxHeight/2;
		
		int x = sX;
		
		for(LayoutComponent lc : components) {
			Dimension size = minimumSizes ? (lc.getMinimumSize() != null ? lc.getMinimumSize() : lc.getPreferredSize()) : lc.getPreferredSize();
			
			lc.getLocation().set(x, sY + maxHeight/2 - size.getHeight()/2, size.getWidth(), size.getHeight());
			
			x += size.getWidth();
		}
	}

}
