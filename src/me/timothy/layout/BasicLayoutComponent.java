package me.timothy.layout;

import java.util.ArrayList;
import java.util.List;

import me.timothy.utils.Constraint;
import me.timothy.utils.IRectangle;

import org.lwjgl.util.Dimension;

public abstract class BasicLayoutComponent implements LayoutComponent {
	private IRectangle location;
	
	private Dimension preferredSize;
	private Dimension minimumSize;
	private Dimension maximumSize;
	
	private List<Constraint> constraints;
	
	public BasicLayoutComponent() {
		location = new IRectangle();
	}
	@Override
	public IRectangle getLocation() {
		return location;
	}

	@Override
	public void setLocation(IRectangle rect) {
		location = rect;
	}

	@Override
	public Dimension getPreferredSize() {
		return preferredSize;
	}

	@Override
	public Dimension getMinimumSize() {
		return minimumSize;
	}

	@Override
	public Dimension getMaximumSize() {
		return maximumSize;
	}

	@Override
	public void setMinimumSize(Dimension rect) {
		minimumSize = rect;
	}

	@Override
	public void setMaximumSize(Dimension rect) {
		maximumSize = rect;
	}

	@Override
	public void setPreferredSize(Dimension rect) {
		preferredSize = rect;
	}

	@Override
	public List<Constraint> getConstraints() {
		if(constraints == null)
			constraints = new ArrayList<>();
		return constraints;
	}
	
	@Override
	public void processMouseMove(int oldx, int oldy, int newx, int newy) {}

	@Override
	public void processMousePressed(int button, int mousex, int mousey) {}

	@Override
	public void processMouseReleased(int button, int mousex, int mousey) {}

	@Override
	public void processKeyPressed(int key, char ch) {}

	@Override
	public void processKeyReleased(int key, char ch) {}

	@Override
	public void update(int delta) {}
}
