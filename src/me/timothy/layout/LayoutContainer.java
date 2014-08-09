package me.timothy.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.timothy.scaling.SizeScaleSystem;
import me.timothy.utils.Constraint;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Graphics;

public class LayoutContainer extends BasicLayoutComponent {
	private List<LayoutComponent> components;
	private LayoutManager layoutManager;
	
	public LayoutContainer(LayoutManager layoutManager, Constraint... constraints) {
		this.layoutManager = layoutManager;
		components = new ArrayList<>();
		
		getConstraints().addAll(Arrays.asList(constraints));
		setPreferredSize(new Dimension((int) SizeScaleSystem.getRealWidth(), (int) SizeScaleSystem.getRealHeight()));
		getLocation().set(0, 0, getPreferredSize().getWidth(), getPreferredSize().getHeight());
	}
	
	public List<LayoutComponent> getComponents() {
		return components;
	}
	
	public void setLayoutManager(LayoutManager lm) {
		layoutManager = lm;
	}

	public void layout() {
		layoutManager.layoutContainer(this);
		
		for(LayoutComponent c : components) {
			c.getLocation().x += getLocation().x;
			c.getLocation().y += getLocation().y;
		}

		for(LayoutComponent lc : components) {
			if(lc instanceof LayoutContainer) {
				((LayoutContainer) lc).layout();
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		for(LayoutComponent k : components) {
			k.render(g);
		}
	}
	
	@Override
	public void processMouseMove(int oldx, int oldy, int newx, int newy) {
		for(LayoutComponent k : components) {
			k.processMouseMove(oldx, oldy, newx, newy);
		}
	}
	@Override
	public void processMousePressed(int button, int mousex, int mousey) {
		for(LayoutComponent k : components) {
			k.processMousePressed(button, mousex, mousey);
		}
	}
	
	@Override
	public void processMouseReleased(int button, int mousex, int mousey) {
		for(LayoutComponent k : components) {
			k.processMouseReleased(button, mousex, mousey);
		}
	}

	@Override
	public void processKeyPressed(int key, char ch) {
		for(LayoutComponent k : components) {
			k.processKeyPressed(key, ch);
		}
	}
	@Override
	public void processKeyReleased(int key, char ch) {
		for(LayoutComponent k : components) {
			k.processKeyReleased(key, ch);
		}
	}
	@Override
	public void update(int delta) {
		for(LayoutComponent k : components) {
			k.update(delta);
		}
	}

	@Override
	public void invalidate() {
		for(LayoutComponent k : components) {
			k.invalidate();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LayoutContainer [");
		if (components != null)
			builder.append("components=").append(components).append(", ");
		if (layoutManager != null)
			builder.append("layoutManager=").append(layoutManager.getClass().getCanonicalName());
		builder.append("]");
		return builder.toString();
	}

	public void add(LayoutComponent lc) {
		components.add(lc);
	}
}
