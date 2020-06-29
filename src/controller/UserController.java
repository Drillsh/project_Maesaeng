package controller;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;

public class UserController implements Initializable {

	@FXML
	private ImageView imgPic;
	@FXML
	private Label lbCharge;
	@FXML
	private Label lbIsWB;
	@FXML
	private Label lbIsTV;
	@FXML
	private Label lbPersonNum;
	@FXML
	private ToggleButton btnA;
	@FXML
	private ToggleButton btnB;
	@FXML
	private ToggleButton btnC;
	@FXML
	private ToggleButton btnD;
	@FXML
	private ToggleButton btnE;
	@FXML
	private ToggleButton btnF;
	@FXML
	private TableView tlvRevList = new TableView();
	@FXML
	private Label lbLogout; // 로그아웃 라벨
	@FXML
	private Pane calendarPane;
	@FXML
	private Button btnNext1;
	@FXML
	private Button btnNext2;
	@FXML
	private TabPane tbpTab;
	@FXML
	private Slider sdrPersonNum;
	@FXML
	public ComboBox<Integer> cmbStart;
	@FXML
	public ComboBox<Integer> cmbEnd;
	@FXML
	private Label lbStart;
	@FXML
	private Label lbEnd;
	@FXML
	private Label lbFinalUser;
	@FXML
	private Label lbFinalNum;
	@FXML
	private Label lbFinalRoom;
	@FXML
	private Label lbFinalStime;
	@FXML
	private Label lbFinalEtime;
	@FXML
	private Label lbFinalCharge;
	@FXML
	private Button btnFinalRev;
	@FXML
	private Tab tabSchedule;
	@FXML
	private Tab tabReservation;
	@FXML
	private Label lbLoginUser;
	@FXML
	private Label lbFinalDate;
	@FXML
	private Label lbAlbum;
	@FXML
	private Label lbNotice;
	@FXML
	private ImageView imgLogout;
	@FXML
	private ImageView imgAlbum;
	@FXML
	private ImageView imgNotice;

	public Stage userStage;
	private ToggleGroup toggleGroup;
	private boolean roomChoiceFlag = false;
	private ImageView selectedImage = new ImageView();

	public ObservableList<Schedule> sObsList = FXCollections.observableArrayList();
	private ObservableList<Integer> cmbObsList = FXCollections.observableArrayList();
	private ObservableList<Album> albumObsList = FXCollections.observableArrayList();

	public RoomDAO roomDAO = new RoomDAO();
	private Schedule schedule = new Schedule();
	private Room room = new Room();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 로그인한 유저 아이디 띄우기
		lbLoginUser.setText(LoginController.user.getName() + " 님 [ " + LoginController.user.getUserid() + " ]");
		// 스케줄에 로그인유저 정보전달
		schedule.setUserID(LoginController.user.getUserid());

		// 버튼 그룹 초기화
		btnGroupInitialize();

		// 테이블 초기화
		tableInitialize();

		// 콤보박스 초기화
		comboInitialize();

		// 중복 예약 방지
		blockOverlapReservation();

		// 앨범 이벤트
		lbAlbum.setOnMouseClicked(event -> handleAlbumAction(event));
		imgAlbum.setOnMouseClicked(event -> handleAlbumAction(event));

		// 공지사항 이벤트
		lbNotice.setOnMouseClicked(event -> handleNoticeAction(event));
		imgNotice.setOnMouseClicked(event -> handleNoticeAction(event));

		// 토글 버튼 이벤트
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue,
				Toggle newValue) -> handleTogleAction());

		// 달력 등록
		calendarPane.getChildren().add(new FullCalendarView(YearMonth.now()).getView());

		// 선택완료 버튼
		btnNext1.setOnAction(event -> {
			if (checkRoomChocie()) {
				tbpTab.getSelectionModel().selectNext();
				tabSchedule.setDisable(false);
			}
		});
		btnNext2.setOnAction(event -> {
			if (checkTimeChoice()) {
				tbpTab.getSelectionModel().selectNext();
				tabReservation.setDisable(false);
				setFinalrevInfo();
			}
		});

		// 슬라이더 이벤트, 선택된 값 전달
		sdrPersonNum.setOnMouseClicked(event -> schedule.setPersonNum((int) sdrPersonNum.getValue()));

		// 콤보박스 예약 시간 선택
		cmbStart.setOnAction(event -> handleCmbBoxAction(event));
		cmbEnd.setOnAction(event -> handleCmbBoxAction(event));

		// 예약하기 버튼 이벤트
		btnFinalRev.setOnAction(event -> handleBtnFinalRevAction(event));

		// [Scene전환] User -> Login // 로그아웃 핸들러 등록
		lbLogout.setOnMouseClicked(event -> handleLogoutAction(event));
		imgLogout.setOnMouseClicked(event -> handleLogoutAction(event));
	}

	// 앨범 이벤트
	private void handleAlbumAction(MouseEvent event) {

		ObservableList<ImageView> albumList = FXCollections.observableArrayList();

		if (albumObsList != null)
			albumObsList.clear();

		getTotalAlbumList();

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/albumForUser.fxml"));
			Scene scene = new Scene(root);
			Stage albumStage = new Stage(StageStyle.UTILITY);

			albumStage.initOwner(userStage);
			albumStage.setScene(scene);
			albumStage.setResizable(false);
			albumStage.setTitle("사진앨범");
			albumStage.show();
			
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


		} catch (Exception e) {

		}
	}

	// 공지사항 이벤트
	private void handleNoticeAction(MouseEvent event) {
		Parent root = null;
		Stage albumstage = new Stage(StageStyle.UTILITY);
		ObservableList<Notice> noticeObsList = FXCollections.observableArrayList();

		try {
			root = FXMLLoader.load(getClass().getResource("/view/noticeForUser.fxml"));
			Scene scene = new Scene(root);

			TableView tlvNotice = (TableView) scene.lookup("#tlvNotice");

			TableColumn colNo = new TableColumn("번호");
			colNo.setCellValueFactory(new PropertyValueFactory("noticeNo"));

			TableColumn colTitle = new TableColumn("제목");
			colTitle.setPrefWidth(200);
			colTitle.setCellValueFactory(new PropertyValueFactory("title"));

			TableColumn colDate = new TableColumn("등록 날짜");
			colDate.setCellValueFactory(new PropertyValueFactory("noticeDate"));

			tlvNotice.getColumns().addAll(colNo, colTitle, colDate);

			NoticeDAO noticeDAO = new NoticeDAO();
			ArrayList<Notice> noticeArrayList = noticeDAO.getNoticeLoadTotalList();

			for (int i = 0; i < noticeArrayList.size(); i++) {
				Notice n = noticeArrayList.get(i);
				noticeObsList.add(n);
			}

			tlvNotice.setItems(noticeObsList);

			albumstage.initOwner(userStage);
			albumstage.setScene(scene);
			albumstage.setResizable(false);
			albumstage.setTitle("공지사항");
			albumstage.show();

		} catch (IOException e1) {

		}
	}

	// 룸선택 이동 플래그
	private boolean checkRoomChocie() {
		if (!roomChoiceFlag) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("룸선택 에러");
			alert.setHeaderText("방을 선택해주세요!!");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	// 콤보박스 시간 체크
	private boolean checkTimeChoice() {
		if (schedule.getStartTime() == 0 || schedule.getEndTime() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("예약 오류");
			alert.setHeaderText("정확한 시간을 입력해주세요!!");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	// 예약하기 버튼 이벤트
	private void handleBtnFinalRevAction(ActionEvent event) {
		ScheduleDAO scheduleDAO = new ScheduleDAO();

		int returnValue = scheduleDAO.registerSchedule(schedule);

		if (returnValue != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("예약 정보");
			alert.setHeaderText("예약 성공!!");
			alert.setContentText("감사합니다");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("예약 정보");
			alert.setHeaderText("예약 실패!!");
			alert.showAndWait();
		}
	}

	// 버튼 그룹 초기화
	private void btnGroupInitialize() {
		toggleGroup = new ToggleGroup();

		btnA.setToggleGroup(toggleGroup);
		btnB.setToggleGroup(toggleGroup);
		btnC.setToggleGroup(toggleGroup);
		btnD.setToggleGroup(toggleGroup);
		btnE.setToggleGroup(toggleGroup);
		btnF.setToggleGroup(toggleGroup);
	}

	// 테이블 초기화
	private void tableInitialize() {

		TableColumn colID = new TableColumn("아이디");
		colID.setCellValueFactory(new PropertyValueFactory("userID"));

		TableColumn colRoom = new TableColumn("방이름");
		colRoom.setCellValueFactory(new PropertyValueFactory("roomName"));

		TableColumn colDate = new TableColumn("날 짜");
		colDate.setCellValueFactory(new PropertyValueFactory("scheduleDate"));

		TableColumn colStart = new TableColumn("시작시간");
		colStart.setCellValueFactory(new PropertyValueFactory("startTime"));

		TableColumn colEnd = new TableColumn("종료시간");
		colEnd.setCellValueFactory(new PropertyValueFactory("endTime"));

		TableColumn colPerson = new TableColumn("인 원");
		colPerson.setCellValueFactory(new PropertyValueFactory("personNum"));

		tlvRevList.getColumns().addAll(colID, colRoom, colDate, colStart, colEnd, colPerson);

		tlvRevList.setItems(sObsList);
	}

	// 콤보 박스 초기화
	private void comboInitialize() {

		for (int i = 9; i <= 21; ++i) {
			cmbObsList.add(i);
		}

		cmbStart.setItems(cmbObsList);
		cmbEnd.setItems(cmbObsList);
	}

	// 콤보박스 시간 Disable
	private void blockOverlapReservation() {

		class EndHoursCell extends ListCell<Integer> {

			EndHoursCell() {
				DBUtil.userCon.cmbStart.valueProperty()
						.addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
			}

			@Override
			protected void updateItem(Integer hours, boolean empty) {
				super.updateItem(hours, empty);
				if (empty) {
					setText(null);
				} else {
					setText(hours.toString());
					updateDisableState();
				}
			}

			private void updateDisableState() {
				boolean disable = getItem() != null && DBUtil.userCon.cmbStart.getValue() != null
						&& getItem().intValue() <= Integer.valueOf(DBUtil.userCon.cmbStart.getValue());
				setDisable(disable);
				setOpacity(disable ? 0.5 : 1);

			}
		}

		cmbEnd.setCellFactory(lv -> new EndHoursCell());
	}

	// 토글 버튼 등록
	private void handleTogleAction() {

		ToggleButton tBtn = (ToggleButton) toggleGroup.getSelectedToggle();
		try {
			tBtn.setOnAction(event -> handleTogBtnEvent(event));
			// 화면이동플래그
			roomChoiceFlag = true;

		} catch (NullPointerException e) {
			roomChoiceFlag = false;
		}
	}

	// 토글 버튼 이벤트
	private void handleTogBtnEvent(ActionEvent event) {

		if (event.getTarget().equals(btnA)) {
			// 이미지 세팅
			imgPic.setImage(new Image("/images/clouds.jpg"));
			// 룸정보 가져오기
			showInfo("A");
		}
		if (event.getTarget().equals(btnB)) {
			imgPic.setImage(new Image("/images/horizon.jpg"));
			showInfo("B");
		}
		if (event.getTarget().equals(btnC)) {
			imgPic.setImage(new Image("/images/milkyWay.jpg"));
			showInfo("C");
		}
		if (event.getTarget().equals(btnD)) {
			imgPic.setImage(new Image("/images/moon.jpg"));
			showInfo("D");
		}
		if (event.getTarget().equals(btnE)) {
			imgPic.setImage(new Image("/images/starryNight.jpg"));
			showInfo("E");
		}
		if (event.getTarget().equals(btnF)) {
			imgPic.setImage(new Image("/images/DSCF3813.jpg"));
			showInfo("F");
		}
	}

	// 정보 보여주기 in 버튼 이벤트
	private void showInfo(String roomName) {
		ObservableList<Object> obsList = FXCollections.observableArrayList();
		ArrayList<Room> arrayList = roomDAO.getRoomInfoFromName(roomName);

		if (!arrayList.isEmpty())
			obsList.clear();

		for (int i = 0; i < arrayList.size(); i++) {
			Room room = arrayList.get(i);
			obsList.add(room);
		}

		room = (Room) obsList.get(0);

		int roomSize = (room.getSize());
		lbPersonNum.setText(roomSize + " 인실");

		// 전달받은 룸사이즈로 슬라이더 초기화
		setSliderSize(roomSize);

		lbIsWB.setText(room.isWB() ? "O" : "X");
		lbIsTV.setText(room.isTV() ? "O" : "X");
		lbCharge.setText(room.getPrice() + " 원");

		// 선택한 룸이름 전달
		schedule.setRoomName(room.getRoomName());
	}

	// 스케줄 가져오기
	public void loadRevList(LocalDate date) {
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		ArrayList<Schedule> arrayList = scheduleDAO.getRevList(date, schedule.getRoomName());

		if (arrayList == null) {
			return;
		} else {
			sObsList.clear();
		}
		// 화면에 띄움
		for (int i = 0; i < arrayList.size(); ++i) {
			Schedule s = arrayList.get(i);
			sObsList.add(s);
		}
		schedule.setScheduleDate(date);
	}

	// 슬라이더 세팅
	private void setSliderSize(int personNum) {
		sdrPersonNum.setMax(personNum);
	}

	// 콤보박스 예약 시간 선택
	private void handleCmbBoxAction(ActionEvent event) {

		try {
			if (event.getTarget().equals(cmbStart)) {
				int startTime = cmbStart.getValue();
				lbStart.setText(startTime + " 시");

				for (Schedule s : sObsList) {

					if (startTime >= s.getStartTime() && startTime < s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("예약 오류");
						alert.setHeaderText("이미 예약된 시간대입니다!!");
						alert.setContentText("시간을 다시 선택해주세요");
						alert.showAndWait();

						return;
					}
				}
				// 시작시간 전달
				schedule.setStartTime(startTime);
			}

			if (event.getTarget().equals(cmbEnd)) {
				int endTime = cmbEnd.getValue();
				lbEnd.setText(endTime + " 시");

				for (Schedule s : sObsList) {
					if (new Integer(endTime) != null && endTime > s.getStartTime() && endTime <= s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("예약 오류");
						alert.setHeaderText("이미 예약된 시간대입니다!!");
						alert.setContentText("시간을 다시 선택해주세요");
						alert.showAndWait();

						return;
					}
				}
				// 종료시간 전달
				schedule.setEndTime(endTime);
			}

		} catch (Exception e) {
			System.out.println("시간 중복 오류" + e.getMessage());
		}

	}

	// 예약창 정보 세팅
	private void setFinalrevInfo() {

		// lbFinalUser.setText(LoginController.user.getName() + " 님 [ " +
		// LoginController.user.getUserid() + " ]");
		lbFinalUser.setText(schedule.getUserID());

		lbFinalNum.setText(schedule.getPersonNum() + "명");
		lbFinalRoom.setText(schedule.getRoomName());
		lbFinalDate.setText(schedule.getScheduleDate().toString());
		lbFinalStime.setText(schedule.getStartTime() + "시");
		lbFinalEtime.setText(schedule.getEndTime() + "시");

		int totalCharge = room.getPrice() * schedule.getPersonNum() * (schedule.getEndTime() - schedule.getStartTime());

		lbFinalCharge.setText(totalCharge + " 원");
	}

	// 로그아웃 라벨 핸들러 등록
	private void handleLogoutAction(MouseEvent event) {

		// 로그아웃시 화면 전환 : 로그인창으로
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

			userStage.close();
			primaryStage.show();

		} catch (Exception e) {
		}
	}

	// 사진 초기화
	private void albumInitialize(ObservableList<ImageView> albumList) {
		Image localImage = null;

		for (int i = 0; i < albumObsList.size() - 1; i++) {
			String localUrl = albumObsList.get(i).getPhoto();

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

	// 선택된 사진 초기화
	private void handleImage1ClickAction(MouseEvent e) {
		selectedImage = (ImageView) e.getTarget();

		try {
			if (e.getClickCount() != 2) {
				return;
			}
			Parent viewRoot = FXMLLoader.load(getClass().getResource("/view/album_popup.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("사진보기");

			ImageView imageView = (ImageView) viewRoot.lookup("#imageView");
			Button btnExit = (Button) viewRoot.lookup("#btnExit");
			
			Image selectedAlbum = selectedImage.getImage();
			imageView.setImage(selectedAlbum);
			
			
			Scene scene = new Scene(viewRoot);
			stage.setScene(scene);
			stage.show();
			
			btnExit.setOnAction(event -> stage.close() );
			
		} catch (IOException except) {
			except.printStackTrace();
		}

	}

}
