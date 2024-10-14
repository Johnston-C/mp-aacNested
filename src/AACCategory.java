import java.util.NoSuchElementException;

import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import java.util.Arrays;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Cade Johnston
 *
 */
public class AACCategory implements AACPage {

	/* Fields */

	/** A string representing the name of the AACCategory. */
	private String name;

	/** The AssociativeArray used to store data about the category. */
	private AssociativeArray<String, String> assocArray;

	/** A String array of the individual imageLocs stored in the AssociativeArray. */
	private String[] imageLocs;

	/* Constructor */

	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.name = name;
		this.assocArray = new AssociativeArray<String, String>();
		this.imageLocs = new String[0];
	} // AACCategory(String)
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
		  this.assocArray.set(imageLoc, text);
			this.imageLocs = Arrays.copyOf(this.imageLocs, this.imageLocs.length + 1);
			this.imageLocs[imageLocs.length - 1] = imageLoc;
		} catch (NullKeyException e) {
			System.err.printf("Null Key Exception in %s\n", this.name);
		} // try / catch
	} // addItem(String, String)

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		return this.imageLocs;
	} // getImageLocs()

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.name;
	} // getCategory()

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
		try {
		  if (assocArray.hasKey(imageLoc)) {
			  return assocArray.get(imageLoc);
		  } else {
			  throw new NoSuchElementException(imageLoc + " not found.");
	  	} // if / else
	  } catch (KeyNotFoundException e) {
			throw new NoSuchElementException(imageLoc + " not found.");
		} // try / catch
	} // select(String)

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return (assocArray.hasKey(imageLoc));
	} // hasImage(String)
} // class AACCategory
