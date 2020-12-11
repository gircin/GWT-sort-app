package com.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class contains an interface of two screens - Input View and Sort View
 *
 * The main methods for the <strong>Input View</strong> are initializeInputView() and renderInputView()
 * The initializeInputView() initializes the variables of the widget elements,
 * and the renderInputView()displays them on the screen, adding handlers for pressing the button
 *
 * The main methods for the <strong>Sort View</strong> are initializeSortView() and renderSortView()
 * The initializeSortView() initializes the variables of the widget elements,
 * and the renderSortView()displays them on the screen, adding handlers for pressing the button
 */
public class InputAndSortView {
    /**
     * The variables of Input View
     */
    final Label question = new Label("How many numbers to display?");
    final TextBox inputField = new TextBox();
    final Label errorLabel = new Label();
    final Button sendButton = new Button("Enter");

    // This method initializes and assigns styles to widgets
    public void initializeInputView() {
        inputField.removeStyleName(inputField.getStyleName());
        errorLabel.removeStyleName(errorLabel.getStyleName());
        sendButton.removeStyleName(sendButton.getStyleName());
        inputField.addStyleName("textField");
        errorLabel.addStyleName("errorMessage");
        sendButton.addStyleName("button");
    }

    /**
     * The method renders Input View widgets to the RootPanel and
     * adds handlers for ENTER button.
     * By pressing the button, the entered text is validate
     * If the requirements are met, the Sort View call is initialized.
     * If the requirements are violated, an error message is issued
     */
    public void renderInputView() {
        RootPanel.get("questionContainer").add(question);
        RootPanel.get("inputFieldContainer").add(inputField);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("sendButtonContainer").add(sendButton);
        inputField.setFocus(true);
        sendButton.addClickHandler(event -> {
            if (isValid()) submit();
        });
        inputField.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && isValid()) submit();
        });
    }

    /**
     * The method confirms the correctness of the entered data,
     * calls Sort View and passes the entered value into it
     */
    private void submit() {
        sendButton.setEnabled(false);
        int value = Integer.parseInt(inputField.getValue());
        if (value > ROWS * COLUMNS) {
            errorLabel.setText("");
            errorLabel.setText("Please enter a number " + ROWS * COLUMNS + " or less");
        } else {
            clearInputView();
            this.initialiseSortView(value);
            this.renderSortView();
        }
    }

    /** This method checks that the entered data meets the requirements:
     * - there must be no empty line or line containing only spaces;
     * - there should be no characters other than numbers;
     * - the entered number must be in the range from 1 to 100
     */
    private boolean isValid() {
        errorLabel.setText("");
        String inputFieldText = inputField.getText();
        if (!isNotEmpty(inputFieldText)) {
            errorLabel.setText("Please enter at least one character");
            return false;
        } else if (!isMoreThanNull(inputFieldText)) {
            errorLabel.setText("Please enter just a number more than 0");
            return false;
        }
        return true;
    }

    // Clearing RootPanel from input widgets before refresh or calling Sort View
    private void clearInputView() {
        RootPanel.get("questionContainer").clear();
        RootPanel.get("inputFieldContainer").clear();
        RootPanel.get("errorLabelContainer").clear();
        RootPanel.get("sendButtonContainer").clear();
    }

    // Static method to help checking input string in IsValid() method
    public static boolean isNotEmpty(String name) { return name != null && !name.trim().isEmpty(); }
    public static boolean isMoreThanNull(String name) {
        if (name.matches("[0-9]+")) return Integer.parseInt(name) > 0;
        return false;
    }

    /**
     * The variables of Sort View
     */
    private int arrLength;
    public static final int ROWS = 10, COLUMNS = 10;
    private int[] arr;
    private final Grid grid = new Grid(ROWS, COLUMNS);
    private final Button sortButton = new Button("Sort");
    private final Button resetButton = new Button("Reset");
    private boolean isSorted;

    /**
     * The method takes the value of a number based on
     * which will generate an array of random numbers.
     */
    public void initialiseSortView(int value) {
        arrLength = Math.min(value, ROWS * COLUMNS);
        /*
         * Sort button click handler
         * On click should determine if the array is sorted
         * <p>
         * If the array is NOT SORTED, then sort it in descending order
         * If the array is SORTED, then it should expand it.
         */
        sortButton.addClickHandler(event -> {
            Timer s_timer = new Timer() {
                /*
                 * This class encapsulates the sorting process
                 * passed array by QuickSort method and
                 * collecting data on the sequence of movement of the elements of this array.
                 * Data about the index of the elements that are moved
                 * are stored in an object of the Pair class.
                 * Created Pair instance are stored in the ArrayList and are available for subsequent iteration
                 * The original array from the SortView class is not overwritten until all elements are sorted
                 * Instead, the original array is passed to the Sort object and a copy is made from it in
                 * internal array using the fast System.arraycopy () method.
                 * <p>
                 * Immediately after object initialization, the array passed to it is copied and the copy is sorted
                 */
                class QuickSort {
                    private final int[] arr;
                    private final List<Pair> pairs = new LinkedList<>();
                    class Pair {
                        private final int first;
                        private final int last;
                        Pair(int first, int last) {
                            this.first = first;
                            this.last = last;
                        }
                        public int getFirst() {
                            return first;
                        }
                        public int getLast() {
                            return last;
                        }
                    }

                    public QuickSort(int[] arr) {
                        this.arr = new int[arr.length];
                        System.arraycopy(arr, 0, this.arr, 0, arr.length);
                        sort(0, this.arr.length - 1);
                    }

                    /*
                     * Simple implementation of the QuickSort algorithm using recursion.
                     * Source code from
                     * https://github.com/eugenp/tutorials/blob/e0a581b6e1b482db3593acf7331d3468f553259d/algorithms-sorting/src/main/java/com/baeldung/algorithms/quicksort/ThreeWayQuickSort.java
                     * Efficiency is about 3 times lower than that of the DualPivotQuicksort.sort () code
                     */
                    private void sort(int begin, int end) {
                        if (end <= begin) return;
                        int i = begin;
                        int less = begin;
                        int greater = end;
                        while (i <= greater) {
                            if (arr[i] > arr[less]) {
                                int tmp = arr[i];
                                arr[i] = arr[less];
                                arr[less] = tmp;
                                pairs.add(new Pair(i, less));
                                i++;
                                less++;
                            } else if (arr[less] > arr[i]) {
                                int tmp = arr[i];
                                arr[i] = arr[greater];
                                arr[greater] = tmp;
                                pairs.add(new Pair(i, greater));
                                greater--;
                            } else {
                                i++;
                            }
                        }
                        sort(begin, less - 1);
                        sort(greater + 1, end);
                    }

                    /*
                     * The method returns a list of Pair objects,
                     * which are the history of array changes
                     */
                    public List<Pair> getPairs() {
                        return pairs;
                    }

                }
                private final QuickSort quickSort = new QuickSort(arr);
                private final List<QuickSort.Pair> list = quickSort.getPairs();
                private final Iterator<QuickSort.Pair> iterator = list.iterator();

                // This method starts a changes of main array step by step
                @Override
                public void run() {
                    if (iterator.hasNext()) {
                        QuickSort.Pair p = iterator.next();
                        int tmp = arr[p.getFirst()];
                        arr[p.getFirst()] = arr[p.getLast()];
                        arr[p.getLast()] = tmp;
                        renderSortView();
                    } else {
                        isSorted = true;
                        enableButtons();
                        quickSort = null;
                        cancel();
                    }
                }
            };
            Timer r_timer = new Timer() {
                private int begin = 0;
                private int end = arr.length - 1;

                @Override
                public void run() {
                    reverseStep();
                    renderSortView();
                }

                private void reverseStep() {
                    if (begin >= end) {
                        enableButtons();
                        cancel();
                        return;
                    }
                    int tmp = arr[end];
                    arr[end] = arr[begin];
                    arr[begin] = tmp;
                    begin++;
                    end--;
                }
            };
            disableButtons();
            if (!isSorted) {
                s_timer.scheduleRepeating(400);
            } else {
                r_timer.scheduleRepeating(400);
            }
        });
        /*
         * Reset button click handler
         * On click should return in input screen
         */
        resetButton.addClickHandler(event -> {
            clearTable();
            clearButtons();
            arr = null;
            grid.clear();
            this.initializeInputView();
            this.renderInputView();
        });
        sortButton.removeStyleName(sortButton.getStyleName());
        resetButton.removeStyleName(resetButton.getStyleName());
        sortButton.addStyleName("sort-reset_button");
        resetButton.addStyleName("sort-reset_button");
        enableButtons();
    }

    /**
     * The method checks if the array has been generated and displays the widget
     * a table in which cells are represented by buttons with randomly generated numbers
     * <p>
     * If the array does not yet exist, then it calls the generation method
     * array generateArr().
     * <p>
     * After that, the method creates a table widget based on the array,
     * using createGrid(),
     * then refreshes the display of the table using the refreshTable() method.
     */
    public void renderSortView() {
        if (arr == null) generateArr();
        createGrid();
        refreshTable();
    }

    /**
     * The method generates an array based on the internal
     * variable arrLength.
     * Before calling this method, you need to change the variable.
     * <p>
     * The method generates numbers in the range from 1 to 1000 (both inclusive)
     * <p>
     * In the process of generation, the method checks if a number <30 was generated
     * and changes the wasThereALimitNumber variable accordingly.
     * <p>
     * If at the end of the generation cycle the variable wasThereALimitNumber is false,
     * then the method chooses a random array index and generates a number between 1 and 30.
     * If the variable is true, then no additional actions are performed.
     */
    private void generateArr() {
        boolean wasThereALimitNumber = false;
        int generatedNum;
        arr = new int[arrLength];
        for (int i = 0; i < arrLength; i++) {
            generatedNum = arr[i] = (int) ((Math.random() * 1000) + 1); // [1 ; 1000]
            if (generatedNum <= 30) {
                wasThereALimitNumber = true;
            }
        }
        if (!wasThereALimitNumber) {
            int arrIndex = (int) (Math.random() * arrLength); // [0 ; arrLength)
            arr[arrIndex] = (int) ((Math.random() * 30) + 1); // [1 ; 30]
        }
        isSorted = false;
    }

    /**
     * This method generates a grid of buttons from an internal array
     */
    private void createGrid() {
        int arrElem = 0; // counting the number of numbers so not get ArrayIndexOutOfBoundsException
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (arrElem == arrLength) break;
                int finalArrElem = arrElem;
                Button numberedButton = new Button("" + arr[finalArrElem]) {
                    final int index = arr[finalArrElem];

                    {
                        this.removeStyleName(getStyleName());
                        this.addStyleName("button");
                        this.addClickHandler(event -> {
                            if (index > 30) new DialogBox() {
                                {
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
                            };
                            else {
                                InputAndSortView newSortView = new InputAndSortView();
                                newSortView.initialiseSortView(index);
                                newSortView.renderSortView();
                            }
                        });
                    }
                };
                numberedButton.setEnabled(true);
                numberedButton.addStyleName("button");
                grid.setWidget(row, col, numberedButton);
                arrElem++;
            }
        }
    }

    /**
     * The method displays the current version of the table widget.
     * First clears the screen from the table,
     * using the clearTable () method and then adding it again.
     */
    private void refreshTable() {
        clearTable();
        RootPanel.get("tableContainer").add(grid);
    }

    // Makes the SORT and RESET buttons available after sorting
    private void enableButtons() {
        clearButtons();
        sortButton.setEnabled(true);
        resetButton.setEnabled(true);
        RootPanel.get("sortButtonContainer").add(sortButton);
        RootPanel.get("resetButtonContainer").add(resetButton);
    }

    // Makes the SORT and RESET buttons NOT available when sorting
    private void disableButtons() {
        clearButtons();
        sortButton.setEnabled(false);
        resetButton.setEnabled(false);
        RootPanel.get("sortButtonContainer").add(sortButton);
        RootPanel.get("resetButtonContainer").add(resetButton);
    }

    // Clearing RootPanel from buttons grid before refresh or calling Input View
    private static void clearTable() { RootPanel.get("tableContainer").clear(); }

    // Clearing RootPanel from SORT and RESET buttons before refresh or calling Input View
    private static void clearButtons() {
        RootPanel.get("sortButtonContainer").clear();
        RootPanel.get("resetButtonContainer").clear();
    }

}