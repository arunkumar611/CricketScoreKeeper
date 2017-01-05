package com.example.android.cricketscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.cricketscorekeeper.R;

public class MainActivity extends AppCompatActivity {

    int numberOfOvers = 10;
    String gameStatus = "Not Started";
    String gameStatusChangeButton;
    String gameStatusText;
    int runs = 0;
    int wickets = 0;
    int completedOvers = 0;
    int completedBallsInAOver = 0;
    int currentBallUserInput = 0;
    int currentBallRuns = 0;
    int currentBallWickets = 0;
    int wide = 0;
    int noball = 0;
    int freeHit = 0;
    String currentBallText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //       getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Increase the Number of Overs by 1.
     * Currently else condition is empty, but would like to display an error or pop-up message
     * saying "Overs cannot be changed when game in Progress" once I learn how to do it
     */
    public void decreaseOvers(View v) {
        if (gameStatus.equals("Not Started")) {
            if (numberOfOvers > 0) {
                numberOfOvers--;
            }
        }
        displayNumberOfOvers();
    }

    /**
     * Increase the Number of Overs by 1.
     * Currently else condition is empty, but would like to display an error or pop-up message
     * saying "Overs cannot be changed when game in Progress" once I learn how to do it
     */
    public void increaseOvers(View v) {
        if (gameStatus.equals("Not Started")) {
            numberOfOvers++;
        }
        displayNumberOfOvers();
    }

    /**
     * Displays the Number of Overs
     */
    public void displayNumberOfOvers() {
        TextView numberOfOversView = (TextView) findViewById(R.id.numberOfOvers);
        numberOfOversView.setText(String.valueOf(numberOfOvers));
    }

    /**
     * Sets the right values and calls the display function to START, PAUSE, RESUME or do a Re-Match
     */
    public void startOrPauseGame(View v) {
        if (gameStatus.equals("Game Complete")) {
            gameStatus = "In Progress";
            gameStatusChangeButton = "PAUSE";
            gameStatusText = "Game is in progress";
            variablesReset();
            currentBallUserInput = 1;
            displayErrorForNoCurrentBallUserInput();
        } else if (numberOfOvers > 0) {
            if (gameStatus.equals("Not Started")) {
                gameStatus = "In Progress";
                gameStatusChangeButton = "PAUSE";
                gameStatusText = "Game is in progress";
                variablesReset();
            } else if (gameStatus.equals("In Progress")) {
                gameStatus = "Paused";
                gameStatusChangeButton = "RESUME";
                gameStatusText = "Game is in paused";
            } else {
                gameStatus = "In Progress";
                gameStatusChangeButton = "PAUSE";
                gameStatusText = "Game is in progress";
            }
            displayCurrentBallStatus();
        } else {
            gameStatusText = "Game should have minimum 1 over to start";
            gameStatusChangeButton = "PLAY !";
        }
        displayGameStatus();

    }

    /**
     * Displays the Status of the Game
     */
    public void displayGameStatus() {
        TextView gameStatusTextView = (TextView) findViewById(R.id.gameStatusText);
        gameStatusTextView.setText(gameStatusText);
        TextView gameStatusChangeButtonView = (TextView) findViewById(R.id.gameStatus);
        gameStatusChangeButtonView.setText(gameStatusChangeButton);
    }

    /**
     * Displays the current ball of the game, and some guidelines once game is complete
     */
    public void displayCurrentBallStatus() {
        if (gameStatus.equals("Not Started")) {
            currentBallText = "";
            TextView currentBallView = (TextView) findViewById(R.id.currentBallText);
            currentBallView.setText(currentBallText);
        } else if (gameStatus.equals("Game Complete")) {
            currentBallText = "Press Re-match to play again with same overs";
            TextView currentBallView = (TextView) findViewById(R.id.currentBallText);
            currentBallView.setText(currentBallText);
        } else if (freeHit == 1) {
            currentBallText = "Free Hit!";
            TextView currentBallView = (TextView) findViewById(R.id.currentBallText);
            currentBallView.setText(currentBallText);
        } else {
            int currentBall = completedBallsInAOver + 1;
            switch (currentBall) {
                case 1:
                    currentBallText = "1st ball of the over";
                    break;
                case 2:
                    currentBallText = "2nd ball of the over";
                    break;
                case 3:
                    currentBallText = "3rd ball of the over";
                    break;
                case 4:
                    currentBallText = "4th ball of the over";
                    break;
                case 5:
                    currentBallText = "5th ball of the over";
                    break;
                case 6:
                    currentBallText = "Last ball of the over";
                    break;
            }
            TextView currentBallView = (TextView) findViewById(R.id.currentBallText);
            currentBallView.setText(currentBallText);
        }
    }

    /**
     * Displays the score
     */
    public void displayScore() {
        String runsAndWickets = runs + "/" + wickets;
        String overs = completedOvers + "." + completedBallsInAOver;
        TextView scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText(runsAndWickets);
        TextView overView = (TextView) findViewById(R.id.overs);
        overView.setText(overs);
    }

    /**
     * Below code takes the game to the next ball, and updates the score in the global variables
     * CurrentBallUserinput is checked to see whether the user has updated the score for that ball
     */
    public void nextBall(View v) {
        if (gameStatus.equals("In Progress") && completedOvers < numberOfOvers && currentBallUserInput == 1) {

            if (wide == 1 || noball == 1 || freeHit == 1) {
                displayCurrentBallStatus();
                updateScore();
            } else {
                if (completedBallsInAOver < 5 && (wickets + currentBallWickets) < 10) {
                    completedBallsInAOver = completedBallsInAOver + 1;
                    updateScore();
                } else {
                    completedOvers = completedOvers + 1;
                    completedBallsInAOver = 0;
                    updateScore();
                    if (completedOvers == numberOfOvers || wickets == 10) {
                        gameStatus = "Game Complete";
                        gameStatusText = "Game is Over. Well Played!";
                        gameStatusChangeButton = "Re-match";
                        displayGameStatus();
                    }
                }
                displayScore();
                displayCurrentBallStatus();
            }

            displayErrorForNoCurrentBallUserInput();
            currentBallUserInput = 0;
        } else {
            displayErrorForNoCurrentBallUserInput();
        }
    }

    /**
     * Displays a message if user fails to select a value for the current ball (i.e. what happened in the current ball)
     */
    public void displayErrorForNoCurrentBallUserInput() {
        String noCurrentBallUserInput = "";
        if (currentBallUserInput == 0) {
            noCurrentBallUserInput = "Please log current ball activity to proceed";
        } else {
            noCurrentBallUserInput = "";
        }
        TextView noCurrentBallUserInputView = (TextView) findViewById(R.id.displayErrorForNoUserInput);
        noCurrentBallUserInputView.setText(noCurrentBallUserInput);
    }

    /**
     * Updates the score permanently to the global variables
     */
    public void updateScore() {
        if (currentBallUserInput == 1) {
            runs = runs + currentBallRuns + wide + noball;
            currentBallRuns = 0;
            wide = 0;
            noball = 0;
            freeHit = 0;
            wickets = wickets + currentBallWickets;
            currentBallWickets = 0;
        }

    }

    /**
     * Following set of functions updates the runs/wickets i.e. what happened in the current ball
     */
    public void noRun(View v) {
        currentBallRuns = 0;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void oneRun(View v) {
        currentBallRuns = 1;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void twoRuns(View v) {
        currentBallRuns = 2;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void threeRuns(View v) {
        currentBallRuns = 3;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void fourRuns(View v) {
        currentBallRuns = 4;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void fiveRuns(View v) {
        currentBallRuns = 5;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void sixRuns(View v) {
        currentBallRuns = 6;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void wicket(View v) {
        currentBallWickets = 1;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void wide(View v) {
        wide = 1;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void noBall(View v) {
        noball = 1;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    public void freeHit(View v) {
        freeHit = 1;
        currentBallUserInput = 1;
        temporaryDisplayScore();
    }

    /**
     * Update the score and displaying it temporary, until the next ball button is pressed.
     * This temporary option will allow users to see the updated score as the buttons are pressed
     */
    public void temporaryDisplayScore() {
        if (gameStatus.equals("In Progress")) {
            int temporaryRuns = runs + currentBallRuns + wide + noball, temporaryWickets = wickets + currentBallWickets;
            String runsAndWickets = temporaryRuns + "/" + temporaryWickets;
            TextView scoreView = (TextView) findViewById(R.id.score);
            scoreView.setText(runsAndWickets);
            displayErrorForNoCurrentBallUserInput();
        }
    }

    /**
     * Resetting the game including numberOfOvers
     */
    public void resetGame(View v) {
        numberOfOvers = 10;
        gameStatus = "Not Started";
        gameStatusChangeButton = "PLAY !";
        gameStatusText = "Game has not started";
        variablesReset();
    }

    /**
     * Resetting variable except numberofOvers; This functions enables re-use of resetting variable during 'reset' and 're-match' option.
     */
    public void variablesReset() {
        runs = 0;
        wickets = 0;
        completedOvers = 0;
        completedBallsInAOver = 0;
        currentBallText = "";
        currentBallUserInput = 0;
        currentBallRuns = 0;
        currentBallWickets = 0;
        wide = 0;
        freeHit = 0;
        noball = 0;
        updateScore();
        displayGameStatus();
        displayNumberOfOvers();
        displayScore();
        displayCurrentBallStatus();
    }
}
