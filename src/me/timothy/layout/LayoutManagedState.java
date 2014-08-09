package me.timothy.layout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class LayoutManagedState extends BasicGameState {
	protected LayoutContainer container;

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		container.processKeyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		
		container.processKeyReleased(key, c);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		
		container.processMousePressed(button, x, y);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		super.mouseReleased(button, x, y);
		
		container.processMouseReleased(button, x, y);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		super.mouseMoved(oldx, oldy, newx, newy);
		
		container.processMouseMove(oldx, oldy, newx, newy);
	}

	@Override
	public void render(GameContainer c, StateBasedGame sbg, Graphics g)
			throws SlickException {
		container.render(g);
	}
}
