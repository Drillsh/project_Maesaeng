package controller;

import java.io.IOException;
import java.net.*;
import java.util.*;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

public class MemberController implements Initializable {

	
	 @FXML private Button btnReco;
	 @FXML private Button btnEnd;
	 @FXML private Button btnAgainId;
	 @FXML private Button btnAgainPw;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btnReco.setOnAction(event -> handleBtnRecoAction(event));
		
		
			
	}
  //++++++++++++++++멤버 아이디 등록++++++++++++++++++++++++
  
  
  
	private void handleBtnRecoAction(ActionEvent event) {
		
	}
}