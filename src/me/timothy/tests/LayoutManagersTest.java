package me.timothy.tests;

import java.util.List;

import me.timothy.gui.Button;
import me.timothy.gui.EmptySpace;
import me.timothy.gui.Text;
import me.timothy.layout.ConstraintBasedLayoutManager;
import me.timothy.layout.FlowLayoutManager;
import me.timothy.layout.GridLayoutManager;
import me.timothy.layout.LayoutComponent;
import me.timothy.layout.LayoutContainer;
import me.timothy.layout.LayoutManagedGame;
import me.timothy.utils.Constraint;
import me.timothy.utils.GamePreparer;
import me.timothy.utils.IRectangle;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class LayoutManagersTest extends LayoutManagedGame {
	private LayoutContainer centerC;
	private boolean direction;
	private Button swapDirection;
	
	public static void main(String[] args) {
		GamePreparer.prepareGame(new LayoutManagersTest("Flow Layout Test"), null);
	}
	
	public LayoutManagersTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		IRectangle pixelScreenRect = new IRectangle();
		pixelScreenRect.x = 0;
		pixelScreenRect.y = 0;
		pixelScreenRect.width = c.getWidth();
		pixelScreenRect.height = c.getHeight();
		
		container = new LayoutContainer(new ConstraintBasedLayoutManager());
		container.setLocation(pixelScreenRect);
		
		List<LayoutComponent> outer = container.getComponents();
		
		LayoutContainer leftSideC = new LayoutContainer(new GridLayoutManager(1, 0, true, false, false), Constraint.LEFT_ALIGN, Constraint.CENTER_Y);
		leftSideC.getPreferredSize().setSize((c.getWidth() / 5), (c.getHeight() * 8) / 10);
		List<LayoutComponent> leftSide = leftSideC.getComponents();
		
		leftSide.add(new Text("Left side", c.getDefaultFont()));
		leftSide.add(new EmptySpace());
		leftSide.add(new Text("Look, a grid!", c.getDefaultFont()));
		
		outer.add(leftSideC);
		outer.add(new Text("Mimicking BorderLayout with Constraints", c.getDefaultFont(), Constraint.TOP_ALIGN, Constraint.CENTER_X));
		
		centerC = new LayoutContainer(new FlowLayoutManager(direction = FlowLayoutManager.HORIZONTAL_LAYOUT), Constraint.CENTER_X, Constraint.CENTER_Y);
		centerC.getPreferredSize().setSize((c.getWidth() * 6) / 10, (c.getHeight() * 6) / 10);
		List<LayoutComponent> center = centerC.getComponents();
		
		center.add(new Text("A FlowLayout", c.getDefaultFont()));
		center.add(new EmptySpace(10, 10));
		center.add(swapDirection = new Button("Swap Direction", c.getDefaultFont()));
		
		outer.add(centerC);
		
		outer.add(new Text("Right side", c.getDefaultFont(), Constraint.RIGHT_ALIGN, Constraint.CENTER_Y));
		outer.add(new Text("It's just that easy", c.getDefaultFont(), Constraint.BOTTOM_ALIGN, Constraint.CENTER_X));
		
		container.layout();
	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {
		container.update(delta);
		
		if(swapDirection.recentlyPressed()) {
			centerC.setLayoutManager(new FlowLayoutManager(direction = !direction));
			
			container.invalidate();
			
			container.layout();
		}
	}
	
}
