package com.client.view;

import com.client.domain.StringUtils;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class InputView {
    final Label question = new Label("How many numbers to display?");
    final TextBox inputField = new TextBox();
    final Label errorLabel = new Label();
    final Button sendButton = new Button("Enter");

    public InputView() {
        inputField.removeStyleName(inputField.getStyleName());
        errorLabel.removeStyleName(errorLabel.getStyleName());
        sendButton.removeStyleName(sendButton.getStyleName());
        inputField.addStyleName("textField");
        errorLabel.addStyleName("errorMessage");
        sendButton.addStyleName("button");
    }

    public void render() {
        RootPanel.get("questionContainer").add(question);
        RootPanel.get("inputFieldContainer").add(inputField);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("sendButtonContainer").add(sendButton);
        inputField.setFocus(true);

        sendButton.addClickHandler(new InputHandler());
        inputField.addKeyUpHandler(new InputHandler());
    }

    private class InputHandler implements ClickHandler, KeyUpHandler {
        public void onClick(ClickEvent event) {
            if (isValid()) submit();
        }

        public void onKeyUp(KeyUpEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && isValid()) {
                submit();
            }
        }

        private void submit() {
            sendButton.setEnabled(false);
            int value = Integer.parseInt(inputField.getValue());
            if (value > SortView.ROWS * SortView.COLUMNS) {
                errorLabel.setText("");
                errorLabel.setText("Please enter a number " + SortView.ROWS * SortView.COLUMNS + " or less");
            } else {
                clear();
                SortView sortView = new SortView(value);
                sortView.renderTable();
            }
        }

        private boolean isValid() {
            errorLabel.setText("");
            String inputFieldText = inputField.getText();
            if (!StringUtils.isNotEmpty(inputFieldText)) {
                errorLabel.setText("Please enter at least one character");
                return false;
            } else if (!StringUtils.isMoreThanNull(inputFieldText)) {
                errorLabel.setText("Please enter just a number more than 0");
                return false;
            }
            return true;
        }

        private void clear() {
            RootPanel.get("questionContainer").clear();
            RootPanel.get("inputFieldContainer").clear();
            RootPanel.get("errorLabelContainer").clear();
            RootPanel.get("sendButtonContainer").clear();
        }
    }
}