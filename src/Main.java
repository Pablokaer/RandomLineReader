import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    //test
    private static final List<String> sortedLines = new ArrayList<>();
    private static String lastPhrase = null;
    private static int sortedCount = 0;
    private static int totalLines = 0;
    private static List<String> lines = new ArrayList<>();
    private static JProgressBar progressBar;
    private static JLabel currentPhraseLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Sorted Lines Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new java.awt.BorderLayout(10, 10));

        currentPhraseLabel = new JLabel("Loading phrases...", SwingConstants.CENTER);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JButton viewSortedLinesButton = new JButton("View Sorted Lines");
        viewSortedLinesButton.addActionListener(e -> showSortedLinesDialog());

        new Thread(Main::processLines).start();

        frame.add(currentPhraseLabel, java.awt.BorderLayout.CENTER);
        frame.add(progressBar, java.awt.BorderLayout.NORTH);
        frame.add(viewSortedLinesButton, java.awt.BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void processLines() {
        String filePath = "phrases.txt";
        int intervalInSeconds = 2;

        try {
            Path path = Paths.get(filePath);
            lines = Files.readAllLines(path);

            if (!lines.isEmpty()) {
                totalLines = lines.size();
                Set<String> processedLines = new HashSet<>();
                Random random = new Random();

                while (sortedCount < totalLines) {
                    String randomLine;

                    do {
                        randomLine = lines.get(random.nextInt(lines.size()));
                    } while (randomLine.equals(lastPhrase));

                    lastPhrase = randomLine;

                    if (!processedLines.contains(randomLine)) {
                        processedLines.add(randomLine);
                        sortedLines.add(randomLine);
                        sortedCount++;
                    }

                    double percentage = calculatePercentage(sortedCount, totalLines);
                    updateGui(randomLine, percentage);

                    Thread.sleep(intervalInSeconds * 1000);
                }

                updateGui("All lines have been sorted!", 100);
            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "The file is empty.", "Error", JOptionPane.ERROR_MESSAGE));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error reading the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
        } catch (InterruptedException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Sorting interrupted: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
        }
    }

    private static void updateGui(String currentPhrase, double percentage) {
        SwingUtilities.invokeLater(() -> {
            currentPhraseLabel.setText(currentPhrase);
            progressBar.setValue((int) percentage);
            progressBar.setString(String.format("%.2f%% Complete", percentage));
        });
    }

    private static double calculatePercentage(int sortedLines, int totalLines) {
        if (totalLines == 0) return 0;
        return (double) sortedLines / totalLines * 100;
    }

    private static void showSortedLinesDialog() {
        JTextArea textArea = new JTextArea(15, 30);
        textArea.setEditable(false);

        synchronized (sortedLines) {
            for (String line : sortedLines) {
                textArea.append(line + "\n");
            }
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, "Sorted Lines", JOptionPane.INFORMATION_MESSAGE);
    }
}