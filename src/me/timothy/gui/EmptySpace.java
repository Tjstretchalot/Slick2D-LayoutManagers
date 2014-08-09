package me.timothy.gui;

import java.util.Arrays;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Graphics;

import me.timothy.layout.BasicLayoutComponent;
import me.timothy.utils.Constraint;

public class EmptySpace extends BasicLayoutComponent {

	public EmptySpace(int prefWidth, int prefHeight, Constraint... constraints) {
		setMinimumSize(new Dimension(0, 0));
		setPreferredSize(new Dimension(prefWidth, prefHeight));


		getConstraints().addAll(Arrays.asList(constraints));
	}
	public EmptySpace(Constraint... constraints) {
		this(1, 1, constraints);
	}
	@Override
	public void invalidate() {
	}

	@Override
	public void render(Graphics g) {	
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmptySpace [");
		if (getPreferredSize() != null)
			builder.append("getPrefferredSize()=").append(getPreferredSize());
		builder.append("]");
		return builder.toString();
	}
}
