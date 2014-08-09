package me.timothy.layout;

import java.util.List;

import me.timothy.utils.Constraint;
import me.timothy.utils.IRectangle;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Graphics;

/**
 * Similiar to AWT Component
 * 
 * This is a basic component that can be laid out via a LayoutManager. A LayoutComponent
 * must have a graphical representation, but may be comprised of multiple sub-components.
 * 
 * A large difference is LayoutComponents are regularly redrawn and may be updated regularly,
 * this is even natively supported via update(delta).
 * 
 * @author Timothy
 *
 */
public interface LayoutComponent {
	public IRectangle getLocation();
	public void setLocation(IRectangle rect);
	
	public Dimension getPreferredSize();
	public Dimension getMinimumSize(); // null implies no minimum
	public Dimension getMaximumSize(); // null implies no maximum
	public void setMinimumSize(Dimension rect);
	public void setMaximumSize(Dimension rect);
	public void setPreferredSize(Dimension rect);
	
	/**
	 * @return constraints, or null
	 */
	public List<Constraint> getConstraints();
	
	/** Clears any cached location information */
	public void invalidate();
	
	public void processMouseMove(int oldx, int oldy, int newx, int newy);
	public void processMousePressed(int button, int mousex, int mousey);
	void processMouseReleased(int button, int mousex, int mousey);
	public void processKeyPressed(int key, char ch);
	public void processKeyReleased(int key, char ch);
	
	public void render(Graphics g);
	public void update(int delta);
}
