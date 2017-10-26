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
	/*
	 * TODO: INVARIANT GOES HERE
	 */

	public Model() {
		/*
		 * This conatins: database and currently selected character TODO: DELETE
		 * THIS LINE AND IMPLEMENT CONSTRUCTOR
		 */
		availableCharacters = FXCollections.observableArrayList();
	}

	public ObservableList<String> getAvailableCharacters() {
		return availableCharacters;
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

	public ArrayList<String> getSelectedCharacterTraits() {
		return new ArrayList<String>(selectedCharacter.getTraits());
	}

	public void setSelectedCharacterTraits(ArrayList<String> traits) {
		//remove all traits, then add these new traits - doesn't care if it's the same!
	}

	/**
	 * @requires Requires the selectedCharacter to be a supercharacter
	 * @return
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

}
