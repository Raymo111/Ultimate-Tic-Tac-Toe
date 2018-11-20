/*
 * Author: Raymond Li
 * Date created: 2018-11-19
 * Description: Ultimate Tic-Tac-Toe Save Game File filter class
 */
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class UTTTSGFilter extends FileFilter {

	/**
	 * Accepts only .utttsg files when selecting files in save loader
	 * 
	 * @param f
	 *            A file to check for
	 * @return Whether the file has the .utttsg extension
	 */
	public boolean accept(File f) {

		// Enable all directories to be opened
		if (f.isDirectory()) {
			return true;
		}

		// Gets the extension of the selected file
		String extension = getExtension(f);

		// Checks if file has an extension first
		if (extension != null) {

			// Only returns true if the file extension is mssg
			if (extension.equals("utttsg")) {
				return true;
			}
		}

		// Returns false if all other conditions fail
		return false;
	}

	/**
	 * The description of this filter to be put into the file selection window
	 */
	public String getDescription() {
		return "Ultimate Tic-Tac-Toe Savegames (.uttsg files)";
	}

	/**
	 * Gets extension of selected file
	 * 
	 * @param f
	 *            The file the user has selected
	 * @return The extension of the selected file
	 */
	public String getExtension(File file) {
		String extension = null;
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');

		if (index > 0 && index < fileName.length() - 1) {
			extension = fileName.substring(index + 1).toLowerCase();
		}
		return extension;
	}

}