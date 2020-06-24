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

	public Stage userStage;
	private ToggleGroup toggleGroup;

	public ObservableList<Schedule> sObsList = FXCollections.observableArrayList();
	public ObservableList<Integer> cmbObsList = FXCollections.observableArrayList();

	public RoomDAO roomDAO = new RoomDAO();

	private String selectedRoomName = new String();
	private Integer personNum = new Integer(0);

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// [Scene��ȯ] User -> Login
		// �α׾ƿ� �� �ڵ鷯 ���
		lbLogout.setOnMouseClicked(event -> handleLogoutAction(event));

		// ��ư ��� �׷� ����
		setToggleGroup();

		// ��� ��ư �̺�Ʈ
		toggleGroup.selectedToggleProperty()
				.addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					ToggleButton tBtn = (ToggleButton) toggleGroup.getSelectedToggle();
					tBtn.setOnAction(event -> handleTogBtnEvent(event));
				});

		// �޷� ���
		calendarPane.getChildren().add(new FullCalendarView(YearMonth.now()).getView());

		// ���ÿϷ� ��ư -> ���������� �� �ش� ������ ���� ����
		btnNext2.setOnAction(event -> tbpTab.getSelectionModel().selectNext());
		btnNext1.setOnAction(event -> tbpTab.getSelectionModel().selectNext());

		// ���̺� �ʱ�ȭ
		tableInitialize();

		// �޺��ڽ� �ʱ�ȭ
		comboInitialize();

		// ���� �ð� ǥ��
		cmbStart.setOnAction(event -> handleCmbBoxAction(event));
		cmbEnd.setOnAction(event -> handleCmbBoxAction(event));
	}

	private void handleCmbBoxAction(ActionEvent event) {

		try {
			if (event.getTarget().equals(cmbStart)) {
				lbStart.setText(cmbStart.getValue() + " ��");
				
				for (Schedule s : sObsList) {
					if ( cmbStart.getValue() >= s.getStartTime() && cmbStart.getValue() < s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("���� ����");
						alert.setHeaderText("�̹� ����� �ð����Դϴ�!!");
						alert.setContentText("�ð��� �ٽ� �������ּ���");
						alert.showAndWait();
					}
				}
			}
			if (event.getTarget().equals(cmbEnd)) {
				lbEnd.setText(cmbEnd.getValue() + " ��");

				for (Schedule s : sObsList) {
					if ( cmbStart.getValue() != null &&  cmbEnd.getValue() > s.getStartTime() && cmbEnd.getValue() <= s.getEndTime()) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("���� ����");
						alert.setHeaderText("�̹� ����� �ð����Դϴ�!!");
						alert.setContentText("�ð��� �ٽ� �������ּ���");
						alert.showAndWait();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("�ð� �ߺ� ����");
		}

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

	// ��ư ��� �׷� ����
	private void setToggleGroup() {
		toggleGroup = new ToggleGroup();

		btnA.setToggleGroup(toggleGroup);
		btnB.setToggleGroup(toggleGroup);
		btnC.setToggleGroup(toggleGroup);
		btnD.setToggleGroup(toggleGroup);
		btnE.setToggleGroup(toggleGroup);
		btnF.setToggleGroup(toggleGroup);
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

		if (arrayList.size() != 0)
			obsList.clear();

		for (int i = 0; i < arrayList.size(); i++) {
			Room room = arrayList.get(i);
			obsList.add(room);
		}

		int roomSize = ((Room) obsList.get(0)).getSize();
		lbPersonNum.setText(roomSize + " �ν�");
		setSliderSize(roomSize);

		lbIsWB.setText((((Room) obsList.get(0)).isWB()) ? "O" : "X");
		lbIsTV.setText((((Room) obsList.get(0)).isTV()) ? "O" : "X");
		lbCharge.setText(String.valueOf(((Room) obsList.get(0)).getPrice()));

		// ������ �� �̸� ����
		selectedRoomName = roomName;
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
			// String str = i + " ��";
			cmbObsList.add(i);
		}
		//cmbStart.setCellFactory(lv -> new StartHoursCell());
		cmbEnd.setCellFactory(lv -> new EndHoursCell());

		cmbStart.setItems(cmbObsList);
		cmbEnd.setItems(cmbObsList);
	}

	// �뺰 �����Ȳ �����ִ� ����Ʈ
	public void loadRevList(LocalDate date) {
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		ArrayList<Schedule> arrayList = scheduleDAO.getRevList(date, selectedRoomName);

		if (arrayList == null) {
			return;
		} else {
			sObsList.clear();
		}

		for (int i = 0; i < arrayList.size(); ++i) {
			Schedule s = arrayList.get(i);

			// ȭ�鿡 ���õ� �÷��������ӿ�ũ�̹Ƿ� �̰��� ����
			sObsList.add(s);
		}
	}

	// �����̴� ����
	private void setSliderSize(int personNum) {
		sdrPersonNum.setMax(personNum);
	}

	// �ߺ� ���� ����
	private void blockOverlapReservation() {

	}

}

// �޺��ڽ� �ð� Disable
//class StartHoursCell extends ListCell<Integer> {
//
//	StartHoursCell() {
//		DBUtil.userCon.cmbEnd.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
//
//	}
//
//	@Override
//	protected void updateItem(Integer hours, boolean empty) {
//		super.updateItem(hours, empty);
//		if (empty) {
//			setText(null);
//		} else {
//			setText(hours.toString());
//			updateDisableState();
//		}
//	}
//
//	private void updateDisableState() {
//		boolean disable = getItem() != null && DBUtil.userCon.cmbEnd.getValue() != null
//				&& getItem().intValue() >= Integer.valueOf(DBUtil.userCon.cmbEnd.getValue());
//		setDisable(disable);
//		setOpacity(disable ? 0.5 : 1);
//	}
//}

//�޺��ڽ� �ð� Disable
class EndHoursCell extends ListCell<Integer> {

	EndHoursCell() {
		DBUtil.userCon.cmbStart.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
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