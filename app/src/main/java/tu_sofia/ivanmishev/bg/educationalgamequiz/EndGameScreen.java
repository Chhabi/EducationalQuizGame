package tu_sofia.ivanmishev.bg.educationalgamequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class EndGameScreen extends Activity {


    TextView congratsTextView;
    Button homeButton;
    public Bundle extras;
    private int currentPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_screen);

        //Initializing app widgets for further use.
        congratsTextView = (TextView) findViewById(R.id.congratsTextView);
        homeButton = (Button) findViewById(R.id.homeButton);

        extras = getIntent().getExtras();
        if (extras != null) {
            currentPoints = extras.getInt("currentPoints");
        }

        congratsTextView.setText(setCongratsText());
    }

    private String setCongratsText(){

        String congratsText;

        congratsText = "Поздравления! Успешно успяхне да победите играта! Вашият резултат е " + currentPoints + " точки." +
                " Можете да пробвате да подобрите резултатът си, точките за всеки въпрос се генерират автоматично на случаен принцип!";

        return congratsText;
    }

    public void backToHome(View view) {
        Intent intent = new Intent(this, HomeScreen.class);
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
