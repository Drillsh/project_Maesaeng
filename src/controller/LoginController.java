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

		// ++++++++++++++++++++++++++++++�� �ؽ�Ʈ�ʵ� ����+++++++++++++++++++++++++++++++++++
		TextField mName = (TextField) root.lookup("#mName");
		TextField mId = (TextField) root.lookup("#mId");
		TextField mphone = (TextField) root.lookup("#mphone");
		TextField mEmail = (TextField) root.lookup("#joinName");
		PasswordField mPw = (PasswordField) root.lookup("#mPw");
		PasswordField mPw2 = (PasswordField) root.lookup("#mPw2");

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
