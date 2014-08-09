package me.timothy.resources;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Resources {
	private static HashMap<String, Image> images;
	private static HashMap<String, SpriteSheet> animations;
	private static HashMap<String, File> sounds;
	private static Logger logger;

	public static void init() {
		images = new HashMap<>();
		animations = new HashMap<>();
		sounds = new HashMap<>();
		logger = LogManager.getLogger();
	}

	public static void initRes() {
		logger.info("Initializing resources..");
		try {
			File[] toCheckDirs = new File[] { new File("."), new File("resources"), new File("sounds") };
			List<File> toCheck = new ArrayList<>();
			
			for(File dir : toCheckDirs) {
				if(dir.exists() && dir.isDirectory()) {
					toCheck.addAll(Arrays.asList(dir.listFiles()));
				}
			}
			
			for(File f : toCheck) {
				if(f.isDirectory())
					continue;
				if(f.getName().endsWith("png")) {
					Image img = read(f);
					images.put(f.getName(), img);
				}else if(f.getName().endsWith("ogg")) {
					sounds.put(f.getName(), f);
				}
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		logger.printf(Level.INFO, "Done initializing resources (Loaded %d images, %d animations, and %d sounds)", images.size(), animations.size(), sounds.size());
	}
	
	public static Image read(File file) throws SlickException {
		return new Image(file.getAbsolutePath());
	}

	public static Image getImage(String string) {
		return images.get(string);
	}

	public static SpriteSheet getSheet(String string) {
		return animations.get(string);
	}

	public static File getAudio(String string) {
		return sounds.get(string);
	}
	
	public static List<File> getAudioStartingWith(String string) {
		Set<String> keys = sounds.keySet();
		List<File> result = new ArrayList<>();
		for(String str : keys) {
			if(str.startsWith(string))
				result.add(sounds.get(str));
		}
		return result;
	}
	
	public static void downloadIfNotExists(String fileName, String url, long expectedSize) {
		downloadIfNotExists(new File("."), fileName, url, null, null, null, expectedSize);
	}
	public static void downloadIfNotExists(File dir, String fileName,
			String downloadURL, String messageIfINeedToDownload, 
			String messageOnError, Runnable runOnError, long expectedSize) {
		if(logger == null)
			init();
		File checking = new File(dir, fileName);
		if(checking.exists())
			return;
		if(!dir.exists())
			dir.mkdirs();
		if(!dir.isDirectory()) {
			logger.error("Expected a directory at " + dir.getAbsolutePath());
			System.exit(1);
		}

		logger.log(Level.DEBUG, "Have to download " + fileName + ", potentially asking user");
		if(messageIfINeedToDownload != null) {
			int resp = JOptionPane.showConfirmDialog(null, messageIfINeedToDownload);
			if(resp != JOptionPane.YES_OPTION) {
				logger.log(Level.DEBUG, "User said not to, cancelling");
				System.exit(0);
			}
		}
		logger.log(Level.DEBUG, "Given permission, starting download");

		try {
			URL url = new URL(downloadURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream outStream = new FileOutputStream(checking);
			copyInputStream(inStream, outStream, expectedSize, true);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Couldn't complete download at " + downloadURL + ", io exception", e);
			
			logger.throwing(Level.ERROR, e);
			if(runOnError == null) {
				throw new RuntimeException(e);
			}
			if(JOptionPane.showConfirmDialog(null, messageOnError) == JOptionPane.YES_OPTION)
				runOnError.run();
			System.exit(1);
		}
		
	}



	public static void extract(String name) {
		extractIfNotFound(null, ".", null);
	}
	
	public static void extractIfNotFound(File dir, String zipFileStrInDir, String fileToSearchFor) {
		if(dir == null)
			dir = new File(".");
		if(fileToSearchFor != null) {
			File toSearchFor = new File(dir, fileToSearchFor);
			if(toSearchFor.exists())
				return;
		}
		File zipFileReal = new File(dir, zipFileStrInDir);
		String zipFileStr = zipFileReal.getAbsolutePath();
		// unzip http://www.devx.com/getHelpOn/10MinuteSolution/20447
		try {
			ZipFile zipFile = new ZipFile(zipFileStr);
			JFrame frame = new JFrame();
			frame.setLocationRelativeTo(null);
			JProgressBar progressBar = new JProgressBar();
			progressBar.setMaximum((int) zipFileReal.length());
			progressBar.setToolTipText("Extracting");
			frame.add(progressBar);
			frame.pack();
			frame.setVisible(true);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while(entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry)entries.nextElement();

				if(entry.isDirectory()) {
					// Assume directories are stored parents first then children.
					logger.log(Level.DEBUG, "Extracting directory: " + entry.getName());
					// This is not robust, just for demonstration purposes.
					(new File(dir, entry.getName())).mkdir();
					continue;
				}

				logger.log(Level.DEBUG, "Extracting file: " + entry.getName());
				File resultingFile = new File(dir, entry.getName());
				File resultFileDir = resultingFile.getParentFile();
				if(!resultFileDir.exists())
					resultFileDir.mkdirs();
				copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(new File(dir, entry.getName()))), entry.getSize(), false);
				progressBar.setValue((int) (progressBar.getValue() + entry.getCompressedSize()));
			}
			progressBar.setValue(progressBar.getMaximum());
			zipFile.close();
			frame.dispose();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.catching(Level.ERROR, ioe);
			System.exit(1);
		}
	}
	
	public static void prepareLWJGL() {
		File dir = null;
		String os = getOS();
		if(os.equals("windows")) {
			dir = new File(System.getenv("APPDATA"), "timgames/");
		}else {
			dir = new File(System.getProperty("user.home"), ".timgames/");
		}
		File lwjglDir = new File(dir, "lwjgl-2.9.1/");
		init();
		downloadIfNotExists(lwjglDir, "lwjgl-2.9.1.zip", 
				"http://umad-barnyard.com/lwjgl-2.9.1.zip",
				"Necessary LWJGL natives couldn't be found, I can attempt " +
				"to download it, but I make no promises",
				"Unfortunately I was unable to download it, so I'll open up the " +
				"link to the official download. Make sure you get LWJGL version 2.9.1, " +
				"and you put it at " + dir.getAbsolutePath() + "/lwjgl-2.9.1.zip", 
				new Runnable() {

					@Override
					public void run() {
						if(!Desktop.isDesktopSupported()) {
							JOptionPane.showMessageDialog(null, "I couldn't " +
									"even do that! Download it manually and try again");
							return;
						}
						
						try {
							Desktop.getDesktop().browse(new URI("http://www.lwjgl.org/download.php"));
						} catch (IOException | URISyntaxException e) {
							JOptionPane.showMessageDialog(null, "Oh cmon.. Address is http://www.lwjgl.org/download.php, good luck");
							System.exit(1);
						}
					}
			
		}, 5843626);
		
		extractIfNotFound(lwjglDir, "lwjgl-2.9.1.zip", "lwjgl-2.9.1");
		System.setProperty("org.lwjgl.librarypath", new File(dir, "lwjgl-2.9.1/lwjgl-2.9.1/native/" + os).getAbsolutePath()); // deal w/ it
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		
	}

	/**
	 * Gets the OS type, prompts the user for a choice if it cannot be determined.
	 * 
	 * @return the os
	 */
	private static String getOS() {
		String envOS = System.getProperty("os.name").toLowerCase();
		System.out.println(envOS);
		if(envOS.contains("win"))
			return "windows";
		else if(envOS.contains("mac"))
			return "macosx";
		else if(envOS.contains("nix"))
			return "linux";
		else if(envOS.contains("sunos"))
			return "solaris";

		String[] os = new String[] {"Windows", "Linux", "MacOSX", "Solaris"};
		String choice = (String) JOptionPane.showInputDialog(null,
				"Your OS could not be detected, please choose from the available options below", "OS Picker",
				JOptionPane.QUESTION_MESSAGE, null, os, os[1]);
		if(choice == null)
			System.exit(0);
		return choice.toLowerCase();
	}
	
	private static void copyInputStream(InputStream in, OutputStream out, long expectedSize, boolean makeProgressBar)
			throws IOException {
		JProgressBar progressBar = null;
		JFrame frame = null;
		if(makeProgressBar) {
			frame = new JFrame();
			frame.setLocationRelativeTo(null);
			progressBar = new JProgressBar();
			progressBar.setMaximum((int) expectedSize);
			frame.add(progressBar);
			frame.pack();
			frame.setVisible(true);
		}
		byte[] buffer = new byte[1024];
		int len;
		int totalBytes = 0;
		while((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
			totalBytes += len;
			if(makeProgressBar)
				progressBar.setValue(totalBytes);
		}

		in.close();
		out.close();
		if(makeProgressBar)
			frame.dispose();
	}
	
}
