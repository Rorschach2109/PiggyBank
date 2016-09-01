package com.piggybank.app.layout;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public abstract class AbstractPiggyCell<T> extends ListCell<T> {

	protected final GridPane cellGrid;
	
	{
		this.cellGrid = new GridPane();
	}
	
	public AbstractPiggyCell(double width, double height) {
		configureGrid(width, height);
	}
	
	protected abstract void addItem(T item);
	
	@Override
	public final void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		
		if (null != item) {
			addContent(item);
		} else {
			clearContent();
		}
	}
	
	private void addContent(T item) {
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		
		addItem(item);
		
		setGraphic(this.cellGrid);
	}
	
	private final void configureGrid(double width, double height) {
		this.cellGrid.setHgap(3);
		this.cellGrid.setVgap(3);
		this.cellGrid.setPadding(new Insets(1, 5, 1, 5));
		this.cellGrid.setMaxSize(width, height);
	}

	private void clearContent() {
		setGraphic(null);
		setText(null);
	}
}
