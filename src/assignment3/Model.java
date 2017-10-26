package assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for the Character Editor
 * 
 * @author leggy (Lachlan Healey)
 */
public class Model {

	private Character selectedCharacter;
	private CharacterDatabase activeDatabase;
	private ObservableList<String> availableCharacters;// for the list view!
	private static final String defaultDatabaseURI = "/csse2002-2017-sem2-ass3/default.dat";

	public boolean isSelectedCharacterSuperCharacter() {
		return selectedCharacter instanceof SuperCharacter;
	}

	public boolean hasSelectedCharacter() {
		return selectedCharacter != null;
	}

	public boolean hasActiveDatabase() {
		return activeDatabase != null;
	}
	/*
	 * TODO: INVARIANT GOES HERE
	 */

	public Model() {
		/*
		 * This conatins: database and currently selected character TODO: DELETE
		 * THIS LINE AND IMPLEMENT CONSTRUCTOR
		 */
		clear();
	}

	public ObservableList<String> getAvailableCharacters() {
		return availableCharacters.sorted();
	}

	public void setImagePath(String imagePath) {
		selectedCharacter.setImagePath(imagePath);
	}

	public String getImagePath() {
		return selectedCharacter.getImagePath();
	}

	public String getSelectedCharacterName() {
		return selectedCharacter.getName();
	}

	public String getSelectedCharacterDescription() {
		return selectedCharacter.getDescription();
	}

	public void setCharacterDescription(String description) {
		selectedCharacter.setDescription(description);
	}

	public String getSuperCharacterPowerRanking() {
		return "" + ((SuperCharacter) selectedCharacter).getPowerRanking();
	}

	public void setSeleectedSuperCharacterPowerRanking(int powerLevel) throws IllegalPowerRankingException {
		((SuperCharacter) selectedCharacter).setPowerRanking(powerLevel);
	}

	public ArrayList<String> getSelectedCharacterTraits() {
		return new ArrayList<String>(selectedCharacter.getTraits());
	}

	public void setSelectedCharacterTraits(ArrayList<String> traits) {
		// remove all traits, then add these new traits provided from
		// view>controller
		for (String trait : selectedCharacter.getTraits()) {
			selectedCharacter.removeTrait(trait);
		}
		for (String trait : traits) {
			selectedCharacter.addTrait(trait);
		}
	}

	/**
	 * @requires Selected character to be a SuperCharacter
	 * @param powers
	 *            The list of powers to replacing the existing set.
	 */
	public void setSelectedSuperCharacterPowers(ArrayList<String> powers) {
		// essentially the same as traits
		SuperCharacter superChar = ((SuperCharacter) selectedCharacter);
		for (String power : superChar.getPowers()) {
			superChar.removePower(power);
		}
		for (String power : powers) {
			superChar.addPower(power);
		}
	}

	/**
	 * @requires Requires the selectedCharacter to be a supercharacter
	 * @return Returns
	 */
	public ArrayList<String> getSelectedSuperCharacterPowers() {
		return new ArrayList<String>(((SuperCharacter) selectedCharacter).getPowers());
	}

	public void createCharacter(String name) {
		Character character = new Character(name, "");
		selectedCharacter = character;
		activeDatabase.add(character);
		availableCharacters.add(character.name);
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

	public void clearSelectedCharacter() {
		selectedCharacter = null;
	}

	public void createSuperCharacter(String name) throws IllegalPowerRankingException {
		Character character = new SuperCharacter(name, "", 5);
		selectedCharacter = character;
		activeDatabase.add(character);
		availableCharacters.add(character.name);
	}

	public void saveDatabase() throws IOException {
		activeDatabase.save();
	}

	public void loadDatabase(String filename) throws FileNotFoundException, Exception {
		// handle errors on the controller/view level
		CharacterDatabase loadedDatabase = new CharacterDatabase(filename);
		loadedDatabase.load();
		activeDatabase = loadedDatabase;
		availableCharacters.clear();
		availableCharacters.addAll(activeDatabase.getCharacterNames());

	}

	public void clear() {
		// set database and selected character to null.
		availableCharacters = FXCollections.observableArrayList();
		activeDatabase = null;
		selectedCharacter = null;
	}

	public boolean searchSuccess(String name) {
		Character result = activeDatabase.search(name);
		selectedCharacter = result;
		return result != null;
	}

}
