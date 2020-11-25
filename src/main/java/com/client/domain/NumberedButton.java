package com.client.domain;

import com.client.view.SortView;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NumberedButton extends Button {

    /**
     * Конструктор принимает число,
     * которое устанавливается как значение и как имя кнопки.
     */
    public NumberedButton(int index) {
        super("" + index);
        this.removeStyleName(getStyleName());
        this.addStyleName("button");
        this.addClickHandler(event -> {
            if (index > 30) {
                final DialogBox dialogBox = new DialogBox();
                dialogBox.removeStyleName(dialogBox.getStyleName());
                dialogBox.setStyleName("errorBox");
                dialogBox.setAnimationEnabled(true);
                final Button closeButton = new Button("OK");
                closeButton.removeStyleName(closeButton.getStyleName());
                closeButton.addStyleName("button");
                VerticalPanel dialogVPanel = new VerticalPanel();
                dialogVPanel.add(new Label("Please choose the number 30 or less"));
                dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
                dialogVPanel.add(closeButton);
                dialogBox.setWidget(dialogVPanel);
                closeButton.setFocus(true);
                dialogBox.center();
                closeButton.addClickHandler(event1 -> {
                    dialogBox.hide();
                    closeButton.setEnabled(true);
                    closeButton.setFocus(true);
                });
            } else {
                SortView newSortView = new SortView(index);
                newSortView.renderTable();
            }
        });
    }
}
