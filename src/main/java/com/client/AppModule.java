package com.client;

import com.google.gwt.core.client.EntryPoint;

/*
* Entry point.
* The class creates an UI with inputting numbers
*/
public class AppModule implements EntryPoint {
    public void onModuleLoad() {
        View inputView = new View();
        inputView.initializeInputView();
        inputView.renderInputView();
    }
}
