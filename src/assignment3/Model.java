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
	 * Cannot have a selected character without an active database. Furthermore,
	 * that character must be in said database. If activeDatabase == null,
	 * selectedCharacter == null. You can however have a database open and no
	 * selected character.
	 */

	public Model() {
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

	public void createDatabase(String filePath) throws IOException {
		CharacterDatabase db = new CharacterDatabase(filePath);
		activeDatabase = db;
		db.save();
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

	public boolean createSuperCharacter(String name, String defaultPath) throws IllegalPowerRankingException {
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

	public void deleteCharacter() {
		availableCharacters.removeIf(c -> c.equals(selectedCharacter.getName()));
		activeDatabase.remove(selectedCharacter);
		clearSelectedCharacter();
	}

	public void saveCharacter(String name, String description, ArrayList<String> Traits, ArrayList<String> Powers,
			String powerRanking) throws IllegalPowerRankingException {
		if (isSelectedCharacterSuperCharacter()) {
			int powerRank = 0;
			try{
				powerRank = Integer.parseInt(powerRanking);
			}
			catch(Exception e){
				throw new IllegalPowerRankingException();
			}
			SuperCharacter character = new SuperCharacter(name, description, powerRank);
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
