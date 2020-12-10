package com.client;

import com.client.view.InputView;
import com.google.gwt.core.client.EntryPoint;

/*
* Entry point.
* The class creates an UI with inputting numbers
*/
public class AppModule implements EntryPoint {
    public void onModuleLoad() {

        InputView inputView = new InputView();
        inputView.render();

    }
}
