package controller;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.*;
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
	private Label lbList;
	@FXML
	private BarChart barChart;

	public Stage stage;
	public Stage managerStage;
	private int tableViewSelectedIndex;
	private Stage reservationstage;
	private ObservableList<User> obslist = FXCollections.observableArrayList();
	private ObservableList<Album> albumObsList = FXCollections.observableArrayList();
	private ObservableList<JoinRevList> obsjrlList = FXCollections.observableArrayList();
	private ObservableList<Notice> noticeObsList = FXCollections.observableArrayList();
	private ArrayList<Notice> noticeArrayList = new ArrayList<>();
	private ArrayList<JoinRevList> joinrevArrayList = new ArrayList<>();
	private Stage editstage;
	private Stage albumstage;
	private Stage noticestage;
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

		XYChart.Series sales = new XYChart.Series();
		sales.setName("Today");
		ObservableList sales1 = FXCollections.observableArrayList(new XYChart.Data("6-25", 4),
				new XYChart.Data("6-26", 1), new XYChart.Data("6-27", 5));

		sales.setData(sales1);
		barChart.getData().add(sales);
		// mainXYchart.getData().add(hp);

		XYChart.Series sales2 = new XYChart.Series();
		sales2.setName("Month");
		sales2.setData(FXCollections.observableArrayList(new XYChart.Data("6��", 107)

		));
	}

	// �������� �̺�Ʈ �ڵ鷯
	private void handlebtnNoticeAction(ActionEvent e) {
		Parent root = null;
		albumstage = new Stage(StageStyle.UTILITY);

		try {
			root = FXMLLoader.load(getClass().getResource("/view/notice.fxml"));
			Scene scene = new Scene(root);

			TableView tlvNotice = (TableView) scene.lookup("#tlvNotice");

			TableColumn colNo = new TableColumn("��ȣ");
			colNo.setCellValueFactory(new PropertyValueFactory("noticeNo"));

			TableColumn colTitle = new TableColumn("����");
			colTitle.setCellValueFactory(new PropertyValueFactory("title"));

			TableColumn colContents = new TableColumn("����");
			colContents.setCellValueFactory(new PropertyValueFactory("contents"));

			TableColumn colDate = new TableColumn("��� ��¥");
			colDate.setCellValueFactory(new PropertyValueFactory("noticeDate"));

			tlvNotice.getColumns().addAll(colNo, colTitle, colContents, colDate);

			Button btnNoticeAdd = (Button) scene.lookup("#btnNoticeAdd");
			Button btnNoticeEdit = (Button) scene.lookup("#btnNoticeEdit");
			Button btnNoticeDelete = (Button) scene.lookup("#btnNoticeDelete");

			// �������׿� �߰���ư�̺�Ʈ��� �߰���ư�������� ������������� ������ �Է�
			btnNoticeAdd.setOnAction(event -> handlebtnNoticeAddAction(event));

			// ���̺�信 ����� ���̺��� Ŭ���ؼ� ���þ׼��ϴ� �̺�Ʈ
			tlvNotice.setOnMouseClicked(
					evnet -> tableViewSelectedIndex = tlvNotice.getSelectionModel().getSelectedIndex());
			// ���̺�信 �� ���õ� �������� �����̺�Ʈ���
			btnNoticeDelete.setOnAction(event -> hanclebtnNoticeDeleteAction(event));
			// ���̺�信 ����� �������� ������ư �̺�Ʈ���
			btnNoticeEdit.setOnAction(event -> handlebtnNoticeEditAction(event));

			NoticeDAO noticeDAO = new NoticeDAO();
			noticeArrayList = noticeDAO.getNoticeLoadTotalList();

			for (int i = 0; i < noticeArrayList.size(); i++) {
				Notice n = noticeArrayList.get(i);
				noticeObsList.add(n);
			}

			tlvNotice.setItems(noticeObsList);

			albumstage.initOwner(stage);
			albumstage.setScene(scene);
			albumstage.setResizable(false);
			albumstage.setTitle("��������");
			albumstage.show();

		} catch (IOException e1) {

		}
	}

	// ���̺�信 ����� ���������� �����̺�Ʈ
	private void handlebtnNoticeEditAction(ActionEvent event) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/NoticeEdit.fxml"));
			Scene scene = new Scene(root);

			TextField txtEditTitle = (TextField) scene.lookup("#txtEditTitle");
			TextArea txaEditContents = (TextArea) scene.lookup("#txaEditContents");
			Button btnNoticeSave = (Button) scene.lookup("#btnNoticeSave");

			Notice n = noticeObsList.get(tableViewSelectedIndex);

			txtEditTitle.setText(n.getTitle());
			txaEditContents.setText(n.getContents());

			// �������� ����â �����ư�̺�Ʈ����
			btnNoticeSave.setOnAction(e -> {
				Notice notice = noticeObsList.get(tableViewSelectedIndex);

				notice.setTitle(txtEditTitle.getText());
				notice.setContents(txtEditTitle.getText());

				int returnValue = 0;

				NoticeDAO noticeDAO = new NoticeDAO();

				returnValue = noticeDAO.getNoticeUpdate(notice);

				if (returnValue != 0) {
					noticeObsList.set(tableViewSelectedIndex, notice);
				} else {
					System.out.println("�������׼��� �������");
				}
				noticestage.close();
				noticeObsList.clear();
				getNoticeLoadTotalList();
			});

			NoticeDAO noticeDAO = new NoticeDAO();
			ArrayList<Notice> arraylist = new ArrayList<Notice>();

			for (int i = 0; i < arraylist.size(); i++) {
				Notice notice = arraylist.get(i);
				noticeObsList.add(notice);
			}

			noticestage = new Stage(StageStyle.UTILITY);
			noticestage.initOwner(stage);
			noticestage.setScene(scene);
			noticestage.setResizable(false);
			noticestage.setTitle("��������");
			noticestage.show();
		} catch (IOException e) {

		}

	}

	// ���̺�信 ����� �����͸� �����ϴ� ��ư ��� ����
	private void hanclebtnNoticeDeleteAction(ActionEvent e) {
		NoticeDAO noticeDAO = new NoticeDAO();
		Notice notice = noticeObsList.get(tableViewSelectedIndex);
		int no = notice.getNoticeNo();
		int returnVlue = noticeDAO.getNoticeDelete(notice);
		if (returnVlue != 0) {
			noticeObsList.clear();
			getNoticeLoadTotalList();
		} else {
			System.out.println("���� ����");
		}
	}

	// �����ͺ��̽��� �ִ� ������ �ٰ�������
	private void getNoticeLoadTotalList() {
		NoticeDAO noticeDAO = new NoticeDAO();
		ArrayList<Notice> noticeList = noticeDAO.getNoticeLoadTotalList();
		if (noticeList == null) {
			return;
		}
		for (int i = 0; i < noticeList.size(); i++) {
			Notice n = noticeList.get(i);
			noticeObsList.add(n);
		}
	}

	// ���������� ����� �������Է��ϴ� â
	private void handlebtnNoticeAddAction(ActionEvent event) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/noticecontents.fxml"));
			Scene scene = new Scene(root);

			TextField txtTitle = (TextField) scene.lookup("#txtTitle");
			TextArea txaContents = (TextArea) scene.lookup("#txaContents");
			Button btnNoticeSave = (Button) scene.lookup("#btnNoticeSave");

			txtTitle.setText(txtTitle.getText());
			txaContents.setText(txaContents.getText());

			// �������� ��������� �����ư
			btnNoticeSave.setOnAction(e -> {
				Notice notice = new Notice();

				notice.setTitle(txtTitle.getText());
				notice.setContents(txaContents.getText());

				int returnValue = 0;

				NoticeDAO noticeDAO = new NoticeDAO();
				returnValue = noticeDAO.getNoticeInsert(notice);

				if (returnValue != 0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�������� ����");
					alert.setHeaderText("������������ ����");
					alert.showAndWait();
				} else {
					System.out.println("�������");
				}
			});
			noticestage.close();
			noticeObsList.clear();
			getNoticeLoadTotalList();

			noticestage = new Stage(StageStyle.UTILITY);
			noticestage.initOwner(stage);
			noticestage.setScene(scene);
			noticestage.setResizable(false);
			noticestage.setTitle("��������");
			noticestage.show();

			NoticeDAO noticeDAO = new NoticeDAO();
			ArrayList<Notice> arraylist = new ArrayList<Notice>();

			for (int i = 0; i < arraylist.size(); i++) {
				Notice n = arraylist.get(i);
				noticeObsList.add(n);
			}

		} catch (IOException e) {

		}
	}

	// �ٹ�������ư �̺�ư�ڵ鷯
	private void handlebtnAlbumAction(ActionEvent event) {

		ObservableList<ImageView> albumList = FXCollections.observableArrayList();

		if (albumObsList != null)
			albumObsList.clear();

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

			// Ŭ���� �ν��ϴ� �̹����� �̺�Ʈ�ڵ鷯
			image1.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image2.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image3.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image4.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image5.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image6.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image7.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image8.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image9.setOnMouseClicked(e -> handleImage1ClickAction(e));

			// �������� ��ư�̺�Ʈ ���
			btnChangePhoto.setOnAction(e -> handlebtnChangePhotoAction(e));

		} catch (IOException e) {
		}
	}

	// ���õ� ���� �ʱ�ȭ
	private void handleImage1ClickAction(MouseEvent e) {
		selectedImage = (ImageView) e.getTarget();
	}

	// ���� �ʱ�ȭ
	private void albumInitialize(ObservableList<ImageView> albumList) {

		for (int i = 0; i < albumObsList.size() - 1; i++) {
			localUrl = albumObsList.get(i).getPhoto();

			if (albumObsList.get(i).getPhoto() == null) {
				localImage = new Image(albumObsList.get(10).getPhoto(), false);
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

	// �ٹ����� ������ư�ڵ鷯
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

			int imgViewIndex = selectedImage.getId().charAt(5) - '0';

			AlbumDAO albumDAO = new AlbumDAO();

			albumDAO.getalbumUpdate(localUrl, imgViewIndex);

		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("���� ����");
			alert.setHeaderText("�������� ���� �����߻�.");
			alert.showAndWait();
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

			// ȸ������ ������ư�̺�Ʈ���
			btnAdd.setOnAction(e -> handlebtnAddAction(e));
			// ȸ������ ������ư�̺�Ʈ���
			btnDelete.setOnAction(e -> handlebtnDeleteAction(e));
			// �˻���ư�̺�Ʈ���
			btnSearch.setOnAction(e -> handlebtnSearchAction(e, txtSearch));
			// ȸ������â ���̺� Ŭ�� �̺�Ʈ
			tlvList.setOnMouseClicked(e -> tableViewSelectedIndex = tlvList.getSelectionModel().getSelectedIndex());

		} catch (Exception e) {

		}

	}

	// ȸ������Ʈ���� �����̸� �˻�������ϴ� �ڵ鷯
	private void handlebtnSearchAction(ActionEvent event, TextField txtSearch) {
		try {
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
		User user = obslist.get(tableViewSelectedIndex);
		String str = user.getUserid();
		int returnValue = userDAO.UserDelete(str);
		if (returnValue != 0) {
			obslist.remove(selectedImage);

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
					System.out.println("���� ����");
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
		Parent root = null;
		reservationstage = new Stage(StageStyle.UTILITY);
		try {

			root = FXMLLoader.load(getClass().getResource("/view/reservation.fxml"));
			Scene scene = new Scene(root);

			TableView tlvReservation = (TableView) scene.lookup("#tlvReservation");

			TableColumn colname = new TableColumn("�����̸�");
			colname.setCellValueFactory(new PropertyValueFactory("jName"));

			TableColumn coluserid = new TableColumn("���� ���̵�");
			coluserid.setPrefWidth(100);
			coluserid.setCellValueFactory(new PropertyValueFactory("jUserID"));

			TableColumn colroomName = new TableColumn("�� �̸�");
			colroomName.setCellValueFactory(new PropertyValueFactory("jRoomName"));

			TableColumn colscheduleDate = new TableColumn("��¥");
			colscheduleDate.setCellValueFactory(new PropertyValueFactory("jDate"));

			TableColumn colstartTime = new TableColumn("���۽ð�");
			colstartTime.setCellValueFactory(new PropertyValueFactory("jStartTime"));

			TableColumn colendTime = new TableColumn("����ð�");
			colendTime.setCellValueFactory(new PropertyValueFactory("jEndTime"));

			TableColumn colpersonNum = new TableColumn("����ο�");
			colpersonNum.setCellValueFactory(new PropertyValueFactory("jPersonNum"));

			TableColumn colphone = new TableColumn("�ڵ��� ��ȣ");
			colphone.setPrefWidth(100);
			colphone.setCellValueFactory(new PropertyValueFactory("jPhone"));

			tlvReservation.getColumns().addAll(colname, coluserid, colroomName, colscheduleDate, colstartTime,
					colendTime, colpersonNum, colphone);

			tlvReservation.setOnMouseClicked(
					e -> tableViewSelectedIndex = tlvReservation.getSelectionModel().getSelectedIndex());

			TextField txfreservationSearch = (TextField) scene.lookup("#txfreservationSearch");
			Button btnReservationEdit = (Button) scene.lookup("#btnReservationEdit");
			Button btnReservationDelete = (Button) scene.lookup("#btnReservationDelete");
			Button btnReservationSearch = (Button) scene.lookup("#btnReservationSearch");

			JoinRevListDAO joinrevlistDAO = new JoinRevListDAO();
			ArrayList<JoinRevList> joinrevArrayList = joinrevlistDAO.getJoinRevListTotalLoadList();

			if (joinrevArrayList != null) {
				obsjrlList.clear();
			}
			for (int i = 0; i < joinrevArrayList.size(); i++) {

				JoinRevList j = joinrevArrayList.get(i);
				obsjrlList.add(j);
			}

			tlvReservation.setItems(obsjrlList);

			reservationstage.initModality(Modality.WINDOW_MODAL);
			reservationstage.initOwner(stage);
			reservationstage.setScene(scene);
			reservationstage.setResizable(false);
			reservationstage.show();
			// �������â �� ����� ���̺�������� �����ϴ¹�ư �̺�Ʈ
			btnReservationEdit.setOnAction(e -> handelbtnResercationEditAction(e));

		} catch (IOException e) {

		}

	}

	// ������� ���̺�信 ����� �����͸� �����ϴ¹�ư �̺�Ʈ���
	private void handelbtnResercationEditAction(ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/reservationEdit.fxml"));
			Scene scene = new Scene(root);

			TextField txfJname = (TextField) scene.lookup("#txfJname");
			TextField txfUserID = (TextField) scene.lookup("#txfUserID");
			TextField txfRoomName = (TextField) scene.lookup("#txfRoomName");
			TextField txfDate = (TextField) scene.lookup("#txfDate");
			TextField txfStartTime = (TextField) scene.lookup("#txfStartTime");
			TextField txfEndTime = (TextField) scene.lookup("#txfEndTime");
			TextField txfPersonNum = (TextField) scene.lookup("#txfPersonNum");
			TextField txfPhone = (TextField) scene.lookup("#txfPhone");
			Button btnRevSave = (Button) scene.lookup("#btnRevSave");

//			JoinRevList j = obsjrlList.get(tableViewSelectedIndex);

			txfJname.setText(txfJname.getText());
			txfUserID.setText(txfUserID.getText());
			txfRoomName.setText(txfRoomName.getText());
			txfDate.setUserData(txfDate.getLocalToParentTransform());
			txfStartTime.setText(txfStartTime.getText());
			txfEndTime.setText(txfEndTime.getText());
			txfPersonNum.setText(txfPersonNum.getText());
			txfPhone.setText(txfPhone.getText());
			txfPersonNum.setText(txfPersonNum.getText());

			btnRevSave.setOnAction(event -> {
				JoinRevList JRL = new JoinRevList();

				JRL.setjName(txfJname.getText());
				JRL.setjUserID(txfUserID.getText());
				JRL.setjRoomName(txfRoomName.getText());
				JRL.setjStartTime(Integer.valueOf(txfStartTime.getText()));
				JRL.setjEndTime(Integer.valueOf(txfEndTime.getText()));
				JRL.setjPersonNum(Integer.valueOf(txfPersonNum.getText()));

				int returnValue = 0;

				JoinRevListDAO joinreclistDAO = new JoinRevListDAO();
				returnValue = JoinRevListDAO.

				if (returnValue != 0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�������� ����");
					alert.setHeaderText("������������ ����");
					alert.showAndWait();
				} else {
					System.out.println("�������");
				}

			});
//			reservationstage.close();
//			obsjrlList.clear();
//			getNoticeLoadTotalList();

			reservationstage.initModality(Modality.WINDOW_MODAL);
			reservationstage.initOwner(stage);
			reservationstage.setScene(scene);
			reservationstage.setResizable(false);
			reservationstage.show();

			JoinRevListDAO joinreclistDAO = new JoinRevListDAO();
			ArrayList<JoinRevList> jrlarraylist = new ArrayList<JoinRevList>();

			for (int i = 0; i < jrlarraylist.size(); i++) {
				JoinRevList r = jrlarraylist.get(i);
				obsjrlList.add(r);
			}
		} catch (IOException e1) {

		}
	}

}
