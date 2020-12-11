package com.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point for GWT app.
 * 
 * The class creates a UI that prompts for a number.
 * The app accepts numbers from 1 to 100 (limitation on the comfortable display on the screen).
 *
 * After entering a suitable value, the application generates the specified number of random
 * numbered buttons, among which there must be a number <= 30.
 * Pressing the number button creates a new array of numbers in the selected quantity,
 * provided that it is <= 30.
 *
 * In addition to the grid with numbers, there are two control buttons - SORT and RESET.
 * When using the SORT button, the array of buttons is sorted in descending order,
 * using the QuickSort algorithm, displays each step of elements move.
 * If the array is already sorted, then the sort is performed in reverse order,
 * also displays each step of elements move.
 *
 * Pressing the RESET button returns to the input screen for entering the number
*/
public class AppModule implements EntryPoint {
    public void onModuleLoad() {
        InputAndSortView inputView = new InputAndSortView();
        inputView.initializeInputView();
        inputView.renderInputView();
    }
}
