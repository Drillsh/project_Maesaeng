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

		XYChart.Series sales = new XYChart.Series();
		sales.setName("Today");
		ObservableList sales1 = FXCollections.observableArrayList(new XYChart.Data("6-25", 4),
				new XYChart.Data("6-26", 1), new XYChart.Data("6-27", 5));

		sales.setData(sales1);
		barChart.getData().add(sales);
		// mainXYchart.getData().add(hp);

		XYChart.Series sales2 = new XYChart.Series();
		sales2.setName("Month");
		sales2.setData(FXCollections.observableArrayList(new XYChart.Data("6월", 107)

		));
	}

	// 공지사항 이벤트 핸들러
	private void handlebtnNoticeAction(ActionEvent e) {
		Parent root = null;
		albumstage = new Stage(StageStyle.UTILITY);

		try {
			root = FXMLLoader.load(getClass().getResource("/view/notice.fxml"));
			Scene scene = new Scene(root);

			TableView tlvNotice = (TableView) scene.lookup("#tlvNotice");

			TableColumn colNo = new TableColumn("번호");
			colNo.setCellValueFactory(new PropertyValueFactory("noticeNo"));

			TableColumn colTitle = new TableColumn("제목");
			colTitle.setCellValueFactory(new PropertyValueFactory("title"));

			TableColumn colContents = new TableColumn("내용");
			colContents.setCellValueFactory(new PropertyValueFactory("contents"));

			TableColumn colDate = new TableColumn("등록 날짜");
			colDate.setCellValueFactory(new PropertyValueFactory("noticeDate"));

			tlvNotice.getColumns().addAll(colNo, colTitle, colContents, colDate);

			Button btnNoticeAdd = (Button) scene.lookup("#btnNoticeAdd");
			Button btnNoticeEdit = (Button) scene.lookup("#btnNoticeEdit");
			Button btnNoticeDelete = (Button) scene.lookup("#btnNoticeDelete");

			// 공지사항에 추가버튼이벤트등록 추가버튼을누르면 공지사항제목과 내용을 입력
			btnNoticeAdd.setOnAction(event -> handlebtnNoticeAddAction(event));

			// 테이블뷰에 저장된 테이블을 클릭해서 선택액션하는 이벤트
			tlvNotice.setOnMouseClicked(
					evnet -> tableViewSelectedIndex = tlvNotice.getSelectionModel().getSelectedIndex());
			// 테이블뷰에 서 선택된 공지사항 삭제이벤트등록
			btnNoticeDelete.setOnAction(event -> hanclebtnNoticeDeleteAction(event));
			// 테이블뷰에 저장된 공지사항 수정버튼 이벤트등록
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
			albumstage.setTitle("공지사항");
			albumstage.show();

		} catch (IOException e1) {

		}
	}

	// 테이블뷰에 저장된 공지사항을 수정이벤트
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

			// 공지사항 수정창 저장버튼이벤트드등록
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
					System.out.println("공지사항수정 연결실패");
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
			noticestage.setTitle("공지사항");
			noticestage.show();
		} catch (IOException e) {

		}

	}

	// 테이블뷰에 저장된 데이터를 삭제하는 버튼 기능 구현
	private void hanclebtnNoticeDeleteAction(ActionEvent e) {
		NoticeDAO noticeDAO = new NoticeDAO();
		Notice notice = noticeObsList.get(tableViewSelectedIndex);
		int no = notice.getNoticeNo();
		int returnVlue = noticeDAO.getNoticeDelete(notice);
		if (returnVlue != 0) {
			noticeObsList.clear();
			getNoticeLoadTotalList();
		} else {
			System.out.println("연결 실패");
		}
	}

	// 데이터베이스에 있는 정보를 다가져오기
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

	// 공지사항의 제목과 내용을입력하는 창
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

			// 공지사항 제목과내용 저장버튼
			btnNoticeSave.setOnAction(e -> {
				Notice notice = new Notice();

				notice.setTitle(txtTitle.getText());
				notice.setContents(txaContents.getText());

				int returnValue = 0;

				NoticeDAO noticeDAO = new NoticeDAO();
				returnValue = noticeDAO.getNoticeInsert(notice);

				if (returnValue != 0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("공지사항 저장");
					alert.setHeaderText("공지사항저장 성공");
					alert.showAndWait();
				} else {
					System.out.println("연결실패");
				}
			});
			noticestage.close();
			noticeObsList.clear();
			getNoticeLoadTotalList();

			noticestage = new Stage(StageStyle.UTILITY);
			noticestage.initOwner(stage);
			noticestage.setScene(scene);
			noticestage.setResizable(false);
			noticestage.setTitle("공지사항");
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

	// 앨범관리버튼 이베튼핸들러
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

			// 클릭을 인식하는 이미지뷰 이벤트핸들러
			image1.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image2.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image3.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image4.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image5.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image6.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image7.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image8.setOnMouseClicked(e -> handleImage1ClickAction(e));
			image9.setOnMouseClicked(e -> handleImage1ClickAction(e));

			// 사진수정 버튼이벤트 등록
			btnChangePhoto.setOnAction(e -> handlebtnChangePhotoAction(e));

		} catch (IOException e) {
		}
	}

	// 선택된 사진 초기화
	private void handleImage1ClickAction(MouseEvent e) {
		selectedImage = (ImageView) e.getTarget();
	}

	// 사진 초기화
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

	// 앨범관리 수정버튼핸들러
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
			alert.setTitle("사진 수정");
			alert.setHeaderText("사진수정 실행 문제발생.");
			alert.showAndWait();
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

			// 회원관리 수정버튼이벤트등록
			btnAdd.setOnAction(e -> handlebtnAddAction(e));
			// 회원관리 삭제버튼이벤트등록
			btnDelete.setOnAction(e -> handlebtnDeleteAction(e));
			// 검색버튼이벤트등록
			btnSearch.setOnAction(e -> handlebtnSearchAction(e, txtSearch));
			// 회원수정창 테이블 클릭 이벤트
			tlvList.setOnMouseClicked(e -> tableViewSelectedIndex = tlvList.getSelectionModel().getSelectedIndex());

		} catch (Exception e) {

		}

	}

	// 회원리스트에서 유저이름 검색기능을하는 핸들러
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
			alert.setTitle("이름입력요망");
			alert.setHeaderText("이름을 입력하세요");
			alert.setContentText("주의하세요");
			alert.showAndWait();
		}
	}

	// 회원리스트 삭제버튼 핸들러
	private void handlebtnDeleteAction(ActionEvent event) {
		UserDAO userDAO = new UserDAO();
		User user = obslist.get(tableViewSelectedIndex);
		String str = user.getUserid();
		int returnValue = userDAO.UserDelete(str);
		if (returnValue != 0) {
			obslist.remove(selectedImage);

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
					System.out.println("연결 실패");
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
		Parent root = null;
		reservationstage = new Stage(StageStyle.UTILITY);
		try {

			root = FXMLLoader.load(getClass().getResource("/view/reservation.fxml"));
			Scene scene = new Scene(root);

			TableView tlvReservation = (TableView) scene.lookup("#tlvReservation");

			TableColumn colname = new TableColumn("유저이름");
			colname.setCellValueFactory(new PropertyValueFactory("jName"));

			TableColumn coluserid = new TableColumn("유저 아이디");
			coluserid.setPrefWidth(100);
			coluserid.setCellValueFactory(new PropertyValueFactory("jUserID"));

			TableColumn colroomName = new TableColumn("방 이름");
			colroomName.setCellValueFactory(new PropertyValueFactory("jRoomName"));

			TableColumn colscheduleDate = new TableColumn("날짜");
			colscheduleDate.setCellValueFactory(new PropertyValueFactory("jDate"));

			TableColumn colstartTime = new TableColumn("시작시간");
			colstartTime.setCellValueFactory(new PropertyValueFactory("jStartTime"));

			TableColumn colendTime = new TableColumn("종료시간");
			colendTime.setCellValueFactory(new PropertyValueFactory("jEndTime"));

			TableColumn colpersonNum = new TableColumn("사용인원");
			colpersonNum.setCellValueFactory(new PropertyValueFactory("jPersonNum"));

			TableColumn colphone = new TableColumn("핸드폰 번호");
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
			// 예약관리창 에 저장된 테이블뷰정보를 수정하는버튼 이벤트
			btnReservationEdit.setOnAction(e -> handelbtnResercationEditAction(e));

		} catch (IOException e) {

		}

	}

	// 예약관리 테이블뷰에 저장된 데이터를 수정하는버튼 이벤트등록
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
					alert.setTitle("공지사항 저장");
					alert.setHeaderText("공지사항저장 성공");
					alert.showAndWait();
				} else {
					System.out.println("연결실패");
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
