package com.client;

import com.client.view.InputView;
import com.google.gwt.core.client.EntryPoint;

public class AppModule implements EntryPoint {
    public void onModuleLoad() {

        InputView inputView = new InputView();
        inputView.render();

    }
}