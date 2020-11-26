package com.client.domain;

import com.client.view.SortView;
import com.google.gwt.user.client.ui.Button;

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
            if (index > 30) new WarningBox();
            else {
                SortView newSortView = new SortView(index);
                newSortView.renderTable();
            }
        });
    }
}