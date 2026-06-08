package view;

import model.LeaderboardDAO;
import model.LeaderboardEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardView {
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

// Removes the default editor so cells cannot enter edit mode.
        table.setDefaultEditor(Object.class, null);

// Prevents the white selected-cell / focused-cell box from appearing.
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog(MainGUI.window, "Leaderboard", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(MainGUI.window);
        dialog.setVisible(true);
    }

    private static String formatTime(double timeSeconds) {
        int totalSeconds = (int) Math.round(timeSeconds);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%d min %02d sec", minutes, seconds);
    }
}