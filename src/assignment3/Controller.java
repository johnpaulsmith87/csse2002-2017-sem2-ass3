package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Controller class for the Character Editor.
 * 
 * @author John-Paul Smith
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
	//constant
	private static final String newLine = System.getProperty("line.separator");
	private String defaultImagePath() {
		File f = new File(
				System.getProperty("user.dir") + "/src/images/default.png");
		return f.getAbsolutePath();
	}

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
		handleChangeImageAction();
		handleSaveCharacterAction();
		handleSelectListViewCharacterAction();
	}

	/*
	 * Event handlers - No javadoc for private methods
	 */
	private void handleSelectListViewCharacterAction() {
		lsvViewDatabase.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				model.selectCharacter(
						lsvViewDatabase.getSelectionModel().getSelectedItem());
				updateAll();
			}
		});
	}

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
					model.clearSelectedCharacter();
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
					errorAlert("Error attempting to save database", "Error");
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
				if (model.hasActiveDatabase() && name != null && !name.isEmpty()
						&& model.createCharacter(name, defaultImagePath())) {
					updateAll();
				} else
					infoAlert(
							"Either you didn't enter a name or there wasn't an open database",
							"Not valid");
			}
		});
	}

	private void handleCreateSuperCharacterAction() {
		btnCreateSuperCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = txtCreateCharacterName.getText();
				try {
					if (model.hasActiveDatabase() && name != null
							&& !name.isEmpty() && model.createSuperCharacter(
									name, defaultImagePath()))
						updateAll();
					else
						infoAlert(
								"Either you didn't enter a name or there wasn't an open database",
								"Not valid");
				} catch (IllegalPowerRankingException e) {
					errorAlert(e.getMessage(), "Illegal Power Ranking");
				}
			}
		});
	}

	private void handleSaveCharacterAction() {
		btnSaveCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (model.hasSelectedCharacter()) {
					try {
						model.saveCharacter(lblCharacterName.getText(),
								txtDescription.getText(), getTraits(),
								getPowers(), txtPowerLevel.getText());
						model.selectCharacter(lblCharacterName.getText());
						updateAll();
					} catch (IllegalPowerRankingException e) {
						errorAlert(e.getMessage(), "Illegal Power Ranking!");
					}
				} else
					infoAlert(
							"You must have a selected character to save a character",
							"No selected character");
			}
		});
	}

	private void handleSearchCharacterAction() {
		btnSearchCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (model.hasActiveDatabase()
						&& !model.searchSuccess(txtSearchCharacter.getText()))
					infoAlert(
							"No character found matching: "
									+ txtSearchCharacter.getText(),
							"No Match!");
				updateAll();
			}
		});
	}

	private void handleDeleteCharacterAction() {
		btnDeleteCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (model.hasActiveDatabase() && model.hasSelectedCharacter()) {
					model.deleteCharacter();
					updateAll();
				} else
					infoAlert("You must have a character selected to delete",
							"No character selected");
			}
		});
	}

	private void handleChangeImageAction() {
		btnChangeImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (model.hasSelectedCharacter()) {
					ExtensionFilter ef = new ExtensionFilter("Image files",
							"*.png", "*.jpg", "*.jpeg", "*.gif");
					FileChooser fc = new FileChooser();
					fc.setTitle("Choose an image for the selected character");
					File initial = new File(
							System.getProperty("user.dir") + "/src/images/");
					fc.setInitialDirectory(initial);
					fc.getExtensionFilters().add(ef);
					File file = fc.showOpenDialog(
							btnChangeImage.getScene().getWindow());
					model.setImagePath(file.getAbsolutePath());
					updateAll();
				} else
					infoAlert(
							"You must have a character selected to add image file",
							"No character selected");
			}
		});
	}

	// Helper methods

	/**
	 * This method essentially acts as a "refresh". It sets all values in the UI
	 * to be up to date with what's in the model
	 */
	private void updateAll() {
		// updates all database and character info in the UI
		if (model.hasSelectedCharacter()) {
			lsvViewDatabase.setItems(model.getAvailableCharacters());
			if (model.hasSelectedCharacter()) {
				// update all info on character
				txtDescription.setText(model.getSelectedCharacterDescription());
				setTraits();
				lblCharacterName.setText(model.getSelectedCharacterName());
				File f = new File(model.getImagePath());
				imvCharacterImage.setImage(new Image(f.toURI().toString()));
				if (model.isSelectedCharacterSuperCharacter()) {
					setPowers();
					txtPowerLevel
							.setText(model.getSuperCharacterPowerRanking());
				} else {
					txtPowerLevel.setText("Not applicable");
					txtPowers.setText("Not applicable");
				}
			}
		} else {
			// no selected character, just set all text to empty/default values!
			lsvViewDatabase.setItems(model.getAvailableCharacters());
			txtDescription.setText("");
			txtPowers.setText("");
			lblCharacterName.setText("");
			txtTraits.setText("");
			File file = new File(
					System.getProperty("user.dir") + "/src/images/default.png");
			imvCharacterImage.setImage(new Image(file.toURI().toString()));
		}
	}

	// generates error alert
	private void errorAlert(String message, String header) {
		generateAlert(message, header, AlertType.ERROR);
	}

	// generates info alert
	private void infoAlert(String message, String header) {
		generateAlert(message, header, AlertType.INFORMATION);
	}

	/**
	 * 
	 * @param message
	 *            Message to display
	 * @param header
	 *            Header on alert
	 * @param type
	 *            Type of alert(AlertType enum)
	 */
	private void generateAlert(String message, String header, AlertType type) {
		Alert alert = new Alert(type);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Helper method to set the traits in the UI
	 */
	private void setTraits() {
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
	 * Helper method to set the powers in the UI
	 */
	private void setPowers() {
		ArrayList<String> powers = model.getSelectedSuperCharacterPowers();
		int size = powers.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(powers.get(i));
			if (i != size - 1)
				sb.append("\n");
		}
		txtPowers.setText(sb.toString());
	}
	
	private ArrayList<String> getTraits() {
		// parse the trait text box by line and add each string to a list
		String rawTraitsText = txtTraits.getText();
		ArrayList<String> result = new ArrayList<String>();
		// we need to remove any possible empty or whitespace only strings!
		for (String rawTrait : rawTraitsText.split("\n" + "+")) {
			String trimmed = rawTrait.trim();
			// just !emtpy didn't work
			if (!trimmed.isEmpty() || trimmed.length() > 0)
				result.add(trimmed);
		}
		return result;
	}

	private ArrayList<String> getPowers() {
		// same as getTraits
		String rawPowersText = txtPowers.getText();
		ArrayList<String> result = new ArrayList<String>();
		for (String rawPower : rawPowersText.split("\n" + "+")) {
			String trimmed = rawPower.trim();
			if (!trimmed.isEmpty() || trimmed.length() > 0)
				result.add(trimmed);
		}
		return result;
	}
}
