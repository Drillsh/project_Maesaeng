package controller;

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

	public Stage userStage;
	private ToggleGroup toggleGroup;

	public ObservableList<Schedule> sObsList = FXCollections.observableArrayList();
	public ObservableList<Integer> cmbObsList = FXCollections.observableArrayList();

	public RoomDAO roomDAO = new RoomDAO();

	private Schedule schedule = new Schedule();
	private Room room = new Room();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// 로그인한 유저 아이디 띄우기
		lbLoginUser.setText(LoginController.user.getName() + " 님 [ " + LoginController.user.getUserid() + " ]");
		
		// 버튼 그룹 초기화
		btnGroupInitialize();

		// 테이블 초기화
		tableInitialize();

		// 콤보박스 초기화
		comboInitialize();

		// 중복 예약 방지
		blockOverlapReservation();

		// 토글 버튼 이벤트
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue,
				Toggle newValue) -> handleTogleAction());

		// 달력 등록
		calendarPane.getChildren().add(new FullCalendarView(YearMonth.now()).getView());

		// 선택완료 버튼 -> 다음페이지 및 해당 페이지 정보 저장
		btnNext1.setOnAction(event -> {
			tbpTab.getSelectionModel().selectNext();
			tabSchedule.setDisable(false);
		});
		btnNext2.setOnAction(event -> {
			tbpTab.getSelectionModel().selectNext();
			tabReservation.setDisable(false);
			setFinalrevInfo();
		});

		// 슬라이더 이벤트, 선택된 값 전달
		sdrPersonNum.setOnMouseClicked(event -> schedule.setPersonNum((int) sdrPersonNum.getValue()));

		// 콤보박스 예약 시간 선택
		cmbStart.setOnAction(event -> handleCmbBoxAction(event));
		cmbEnd.setOnAction(event -> handleCmbBoxAction(event));
		
		// 예약하기 버튼 이벤트
		btnFinalRev.setOnAction(event -> handleBtnFinalRevAction());
		

		// [Scene전환] User -> Login  // 로그아웃 라벨 핸들러 등록
		lbLogout.setOnMouseClicked(event -> handleLogoutAction(event));
	}

	private void handleBtnFinalRevAction() {
		
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

	// 중복 예약 방지
	private void blockOverlapReservation() {
		// 콤보박스 시간 Disable
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
		tBtn.setOnAction(event -> handleTogBtnEvent(event));
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

		if (arrayList.size() != 0)
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

				// 시작시간 전달
				schedule.setStartTime(startTime);

				for (Schedule s : sObsList) {
					if (startTime >= s.getStartTime() && startTime < s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("예약 오류");
						alert.setHeaderText("이미 예약된 시간대입니다!!");
						alert.setContentText("시간을 다시 선택해주세요");
						alert.showAndWait();

					}
				}
			}
			if (event.getTarget().equals(cmbEnd)) {
				int endTime = cmbEnd.getValue();
				lbEnd.setText(endTime + " 시");

				// 종료시간 전달
				schedule.setEndTime(endTime);

				for (Schedule s : sObsList) {
					if (new Integer(endTime) != null && endTime > s.getStartTime() && endTime <= s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("예약 오류");
						alert.setHeaderText("이미 예약된 시간대입니다!!");
						alert.setContentText("시간을 다시 선택해주세요");
						alert.showAndWait();
					}
				}
			}

		} catch (Exception e) {
			System.out.println("시간 중복 오류");
		}

	}

	// 예약창 정보 세팅
	private void setFinalrevInfo() {
		
		lbFinalUser.setText(LoginController.user.getName() + " 님 [ "+LoginController.user.getUserid()+" ]");
		lbFinalNum.setText(schedule.getPersonNum() + "명");
		lbFinalRoom.setText(schedule.getRoomName());
		lbFinalStime.setText(schedule.getStartTime() + "시");
		lbFinalEtime.setText(schedule.getEndTime() + "시");

		int totalCharge = room.getPrice() * schedule.getPersonNum();

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
}
