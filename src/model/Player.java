package model;

import view.*;
import controller.*;

public class Player {
    public String myPlayerName;
    public double myRecordTime;
    public int myCorrectScore;
    public int myIncorrectScore;
    private long startTime;

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

    public int getIncorrectScore() {
        return myIncorrectScore;
    }

    public void setIncorrectScore(int theIncorrectScore) {
        this.myIncorrectScore = theIncorrectScore;
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

    /*
    public String playerDirection() {

    }
     */
}
