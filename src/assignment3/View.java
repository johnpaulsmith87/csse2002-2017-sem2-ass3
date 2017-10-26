package assignment3;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		try {
			Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
			Scene scene = new Scene(root);

			// format main window (Stage)
			stage.setTitle("Character Editor");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			System.err.println("Error starting GUI.");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
