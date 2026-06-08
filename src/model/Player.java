package model;

import view.*;
import java.io.Serializable;
import controller.*;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String myPlayerName;
    private double myRecordTime;
    private int myCorrectScore;
    private int myIncorrectScore;
    private long startTime;
    private Room myCurrentRoom;
    private int myRemainingAttempts;

    public String getName() {
        return myPlayerName;
    }

    public void setName(String theName) {
        this.myPlayerName =  theName;
    }

    public int getCorrectScore() {
        return myCorrectScore;
    }

    public void setCorrectScore(int theCorrectScore) {
        this.myCorrectScore = theCorrectScore;
    }

    public void incrementCorrectScore() {
        myCorrectScore++;
    }
    public int getIncorrectScore() {
        return myIncorrectScore;
    }

    public void setIncorrectScore(int theIncorrectScore) {
        this.myIncorrectScore = theIncorrectScore;
    }

    public void incrementIncorrectScore() {
        myIncorrectScore++;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }
    public void stopTimer() {
        myRecordTime = (System.currentTimeMillis() - startTime) / 1000.0;
    }
    public double elapsedTime() {

        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
    public double getCurrentTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
    public double getRecordTime() {
        return myRecordTime;
    }

    public void setRecordTime(double recordTime) {
        this.myRecordTime = recordTime;
    }


    public void reset() {
        myPlayerName = null;
        myRecordTime = 0;
        myCorrectScore = 0;
        myIncorrectScore = 0;
        startTime = 0;
        MainGUI.setupView.namePrompt.setText("");
    }


    public Room getCurrentRoom() {
        return myCurrentRoom;
    }

    public void setCurrentRoom(Room theRoom) {
        myCurrentRoom = theRoom;
    }

    public int getRemainingAttempts() {
        return myRemainingAttempts;
    }

    public void setRemainingAttempts(int theAttempts) {
        myRemainingAttempts = theAttempts;
    }

    public void decrementAttempts() {
        myRemainingAttempts--;
    }

    public boolean isGameOver() {
        return myRemainingAttempts <= 0;
    }
}
