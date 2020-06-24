package controller;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.input.*;
import javafx.stage.*;

public class LoginController implements Initializable {

	@FXML
	private Button btnLogin; // 로그인 버튼
	@FXML
	private RadioButton rdnManagerMode; // 관리자 전환 라디오 버튼
	@FXML
	private TextField txfId;
	@FXML
	private PasswordField pxtPw;
	@FXML
	private Label lbMember;
	@FXML
	private Label lbFindPw;
	@FXML
	private Label lbFindId;

	public Stage primaryStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// [Scene전환] Login -> User or Manager
		// 로그인 버튼 핸들러 등록
		btnLogin.setOnAction(event -> handleLoginAction(event));
		// 회원가입 클릭 이벤트 등록
		lbMember.setOnMouseClicked(event -> handleMemberAction(event));
	}

	// 회원가입 창 and 정보입력
	private void handleMemberAction(MouseEvent event) {
		Stage memberStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/member.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		memberStage.setScene(scene);
		primaryStage.close();
		memberStage.show();

		// ++++++++++++++++++++++++++++++각 텍스트필드 정의+++++++++++++++++++++++++++++++++++
		TextField mName = (TextField) root.lookup("#mName");
		TextField mId = (TextField) root.lookup("#mId");
		TextField mphone = (TextField) root.lookup("#mphone");
		TextField mEmail = (TextField) root.lookup("#joinName");
		PasswordField mPw = (PasswordField) root.lookup("#mPw");
		PasswordField mPw2 = (PasswordField) root.lookup("#mPw2");

	}

	// 로그인 버튼 핸들러 등록
	private void handleLoginAction(ActionEvent event) {

		// 로그인 체크
		// -> 유저인지, 관리자인지 체크
		// -> 파악이 됐다면 DB와 비교하여 로그인여부 결정

		// 관리자모드 선택되어있는지 체크
		if (rdnManagerMode.isSelected()) {
			// 로그인 성공시 매니저창으로 전환
			try {
				Stage managerStage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/manager.fxml"));
				Parent root = loader.load();
				ManagerController managerController = loader.getController();
				managerController.managerStage = managerStage;
				Scene scene = new Scene(root);

				// scene.getStylesheets().add(getClass().getResource("../application/stu.css").toString());
				// //css

				managerStage.setScene(scene);

				primaryStage.close();
				managerStage.show();

			} catch (Exception e) {
			}
		} else {
			// 로그인 성공시 유저창으로 전환
			try {

				Stage userStage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
				Parent root = loader.load();

				DBUtil.userCon = loader.getController();
				DBUtil.userCon.userStage = userStage;
				Scene scene = new Scene(root);

				// scene.getStylesheets().add(getClass().getResource("../application/stu.css").toString());
				// //css

				userStage.setScene(scene);

				primaryStage.close();
				userStage.show();

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점검요망");
				alert.setHeaderText("로그인 문제발생! ");
				alert.setContentText("원인: \n" + e.getMessage());
				alert.showAndWait();
			}
		} // end of if

	}

}
