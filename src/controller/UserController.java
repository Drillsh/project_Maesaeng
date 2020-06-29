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
	private Label lbLogout; // �α׾ƿ� ��
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

		// �α����� ���� ���̵� ����
		lbLoginUser.setText(LoginController.user.getName() + " �� [ " + LoginController.user.getUserid() + " ]");
		// �����ٿ� �α������� ��������
		schedule.setUserID(LoginController.user.getUserid());

		// ��ư �׷� �ʱ�ȭ
		btnGroupInitialize();

		// ���̺� �ʱ�ȭ
		tableInitialize();

		// �޺��ڽ� �ʱ�ȭ
		comboInitialize();

		// �ߺ� ���� ����
		blockOverlapReservation();

		// �ٹ� �̺�Ʈ
		lbAlbum.setOnMouseClicked(event -> handleAlbumAction(event));
		imgAlbum.setOnMouseClicked(event -> handleAlbumAction(event));

		// �������� �̺�Ʈ
		lbNotice.setOnMouseClicked(event -> handleNoticeAction(event));
		imgNotice.setOnMouseClicked(event -> handleNoticeAction(event));

		// ��� ��ư �̺�Ʈ
		toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue,
				Toggle newValue) -> handleTogleAction());

		// �޷� ���
		calendarPane.getChildren().add(new FullCalendarView(YearMonth.now()).getView());

		// ���ÿϷ� ��ư
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

		// �����̴� �̺�Ʈ, ���õ� �� ����
		sdrPersonNum.setOnMouseClicked(event -> schedule.setPersonNum((int) sdrPersonNum.getValue()));

		// �޺��ڽ� ���� �ð� ����
		cmbStart.setOnAction(event -> handleCmbBoxAction(event));
		cmbEnd.setOnAction(event -> handleCmbBoxAction(event));

		// �����ϱ� ��ư �̺�Ʈ
		btnFinalRev.setOnAction(event -> handleBtnFinalRevAction(event));

		// [Scene��ȯ] User -> Login // �α׾ƿ� �ڵ鷯 ���
		lbLogout.setOnMouseClicked(event -> handleLogoutAction(event));
		imgLogout.setOnMouseClicked(event -> handleLogoutAction(event));
	}

	// �ٹ� �̺�Ʈ
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
			albumStage.setTitle("�����ٹ�");
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


		} catch (Exception e) {

		}
	}

	// �������� �̺�Ʈ
	private void handleNoticeAction(MouseEvent event) {
		Parent root = null;
		Stage albumstage = new Stage(StageStyle.UTILITY);
		ObservableList<Notice> noticeObsList = FXCollections.observableArrayList();

		try {
			root = FXMLLoader.load(getClass().getResource("/view/noticeForUser.fxml"));
			Scene scene = new Scene(root);

			TableView tlvNotice = (TableView) scene.lookup("#tlvNotice");

			TableColumn colNo = new TableColumn("��ȣ");
			colNo.setCellValueFactory(new PropertyValueFactory("noticeNo"));

			TableColumn colTitle = new TableColumn("����");
			colTitle.setPrefWidth(200);
			colTitle.setCellValueFactory(new PropertyValueFactory("title"));

			TableColumn colDate = new TableColumn("��� ��¥");
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
			albumstage.setTitle("��������");
			albumstage.show();

		} catch (IOException e1) {

		}
	}

	// �뼱�� �̵� �÷���
	private boolean checkRoomChocie() {
		if (!roomChoiceFlag) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�뼱�� ����");
			alert.setHeaderText("���� �������ּ���!!");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	// �޺��ڽ� �ð� üũ
	private boolean checkTimeChoice() {
		if (schedule.getStartTime() == 0 || schedule.getEndTime() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText("��Ȯ�� �ð��� �Է����ּ���!!");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	// �����ϱ� ��ư �̺�Ʈ
	private void handleBtnFinalRevAction(ActionEvent event) {
		ScheduleDAO scheduleDAO = new ScheduleDAO();

		int returnValue = scheduleDAO.registerSchedule(schedule);

		if (returnValue != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText("���� ����!!");
			alert.setContentText("�����մϴ�");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText("���� ����!!");
			alert.showAndWait();
		}
	}

	// ��ư �׷� �ʱ�ȭ
	private void btnGroupInitialize() {
		toggleGroup = new ToggleGroup();

		btnA.setToggleGroup(toggleGroup);
		btnB.setToggleGroup(toggleGroup);
		btnC.setToggleGroup(toggleGroup);
		btnD.setToggleGroup(toggleGroup);
		btnE.setToggleGroup(toggleGroup);
		btnF.setToggleGroup(toggleGroup);
	}

	// ���̺� �ʱ�ȭ
	private void tableInitialize() {

		TableColumn colID = new TableColumn("���̵�");
		colID.setCellValueFactory(new PropertyValueFactory("userID"));

		TableColumn colRoom = new TableColumn("���̸�");
		colRoom.setCellValueFactory(new PropertyValueFactory("roomName"));

		TableColumn colDate = new TableColumn("�� ¥");
		colDate.setCellValueFactory(new PropertyValueFactory("scheduleDate"));

		TableColumn colStart = new TableColumn("���۽ð�");
		colStart.setCellValueFactory(new PropertyValueFactory("startTime"));

		TableColumn colEnd = new TableColumn("����ð�");
		colEnd.setCellValueFactory(new PropertyValueFactory("endTime"));

		TableColumn colPerson = new TableColumn("�� ��");
		colPerson.setCellValueFactory(new PropertyValueFactory("personNum"));

		tlvRevList.getColumns().addAll(colID, colRoom, colDate, colStart, colEnd, colPerson);

		tlvRevList.setItems(sObsList);
	}

	// �޺� �ڽ� �ʱ�ȭ
	private void comboInitialize() {

		for (int i = 9; i <= 21; ++i) {
			cmbObsList.add(i);
		}

		cmbStart.setItems(cmbObsList);
		cmbEnd.setItems(cmbObsList);
	}

	// �޺��ڽ� �ð� Disable
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

	// ��� ��ư ���
	private void handleTogleAction() {

		ToggleButton tBtn = (ToggleButton) toggleGroup.getSelectedToggle();
		try {
			tBtn.setOnAction(event -> handleTogBtnEvent(event));
			// ȭ���̵��÷���
			roomChoiceFlag = true;

		} catch (NullPointerException e) {
			roomChoiceFlag = false;
		}
	}

	// ��� ��ư �̺�Ʈ
	private void handleTogBtnEvent(ActionEvent event) {

		if (event.getTarget().equals(btnA)) {
			// �̹��� ����
			imgPic.setImage(new Image("/images/clouds.jpg"));
			// ������ ��������
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

	// ���� �����ֱ� in ��ư �̺�Ʈ
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
		lbPersonNum.setText(roomSize + " �ν�");

		// ���޹��� �������� �����̴� �ʱ�ȭ
		setSliderSize(roomSize);

		lbIsWB.setText(room.isWB() ? "O" : "X");
		lbIsTV.setText(room.isTV() ? "O" : "X");
		lbCharge.setText(room.getPrice() + " ��");

		// ������ ���̸� ����
		schedule.setRoomName(room.getRoomName());
	}

	// ������ ��������
	public void loadRevList(LocalDate date) {
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		ArrayList<Schedule> arrayList = scheduleDAO.getRevList(date, schedule.getRoomName());

		if (arrayList == null) {
			return;
		} else {
			sObsList.clear();
		}
		// ȭ�鿡 ���
		for (int i = 0; i < arrayList.size(); ++i) {
			Schedule s = arrayList.get(i);
			sObsList.add(s);
		}
		schedule.setScheduleDate(date);
	}

	// �����̴� ����
	private void setSliderSize(int personNum) {
		sdrPersonNum.setMax(personNum);
	}

	// �޺��ڽ� ���� �ð� ����
	private void handleCmbBoxAction(ActionEvent event) {

		try {
			if (event.getTarget().equals(cmbStart)) {
				int startTime = cmbStart.getValue();
				lbStart.setText(startTime + " ��");

				for (Schedule s : sObsList) {

					if (startTime >= s.getStartTime() && startTime < s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("���� ����");
						alert.setHeaderText("�̹� ����� �ð����Դϴ�!!");
						alert.setContentText("�ð��� �ٽ� �������ּ���");
						alert.showAndWait();

						return;
					}
				}
				// ���۽ð� ����
				schedule.setStartTime(startTime);
			}

			if (event.getTarget().equals(cmbEnd)) {
				int endTime = cmbEnd.getValue();
				lbEnd.setText(endTime + " ��");

				for (Schedule s : sObsList) {
					if (new Integer(endTime) != null && endTime > s.getStartTime() && endTime <= s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("���� ����");
						alert.setHeaderText("�̹� ����� �ð����Դϴ�!!");
						alert.setContentText("�ð��� �ٽ� �������ּ���");
						alert.showAndWait();

						return;
					}
				}
				// ����ð� ����
				schedule.setEndTime(endTime);
			}

		} catch (Exception e) {
			System.out.println("�ð� �ߺ� ����" + e.getMessage());
		}

	}

	// ����â ���� ����
	private void setFinalrevInfo() {

		// lbFinalUser.setText(LoginController.user.getName() + " �� [ " +
		// LoginController.user.getUserid() + " ]");
		lbFinalUser.setText(schedule.getUserID());

		lbFinalNum.setText(schedule.getPersonNum() + "��");
		lbFinalRoom.setText(schedule.getRoomName());
		lbFinalDate.setText(schedule.getScheduleDate().toString());
		lbFinalStime.setText(schedule.getStartTime() + "��");
		lbFinalEtime.setText(schedule.getEndTime() + "��");

		int totalCharge = room.getPrice() * schedule.getPersonNum() * (schedule.getEndTime() - schedule.getStartTime());

		lbFinalCharge.setText(totalCharge + " ��");
	}

	// �α׾ƿ� �� �ڵ鷯 ���
	private void handleLogoutAction(MouseEvent event) {

		// �α׾ƿ��� ȭ�� ��ȯ : �α���â����
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

	// ���� �ʱ�ȭ
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

	// ���õ� ���� �ʱ�ȭ
	private void handleImage1ClickAction(MouseEvent e) {
		selectedImage = (ImageView) e.getTarget();

		try {
			if (e.getClickCount() != 2) {
				return;
			}
			Parent viewRoot = FXMLLoader.load(getClass().getResource("/view/album_popup.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("��������");

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
