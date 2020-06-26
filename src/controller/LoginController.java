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
import model.*;

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
	public static User newUser;

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signUp.fxml"));
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
		TextField txfName = (TextField) root.lookup("#txfName");
		TextField txfId = (TextField) root.lookup("#txfId");
		TextField txfPhone = (TextField) root.lookup("#txfPhone");
		TextField txfEmail = (TextField) root.lookup("#txfEmail");
		PasswordField pxtPw = (PasswordField) root.lookup("#pxtPw");
		PasswordField pxtCheckPw = (PasswordField) root.lookup("#pxtCheckPw");
		Button btnAgainId = (Button) root.lookup("#btnAgainId");
		Button btnAgainPw = (Button) root.lookup("#btnAgainPw");
		Button btnReco = (Button) root.lookup("#btnReco");
		Button btnEnd = (Button) root.lookup("#btnEnd");
		Label lbCheckPw = (Label) root.lookup("#lbCheckPw");

		// 아이디 중복체크
		btnAgainId.setOnAction(e -> handleBtnAgainIdAction(e, txfId));

		// 비밀번호 일치 체크
		pxtCheckPw.textProperty()
				.addListener((observable, oldValue, newValue) -> checkPwCorrect(pxtPw, newValue, lbCheckPw, btnReco));

		// 회원가입 버튼 이벤트
		btnReco.setOnAction(e -> handleBtnRecoAction(txfId, pxtPw, txfName, txfPhone, txfEmail));
	}

	// 아이디 중복체크
	private void handleBtnAgainIdAction(ActionEvent event, TextField txfId) {

		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;

		String id = txfId.getText().trim();

		if (id.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("아이디 중복확인 에러");
			alert.setHeaderText("이이디를 입력하세요");
			alert.showAndWait();

			return;
		}

		arrayList = userDAO.getIdSearch(id);

		if (arrayList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("아이디 중복확인");
			alert.setHeaderText("사용 가능한 아이디입니다.");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("아이디 중복확인");
			alert.setHeaderText("중복된 아이디입니다.");
			alert.setContentText("다른 아이디를 입력해주세요");
			alert.showAndWait();
		}

	}

	// 비밀번호 일치 체크
	private void checkPwCorrect(PasswordField pxtPw2, String pxtCheckPw, Label lbCheckPw, Button btnReco) {

		if (pxtPw2.getText().equals(pxtCheckPw)) {
			lbCheckPw.setText("일 치");
			btnReco.setDisable(false);
			
		} else {
			lbCheckPw.setText("불일치");
		}
	}

	// 회원가입 버튼 이벤트
	private void handleBtnRecoAction(TextField txfId, PasswordField pxtPw, TextField txfName, TextField txfPhone,
			TextField txfEmail) {

		newUser = new User(txfId.getText(), pxtPw.getText(), txfName.getText(), txfPhone.getText(), txfEmail.getText());

		UserDAO userDAO = new UserDAO();

		int returnValue = userDAO.userRegistry(newUser);

		if (returnValue != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("가입 완료");
			alert.setHeaderText(newUser.getName() + "님 가입 성공!!");
			alert.setContentText(newUser.getName() + "님 HELLO");
			alert.showAndWait();
		} else {
			System.out.println("가입 에러");
		}

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