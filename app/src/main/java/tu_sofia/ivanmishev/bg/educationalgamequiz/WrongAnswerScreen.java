package tu_sofia.ivanmishev.bg.educationalgamequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class WrongAnswerScreen extends Activity {

    TextView wrongAnswerText, sessionPointsText, numberOfQuestionText, correctAnswerText, currentQuestionText;
    Button playAgainButton, homeButton;
    Bundle extras;
    String value;
    private int currentPoints;
    private int questionNumber;
    private String currentQuestion;
    private String answerForCurrentQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_answer_screen);

        //Initializing app widgets for further use.
        wrongAnswerText = (TextView) findViewById(R.id.wrongAnswerText);
        sessionPointsText = (TextView) findViewById(R.id.sessionPointsText);
        numberOfQuestionText = (TextView) findViewById(R.id.numberOfQuestionText);
        correctAnswerText = (TextView) findViewById(R.id.correctAnswerText);
        currentQuestionText = (TextView) findViewById(R.id.currentQuestionText);

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        homeButton = (Button) findViewById(R.id.homeButton);

        extras = getIntent().getExtras();
        if (extras != null){
            value = extras.getString("key");
            currentPoints = extras.getInt("currentPoints");
            questionNumber = extras.getInt("questionNumber");
            currentQuestion = extras.getString("currentQuestion");
            answerForCurrentQuestion = extras.getString("answerForCurrentQuestion");
            //wrongAnswerText.append(" " + value);
        }

        sessionPointsText.append(" "+currentPoints);
        numberOfQuestionText.append(" "+questionNumber);
        currentQuestionText.append(" " + currentQuestion + " е:");
        correctAnswerText.setText(answerForCurrentQuestion);


    }

    public void backToHome(View view) {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    public void playAgain(View view) {
        Question.setHelpers();
        Intent intent = new Intent(this, GameScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
