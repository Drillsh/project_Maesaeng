package controller;

import java.time.*;

import javafx.scene.*;
import javafx.scene.layout.*;

/**
 * Create an anchor pane that can store additional data.
 */

public class AnchorPaneNode extends AnchorPane {

	// Date associated with this pane
	private LocalDate date;
	/**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        
        this.setOnMouseClicked(e -> {
        	
        	
        	System.out.println("This pane's date is: " + date);
        	
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
