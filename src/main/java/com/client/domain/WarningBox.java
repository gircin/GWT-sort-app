package com.client.domain;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WarningBox extends DialogBox {
    public WarningBox() {
        super();
        removeStyleName(this.getStyleName());
        setStyleName("warningBox");
        setAnimationEnabled(true);
        final Button closeButton = new Button("OK");
        closeButton.removeStyleName(closeButton.getStyleName());
        closeButton.addStyleName("button");
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.add(new Label("Please choose the number 30 or less"));
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        dialogVPanel.add(closeButton);
        setWidget(dialogVPanel);
        center();
        closeButton.setFocus(true);
        closeButton.addClickHandler(event1 -> {
            this.hide();
            closeButton.setEnabled(true);
            closeButton.setFocus(true);
        });
    }

    @Override
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        super.onPreviewNativeEvent(event);
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) hide();
    }
}