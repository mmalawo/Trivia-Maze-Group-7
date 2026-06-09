package view;

import model.LeaderboardDAO;
import model.LeaderboardEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * LeaderboardView displays the top scores in a dialog window.
 * It does not control app navigation; controllers decide what happens after it closes.
 */
public class LeaderboardView {

    /**
     * Displays the leaderboard dialog with no specific owner component.
     * The dialog will be centered on the screen.
     */
    public static void showLeaderboard() {
        showLeaderboard(null);
    }

    /**
     * Displays the leaderboard dialog showing the top recorded scores.
     * The dialog is positioned relative to the specified owner component.
     *
     * @param theOwner the component relative to which the dialog is displayed.
     */
    public static void showLeaderboard(Component theOwner) {
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
        JButton closeButton = new JButton("Close");

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(theOwner),
                "Leaderboard",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setSize(600, 450);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(theOwner);

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    /**
     * Converts a time value in seconds into a formatted string
     * showing minutes and seconds.
     *
     * @param theTimeSeconds the elapsed time in seconds
     * @return a formatted time string in the form "X min YY sec"
     */
    private static String formatTime(double theTimeSeconds) {
        int totalSeconds = (int) Math.round(theTimeSeconds);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d min %02d sec", minutes, seconds);
    }
}
