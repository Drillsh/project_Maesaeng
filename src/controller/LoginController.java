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

	private final String ADMIN_ID = "admin";
	private final String ADMIN_PW = "123456";

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
	public static User user;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// [Scene전환] Login -> User or Manager
		// 로그인 버튼 핸들러 등록
		btnLogin.setOnAction(event -> handleLoginAction(event));
		// 회원가입 클릭 이벤트 등록
		lbMember.setOnMouseClicked(event -> handleMemberAction(event));
		// 아이디 찾기 이벤트 등록
		lbFindId.setOnMouseClicked(event -> handleFindIdAction(event));
		// 비밀번호 찾기 이벤트 등록
		lbFindPw.setOnMouseClicked(event -> handleFindPwAction(event));
	}

	// 비밀번호 찾기 마우스 이벤트 등록
	private void handleFindPwAction(MouseEvent event) {
		Stage findPwStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/findpw.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		findPwStage.setScene(scene);
		findPwStage.show();

		// +++++++++++++++++++++각각 공간 정의해주기++++++++++++++++++++
		TextField txfName = (TextField) root.lookup("#txfName");
		TextField txfId = (TextField) root.lookup("#txfId");
		TextField txfPhone = (TextField) root.lookup("#txfPhone");
		Button btnOkay = (Button) root.lookup("#btnOkay");
		Button btnCancel = (Button) root.lookup("#btnCancel");
		Label lbMessage = (Label) root.lookup("#lbMessage");

		// 비밀번호 찾기 확인버튼 이벤트 등록
		btnOkay.setOnAction(e -> handleBtnOkayAction1(txfName, txfId, txfPhone));
		btnCancel.setOnAction(e -> findPwStage.close());
	}

	// 비밀번호 찾기 확인버튼 이벤트 등록++++++++++++
	private void handleBtnOkayAction1(TextField txfName, TextField txfId, TextField txfPhone) {
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;
		String name = txfName.getText().trim();
		String id = txfId.getText().trim();
		String phone = txfPhone.getText().trim();

		// 필드에 아무값도 입력되지 않을때
		if (name.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("비밀번호 찾기");
			alert.setHeaderText("이름을 입력하세요");
			alert.showAndWait();
			return;
		}

		// 리스트에서 이름을 검색해 리스트에 저장한다
		arrayList = userDAO.getUserSearch(name);

		// 빈 공간이 아닐경우
		if (!arrayList.isEmpty()) {

			String foundPw = findPw(arrayList, id, phone);
			// foundPw가 null이 아닐 경우를 출력한다
			if (foundPw != null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("비밀번호 찾기");
				alert.setHeaderText("회원님의 비밀번호는 " + foundPw + "입니다");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("비밀번호 찾기");
				alert.setHeaderText("일치하는 회원정보가 없습니다");
				alert.showAndWait();
			} // end of if if
		} // end of if
	}

	// db의 정보와 입력값을 비교하는 함수
	public String findPw(ArrayList<User> arrayList, String id, String phone) {
		for (User user : arrayList) {
			if (user.getPhone().equals(phone) && user.getUserid().equals(id)) {
				return user.getPassword();
			}
		} // end of for
		return null;
	}// end of findPw

	// 아이디 찾기 이벤트 등록//
	private void handleFindIdAction(MouseEvent event) {
		Stage findIdStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/findid.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		findIdStage.setScene(scene);
		findIdStage.show();

		// +++++++++++++++++++++각각 공간 정의해주기++++++++++++++++++++

		TextField txfName = (TextField) root.lookup("#txfName");
		TextField txfPhone = (TextField) root.lookup("#txfPhone");
		Button btnOkay = (Button) root.lookup("#btnOkay");
		Button btnCancel = (Button) root.lookup("#btnCancel");
		Label lbMessage = (Label) root.lookup("#lbMessage");

		// 찾기 버튼 이벤트 등록
		btnOkay.setOnAction(e -> handleBtnOkayAction(txfName, txfPhone));
		btnCancel.setOnAction(e -> findIdStage.close());

	}

	// 아이이 찾기 확인버튼 이벤트 등록==========/
	private void handleBtnOkayAction(TextField txfName, TextField txfPhone) {
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;
		String name = txfName.getText().trim();
		String phone = txfPhone.getText().trim();

		if (name.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("아이디 찾기");
			alert.setHeaderText("이름을 입력하세요");
			alert.showAndWait();
			return;
		} // end of if

		arrayList = userDAO.getUserSearch(name);

		if (!arrayList.isEmpty()) {

			String foundId = findId(arrayList, phone);

			if (foundId != null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 찾기");
				alert.setHeaderText("회원님의 아이디는 " + foundId + " 입니다");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 찾기");
				alert.setHeaderText("일치하는 정보의 회원이 없습니다");
				alert.showAndWait();
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("아이디 찾기");
			alert.setHeaderText("정보를 확인하세요");
			alert.showAndWait();
		}
		// arrayList = userDAO.getUserSearch(phone);
	}

	// db의 정보와 입력값을 비교하는 함수
	private String findId(ArrayList<User> arrayList, String phone) {

		for (User user : arrayList) {

			if (user.getPhone().equals(phone)) {

				return user.getUserid();
			}
		}
		return null;
	}

	// 회원가입 창 and 정보입력
	private void handleMemberAction(MouseEvent event) {
		// 아이디 찾기 폴더 연동하기
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
		btnAgainId.setOnAction(e -> handleBtnAgainIdAction(txfId));

		// 비밀번호 일치 체크
		pxtCheckPw.textProperty()
				.addListener((observable, oldValue, newValue) -> checkPwCorrect(pxtPw, newValue, lbCheckPw, btnReco));

		// 회원가입 버튼 이벤트
		btnReco.setOnAction(e -> handleBtnRecoAction(txfId, pxtPw, txfName, txfPhone, txfEmail, memberStage));

		btnEnd.setOnAction(e -> memberStage.close());
	}

	// 아이디 중복 체크 in 회원 가입창
	private void handleBtnAgainIdAction(TextField txfId) {

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
			TextField txfEmail, Stage stage) {

		user = new User(txfId.getText(), pxtPw.getText(), txfName.getText(), txfPhone.getText(), txfEmail.getText());

		UserDAO userDAO = new UserDAO();

		int returnValue = userDAO.registerUser(user);

		if (returnValue != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("가입 완료");
			alert.setHeaderText(user.getName() + "님 가입 성공!!");
			alert.setContentText(user.getName() + "님 반갑습니다~");
			alert.showAndWait();

			stage.close();
		} else {
			System.out.println("가입 에러");
		}
	}

	// 로그인 버튼 핸들러 등록
	private void handleLoginAction(ActionEvent event) {

		// 관리자모드 선택되어있는지 체크
		if (rdnManagerMode.isSelected()) {
			// 관리자 로그인
			if (checkAdminLogin()) {

				// 로그인 성공시 매니저창으로 전환
				try {
					Stage managerStage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/manager.fxml"));
					Parent root = loader.load();
					ManagerController managerController = loader.getController();
					managerController.managerStage = managerStage;
					Scene scene = new Scene(root);

					scene.getStylesheets().add(getClass().getResource("/application/manager.css").toString());

					managerStage.setScene(scene);

					primaryStage.close();
					managerStage.show();

				} catch (Exception e) {
				}

			} // end of checkAdminLogin()

		} else {// 유저 로그인

			if (checkUserLogin()) {
				// 로그인 성공시 유저창으로 전환
				try {

					Stage userStage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
					Parent root = loader.load();

					DBUtil.userCon = loader.getController();
					DBUtil.userCon.userStage = userStage;
					Scene scene = new Scene(root);

					scene.getStylesheets().add(getClass().getResource("/application/user.css").toString());

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
			} else {

				System.out.println("유저 로그인 실패");
			}

		}

	}

	// 관리자 로그인 정보 체크
	private boolean checkAdminLogin() {

		String id = txfId.getText().trim();
		String pw = pxtPw.getText().trim();

		// 입력 정보가 없을시
		if (id.equals("") || pw.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("로그인 입력 에러");
			alert.setHeaderText("로그인 정보를 입력해주세요!!");
			alert.showAndWait();

			return false;
		}

		if (ADMIN_ID.equals(id)) { // 아이디 일치

			if (ADMIN_PW.equals(pw)) { // 비밀번호 일치

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("로그인");
				alert.setHeaderText("로그인 성공!!");
				alert.setContentText("반갑습니다 관리자님");
				alert.showAndWait();

				return true;

			} else { // 아이디 일치 + 비번 불일치
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("로그인");
				alert.setHeaderText("로그인 실패!!");
				alert.setContentText("비밀번호가 틀립니다.");
				alert.showAndWait();

				return false;
			}
		} else { // 아이디 불일치

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("로그인");
			alert.setHeaderText("로그인 실패!!");
			alert.setContentText("존재하지 않는 아이디입니다.");
			alert.showAndWait();

			return false;
		}
	}

	// 유저 로그인 입력정보 체크
	private boolean checkUserLogin() {
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;

		String id = txfId.getText().trim();
		String pw = pxtPw.getText().trim();

		// 입력 정보가 없을시
		if (id.equals("") || pw.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("로그인 입력 에러");
			alert.setHeaderText("로그인 정보를 입력해주세요!!");
			alert.showAndWait();

			return false;
		}

		arrayList = userDAO.getIdSearch(id);

		// 아이디가 존재하지 않을시
		if (arrayList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("로그인");
			alert.setHeaderText("로그인 실패!!");
			alert.setContentText("존재하지 않는 아이디입니다.");
			alert.showAndWait();

			return false;

		} else {
			// 로그인 정보 모두 일치
			if (pw.equals(arrayList.get(0).getPassword())) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("로그인");
				alert.setHeaderText("로그인 성공!!");
				alert.setContentText("반갑습니다 " + id + "님");
				alert.showAndWait();

				user = arrayList.get(0);
				return true;
			} else {
				// 아이디 일치 + 비번 불일치
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("로그인");
				alert.setHeaderText("로그인 실패!!");
				alert.setContentText("비밀번호가 틀립니다.");
				alert.showAndWait();

				return false;
			}
		}
	}
}
