package controller;

import java.time.*;

import javafx.scene.*;
import javafx.scene.layout.*;


public class AnchorPaneNode extends AnchorPane {

	// Date associated with this pane
	private LocalDate date;
	
    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        
        this.setOnMouseClicked(e -> {
        	// 눌리는 날짜 전달 
        	DBUtil.userCon.loadRevList(date);
        });	
        
    }

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
