import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Cade Johnston
 *
 */
public class AACMappings implements AACPage {
	
	/** An AACCategory that stores all items in the mapping. */
	AACCategory masterCategory;
	
	/** An associativeArray of AACCategories that stires all items, sorted. */
	AssociativeArray<String, AACCategory> categories;
	
	/** A String representing the current category. */
	String currentCategory;
	
	/** A String array representing the keys for every category. */
	String[] categoryKeys;

	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		try {
			File targetFile = new File(filename);
			Scanner input = new Scanner(targetFile);
			this.currentCategory = "";
			this.categories = new AssociativeArray<String, AACCategory>();
			this.masterCategory = new AACCategory("");
			this.categoryKeys = new String[0];
			String[] newPair;
			this.categories.set("", new AACCategory("default"));
			while (input.hasNext()) {
				newPair = input.nextLine().split(" ", 2);
				if (newPair[0].startsWith(">")) {
					addItem(newPair[0].substring(1), newPair[1]);
				} else {
					this.categoryKeys = Arrays.copyOf(this.categoryKeys, this.categoryKeys.length + 1);
					this.categoryKeys[this.categoryKeys.length - 1] = newPair[0];
					this.currentCategory = "";
					addItem(newPair[0], newPair[1]);
					this.currentCategory = newPair[1];
				} // if / else
			} // while
			this.currentCategory = "";
			input.close();
		} catch (NullKeyException e) {
			// Should never happen
			System.err.println("NullKeyException: Nonsense error.");
		} catch (FileNotFoundException e) {
			// Should never happen
			System.err.println("FileNotFoundException: File not found.");
		}
	} // AACMappings(String)
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
		try {
			if (this.currentCategory.equals("")) {
				this.currentCategory = this.categories.get(this.currentCategory).select(imageLoc);
				return "";				
			} else {
				return this.categories.get(this.currentCategory).select(imageLoc);
			} // if / else
		} catch (KeyNotFoundException e) {
			throw new NoSuchElementException("NoSuchElementException");
		} // try / catch
	} // select(String)
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		try {
      return this.categories.get(this.currentCategory).getImageLocs();
		} catch (KeyNotFoundException e) {
			System.err.println("KeyNotFoundException: Bad Category");
			return new String[]{}; // I don't know why this line is needed, but it is
		} // try / catch
	} // getImageLocs()
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		this.currentCategory = "";
	} // reset()
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		try {
			FileWriter outputFile = new FileWriter(filename);
			for (int i = 0; i < this.categoryKeys.length; i++) {
				outputFile.write(this.categoryKeys[i] + " " + this.masterCategory.select(this.categoryKeys[i]) + "\n");
				String[] inCatKeys = this.categories.get(this.categories.get("").select(this.categoryKeys[i])).getImageLocs();
				for (int j = 0; j < inCatKeys.length; j++) {
					outputFile.write(">" + inCatKeys[j] + " " + this.categories.get(this.categories.get("").select(this.categoryKeys[i])).select(inCatKeys[j]) + "\n");
				} // for [j]
			} // for [i]
			outputFile.close();
		} catch (IOException e) {
			System.err.println("IOException: Error writing to file");
		} catch (KeyNotFoundException e) {
			System.err.println("KeyNotFoundException: Should not happen");
		} // try / catch / catch
	} // writeToFile(String)
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		try {
			this.masterCategory.addItem(imageLoc, text);
			this.categories.get(this.currentCategory).addItem(imageLoc, text);
			if (currentCategory.equals("")) {
				this.categories.set(text, new AACCategory(text));
			} // if
		} catch (KeyNotFoundException e) {
			System.err.println("KeyNotFoundException: Item was given without a category.");
		} catch (NullKeyException e ) {
			System.err.println("NullKeyException: Null Category Name");
		}// try / catch
	} // addItem(String, String)


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		return this.currentCategory;
	} // getCategory()


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return (this.masterCategory.hasImage(imageLoc));
	} // hasImage(String)
} // class AACMappings
