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
            data[i][2] = String.format("%.2f sec", entry.getTimeSeconds());
            data[i][3] = entry.getCorrectScore();
            data[i][4] = entry.getIncorrectScore();
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog(MainGUI.window, "Leaderboard", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(MainGUI.window);
        dialog.setVisible(true);
    }
}