package assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller class for the Character Editor.
 * 
 * @author leggy (Lachlan Healey)
 */
public class Controller implements Initializable {

	private Model model;

	// text fields
	@FXML
	private TextField txtFilename;
	@FXML
	private TextField txtCreateCharacterName;
	@FXML
	private TextField txtSearchCharacter;
	@FXML
	private TextField txtDescription;
	@FXML
	private TextField txtPowerLevel;
	// text areas
	@FXML
	private TextArea txtTraits;
	@FXML
	private TextArea txtPowers;
	// buttons
	@FXML
	private Button btnSaveDatabase;
	@FXML
	private Button btnLoadDatabase;
	@FXML
	private Button btnCreateDatabase;
	@FXML
	private Button btnCreateCharacter;
	@FXML
	private Button btnCreateSuperCharacter;
	@FXML
	private Button btnSearchCharacter;
	@FXML
	private Button btnClearSearch;
	@FXML
	private Button btnDeleteCharacter;
	@FXML
	private Button btnChangeImage;
	@FXML
	private Button btnSaveCharacter;
	// imageview
	@FXML
	private ImageView imvCharacterImage;
	@FXML
	private Label lblCharacterName;
	// listview
	@FXML
	private ListView<String> lsvViewDatabase;
	// just a commonly used constant
	private static final String newLine = System.getProperty("line.separator");

	public Controller() {
		this.model = new Model();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleClearSearchAction();
		handleSearchCharacterAction();
		handleLoadDatabaseAction();
		handleSaveDatabaseAction();
		handleSearchCharacterAction();
		handleCreateDatabaseAction();
		handleCreateCharacterAction();
		handleCreateSuperCharacterAction();
		handleDeleteCharacterAction();
	}

	/*
	 * / TODO: **fix search**, finish up all the handles, test, comment &
	 * submit!
	 */
	private void handleClearSearchAction() {
		btnClearSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				model.clearSelectedCharacter();
				txtSearchCharacter.setText("");
				updateAll();
			}
		});
	}

	private void handleLoadDatabaseAction() {
		btnLoadDatabase.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String filename = txtFilename.getText();
				try {
					// load database
					model.loadDatabase(filename);
					// populate listview with items
					updateAll();

				} catch (FileNotFoundException e) {
					errorAlert("File not found. Please try again.", "Error");
				} catch (Exception e) {
					errorAlert(e.getMessage(), "Error");
				}
			}
		});
	}

	private void handleSaveDatabaseAction() {
		btnSaveDatabase.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					// save active database to file
					model.saveDatabase();

				} catch (Exception e) {
					errorAlert(e.getMessage(), "Error");
				}
			}
		});
	}

	private void handleCreateDatabaseAction() {
		btnCreateDatabase.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String filename = txtFilename.getText();
					model.clear();
					model.createDatabase(filename);
					updateAll();
				} catch (IOException e) {
					errorAlert(e.getMessage(), "Error");
				}
			}
		});
	}

	private void handleCreateCharacterAction() {
		btnCreateCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = txtCreateCharacterName.getText();
				if (model.hasActiveDatabase() && name != null && model.createCharacter(name)) {
					updateAll();
				} else
					infoAlert("You must have an open database to create a character", "No open database");
			}
		});
	}

	private void handleCreateSuperCharacterAction() {
		btnCreateSuperCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = txtCreateCharacterName.getText();
				try {
					if (model.hasActiveDatabase() && name != null && model.createSuperCharacter(name))
						updateAll();
					else
						infoAlert("You must have an open database to create a character", "No open database");
				} catch (IllegalPowerRankingException e) {
					errorAlert(e.getMessage(), "Illegal Power Ranking");
				}
			}
		});
	}

	private void handleSearchCharacterAction() {
		btnSearchCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (model.hasActiveDatabase() && !model.searchSuccess(txtSearchCharacter.getText()))
					infoAlert("No character found matching: " + txtSearchCharacter.getText(), "No Match!");
				updateAll();
			}
		});
	}

	private void handleDeleteCharacterAction() {
		btnDeleteCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(model.hasActiveDatabase() && model.hasSelectedCharacter())
				{
					model.deleteCharacter();
					updateAll();
				}
				else
					infoAlert("You must have a character selected to delete","No character selected");
			}
		});
	}

	private void updateAll() {
		// updates all database and character info in the UI
		if (model.hasSelectedCharacter()) {
			lsvViewDatabase.setItems(model.getAvailableCharacters());
			if (model.hasSelectedCharacter()) {
				// update all info on character
				txtDescription.setText(model.getSelectedCharacterDescription());
				setTraits();
				lblCharacterName.setText(model.getSelectedCharacterName());
				if (model.isSelectedCharacterSuperCharacter()) {
					setPowers();
					txtPowerLevel.setText(model.getSuperCharacterPowerRanking());
				} else {
					txtPowerLevel.setText("Not applicable");
					txtPowers.setText("Not applicable");
				}

			}
		} else {
			// no selected character, just set all text to empty/default values!
			txtDescription.setText("");
			txtPowers.setText("");
			lblCharacterName.setText("");
			txtTraits.setText("");
			imvCharacterImage.setImage(new Image("..\\images\\default.png"));
		}
	}

	private void errorAlert(String message, String header) {
		generateAlert(message, header, AlertType.ERROR);
	}

	private void infoAlert(String message, String header) {
		generateAlert(message, header, AlertType.INFORMATION);
	}

	private void generateAlert(String message, String header, AlertType type) {
		Alert alert = new Alert(type);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void setTraits() {
		// I went for a messy approach here :(
		ArrayList<String> traits = model.getSelectedCharacterTraits();
		int size = traits.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(traits.get(i));
			if (i != size - 1)
				sb.append(newLine);
		}
		txtTraits.setText(sb.toString());
	}

	/**
	 * @requires the selected character in the model is a SuperCharacter
	 */
	private void setPowers() {
		ArrayList<String> powers = model.getSelectedSuperCharacterPowers();
		int size = powers.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(powers.get(i));
			if (i != size - 1)
				sb.append(newLine);
		}
		txtPowers.setText(sb.toString());
	}

	private ArrayList<String> getTraits() {
		// parse the trait text box by line and add each string to a list
		String rawTraitsText = txtTraits.getText();
		ArrayList<String> result = new ArrayList<String>();
		// we need to remove any possible empty or whitespace only strings!
		for (String rawTrait : rawTraitsText.split(newLine + "+")) {
			String trimmed = rawTrait.trim();
			if (!trimmed.isEmpty())
				result.add(trimmed);
		}
		return result;
	}

	private ArrayList<String> getPowers() {
		// same as getTraits
		String rawPowersText = txtPowers.getText();
		ArrayList<String> result = new ArrayList<String>();
		for (String rawPower : rawPowersText.split(newLine + "+")) {
			String trimmed = rawPower.trim();
			if (!trimmed.isEmpty())
				result.add(trimmed);
		}
		return result;
	}
	/*
	 * chooser.showOpenDialog(node.getScene().getWindow());
	 */

}
