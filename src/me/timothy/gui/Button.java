package me.timothy.gui;

import static me.timothy.utils.Constraint.CENTER_X;
import static me.timothy.utils.Constraint.CENTER_Y;

import java.util.Arrays;

import me.timothy.layout.BasicLayoutComponent;
import me.timothy.utils.Constraint;
import me.timothy.utils.IRectangle;
import me.timothy.utils.ScaledGraphics;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Button extends BasicLayoutComponent {
	private String text;
	private Color background;
	private Color backgroundHover;
	private Color foreground;
	private Color foregroundHover;
	
	private Rectangle textRect;
	private boolean hover;
	private boolean pressed;
	
	private boolean noPress;

	public Button(String text, Font f, Color background,
			Color backgroundHover, Color foreground, Color foregroundHover, Constraint... constraints) {
		this.text = text;
		this.background = background;
		this.backgroundHover = backgroundHover;
		this.foreground = foreground;
		this.foregroundHover = foregroundHover;
		
		setSize(f);


		getConstraints().addAll(Arrays.asList(constraints));
	}

	private void setSize(Font f) {
		setMinimumSize(new Dimension(f.getWidth(text), f.getHeight(text)));
		setPreferredSize(new Dimension((int) Math.round(getMinimumSize().getWidth() + 40), (int) Math.round(getMinimumSize().getHeight() + 40)));
	}

	public Button(String text, Font f, Color background, Color foreground, Constraint... constraints) {
		this(text, f, background, background.darker(),
				foreground, foreground.brighter(), constraints);
	}
	
	public Button(String text, Font f, Constraint... constraints) {
		this(text, f, new Color(30, 30, 30), new Color(248, 248, 255), constraints);
	}
	
	@Override
	public void render(Graphics g) {
		if(!hover)
			g.setColor(background);
		else
			g.setColor(backgroundHover);
		
		final IRectangle loc = getLocation();
		g.fillRect(loc.x, loc.y, loc.width, loc.height);
		
		if(!hover)
			g.setColor(foreground);
		else
			g.setColor(foregroundHover);
		
		if(textRect == null) {
			textRect = ScaledGraphics.getLocation(loc.x, loc.y, loc.width, loc.height, getMinimumSize().getWidth(), getMinimumSize().getHeight(), CENTER_X, CENTER_Y);
		}
		
		g.drawString(text, (int) textRect.getX(), (int) textRect.getY());
	}
	
	
	@Override
	public void processMouseMove(int oldx, int oldy, int newx, int newy) {
		hover = getLocation().contains(oldx, oldy);
	}
	
	@Override
	public void processMousePressed(int button, int mousex, int mousey) {
		if(button == 0 && hover && !noPress) {
			pressed = true;
			noPress = true;
		}
	}
	@Override
	public void processMouseReleased(int button, int mousex, int mousey) {
		if(button == 0 && noPress) {
			noPress = false;
		}
	}

	public boolean isBeingHovered() {
		return hover;
	}
	
	public boolean recentlyPressed() {
		if(pressed) {
			pressed = false;
			return true;
		}
		return false;
	}
	
	@Override
	public void invalidate() {
		textRect = null;
	}
	
	public void setText(String text, Font f) {
		this.text = text;
		
		setSize(f);
		invalidate();
	}
	
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Button [");
		if (text != null)
			builder.append("text=").append(text).append(", ");
		builder.append("hover=").append(hover).append(", pressed=")
				.append(pressed).append("]");
		return builder.toString();
	}
}
