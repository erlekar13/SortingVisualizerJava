import javax.swing.*;                     // For GUI components
import java.awt.*;                        // For drawing and colors
import java.util.Random;                  // For generating random arrays

public class SortingVisualizer extends JPanel {

    private int[] array;                  // Array to be sorted and visualized

    // Constructor initializes array and background color
    public SortingVisualizer() {
        array = generateRandomArray(70);  // Generate array with 70 random elements
        setBackground(Color.BLACK);       // Set background color of panel
    }

    // Generates a random array with values between 50 to 450
    private int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++)
            arr[i] = rand.nextInt(400) + 50;
        return arr;
    }

    // Draws the bars representing array elements
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / array.length;  // Width of each bar
        for (int i = 0; i < array.length; i++) {
            g.setColor(Color.CYAN);  // Bar color
            g.fillRect(i * width, getHeight() - array[i], width - 2, array[i]);  // Draw bar
        }
    }

    // Shuffle and regenerate array
    public void shuffleArray() {
        array = generateRandomArray(array.length);  // Replace with new random array
        repaint();                                   // Refresh display
    }

    // ------------ Sorting Algorithms ------------

    // Bubble Sort with animation
    public void bubbleSort() {
        new Thread(() -> {
            try {
                for (int i = 0; i < array.length - 1; i++) {
                    for (int j = 0; j < array.length - i - 1; j++) {
                        if (array[j] > array[j + 1]) {
                            // Swap elements
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                        }
                        repaint();             // Refresh visualization
                        Thread.sleep(10);      // Slow down for animation
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();  // Start sorting in a separate thread
    }

    // Insertion Sort with animation
    public void insertionSort() {
        new Thread(() -> {
            try {
                for (int i = 1; i < array.length; i++) {
                    int key = array[i];            // Current value to insert
                    int j = i - 1;
                    while (j >= 0 && array[j] > key) {
                        array[j + 1] = array[j];    // Shift element right
                        j--;
                        repaint();
                        Thread.sleep(10);
                    }
                    array[j + 1] = key;             // Insert key
                    repaint();
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Merge Sort (recursive)
    public void mergeSort() {
        new Thread(() -> mergeSortHelper(0, array.length - 1)).start();
    }

    // Helper for recursive merge sort
    private void mergeSortHelper(int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSortHelper(left, mid);         // Sort left half
        mergeSortHelper(mid + 1, right);    // Sort right half
        merge(left, mid, right);            // Merge both halves
    }

    // Merge two sorted halves
    private void merge(int left, int mid, int right) {
        int[] temp = new int[right - left + 1];  // Temp array
        int i = left, j = mid + 1, k = 0;

        // Merge values from both halves
        while (i <= mid && j <= right)
            temp[k++] = array[i] <= array[j] ? array[i++] : array[j++];

        // Copy remaining elements
        while (i <= mid) temp[k++] = array[i++];
        while (j <= right) temp[k++] = array[j++];

        // Copy back to original array
        for (int l = 0; l < temp.length; l++) {
            array[left + l] = temp[l];
            repaint();               // Visualize merge
            try { Thread.sleep(10); } catch (InterruptedException e) {}
        }
    }

    // Quick Sort (recursive)
    public void quickSort() {
        new Thread(() -> quickSortHelper(0, array.length - 1)).start();
    }

    // Recursive helper for quick sort
    private void quickSortHelper(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);       // Partition index
            quickSortHelper(low, pi - 1);        // Sort left part
            quickSortHelper(pi + 1, high);       // Sort right part
        }
    }

    // Partition logic for quick sort
    private int partition(int low, int high) {
        int pivot = array[high];                 // Choose pivot
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i]; array[i] = array[j]; array[j] = temp;
                repaint();
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
        }
        int temp = array[i + 1]; array[i + 1] = array[high]; array[high] = temp;
        repaint();
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        return i + 1;
    }

    // Heap Sort with animation
    public void heapSort() {
        new Thread(() -> {
            int n = array.length;

            // Build max heap
            for (int i = n / 2 - 1; i >= 0; i--)
                heapify(n, i);

            // Extract elements one by one
            for (int i = n - 1; i > 0; i--) {
                int temp = array[0]; array[0] = array[i]; array[i] = temp;
                repaint();
                try { Thread.sleep(10); } catch (InterruptedException e) {}
                heapify(i, 0);
            }
        }).start();
    }

    // Heapify subtree
    private void heapify(int n, int i) {
        int largest = i;            // Initialize largest as root
        int l = 2 * i + 1;          // Left child
        int r = 2 * i + 2;          // Right child

        if (l < n && array[l] > array[largest]) largest = l;
        if (r < n && array[r] > array[largest]) largest = r;

        if (largest != i) {
            int temp = array[i]; array[i] = array[largest]; array[largest] = temp;
            repaint();
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            heapify(n, largest);   // Recursively heapify the affected subtree
        }
    }

    // ------------ Main UI Code ------------

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");           // Create window
        SortingVisualizer visualizer = new SortingVisualizer();    // Our panel

        // Dropdown menu for selecting sorting algorithm
        String[] algorithms = {"Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Heap Sort"};
        JComboBox<String> algoSelector = new JComboBox<>(algorithms);

        // Button to start selected algorithm
        JButton sortBtn = new JButton("Start");
        sortBtn.addActionListener(e -> {
            String selected = (String) algoSelector.getSelectedItem();
            switch (selected) {
                case "Bubble Sort": visualizer.bubbleSort(); break;
                case "Insertion Sort": visualizer.insertionSort(); break;
                case "Merge Sort": visualizer.mergeSort(); break;
                case "Quick Sort": visualizer.quickSort(); break;
                case "Heap Sort": visualizer.heapSort(); break;
            }
        });

        // Button to shuffle array
        JButton shuffleBtn = new JButton("Shuffle");
        shuffleBtn.addActionListener(e -> visualizer.shuffleArray());

        // Add controls to a panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(algoSelector);
        controlPanel.add(sortBtn);
        controlPanel.add(shuffleBtn);

        // Set up the frame layout
        frame.setLayout(new BorderLayout());
        frame.add(visualizer, BorderLayout.CENTER);       // Drawing panel
        frame.add(controlPanel, BorderLayout.SOUTH);       // Buttons
        frame.setSize(800, 600);                           // Window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);                            // Show window
    }
}
