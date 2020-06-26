package controller;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import javafx.beans.Observable;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import model.*;

public class ManagerController implements Initializable {
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnNotice;
	@FXML
	private Button btnAlbum;
	@FXML
	private Button btnManagement;
	@FXML
	private Label lbId;
	@FXML
	private Label lbLogout;
	@FXML
	private TextArea txtMemo;
	@FXML
	private Label lbMemo;
	@FXML
	private Button btnDelete;
	@FXML
	private Label lbList;
//	@FXML
//	private TextField txtSearch;

	public Stage stage;
	public Stage managerStage;
	private int tableViewSelectedIndex;
	private ObservableList<User> obslist = FXCollections.observableArrayList();
	private ObservableList<Album> albumObsList = FXCollections.observableArrayList();
	private ObservableList<ImageView> albumList = FXCollections.observableArrayList();
	private Stage editstage;
	private Stage albumstage;
	private File selectFile = null;
	private String localUrl = "";
	private Image localImage;
	private ToggleGroup toggleGroup;
	private ImageView selectedImage = new ImageView();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// ���������ư �̺�Ʈ ���
		btnManagement.setOnAction(event -> handlebtnManagementAction(event));
		// ȸ������ ��ư �̺�Ʈ ���
		btnEdit.setOnAction(event -> handlebtnEditAction(event));
		// �α׾ƿ� ��Ŭ�� �̺�Ʈ ���
		lbLogout.setOnMouseClicked(event -> handlelbLogoutAction(event));
		// �ٹ����� ��ư�̺�Ʈ���
		btnAlbum.setOnAction(event -> handlebtnAlbumAction(event));
		// �������׹�ư �̺�Ʈ���
		btnNotice.setOnAction(e -> handlebtnNoticeAction(e));
	}

	// �������� �̺�Ʈ �ڵ鷯
	private void handlebtnNoticeAction(ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/notice.fxml"));
			Scene scene = new Scene(root);

			albumstage = new Stage(StageStyle.UTILITY);
			albumstage.initOwner(stage);
			albumstage.setScene(scene);
			albumstage.setResizable(false);
			albumstage.setTitle("��������");
			albumstage.show();
		} catch (IOException e1) {
		}
	}

	// �ٹ�������ư �̺�ư�ڵ鷯
	private void handlebtnAlbumAction(ActionEvent event) {

		getTotalAlbumList();

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/album.fxml"));
			Scene scene = new Scene(root);

			albumstage = new Stage(StageStyle.UTILITY);
			albumstage.initOwner(stage);
			albumstage.setScene(scene);
			albumstage.setResizable(false);
			albumstage.setTitle("�����ٹ�");
			albumstage.show();

			ImageView image1 = (ImageView) scene.lookup("#image1");
			ImageView image2 = (ImageView) scene.lookup("#image2");
			ImageView image3 = (ImageView) scene.lookup("#image3");
			ImageView image4 = (ImageView) scene.lookup("#image4");
			ImageView image5 = (ImageView) scene.lookup("#image5");
			ImageView image6 = (ImageView) scene.lookup("#image6");
			ImageView image7 = (ImageView) scene.lookup("#image7");
			ImageView image8 = (ImageView) scene.lookup("#image8");
			ImageView image9 = (ImageView) scene.lookup("#image9");
			Button btnChangePhoto = (Button) scene.lookup("#btnChangePhoto");
			Button btnremove = (Button) scene.lookup("#btnremove");

			albumList.add(image1);
			albumList.add(image2);
			albumList.add(image3);
			albumList.add(image4);
			albumList.add(image5);
			albumList.add(image6);
			albumList.add(image7);
			albumList.add(image8);
			albumList.add(image9);

			// DB ���� ������ �� �ܾ�ͼ� ���
			albumInitialize(albumList);

			// �������� ��ư�̺�Ʈ ���
			btnChangePhoto.setOnAction(e -> handlebtnChangePhotoAction(e));

			// Ŭ���� �ν��ϴ� �̹����� �̺�Ʈ�ڵ鷯
			image1.setOnMouseClicked(e -> handleImage1ClickAction(e));
			// �ٹ�����â ����������ư �̺�Ʈ���
			btnremove.setOnAction(e -> handlebtnremoveAction(e));
		} catch (IOException e) {
		}

	}

	// �ٹ������� ����������ư �̺�Ʈ��� �Լ�
	private void handlebtnremoveAction(ActionEvent e) {
		try {
			AlbumDAO albumDAO = new AlbumDAO();
			String query = "delete from albumtbl";
			Album album = albumObsList.get(tableViewSelectedIndex);
			int no = album.getAlbumNo();
			int returnValue = albumDAO.getAlbumDelete(no);
			if (returnValue != 0) {
				albumObsList.remove(tableViewSelectedIndex);
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̺�� ���� ����");
			alert.setHeaderText("���� ���˿��");
			alert.setContentText("���������ϼ���");
			alert.showAndWait();
		}
	}

	// �ٹ� ���� �̹������� �ڵ鷯
	private void handleImage1ClickAction(MouseEvent e) {
		selectedImage = (ImageView) e.getTarget();
	}

	// ���� �ʱ�ȭ
	private void albumInitialize(ObservableList<ImageView> albumList) {
		for (int i = 0; i < albumObsList.size(); i++) {
			localUrl = albumObsList.get(i).getPhoto();
			System.out.println(localUrl);
			if (albumObsList.get(i).getPhoto() == null) {
				localImage = new Image("file:///C:/images/default.jpg", false);
				albumList.get(i).setImage(localImage);
			} else {
				localImage = new Image(localUrl, false);
				albumList.get(i).setImage(localImage);
			}

		}

	}

	// DB���� �ڵ鷯
	public void getTotalAlbumList() {

		// �ٹ� ��ü ������

		AlbumDAO albumDAO = new AlbumDAO();

		ArrayList<Album> arrayList = albumDAO.AlbumTotal();

		if (arrayList == null) {
			return;
		}

		for (int i = 0; i < arrayList.size(); i++) {
			Album album = arrayList.get(i);
			albumObsList.add(album);
		}
	}

	// ������ư�ڵ鷯
	private void handlebtnChangePhotoAction(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		selectFile = fileChooser.showOpenDialog(albumstage);

		try {
			if (selectFile != null) {

				localUrl = selectFile.toURI().toURL().toString();
				Image image = new Image(localUrl, false);
				selectedImage.setImage(image);
			}
		} catch (MalformedURLException e) {

		}

	}

	// ���� DB�����ڵ鷯
	private void loadUserList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("ManagerController.loadUserList: DB ���� ����");
			} else {
				System.out.println("ManagerController.loadUserList : DB ���� ����");
			}
			String query = "select * from usertbl";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<User>();
			while (rs.next()) {
				User user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				arrayList.add(user);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("TotalList ���˿��");
			alert.setHeaderText("TotalList �������");
			alert.setContentText("��������:" + e.getMessage());
			alert.showAndWait();
		} finally {
			if (rs != null)
				try {
					rs.close();
					if (rs != null)
						rs.close();
					if (rs != null)
						rs.close();
				} catch (SQLException e) {

				}
		}
	}

	// �α׾ƿ� ��ư �̺�Ʈ �ڵ鷯
	private void handlelbLogoutAction(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
			Parent root = loader.load();
			LoginController loginController = loader.getController();
			loginController.primaryStage = primaryStage;
			Scene scene = new Scene(root);

			// scene.getStylesheets().add(getClass().getResource("../application/stu.css").toString());
			// //css

			primaryStage.setScene(scene);

			managerStage.close();
			primaryStage.show();
		} catch (IOException e1) {

		}
	}

	// ȸ�������̺�Ʈ �ڵ鷯�� �˾�â �۾�
	private void handlebtnEditAction(ActionEvent event) {

		Parent root = null;
		// loadUserList();
		try {
			root = FXMLLoader.load(getClass().getResource("/view/userList.fxml"));
			Scene scene = new Scene(root);

			TableView tlvList = (TableView) scene.lookup("#tlvList");

			TableColumn colUserId = new TableColumn("���� ���̵�");
			colUserId.setCellValueFactory(new PropertyValueFactory("userid"));

			TableColumn colPassword = new TableColumn("��й�ȣ");
			colPassword.setCellValueFactory(new PropertyValueFactory("password"));

			TableColumn colName = new TableColumn("����");
			colName.setCellValueFactory(new PropertyValueFactory("name"));

			TableColumn colPhone = new TableColumn("�ڵ��� ��ȣ");
			colPhone.setCellValueFactory(new PropertyValueFactory("phone"));

			TableColumn colMail = new TableColumn("�̸���");
			colMail.setCellValueFactory(new PropertyValueFactory("mail"));

			tlvList.getColumns().addAll(colUserId, colPassword, colName, colPhone, colMail);

			editstage = new Stage(StageStyle.UTILITY);
			editstage.initOwner(stage);
			editstage.setScene(scene);
			editstage.setResizable(false);
			editstage.setTitle("ȸ������Ʈ");
			editstage.show();

			UserDAO userDAO = new UserDAO();
			ArrayList<User> arraylist = new ArrayList<User>();
			arraylist = userDAO.loadUserList();

			for (int i = 0; i < arraylist.size(); i++) {
				User s = arraylist.get(i);
				obslist.add(s);
			}

			tlvList.setItems(obslist);

			Button btnSearch = (Button) scene.lookup("#btnSearch");
			Button btnSave = (Button) scene.lookup("#btnSave");
			Button btnAdd = (Button) scene.lookup("#btnAdd");
			Button btnDelete = (Button) scene.lookup("#btnDelete");
			TextField txtSearch = (TextField) scene.lookup("#txtSearch");

			// ������ư�̺�Ʈ���
			btnAdd.setOnAction(e -> handlebtnAddAction(e));
			// ������ư�̺�Ʈ���
			btnDelete.setOnAction(e -> handlebtnDeleteAction(e));
			// �˻���ư�̺�Ʈ���
			btnSearch.setOnAction(e -> handlebtnSearchAction(e, txtSearch));
			// �ٹ�����â�� �����������ϴ� �̺�Ʈ���
			tlvList.setOnMouseClicked(e -> tableViewSelectedIndex = tlvList.getSelectionModel().getSelectedIndex());

		} catch (Exception e) {

		}

	}

	// ȸ������Ʈ���� �����̸� �˻�������ϴ� �ڵ鷯
	private void handlebtnSearchAction(ActionEvent event, TextField txtSearch) {
		try {
//		txtSearch.getText().trim();
			UserDAO userDAO = new UserDAO();
			ArrayList<User> searchlist = userDAO.getUserSearch(txtSearch.getText().trim());
			if (txtSearch.getText().trim().equals("")) {
				throw new Exception();
			}
			if (searchlist.size() != 0) {
				obslist.clear();
				for (int i = 0; i < searchlist.size(); i++) {
					User u = searchlist.get(i);
					obslist.add(u);
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�̸��Է¿��");
			alert.setHeaderText("�̸��� �Է��ϼ���");
			alert.setContentText("�����ϼ���");
			alert.showAndWait();
		}
	}

	// ȸ������Ʈ ������ư �ڵ鷯
	private void handlebtnDeleteAction(ActionEvent event) {
		UserDAO userDAO = new UserDAO();
		String query = "delete from usertbl where userid=?";
		User user = obslist.get(tableViewSelectedIndex);
		String str = user.getUserid();
		int returnValue = userDAO.UserDelete(str);
		if (returnValue != 0) {
			obslist.remove(tableViewSelectedIndex);

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̺�� ���� ����");
			alert.setHeaderText("���� ���˿��");
			alert.setContentText("���������ϼ���");
			alert.showAndWait();
		}
	}

	// ������ ȸ�������� ���̺�����
	private void handlebtnAddAction(ActionEvent event) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/useredit.fxml"));
			Scene scene = new Scene(root);

			TextField txtUserId = (TextField) scene.lookup("#txtUserId");
			PasswordField txtPassWord = (PasswordField) scene.lookup("#txtPassWord");
			TextField txtName = (TextField) scene.lookup("#txtName");
			TextField txtMail = (TextField) scene.lookup("#txtMail");
			Button btnSave = (Button) scene.lookup("#btnSave");
			Button btnExit = (Button) scene.lookup("#btnExit");

			User user = obslist.get(tableViewSelectedIndex);

			txtUserId.setText(String.valueOf(user.getUserid()));
			txtName.setText(user.getName());
			txtPassWord.setText(user.getPassword());
			txtMail.setText(user.getMail());
			txtUserId.setDisable(true);

			btnSave.setOnAction(e -> {

				// users.setUserid(String.valueOf(user.getUserid()));
				user.setPassword(txtPassWord.getText());
				user.setName(txtName.getText());
				user.setMail(txtMail.getText());

				int returnValue = 0;
				UserDAO userDAO = new UserDAO();
				returnValue = userDAO.getUpdate(user);
				if (returnValue != 0) {
					obslist.set(tableViewSelectedIndex, user);
				} else {
					System.out.println("�������");
				}
			});
			btnExit.setOnAction(e -> {
				editstage.close();
			});
			editstage = new Stage(StageStyle.UTILITY);
			editstage.initModality(Modality.WINDOW_MODAL);
			editstage.initOwner(stage);
			editstage.setScene(scene);
			editstage.setResizable(false);
			editstage.setTitle("ȸ�� ���α׷� ����");
			editstage.show();

		} catch (IOException e) {

		}

	}

	// ��������̺�Ʈ �ڵ鷯 �� �˾�â �۾�
	private void handlebtnManagementAction(ActionEvent event) {

	}
}
