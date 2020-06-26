package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;

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

		btnAgainId.setOnAction(e -> handleBtnAgainIdAction(e, txfId));

	}

	private void handleBtnAgainIdAction(ActionEvent event, TextField txfId) {
		String id = null;
		UserDAO userDAO = new UserDAO();
		ArrayList<User> arrayList = null;
		User user;

		id = txfId.getText();
		if (id.equals(null)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� �ߺ�Ȯ�� ����");
			alert.setHeaderText("���̵� �Է��ϼ���");
			alert.showAndWait();
			
			return;
		}
	
		arrayList = userDAO.getIdSearch(id);
		user = arrayList.get(0); 
		if(user != null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� �ߺ�Ȯ��"); 
			alert.setHeaderText("�ߺ��� ���̵��Դϴ�.");
			alert.setContentText("�ٸ� ���̵� �Է����ּ���");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̵� �ߺ�Ȯ��");
			alert.setHeaderText("��� ������ ���̵��Դϴ�.");
			alert.showAndWait();
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