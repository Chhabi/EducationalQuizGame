package tu_sofia.ivanmishev.bg.educationalgamequiz;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;


public class GameScreen extends Activity {

    //session points
    private int sessionPoints = 0;
    private int tempSessionPoints = 0;
    private int pointsForCurrentQuestion = 0;

    Button fiftyButton, friendButton, crowdButton;
    TextView sessionPointsText, randomPointsForCurrentAnswerText, sessionQuestionCounterText, questionText;
    Button aButton, bButton, cButton, dButton;

    private MyData db;
    private Cursor questionsCursor;
    private LinkedList<Question> questionsInList;
    ListIterator iterator;
    private int questionNumber = 1;

    int start = Color.rgb(0x00, 0x99, 0x00);
    int end = Color.rgb(0x00, 0xff, 0x00);

    private ValueAnimator va;

    private final MyHandler mHandler = new MyHandler(this);

    private Button mButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        //Initializing app widgets for further use.
        fiftyButton = (Button) findViewById(R.id.fiftyButton);
        friendButton = (Button) findViewById(R.id.friendButton);
        crowdButton = (Button) findViewById(R.id.crowdButton);

        sessionPointsText = (TextView) findViewById(R.id.sessionPointsText);
        randomPointsForCurrentAnswerText = (TextView) findViewById(R.id.randomPointsForCurrentAnswerText);
        sessionQuestionCounterText = (TextView) findViewById(R.id.sessionQuestionCounterText);
        questionText = (TextView) findViewById(R.id.questionText);

        aButton = (Button) findViewById(R.id.aButton);
        bButton = (Button) findViewById(R.id.bButton);
        cButton = (Button) findViewById(R.id.cButton);
        dButton = (Button) findViewById(R.id.dButton);


        questionsInList = new LinkedList<>();

        va = ObjectAnimator.ofInt(mButton ,"backgroundColor", start, end);

        //open db
        db = new MyData(this);
        //populate cursor
        questionsCursor = db.getGameQuestions();

        //move cursor results to questionsList
        questionsCursor.moveToFirst();

        do {

            questionsInList.add(new Question(
                    questionsCursor.getString(questionsCursor.getColumnIndexOrThrow("Question")),
                    questionsCursor.getString(questionsCursor.getColumnIndexOrThrow("cAnswer")),
                    questionsCursor.getString(questionsCursor.getColumnIndexOrThrow("wAnswer1")),
                    questionsCursor.getString(questionsCursor.getColumnIndexOrThrow("wAnswer2")),
                    questionsCursor.getString(questionsCursor.getColumnIndexOrThrow("wAnswer3")),
                    questionsCursor.getInt(questionsCursor.getColumnIndexOrThrow("difficulty"))));


        } while (questionsCursor.moveToNext());

        questionsCursor.close();

        iterator = (ListIterator) questionsInList.iterator();

        setCurrentQuestion();
        checkForUsedHelpers();
    }

    //OnClick listener for game session
    public void checkAnswer(View view) {

        mButton = (Button) view;

        if (mButton.getText().toString().equals(questionsInList.get(0).getRightAnswer())) {
            //Toast.makeText(this, "Correct answer", Toast.LENGTH_SHORT).show();
            questionNumberIncrement();
            checkIfLastQuestion();

            mHandler.postDelayed(r, 1200);

            try {
                questionsInList.removeFirst();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }

            countSessionPoints();
            setCurrentQuestion();
            iterator = (ListIterator) questionsInList.iterator();

        } else {
            Toast.makeText(this, "Wrong answer", Toast.LENGTH_SHORT).show();
            wrongAnswer();

        }


    }
    //TODO this method does not let user to answer to 50-th question and isEmpty never return true
    public void checkIfLastQuestion(){
        if(questionsInList.isEmpty() || questionNumber == 50){
            endGame();
        }
    }

    private int pointsForCurrentQuestion(){
        int localPoints = questionsInList.getFirst().getDifficulty();
        setPointsForCurrentQuestion(localPoints);
        return getPointsForCurrentQuestion();

    }

    private void setCurrentQuestion(){
        setTempSessionPoints(questionsInList.getFirst().getDifficulty());
        sessionPointsText.setText("Точки: "+getSessionPoints());
        randomPointsForCurrentAnswerText.setText("+" + pointsForCurrentQuestion() +" т.");
        sessionQuestionCounterText.setText(questionNumber + " въпрос");
        questionText.setText(questionsInList.getFirst().getQuestion());
        aButton.setText(questionsInList.getFirst().getPositionA());
        bButton.setText(questionsInList.getFirst().getPositionB());
        cButton.setText(questionsInList.getFirst().getPositionC());
        dButton.setText(questionsInList.getFirst().getPositionD());
        aButton.setBackgroundResource(R.drawable.custom_button);
        bButton.setBackgroundResource(R.drawable.custom_button);
        cButton.setBackgroundResource(R.drawable.custom_button);
        dButton.setBackgroundResource(R.drawable.custom_button);

    }

    public int getPointsForCurrentQuestion() {
        return pointsForCurrentQuestion;
    }

    public void setPointsForCurrentQuestion(int pointsForCurrentQuestion) {
        this.pointsForCurrentQuestion = pointsForCurrentQuestion;
    }

    public LinkedList<Question> getQuestionsInList() {
        return questionsInList;
    }

    public void setQuestionsInList(LinkedList<Question> questionsInList) {
        this.questionsInList = questionsInList;
    }

    public void setSessionPoints(int sessionPoints) {
        this.sessionPoints = sessionPoints;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void questionNumberIncrement(){
        this.questionNumber++;
    }

   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        String savedSessionPointsText = savedInstanceState.getString("saveSessionPoints");
        String savedRandomPointsText = savedInstanceState.getString("randomPointsText");
        String savedQuestionCounter = savedInstanceState.getString("sessionCounterText");
        int savedSessionPoints = savedInstanceState.getInt("sessionPoints");
        int savedQuestionNumber = savedInstanceState.getInt("questionNumber");
        int savedRandomPoints = savedInstanceState.getInt("randomPoints");


        @SuppressWarnings("unchecked")
        LinkedList<Question>  savedList = (LinkedList<Question>) savedInstanceState.getSerializable("savedList");


        sessionPointsText.setText(savedSessionPointsText);
        randomPointsForCurrentAnswerText.setText(savedRandomPointsText);
        sessionQuestionCounterText.setText(savedQuestionCounter);
        setSessionPoints(savedSessionPoints);
        setQuestionNumber(savedQuestionNumber);
        setQuestionsInList(savedList);
        setPointsForCurrentQuestion(savedRandomPoints);


        String savedQuestionText = savedInstanceState.getString("saveQuestionText");
        String savedAButtonText = savedInstanceState.getString("saveAButtonText");
        String savedBButtonText = savedInstanceState.getString("saveBButtonText");
        String savedCButtonText = savedInstanceState.getString("saveCButtonText");
        String savedDButtonText = savedInstanceState.getString("saveDButtonText");

        questionText.setText(savedQuestionText);
        aButton.setText(savedAButtonText);
        bButton.setText(savedBButtonText);
        cButton.setText(savedCButtonText);
        dButton.setText(savedDButtonText);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        //saved state for current points
        String saveSessionPointsText = sessionPointsText.getText().toString();
        outState.putString("saveSessionPoints", saveSessionPointsText);

        String saveRandomPointsText = randomPointsForCurrentAnswerText.getText().toString();
        outState.putString("randomPointsText", saveRandomPointsText);

        String saveSessionQuestionCounter = sessionQuestionCounterText.getText().toString();
        outState.putString("sessionCounterText", saveSessionQuestionCounter);

        int saveSessionPoints = getSessionPoints();
        outState.putInt("sessionPoints", saveSessionPoints);

        int saveQuestionNumber = getQuestionNumber();
        outState.putInt("questionNumber", saveQuestionNumber);

        int saveRandomPoints = getPointsForCurrentQuestion();
        outState.putInt("randomPoints", saveRandomPoints);


        //saved state of current questionList

        String saveQuestionText = questionText.getText().toString();
        String saveAButtonText = aButton.getText().toString();
        String saveBButtonText = bButton.getText().toString();
        String saveCButtonText = cButton.getText().toString();
        String saveDButtonText = dButton.getText().toString();

        outState.putString("saveQuestionText", saveQuestionText);
        outState.putString("saveAButtonText", saveAButtonText);
        outState.putString("saveBButtonText", saveBButtonText);
        outState.putString("saveCButtonText", saveCButtonText);
        outState.putString("saveDButtonText", saveDButtonText);

        //TODO somehow to fix that because it cause game crash when activity switch states... Logic is good and work on activity recreation
        LinkedList<Question> savedList = getQuestionsInList();
        outState.putSerializable("savedList", savedList);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        questionsCursor.close();
        db.close();
        destroyGreenAnimation();
        mHandler.removeCallbacks(r);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public int getSessionPoints(){
        return sessionPoints;
    }

    public int getTempSessionPoints() {
        return tempSessionPoints;
    }

    public void setTempSessionPoints(int tempSessionPoints) {
        this.tempSessionPoints = tempSessionPoints;
    }


    public void countSessionPoints() {

        if(getPointsForCurrentQuestion()>0){
            setTempSessionPoints(getPointsForCurrentQuestion());
            setPointsForCurrentQuestion(0);
        }

        sessionPoints = sessionPoints + getTempSessionPoints();
        //Toast.makeText(this, "Current points " + sessionPoints, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (getSessionPoints() > (sharedPreferences.getInt("gameScore",HomeScreen.DEFAULT_SCORE))){
            editor.putInt("gameScore", getSessionPoints());
            editor.commit();
            Toast.makeText(this, "Data was saved into shared pref folder", Toast.LENGTH_SHORT).show();
        }
    }

    public void wrongAnswer(){

        Intent intent = new Intent(this, WrongAnswerScreen.class);
        intent.putExtra("key","yey");
        intent.putExtra("currentPoints", getSessionPoints());
        intent.putExtra("questionNumber", this.questionNumber);
        intent.putExtra("currentQuestion", this.questionsInList.getFirst().getQuestion());
        intent.putExtra("answerForCurrentQuestion", this.questionsInList.getFirst().getRightAnswer());
        startActivity(intent);
        finish();

    }

    public void endGame(){
        Intent intent = new Intent(this, EndGameScreen.class);
        intent.putExtra("currentPoints", getSessionPoints());
        startActivity(intent);
        finish();
    }


    public void useMightyBlaster(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Мощтния бластер");
        alertDialogBuilder
                .setMessage(questionsInList.getFirst().blastQuestion())
                .setCancelable(false)
                .setNegativeButton("ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        blastCAnswer();
        fiftyButton.setClickable(false);
        fiftyButton.setBackgroundResource(R.drawable.button_pressed);
        fiftyButton.setText("X");
    }

    private void blastCAnswer(){

        if(Question.isBlastQuestionUsed()){
            if(aButton.getText().toString().equals(questionsInList.get(0).getRightAnswer())){
                aButton.setBackgroundResource(R.drawable.radiation_button);
            }else if(bButton.getText().toString().equals(questionsInList.get(0).getRightAnswer())){
                    bButton.setBackgroundResource(R.drawable.radiation_button);
            }else if(cButton.getText().toString().equals(questionsInList.get(0).getRightAnswer())){
                    cButton.setBackgroundResource(R.drawable.radiation_button);
            }else{
                dButton.setBackgroundResource(R.drawable.radiation_button);
            }
        }
    }

    public void useWiseAlienButton(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Отговорът на мъдреца");
        alertDialogBuilder
                .setMessage(questionsInList.getFirst().askAlien())
                .setCancelable(false)
                .setNegativeButton("ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

        friendButton.setClickable(false);
        friendButton.setBackgroundResource(R.drawable.button_pressed);
        friendButton.setText("X");

    }

    public void useConsortiumButton(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Решението на консорциума");
        alertDialogBuilder
                .setMessage(questionsInList.getFirst().askConsortium())
                .setCancelable(false)
                .setNegativeButton("ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

        crowdButton.setClickable(false);
        crowdButton.setBackgroundResource(R.drawable.button_pressed);
        crowdButton.setText("X");

    }

    private void checkForUsedHelpers(){
        if(Question.isBlastQuestionUsed()){
            fiftyButton.setClickable(false);
            fiftyButton.setBackgroundResource(R.drawable.button_pressed);
            fiftyButton.setText("X");
        }
        if(Question.isAskAlienUsed()){
            friendButton.setClickable(false);
            friendButton.setBackgroundResource(R.drawable.button_pressed);
            friendButton.setText("X");
        }
        if(Question.isAskConsortiumUsed()){
            crowdButton.setClickable(false);
            crowdButton.setBackgroundResource(R.drawable.button_pressed);
            crowdButton.setText("X");
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<GameScreen> mActivity;


        public MyHandler(GameScreen activity) {
            mActivity = new WeakReference<GameScreen>(activity);
        }

        public void handleMessage(Message msg) {
            GameScreen activity = mActivity.get();
            if (activity != null) {

            }
        }


    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 1200);
        }
    };

    public void setGreenAnimation(){
        //int start2 = Color.parseColor("#ff22ff");
        //int end2 = Color.parseColor("#3322ff");
        va.setDuration(75);
        va.setEvaluator(new ArgbEvaluator());
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.REVERSE);
        va.start();

        //va.cancel();
    }

    public void destroyGreenAnimation(){
        va.cancel();
    }

}