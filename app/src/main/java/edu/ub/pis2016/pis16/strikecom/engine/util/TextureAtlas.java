package edu.ub.pis2016.pis16.strikecom.engine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;

/**
 * A class that loads an .atlas file and adds all the regions to a HashMap using the {@code String}
 * name as key. All regions are stored into TextureRegion[] arrays. The default max page size is 8.
 * <p>
 * To quickly recover a region use {@code getRegion(String name)}, if you need a certain index use
 * {@code getRegion(String name, int index}.
 * <p>
 * <strong>Note:</strong> Only one Texture per texture Atlas.
 */
public class TextureAtlas {

	private static final int PAGE_SIZE = 8;

	Game game;
	Texture texture;

	HashMap<String, TextureRegion[]> regions;

	/** Path to the folder containing the atlas files. */
	private String path;

	/**
	 * @param game Game instance to get the FileReader.
	 * @param path Path to the .atlas file.
	 */
	public TextureAtlas(Game game, String path) {
		this.game = game;
		this.regions = new HashMap<>();

		// Extract folder path from file path
		Pattern folder = Pattern.compile("^(.*/)([^/]*)$");
		Matcher match = folder.matcher(path);
		this.path = match.group(1);

		loadAtlasFile(path);
	}

	public Texture getTexture() {
		return texture;
	}

	/**
	 * Returns the texture region by the named indicated by parameter.
	 *
	 * @param name  Name of the region in the atlas file
	 * @param index Index of the region
	 * @throws IllegalArgumentException if the name does not exist
	 */
	public TextureRegion getRegion(String name, int index) {
		TextureRegion region;
		if ((region = regions.get(name)[index]) == null)
			throw new IllegalArgumentException("No region by that name");
		return region;
	}

	public TextureRegion getRegion(String name) {
		return getRegion(name, 0);
	}

	public void addRegion(String name, int x, int y, int w, int h) {
		addRegion(name, x, y, w, h, 0);
	}

	public void addRegion(String name, int x, int y, int w, int h, int index) {
		// Try to get an existing bucket, otherwise create it.
		TextureRegion[] bucket;
		if ((bucket = regions.get(name)) == null)
			bucket = new TextureRegion[PAGE_SIZE];

		TextureRegion newTR = new TextureRegion(texture, x, y, w, h);
		bucket[index] = newTR;
		regions.put(name, bucket);
	}

	private void loadAtlasFile(String path) {
		FileIO fio = game.getFileIO();
		this.regions.clear();

		// Regex matchers
		Pattern patternImgFIle = Pattern.compile("^(\\w+\\.\\w)+$"); // Match ****.*** (file name + extension)
		Pattern patternRgnName = Pattern.compile("^(\\w+)$"); // Match a single word in a line, from start to end
		Pattern patternXY = Pattern.compile("\\s+xy:.+(\\d+),\\s(\\d+)"); // match "  xy: * *" and capture x, y cords
		Pattern patternWH = Pattern.compile("\\s+size:.+(\\d+),\\s(\\d+)"); // match "  size: * *" and capture w, h size
		Pattern patternIND = Pattern.compile("^\\s+index:.+(\\d+)"); // match "  index: *" and capture index

		BufferedReader reader = null;
		try {
			// Construct a BufferedReader from InputStream to read text line-by-line
			reader = new BufferedReader(new InputStreamReader(fio.readAsset(path)));
			String line;

			Matcher match;
			String regionName = null;
			int x = 0, y = 0, w = 0, h = 0, index;

			while ((line = reader.readLine()) != null) {

				// Filename matcher
				match = patternImgFIle.matcher(line);
				if (match.find()) {
					// Check if we've already loaded a texture
					if (texture != null)
						throw new IllegalStateException("Atlas already associated with a Texture");

					String imgFile = match.group(1);
					this.texture = new Texture(game, this.path + File.separator + imgFile);

					System.out.println("Texture loaded: "+imgFile);

					continue;
				}

				// Region name matcher
				match = patternRgnName.matcher(line);
				if (match.find()) {
					// Found new region definition
					regionName = match.group(1);

					String[] regionSpec = new String[6];
					for (int i = 0; i < 6; i++)
						regionSpec[i] = reader.readLine();

					// Read the 2nd and 3rd line of spec, X Y coords and W H size
					match = patternXY.matcher(regionSpec[1]);
					x = Integer.valueOf(match.group(1));
					y = Integer.valueOf(match.group(2));
					match = patternWH.matcher(regionSpec[2]);
					w = Integer.valueOf(match.group(1));
					h = Integer.valueOf(match.group(2));
					match = patternIND.matcher(regionSpec[5]);
					index = Integer.valueOf(match.group(1));

					// Check index value
					if (index >= 0)
						addRegion(regionName, x, y, w, h, index);
					else
						addRegion(regionName, x, y, w, h);


					System.out.println("Region added: "+regionName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dispose() {
		if (this.texture != null) {
			this.texture.dispose();
			this.texture = null;
		}
	}
}
