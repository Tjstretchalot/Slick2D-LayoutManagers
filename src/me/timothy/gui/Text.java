package me.timothy.gui;

import java.util.Arrays;

import me.timothy.layout.BasicLayoutComponent;
import me.timothy.utils.Constraint;
import me.timothy.utils.IRectangle;
import me.timothy.utils.ScaledGraphics;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class Text extends BasicLayoutComponent {
	public static final Color DEFAULT_BACKGROUND = new Color(30, 30, 30);
	public static final Color DEFAULT_FOREGROUND = new Color(248, 248, 255);

	private String text;
	
	private IRectangle textRect;
	private Color background;
	private Color foreground;
	
	public Text(String text, Font font, Color background, Color foreground, Constraint... constraints) {
		this.text = text;
		this.background = background;
		this.foreground = foreground;
	
		setSize(font);
		getConstraints().addAll(Arrays.asList(constraints));
	}
	
	public Text(String text, Font font, Constraint... constraints) {
		this(text, font, DEFAULT_BACKGROUND, DEFAULT_FOREGROUND, constraints);
	}
	
	
	private void setSize(Font font) {
		setMinimumSize(new Dimension(font.getWidth(text), font.getHeight(text)));
		setPreferredSize(new Dimension((int) Math.round(getMinimumSize().getWidth() + 20), (int) Math.round(getMinimumSize().getHeight() + 20)));
	}


	@Override
	public void render(Graphics g) {
		IRectangle l = getLocation();
		
		if(background != null) {
			g.setColor(background);
			g.fillRect(l.x, l.y, l.width, l.height);
		}
		
		if(textRect == null) {
			textRect = new IRectangle(ScaledGraphics.getLocation(l.x, l.y, l.width, l.height, getMinimumSize().getWidth(), getMinimumSize().getHeight(), Constraint.CENTER_X, Constraint.CENTER_Y));
		}
		g.setColor(foreground);
		g.drawString(text, textRect.x, textRect.y);
		
	}


	public void setText(String text, Font font) {
		this.text = text;
		
		setSize(font);
		invalidate();
	}
	
	@Override
	public void invalidate() {
		textRect = null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Text [");
		if (text != null)
			builder.append("text=").append(text);
		builder.append("]");
		return builder.toString();
	}
	
	
}
