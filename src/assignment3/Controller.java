package assignment3;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private TextField txtCreateDatabase;
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
	//dialogue for messages
	@FXML
	private DialogPane diaAlert;

	public Controller() {
		/*
		 * TODO: DELETE THIS COMMENT AND INITIALISE YOUR CONTROLLER HERE
		 */
		
		this.model = new Model();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleClearSearchAction();
		handleSearchCharacterAction();
		
	}

	private void handleClearSearchAction() {
		btnClearSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//add code here!
			}
		});
	}
	private void handleSearchCharacterAction() {
		btnSearchCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//add code here!
			}
		});
	}
	/*
	 * chooser.showOpenDialog(node.getScene().getWindow());
	 */

}
