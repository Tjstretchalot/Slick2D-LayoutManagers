package me.timothy.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import me.timothy.resources.Resources;
import me.timothy.scaling.SizeScaleSystem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

public class GamePreparer {
	public static void prepareGame(Game game, String baseUrl, NecessaryFile... extraFiles) {
		float defaultDisplayWidth = SizeScaleSystem.EXPECTED_WIDTH / 2;
		float defaultDisplayHeight = SizeScaleSystem.EXPECTED_HEIGHT / 2;
		boolean fullscreen = false, defaultDisplay = !fullscreen;
		File resolutionInfo = new File("graphics.json");

		if(resolutionInfo.exists()) {
			try(FileReader fr = new FileReader(resolutionInfo)) {
				JSONObject obj = (JSONObject) new JSONParser().parse(fr);
				Set<?> keys = obj.keySet();
				for(Object o : keys) {
					if(o instanceof String) {
						String key = (String) o;
						switch(key.toLowerCase()) {
						case "width":
							defaultDisplayWidth = JSONCompatible.getFloat(obj, key);
							break;
						case "height":
							defaultDisplayHeight = JSONCompatible.getFloat(obj, key);
							break;
						case "fullscreen":
							fullscreen = JSONCompatible.getBoolean(obj, key);
							defaultDisplay = !fullscreen;
							break;
						}
					}
				}
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
			float expHeight = defaultDisplayWidth * (SizeScaleSystem.EXPECTED_HEIGHT/SizeScaleSystem.EXPECTED_WIDTH);
			float expWidth = defaultDisplayHeight * (SizeScaleSystem.EXPECTED_WIDTH/SizeScaleSystem.EXPECTED_HEIGHT);
			if(Math.round(defaultDisplayWidth) != Math.round(expWidth)) {
				if(defaultDisplayHeight < expHeight) {
					System.err.printf("%f x %f is an invalid resolution; adjusting to %f x %f\n", defaultDisplayWidth, defaultDisplayHeight, defaultDisplayWidth, expHeight);
					defaultDisplayHeight = expHeight;
				}else {
					System.err.printf("%f x %f is an invalid resolution; adjusting to %f x %f\n", defaultDisplayWidth, defaultDisplayHeight, expWidth, defaultDisplayHeight);
					defaultDisplayWidth = expWidth;
				}
			}
		}

		Resources.prepareLWJGL();
		
		for(NecessaryFile extraFile : extraFiles) {
			String url = baseUrl + extraFile;

			if(extraFile.name.endsWith(".zip")) {
				if(!new File(extraFile.name.substring(0, extraFile.name.length() - 4)).exists()) {
					Resources.downloadIfNotExists(extraFile.name, url, extraFile.expectedSize);
					Resources.extract(extraFile.name);
				}
			}else {
				Resources.downloadIfNotExists(extraFile.name, url, extraFile.expectedSize);
			}
		}
		
		Resources.initRes();

		try {
			AppGameContainer appgc = new AppGameContainer(game);
			appgc.setTargetFrameRate(60);
//			appgc.setShowFPS(false);
			appgc.setAlwaysRender(true);
			if(fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				DisplayMode current, best = null;
				float ratio = 0f;
				for (int i=0;i<modes.length;i++) {
					current = modes[i];
					float rX = (float) current.getWidth() / SizeScaleSystem.EXPECTED_WIDTH;
					float rY = (float) current.getHeight() / SizeScaleSystem.EXPECTED_HEIGHT;
					System.out.println(current.getWidth() + "x" + current.getHeight() + " -> " + rX + "x" + rY);
					if(rX == rY && rX > ratio) {
						best = current;
						ratio = rX;
					}
				}
				if(best == null) {
					System.out.println("Failed to find an appropriately scaled resolution, using default display");
					defaultDisplay = true;
				} else {
					appgc.setDisplayMode(best.getWidth(), best.getHeight(), true);
					SizeScaleSystem.setRealHeight(best.getHeight());
					SizeScaleSystem.setRealWidth(best.getWidth());
					System.out.println("I choose " + best.getWidth() + "x" + best.getHeight());
				}
			}

			if(defaultDisplay) {
				SizeScaleSystem.setRealWidth(Math.round(defaultDisplayWidth));
				SizeScaleSystem.setRealHeight(Math.round(defaultDisplayHeight));
				appgc.setDisplayMode(Math.round(defaultDisplayWidth), Math.round(defaultDisplayHeight), false);
			}
			appgc.start();
		}catch(SlickException | LWJGLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
