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

		// 예약관리버튼 이벤트 등록
		btnManagement.setOnAction(event -> handlebtnManagementAction(event));
		// 회원수정 버튼 이벤트 등록
		btnEdit.setOnAction(event -> handlebtnEditAction(event));
		// 로그아웃 라벨클릭 이벤트 등록
		lbLogout.setOnMouseClicked(event -> handlelbLogoutAction(event));
		// 앨범관리 버튼이벤트등록
		btnAlbum.setOnAction(event -> handlebtnAlbumAction(event));
		// 공지사항버튼 이벤트등록
		btnNotice.setOnAction(e -> handlebtnNoticeAction(e));
	}

	// 공지사항 이벤트 핸들러
	private void handlebtnNoticeAction(ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/notice.fxml"));
			Scene scene = new Scene(root);

			albumstage = new Stage(StageStyle.UTILITY);
			albumstage.initOwner(stage);
			albumstage.setScene(scene);
			albumstage.setResizable(false);
			albumstage.setTitle("공지사항");
			albumstage.show();
		} catch (IOException e1) {
		}
	}

	// 앨범관리버튼 이베튼핸들러
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
			albumstage.setTitle("사진앨범");
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

			// DB 에서 사진을 싹 긁어와서 띄움
			albumInitialize(albumList);

			// 사진수정 버튼이벤트 등록
			btnChangePhoto.setOnAction(e -> handlebtnChangePhotoAction(e));

			// 클릭을 인식하는 이미지뷰 이벤트핸들러
			image1.setOnMouseClicked(e -> handleImage1ClickAction(e));
			// 앨범관리창 사진삭제버튼 이벤트등록
			btnremove.setOnAction(e -> handlebtnremoveAction(e));
		} catch (IOException e) {
		}

	}

	// 앨범관리에 사진삭제버튼 이벤트등록 함수
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
			alert.setTitle("테이블뷰 삭제 에러");
			alert.setHeaderText("삭제 점검요망");
			alert.setContentText("삭제주의하세요");
			alert.showAndWait();
		}
	}

	// 앨범 관리 이미지수정 핸들러
	private void handleImage1ClickAction(MouseEvent e) {
		selectedImage = (ImageView) e.getTarget();
	}

	// 사진 초기화
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

	// DB연동 핸들러
	public void getTotalAlbumList() {

		// 앨범 객체 가져옴

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

	// 수정버튼핸들러
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

	// 유저 DB연동핸들러
	private void loadUserList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("ManagerController.loadUserList: DB 연결 성공");
			} else {
				System.out.println("ManagerController.loadUserList : DB 연결 실패");
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
			alert.setTitle("TotalList 점검요망");
			alert.setHeaderText("TotalList 문제방생");
			alert.setContentText("문제사항:" + e.getMessage());
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

	// 로그아웃 버튼 이벤트 핸들러
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

	// 회원수정이벤트 핸들러및 팝업창 작업
	private void handlebtnEditAction(ActionEvent event) {

		Parent root = null;
		// loadUserList();
		try {
			root = FXMLLoader.load(getClass().getResource("/view/userList.fxml"));
			Scene scene = new Scene(root);

			TableView tlvList = (TableView) scene.lookup("#tlvList");

			TableColumn colUserId = new TableColumn("유저 아이디");
			colUserId.setCellValueFactory(new PropertyValueFactory("userid"));

			TableColumn colPassword = new TableColumn("비밀번호");
			colPassword.setCellValueFactory(new PropertyValueFactory("password"));

			TableColumn colName = new TableColumn("성명");
			colName.setCellValueFactory(new PropertyValueFactory("name"));

			TableColumn colPhone = new TableColumn("핸드폰 번호");
			colPhone.setCellValueFactory(new PropertyValueFactory("phone"));

			TableColumn colMail = new TableColumn("이메일");
			colMail.setCellValueFactory(new PropertyValueFactory("mail"));

			tlvList.getColumns().addAll(colUserId, colPassword, colName, colPhone, colMail);

			editstage = new Stage(StageStyle.UTILITY);
			editstage.initOwner(stage);
			editstage.setScene(scene);
			editstage.setResizable(false);
			editstage.setTitle("회원리스트");
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

			// 수정버튼이벤트등록
			btnAdd.setOnAction(e -> handlebtnAddAction(e));
			// 삭제버튼이벤트등록
			btnDelete.setOnAction(e -> handlebtnDeleteAction(e));
			// 검색버튼이벤트등록
			btnSearch.setOnAction(e -> handlebtnSearchAction(e, txtSearch));
			// 앨범관리창에 사진을선택하는 이벤트등록
			tlvList.setOnMouseClicked(e -> tableViewSelectedIndex = tlvList.getSelectionModel().getSelectedIndex());

		} catch (Exception e) {

		}

	}

	// 회원리스트에서 유저이름 검색기능을하는 핸들러
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
			alert.setTitle("이름입력요망");
			alert.setHeaderText("이름을 입력하세요");
			alert.setContentText("주의하세요");
			alert.showAndWait();
		}
	}

	// 회원리스트 삭제버튼 핸들러
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
			alert.setTitle("테이블뷰 삭제 에러");
			alert.setHeaderText("삭제 점검요망");
			alert.setContentText("삭제주의하세요");
			alert.showAndWait();
		}
	}

	// 수정된 회원정보를 테이블에저장
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
					System.out.println("연결실패");
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
			editstage.setTitle("회원 프로그램 수정");
			editstage.show();

		} catch (IOException e) {

		}

	}

	// 예약관리이벤트 핸들러 및 팝업창 작업
	private void handlebtnManagementAction(ActionEvent event) {

	}
}
