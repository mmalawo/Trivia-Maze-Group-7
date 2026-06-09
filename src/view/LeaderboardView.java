package view;

import model.LeaderboardDAO;
import model.LeaderboardEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * LeaderboardView displays the top scores in a dialog window.
 * After viewing, the player can return to the main menu.
 */
public class LeaderboardView {

    /**
     * Displays the leaderboard in a modal dialog.
     * After closing, navigates back to the main menu.
     */
    public static void showLeaderboard() {
        LeaderboardDAO dao = new LeaderboardDAO();
        List<LeaderboardEntry> scores = dao.getTopScores();

        String[] columns = {"Rank", "Player Name", "Time", "Correct", "Incorrect"};
        Object[][] data = new Object[scores.size()][5];

        for (int i = 0; i < scores.size(); i++) {
            LeaderboardEntry entry = scores.get(i);
            data[i][0] = i + 1;
            data[i][1] = entry.getPlayerName();
            data[i][2] = formatTime(entry.getTimeSeconds());
            data[i][3] = entry.getCorrectScore();
            data[i][4] = entry.getIncorrectScore();
        }

        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setDefaultEditor(Object.class, null);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(table);

        // Main Menu button
        JButton mainMenuButton = new JButton("Main Menu");

        JDialog dialog = new JDialog(MainGUI.getWindow(), "Leaderboard", true);
        dialog.setSize(600, 450);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(mainMenuButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(MainGUI.getWindow());

        // When Main Menu is clicked, close dialog and go to main menu
        mainMenuButton.addActionListener(e -> {
            dialog.dispose();
            MainGUI.startNewGame();
            MainGUI.switchView(MainGUI.menuView);
        });

        // If player closes dialog via X button, also go to main menu
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                MainGUI.startNewGame();
                MainGUI.switchView(MainGUI.menuView);
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Formats a time in seconds into a human-readable string.
     *
     * @param theTimeSeconds the time in seconds
     * @return a formatted string like "2 min 05 sec"
     */
    private static String formatTime(double theTimeSeconds) {
        int totalSeconds = (int) Math.round(theTimeSeconds);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d min %02d sec", minutes, seconds);
    }
}