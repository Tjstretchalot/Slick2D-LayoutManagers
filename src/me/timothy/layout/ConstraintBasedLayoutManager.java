package me.timothy.layout;

import java.util.List;

import me.timothy.utils.Constraint;
import me.timothy.utils.IRectangle;
import me.timothy.utils.ScaledGraphics;

public class ConstraintBasedLayoutManager implements LayoutManager {

	@Override
	public void layoutContainer(LayoutContainer container) {
		List<LayoutComponent> components = container.getComponents();
		IRectangle o = container.getLocation();
		for(LayoutComponent lc : components) {
			List<Constraint> constraints = lc.getConstraints();
			lc.getLocation().set(ScaledGraphics.getLocation(o.x, o.y, o.width, o.height, lc.getPreferredSize().getWidth(), lc.getPreferredSize().getHeight(), constraints));
		}
	}

}
