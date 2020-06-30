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
	public static User user;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// [Scene��ȯ] Login -> User or Manager
		// �α��� ��ư �ڵ鷯 ���
		btnLogin.setOnAction(event -> handleLoginAction(event));
		// ȸ������ Ŭ�� �̺�Ʈ ���
		lbMember.setOnMouseClicked(event -> handleMemberAction(event));
		// ���̵� ã�� �̺�Ʈ ���
		lbFindId.setOnMouseClicked(event -> handleFindIdAction(event));
		// ��й�ȣ ã�� �̺�Ʈ ���
		lbFindPw.setOnMouseClicked(event -> handleFindPwAction(event));
	}

	// ��й�ȣ ã�� ���콺 �̺�Ʈ ���
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

		// +++++++++++++++++++++���� ���� �������ֱ�++++++++++++++++++++
		TextField txfName = (TextField) root.lookup("#txfName");
		TextField txfId = (TextField) root.lookup("#txfId");
		TextField txfPhone = (TextField) root.lookup("#txfPhone");
		Button btnOkay = (Button) root.lookup("#btnOkay");
		Button btnCancel = (Button) root.lookup("#btnCancel");
		Label lbMessage = (Label) root.lookup("#lbMessage");

		// ��й�ȣ ã�� Ȯ�ι�ư �̺�Ʈ ���
		btnOkay.setOnAction(e -> handleBtnOkayAction1(txfName, txfId, txfPhone));
		btnCancel.setOnAction(e -> findPwStage.close());
	}

	// ��й�ȣ ã�� Ȯ�ι�ư �̺�Ʈ ���++++++++++++
	private void handleBtnOkayAction1(TextField txfName, TextField txfId, TextField txfPhone) {
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;
		String name = txfName.getText().trim();
		String id = txfId.getText().trim();
		String phone = txfPhone.getText().trim();

		// �ʵ忡 �ƹ����� �Էµ��� ������
		if (name.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("��й�ȣ ã��");
			alert.setHeaderText("�̸��� �Է��ϼ���");
			alert.showAndWait();
			return;
		}

		// ����Ʈ���� �̸��� �˻��� ����Ʈ�� �����Ѵ�
		arrayList = userDAO.getUserSearch(name);

		// �� ������ �ƴҰ��
		if (!arrayList.isEmpty()) {

			String foundPw = findPw(arrayList, id, phone);
			// foundPw�� null�� �ƴ� ��츦 ����Ѵ�
			if (foundPw != null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("��й�ȣ ã��");
				alert.setHeaderText("ȸ������ ��й�ȣ�� " + foundPw + "�Դϴ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("��й�ȣ ã��");
				alert.setHeaderText("��ġ�ϴ� ȸ�������� �����ϴ�");
				alert.showAndWait();
			} // end of if if
		} // end of if
	}

	// db�� ������ �Է°��� ���ϴ� �Լ�
	public String findPw(ArrayList<User> arrayList, String id, String phone) {
		for (User user : arrayList) {
			if (user.getPhone().equals(phone) && user.getUserid().equals(id)) {
				return user.getPassword();
			}
		} // end of for
		return null;
	}// end of findPw

	// ���̵� ã�� �̺�Ʈ ���//
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

		// +++++++++++++++++++++���� ���� �������ֱ�++++++++++++++++++++

		TextField txfName = (TextField) root.lookup("#txfName");
		TextField txfPhone = (TextField) root.lookup("#txfPhone");
		Button btnOkay = (Button) root.lookup("#btnOkay");
		Button btnCancel = (Button) root.lookup("#btnCancel");
		Label lbMessage = (Label) root.lookup("#lbMessage");

		// ã�� ��ư �̺�Ʈ ���
		btnOkay.setOnAction(e -> handleBtnOkayAction(txfName, txfPhone));
		btnCancel.setOnAction(e -> findIdStage.close());

	}

	// ������ ã�� Ȯ�ι�ư �̺�Ʈ ���==========/
	private void handleBtnOkayAction(TextField txfName, TextField txfPhone) {
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;
		String name = txfName.getText().trim();
		String phone = txfPhone.getText().trim();

		if (name.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� ã��");
			alert.setHeaderText("�̸��� �Է��ϼ���");
			alert.showAndWait();
			return;
		} // end of if

		arrayList = userDAO.getUserSearch(name);

		if (!arrayList.isEmpty()) {

			String foundId = findId(arrayList, phone);

			if (foundId != null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("���̵� ã��");
				alert.setHeaderText("ȸ������ ���̵�� " + foundId + " �Դϴ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("���̵� ã��");
				alert.setHeaderText("��ġ�ϴ� ������ ȸ���� �����ϴ�");
				alert.showAndWait();
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� ã��");
			alert.setHeaderText("������ Ȯ���ϼ���");
			alert.showAndWait();
		}
		// arrayList = userDAO.getUserSearch(phone);
	}

	// db�� ������ �Է°��� ���ϴ� �Լ�
	private String findId(ArrayList<User> arrayList, String phone) {

		for (User user : arrayList) {

			if (user.getPhone().equals(phone)) {

				return user.getUserid();
			}
		}
		return null;
	}

	// ȸ������ â and �����Է�
	private void handleMemberAction(MouseEvent event) {
		// ���̵� ã�� ���� �����ϱ�
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
		btnAgainId.setOnAction(e -> handleBtnAgainIdAction(txfId));

		// ��й�ȣ ��ġ üũ
		pxtCheckPw.textProperty()
				.addListener((observable, oldValue, newValue) -> checkPwCorrect(pxtPw, newValue, lbCheckPw, btnReco));

		// ȸ������ ��ư �̺�Ʈ
		btnReco.setOnAction(e -> handleBtnRecoAction(txfId, pxtPw, txfName, txfPhone, txfEmail, memberStage));

		btnEnd.setOnAction(e -> memberStage.close());
	}

	// ���̵� �ߺ� üũ in ȸ�� ����â
	private void handleBtnAgainIdAction(TextField txfId) {

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
			TextField txfEmail, Stage stage) {

		user = new User(txfId.getText(), pxtPw.getText(), txfName.getText(), txfPhone.getText(), txfEmail.getText());

		UserDAO userDAO = new UserDAO();

		int returnValue = userDAO.registerUser(user);

		if (returnValue != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� �Ϸ�");
			alert.setHeaderText(user.getName() + "�� ���� ����!!");
			alert.setContentText(user.getName() + "�� �ݰ����ϴ�~");
			alert.showAndWait();

			stage.close();
		} else {
			System.out.println("���� ����");
		}
	}

	// �α��� ��ư �ڵ鷯 ���
	private void handleLoginAction(ActionEvent event) {

		// �����ڸ�� ���õǾ��ִ��� üũ
		if (rdnManagerMode.isSelected()) {
			// ������ �α���
			if (checkAdminLogin()) {

				// �α��� ������ �Ŵ���â���� ��ȯ
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

		} else {// ���� �α���

			if (checkUserLogin()) {
				// �α��� ������ ����â���� ��ȯ
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
					alert.setTitle("���˿��");
					alert.setHeaderText("�α��� �����߻�! ");
					alert.setContentText("����: \n" + e.getMessage());
					alert.showAndWait();
				}
			} else {

				System.out.println("���� �α��� ����");
			}

		}

	}

	// ������ �α��� ���� üũ
	private boolean checkAdminLogin() {

		String id = txfId.getText().trim();
		String pw = pxtPw.getText().trim();

		// �Է� ������ ������
		if (id.equals("") || pw.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�α��� �Է� ����");
			alert.setHeaderText("�α��� ������ �Է����ּ���!!");
			alert.showAndWait();

			return false;
		}

		if (ADMIN_ID.equals(id)) { // ���̵� ��ġ

			if (ADMIN_PW.equals(pw)) { // ��й�ȣ ��ġ

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�α���");
				alert.setHeaderText("�α��� ����!!");
				alert.setContentText("�ݰ����ϴ� �����ڴ�");
				alert.showAndWait();

				return true;

			} else { // ���̵� ��ġ + ��� ����ġ
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�α���");
				alert.setHeaderText("�α��� ����!!");
				alert.setContentText("��й�ȣ�� Ʋ���ϴ�.");
				alert.showAndWait();

				return false;
			}
		} else { // ���̵� ����ġ

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�α���");
			alert.setHeaderText("�α��� ����!!");
			alert.setContentText("�������� �ʴ� ���̵��Դϴ�.");
			alert.showAndWait();

			return false;
		}
	}

	// ���� �α��� �Է����� üũ
	private boolean checkUserLogin() {
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;

		String id = txfId.getText().trim();
		String pw = pxtPw.getText().trim();

		// �Է� ������ ������
		if (id.equals("") || pw.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�α��� �Է� ����");
			alert.setHeaderText("�α��� ������ �Է����ּ���!!");
			alert.showAndWait();

			return false;
		}

		arrayList = userDAO.getIdSearch(id);

		// ���̵� �������� ������
		if (arrayList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�α���");
			alert.setHeaderText("�α��� ����!!");
			alert.setContentText("�������� �ʴ� ���̵��Դϴ�.");
			alert.showAndWait();

			return false;

		} else {
			// �α��� ���� ��� ��ġ
			if (pw.equals(arrayList.get(0).getPassword())) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�α���");
				alert.setHeaderText("�α��� ����!!");
				alert.setContentText("�ݰ����ϴ� " + id + "��");
				alert.showAndWait();

				user = arrayList.get(0);
				return true;
			} else {
				// ���̵� ��ġ + ��� ����ġ
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�α���");
				alert.setHeaderText("�α��� ����!!");
				alert.setContentText("��й�ȣ�� Ʋ���ϴ�.");
				alert.showAndWait();

				return false;
			}
		}
	}
}
