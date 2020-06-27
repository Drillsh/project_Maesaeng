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
	private Button btnLogin; // �α��� ��ư
	@FXML
	private RadioButton rdnManagerMode; // ������ ��ȯ ���� ��ư
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

		// [Scene��ȯ] Login -> User or Manager
		// �α��� ��ư �ڵ鷯 ���
		btnLogin.setOnAction(event -> handleLoginAction(event));
		// ȸ������ Ŭ�� �̺�Ʈ ���
		lbMember.setOnMouseClicked(event -> handleMemberAction(event));
	}

	// ȸ������ â and �����Է�
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

		// ++++++++++++++++++++++++++++++�� �ؽ�Ʈ�ʵ� ����+++++++++++++++++++++++++++++++++++
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

		// ���̵� �ߺ�üũ
		btnAgainId.setOnAction(e -> handleBtnAgainIdAction(e, txfId));

		// ��й�ȣ ��ġ üũ
		pxtCheckPw.textProperty()
				.addListener((observable, oldValue, newValue) -> checkPwCorrect(pxtPw, newValue, lbCheckPw, btnReco));

		// ȸ������ ��ư �̺�Ʈ
		btnReco.setOnAction(e -> handleBtnRecoAction(txfId, pxtPw, txfName, txfPhone, txfEmail));
	}

	// ���̵� �ߺ�üũ
	private void handleBtnAgainIdAction(ActionEvent event, TextField txfId) {

		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;

		String id = txfId.getText().trim();

		if (id.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� �ߺ�Ȯ�� ����");
			alert.setHeaderText("���̵� �Է��ϼ���");
			alert.showAndWait();

			return;
		}

		arrayList = userDAO.getIdSearch(id);

		if (arrayList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� �ߺ�Ȯ��");
			alert.setHeaderText("��� ������ ���̵��Դϴ�.");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� �ߺ�Ȯ��");
			alert.setHeaderText("�ߺ��� ���̵��Դϴ�.");
			alert.setContentText("�ٸ� ���̵� �Է����ּ���");
			alert.showAndWait();
		}

	}

	// ��й�ȣ ��ġ üũ
	private void checkPwCorrect(PasswordField pxtPw2, String pxtCheckPw, Label lbCheckPw, Button btnReco) {

		if (pxtPw2.getText().equals(pxtCheckPw)) {
			lbCheckPw.setText("�� ġ");
			btnReco.setDisable(false);
			
		} else {
			lbCheckPw.setText("����ġ");
		}
	}

	// ȸ������ ��ư �̺�Ʈ
	private void handleBtnRecoAction(TextField txfId, PasswordField pxtPw, TextField txfName, TextField txfPhone,
			TextField txfEmail) {

		newUser = new User(txfId.getText(), pxtPw.getText(), txfName.getText(), txfPhone.getText(), txfEmail.getText());

		UserDAO userDAO = new UserDAO();

		int returnValue = userDAO.userRegistry(newUser);

		if (returnValue != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� �Ϸ�");
			alert.setHeaderText(newUser.getName() + "�� ���� ����!!");
			alert.setContentText(newUser.getName() + "�� HELLO");
			alert.showAndWait();
		} else {
			System.out.println("���� ����");
		}

	}

	// �α��� ��ư �ڵ鷯 ���
	private void handleLoginAction(ActionEvent event) {

		// �α��� üũ
		// -> ��������, ���������� üũ
		// -> �ľ��� �ƴٸ� DB�� ���Ͽ� �α��ο��� ����

		// �����ڸ�� ���õǾ��ִ��� üũ
		if (rdnManagerMode.isSelected()) {
			// �α��� ������ �Ŵ���â���� ��ȯ
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
			// �α��� ������ ����â���� ��ȯ
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
				alert.setTitle("���˿��");
				alert.setHeaderText("�α��� �����߻�! ");
				alert.setContentText("����: \n" + e.getMessage());
				alert.showAndWait();
			}
		} // end of if
	}
}