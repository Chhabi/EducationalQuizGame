package tu_sofia.ivanmishev.bg.educationalgamequiz;

import android.widget.Switch;

import java.util.Random;

/**
 * Created by Admin on 5.4.2015 г..
 */
public class Question {
    private String question= "";
    private String correctAnswer="";
    private String wrongAnswer1="";
    private String wrongAnswer2="";
    private String wrongAnswer3="";
    private int difficulty = 1;
    private static boolean askAlienUsed= false;
    private static boolean askConsortiumUsed= false;
    private static boolean blastQuestionUsed= false;


    public Question(String question,String correctAnswer,String wrongAnswer1,String wrongAnswer2,String wrongAnswer3, int difficulty){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
        this.difficulty = difficulty * RandomPointGeneratorForCurrentAnswer();
    }


    //TODO different scores for different difficulties
    public int RandomPointGeneratorForCurrentAnswer(){

        int min = 2;
        int max = 7;
        Random rand = new Random();
        int randomNum;
        switch(this.getDifficulty()){
            case 1: randomNum = (rand.nextInt((max - min) + 1) + min) * bonus();
                break;

            case 2: randomNum = (rand.nextInt((max - min) + 1) + min) * bonus();
                break;

            case 3: randomNum = (rand.nextInt((max - min) + 1) + min) * bonus();
                break;

            case 4: randomNum = (rand.nextInt((max - min) + 1) + min) * bonus();
                break;

            default: randomNum = (rand.nextInt((max - min) + 1) + min) * bonus();
                break;

        }
        return randomNum;
    }

    private int bonus() {

        Random rand = new Random();
        int randInt = rand.nextInt(10);
        if (randInt <=2 ){
            return 2;
        }else{
            return 1;
        }

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRightAnswer() {
        return correctAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.correctAnswer = rightAnswer;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
    }

    public static boolean isBlastQuestionUsed() {
        return blastQuestionUsed;
    }

    public static void setBlastQuestionUsed(boolean blastQuestionUsed) {
        Question.blastQuestionUsed = blastQuestionUsed;
    }

    public static boolean isAskConsortiumUsed() {
        return askConsortiumUsed;
    }

    public static void setAskConsortiumUsed(boolean askConsortiumUsed) {
        Question.askConsortiumUsed = askConsortiumUsed;
    }

    public static boolean isAskAlienUsed() {
        return askAlienUsed;
    }

    public static void setAskAlienUsed(boolean askAlienUsed) {
        Question.askAlienUsed = askAlienUsed;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

//    //check for one wrong answer that will stay in object field
//    public void useFiftyFifty(){
//        if (isFiftyFiftyIsUsed() == false){
//            Random rand = new Random();
//            int wAnswer1 = rand.nextInt(100);
//            int wAnswer2 = rand.nextInt(100);
//            int wAnswer3 = rand.nextInt(100);
//            if((wAnswer1 <= wAnswer2) && (wAnswer1 <= wAnswer3)){
//                setWrongAnswer2("");
//                setWrongAnswer3("");
//            }else if((wAnswer2 <= wAnswer1) && (wAnswer2 <= wAnswer3)){
//                setWrongAnswer3("");
//                setWrongAnswer1("");
//            }else{
//                setWrongAnswer1("");
//                setWrongAnswer2("");
//            }
//            setFiftyFiftyIsUsed(true);
//        }
//   }

    public String blastQuestion(){
        setBlastQuestionUsed(true);
        return "Военоначалникът на извънземната раса Грох'ул използва мощен бластер върху въпросът и облъчи с радианция верният отговор!";
    }

    public String askAlien(){
        setAskAlienUsed(true);
        String cAnswer = getRightAnswer();
        return "Мъдрецът на извънземните мисли, че верният отговор е: " + cAnswer;
    }

    public String askConsortium(){
        setAskConsortiumUsed(true);
        int cAnswer = 40;
        int wAnswer1 = 0;
        int wAnswer2 = 0;
        int wAnswer3 = 0;
        Random rand = new Random();
        cAnswer = cAnswer + rand.nextInt(30)+1;
        wAnswer1 = rand.nextInt(100-cAnswer)+1;
        wAnswer2 = rand.nextInt(100-(wAnswer1+cAnswer))+1;
        wAnswer3 = (100-(wAnswer2+wAnswer1+cAnswer));

        return "Консорциумът на извънземните отсъди:  \n" +
                getRightAnswer() + " " + cAnswer + "%" +"\n" +
                getWrongAnswer1() + " " + wAnswer1 + "%" +"\n" +
                getWrongAnswer2() + " " + wAnswer2 + "%" +"\n" +
                getWrongAnswer3() + " " + wAnswer3 + "%";

    }

}
