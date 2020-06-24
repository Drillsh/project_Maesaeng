package application;

import controller.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent root = loader.load();
		LoginController loginController = loader.getController();
		loginController.primaryStage = primaryStage;
		Scene scene = new Scene(root);
	
		//scene.getStylesheets().add(getClass().getResource("login.css").toString());	// css
		
		primaryStage.setTitle("LOGIN");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
