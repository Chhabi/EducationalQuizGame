package tu_sofia.ivanmishev.bg.educationalgamequiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HomeScreen extends Activity {

    public static final int DEFAULT_SCORE = 0;

    TextView overallScoreText;
    Button startNewGameButton, exitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Flag for NoTitle.FullScreen
        //crashing on API 19!!!
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Initializing app widgets for further use.
        overallScoreText = (TextView) findViewById(R.id.overallScoreText);
        startNewGameButton = (Button) findViewById(R.id.startNewGameButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        overallScoreText.setGravity(Gravity.CENTER);



        //We get gameScore.xml sharedPref file
        SharedPreferences sharedPreferences = getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        //check if there is game score with some points in, it if not create one with Default value
        //Note: By clearing application cache we delete gameScore.xml... so the game score will be reset!
        if (!sharedPreferences.contains("gameScore")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("gameScore", DEFAULT_SCORE);
            editor.commit();

            //get the value of sharedPref gameScore with key "gameScore"
            overallScoreText.setText(getResources().getString(R.string.overall_score_text) + "\n" + sharedPreferences.getInt("gameScore", DEFAULT_SCORE) + " "
                    + getResources().getString(R.string.points_text));
        }else{

            //if there is a sharedPref with key gameScore we take it and set scoreTextView with the value that was stored
            Toast.makeText(this, "Data was loaded successfully!", Toast.LENGTH_SHORT).show();
            overallScoreText.setText(getResources().getString(R.string.overall_score_text) + "\n" + sharedPreferences.getInt("gameScore", DEFAULT_SCORE)+ " "
                    + getResources().getString(R.string.points_text));

        }

    }

    @Override
    protected void onResume() {

        //Every time on resume on that activity we set scoreTextView with the recent score!
        SharedPreferences sp = getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        overallScoreText.setText(getResources().getString(R.string.overall_score_text) + "\n" + sp.getInt("gameScore", DEFAULT_SCORE) + " "
                + getResources().getString(R.string.points_text));

        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startNewGame(View view) {
        Question.setHelpers();
        Intent intent = new Intent(this, GameScreen.class);
        startActivity(intent);
        finish();
    }

    public void exitApp(View view) {
        finish();
    }

}