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
 * Класс генерирует интерфейс и логику страницы сортировки
 * Страница не может быть сгенерирована с 0 (или вообще без чисел),
 * поэтому создан конструктор, который принимает значение кол-ва генерящихся кнопок
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
     * Конструктор принимает значение числа, на основании
     * которого будет генерироваться массив случайных чисел.
     */
    public SortView(int value) {
        arrLength = Math.min(value, ROWS * COLUMNS);
        sortButton.addClickHandler(new SortHandler());
        resetButton.addClickHandler(event -> {
            clearTable();
            clearButtons();
            InputView inputView = new InputView();
            inputView.render();
            enableButtons();
        });
        sortButton.removeStyleName(sortButton.getStyleName());
        resetButton.removeStyleName(resetButton.getStyleName());
        sortButton.addStyleName("sort-reset_button");
        resetButton.addStyleName("sort-reset_button");
    }

    /**
     * Класс-обработчик нажатия на кнопку Sort
     * По клику должен определять, отсортирован-ли массив
     * <p>
     * Если массив НЕ ОТСОРТИРОВАН, то сортировать его в порядке убывания
     * Если массив ОТСОРТИРОВАН, то должен разворачивать его.
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
     * Метод проверяет, сгенерирован-ли массив и выводит виджет
     * таблицы, в которой ячейки представлены кнопками со случайно сгенерированными числами
     * <p>
     * Если массива ещё нет, то он вызывает метод генерирования
     * массива generateArr().
     * <p>
     * После этого метод создаёт на основе массива виджет таблицы,
     * используя createGrid(),
     * после чего обновляет отображение таблицы методом refreshTable().
     */
    public void renderTable() {
        if (arr == null) generateArr();
        createGrid();
        refreshTable();
    }

    /**
     * Метод генерирует массив на основании внутренней
     * переменной arrLength.
     * Перед тем, как вызывать этот метод, нужно изменить переменную.
     * <p>
     * Метод генерирует числа в диапазоне от 1 до 1000 (оба включительно)
     * <p>
     * В процессе генерации метод проверяет, было ли сгенерировано число < 30
     * и соответственно меняет переменную wasThereALimitNumber.
     * <p>
     * Если в конце цикла генерации переменная wasThereALimitNumber будет false,
     * то метод выбирает случайный индекс массива и генерит число от 1 до 30.
     * Если переменная true, то дополнительные действия не выполняются.
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
     * Данный метод из внутреннего массива генерирует таблицу кнопок
     */
    private void createGrid() {
        int arrElem = 0; // подсчёт к-ва чисел, чтоб не выйти за предел массива
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
     * Метод выводит актуальный вариант виджета таблицы.
     * <p>
     * Сначала очищает экран от таблицы,
     * используя метод clearTable(), а затем добавляет её снова.
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