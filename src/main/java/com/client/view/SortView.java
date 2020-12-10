package com.client.view;

import com.client.domain.NumberedButton;
import com.client.domain.Pair;
import com.client.domain.QuickSort;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Iterator;
import java.util.List;

/**
 * The class generates the UI and logic of the sort page
 * The page cannot be generated from 0 (or no numbers at all),
 * therefore, a constructor has been created that takes the value of the number of generated buttons
 */
public class SortView {
    private final int arrLength;
    public static final int ROWS = 10, COLUMNS = 10;
    private int[] arr;
    private final Grid grid = new Grid(ROWS, COLUMNS);
    private final Button sortButton = new Button("Sort");
    private final Button resetButton = new Button("Reset");
    private boolean isSorted;

    /**
     * The constructor takes the value of a number based on
     * which will generate an array of random numbers.
     */
    public SortView(int value) {
        arrLength = Math.min(value, ROWS * COLUMNS);
        sortButton.addClickHandler(new SortHandler());
        resetButton.addClickHandler(event -> {
            clearTable();
            clearButtons();
            InputView inputView = new InputView();
            inputView.render();
        });
        sortButton.removeStyleName(sortButton.getStyleName());
        resetButton.removeStyleName(resetButton.getStyleName());
        sortButton.addStyleName("sort-reset_button");
        resetButton.addStyleName("sort-reset_button");
        enableButtons();
    }

    /**
     * Sort button click handler class
     * On click should determine if the array is sorted
     * <p>
     * If the array is NOT SORTED, then sort it in descending order
     * If the array is SORTED, then it should expand it.
     */
    class SortHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            SortTimer s_timer = new SortTimer();
            ReverseTimer r_timer = new ReverseTimer();
            disableButtons();
            if (!isSorted) {
                    s_timer.scheduleRepeating(400);
            } else {
                   r_timer.scheduleRepeating(400);
            }
        }

        private class SortTimer extends Timer {
            private final QuickSort quickSort = new QuickSort(arr);
            private final List<Pair> list = quickSort.getPairs();
            private final Iterator<Pair> iterator = list.iterator();

            @Override
            public void run() {
                if (iterator.hasNext()) {
                    Pair p = iterator.next();
                    int tmp = arr[p.getFirst()];
                    arr[p.getFirst()] = arr[p.getLast()];
                    arr[p.getLast()] = tmp;
                    renderTable();
                } else {
                    isSorted = true;
                    enableButtons();
                    cancel();
                }
            }
        }

        private class ReverseTimer extends Timer {
            private int begin = 0;
            private int end = arr.length - 1;

            @Override
            public void run() {
                reverseStep();
                renderTable();
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
        }
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
    public void renderTable() {
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
                NumberedButton numberedButton = new NumberedButton(arr[arrElem]);
                numberedButton.setEnabled(true);
                numberedButton.addStyleName("button");
                grid.setWidget(row, col, numberedButton);
                arrElem++;
            }
        }
    }

    /**
     * The method displays the current version of the table widget.
     * <p>
     * First clears the screen from the table,
     * using the clearTable () method and then adding it again.
     */
    private void refreshTable() {
        clearTable();
        RootPanel.get("tableContainer").add(grid);
    }

    private void enableButtons() {
        clearButtons();
        sortButton.setEnabled(true);
        resetButton.setEnabled(true);
        RootPanel.get("sortButtonContainer").add(sortButton);
        RootPanel.get("resetButtonContainer").add(resetButton);
    }

    private void disableButtons() {
        clearButtons();
        sortButton.setEnabled(false);
        resetButton.setEnabled(false);
        RootPanel.get("sortButtonContainer").add(sortButton);
        RootPanel.get("resetButtonContainer").add(resetButton);
    }

    private static void clearTable() {
        RootPanel.get("tableContainer").clear();
    }

    private static void clearButtons() {
        RootPanel.get("sortButtonContainer").clear();
        RootPanel.get("resetButtonContainer").clear();
    }
}
