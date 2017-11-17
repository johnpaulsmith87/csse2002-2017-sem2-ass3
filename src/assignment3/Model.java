package assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for the Character Editor
 * 
 * @author John-Paul Smith
 */
public class Model {
	private Character selectedCharacter;
	private CharacterDatabase activeDatabase;
	// list of characters in a database for display
	private ObservableList<String> availableCharacters;

	// some helper methods for the controller
	public boolean isSelectedCharacterSuperCharacter() {
		return selectedCharacter instanceof SuperCharacter;
	}

	public boolean hasSelectedCharacter() {
		return selectedCharacter != null;
	}

	public boolean hasActiveDatabase() {
		return activeDatabase != null;
	}

	/**
	 * The model allows for both selectedCharacter and activeDatabase to be null
	 * (just like at start up). Cannot have a selected character without an
	 * active database. Furthermore, that character must be in said database. If
	 * activeDatabase == null, selectedCharacter == null. You can however have a
	 * database open and no selected character. availableCharacter is always at
	 * least an empty list (never null).
	 */

	public Model() {
		clear();
	}

	/**
	 * 
	 * @return sorted list of characters in database
	 */
	public ObservableList<String> getAvailableCharacters() {
		return availableCharacters.sorted();
	}

	/**
	 * Sets the image path to the selectedCharacter
	 * 
	 * @requires selectCharcater is not null
	 * 
	 * @param imagePath
	 */
	public void setImagePath(String imagePath) {
		selectedCharacter.setImagePath(imagePath);
	}

	/**
	 * @requires selectedCharacter is not null
	 * @return The image path of selectedCharacter
	 */

	public String getImagePath() {
		return selectedCharacter.getImagePath();
	}

	/**
	 * @requires selectedCharacter is not null
	 * @return The name of the selectedCharacter
	 */
	public String getSelectedCharacterName() {
		return selectedCharacter.getName();
	}

	/**
	 * @requires selectedCharacter is not null
	 * @return The description of the selected character
	 */
	public String getSelectedCharacterDescription() {
		return selectedCharacter.getDescription();
	}

	/**
	 * @requires selectedCharacter is a superCharacter and not null
	 * @return The power ranking of the selected character
	 */
	public String getSuperCharacterPowerRanking() {
		return "" + ((SuperCharacter) selectedCharacter).getPowerRanking();
	}

	/**
	 * @requires Selected character is not null
	 * @return arraylist with traits
	 */
	public ArrayList<String> getSelectedCharacterTraits() {
		return new ArrayList<String>(selectedCharacter.getTraits());
	}

	/**
	 * @requires Requires the selectedCharacter to be a SuperCharacter
	 * @return Returns arraylist with powers
	 */
	public ArrayList<String> getSelectedSuperCharacterPowers() {
		return new ArrayList<String>(
				((SuperCharacter) selectedCharacter).getPowers());
	}

	/**
	 * Creates a new character with some default values
	 * 
	 * @param name
	 *            name of character
	 * @param defaultPath
	 *            default image filepath
	 * @return True if character didn't already exist, False otherwise
	 */
	public boolean createCharacter(String name, String defaultPath) {
		Character character = new Character(name, "");
		boolean result = activeDatabase.search(name) == null;
		if (result) {
			character.setImagePath(defaultPath);
			activeDatabase.add(character);
			selectedCharacter = character;
			availableCharacters.add(character.name);
		}
		return result;
	}

	/**
	 * Creates a new database with an absolute filepath
	 * 
	 * @param filePath
	 *            Absolute filepath for new database, will overwite if one
	 *            already exists
	 * @throws IOException
	 */
	public void createDatabase(String filePath) throws IOException {
		CharacterDatabase db = new CharacterDatabase(filePath);
		activeDatabase = db;
	}

	/**
	 * @requires CharacterDatabase is active (i.e not null)
	 * @param name
	 *            of the character to select
	 */
	public void selectCharacter(String name) {
		// select character with given name - only makes sense if db is active
		selectedCharacter = activeDatabase.search(name);
	}

	// helper method
	public void clearSelectedCharacter() {
		selectedCharacter = null;
	}

	/**
	 * Creates a new supercharacter and adds it to the open database
	 * 
	 * @param name
	 *            name of character
	 * @param defaultPath
	 *            default image filepath
	 * @return True if character didn't already exist, False otherwise
	 * @throws IllegalPowerRankingException
	 */
	public boolean createSuperCharacter(String name, String defaultPath)
			throws IllegalPowerRankingException {
		Character character = new SuperCharacter(name, "", 5);
		boolean result = activeDatabase.search(name) == null;
		if (result) {
			character.setImagePath(defaultPath);
			selectedCharacter = character;
			activeDatabase.add(character);
			availableCharacters.add(character.name);
		}
		return result;
	}

	/**
	 * Removes character from database
	 * 
	 * @requires selectedCharacter is not null
	 * 
	 */
	public void deleteCharacter() {
		availableCharacters
				.removeIf(c -> c.equals(selectedCharacter.getName()));
		activeDatabase.remove(selectedCharacter);
		clearSelectedCharacter();
	}
	/**
	 * Saves character to database.
	 * 
	 * @param name
	 * @param description
	 * @param Traits ArrayList of traits
	 * @param Powers ArrayList of powers
	 * @param powerRanking
	 * @throws IllegalPowerRankingException
	 */
	public void saveCharacter(String name, String description,
			ArrayList<String> Traits, ArrayList<String> Powers,
			String powerRanking) throws IllegalPowerRankingException {
		if (isSelectedCharacterSuperCharacter()) {
			int powerRank = 0;
			try {
				powerRank = Integer.parseInt(powerRanking);
			} catch (Exception e) {
				throw new IllegalPowerRankingException();
			}
			SuperCharacter character = new SuperCharacter(name, description,
					powerRank);
			Traits.stream().forEach(t -> character.addTrait(t));
			Powers.stream().forEach(p -> character.addPower(p));
			character.setImagePath(getImagePath());
			activeDatabase.update(character);
		} else {
			Character character = new Character(name, description);
			Traits.stream().forEach(t -> character.addTrait(t));
			character.setImagePath(getImagePath());
			activeDatabase.update(character);
		}
	}
	/**
	 * Saves database to file.
	 * @requires activeDatabase != null
	 * @throws IOException
	 */
	public void saveDatabase() throws IOException {
		activeDatabase.save();
	}

	/**
	 * Loads database from file.
	 * @param filename Absolute path to database file
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public void loadDatabase(String filename)
			throws FileNotFoundException, Exception {
		// handle errors on the controller/view level
		CharacterDatabase loadedDatabase = new CharacterDatabase(filename);
		loadedDatabase.load();
		activeDatabase = loadedDatabase;
		availableCharacters.clear();
		availableCharacters.addAll(activeDatabase.getCharacterNames());

	}

	/**
	 * Helper function to clear instance variables. It's called on
	 * initialisation to maintain the invariant
	 */
	public void clear() {
		// set database and selected character to null.
		availableCharacters = FXCollections.observableArrayList();
		activeDatabase = null;
		selectedCharacter = null;
	}
	/**
	 * 
	 * @param name
	 * @return True if character name is in db, False otherwise
	 */
	public boolean searchSuccess(String name) {
		Character result = activeDatabase.search(name);
		selectedCharacter = result;
		return result != null;
	}

}
