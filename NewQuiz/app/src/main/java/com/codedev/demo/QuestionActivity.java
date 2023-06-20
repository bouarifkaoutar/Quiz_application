package com.codedev.demo;
import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question,qCount,timer;
    private Button option1,option2,option3,option4;
    private List<Questions> questionsList;
    private int quesNum;
    private CountDownTimer countDown;
    private int score;
    private int incorrect;
    private int unans;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question=findViewById(R.id.question);
        qCount=findViewById(R.id.quest_no);
        timer=findViewById(R.id.countdown);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog=new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        questionsList=new ArrayList<>();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        getQuestionsList();

        score=0;

    }
    private void getQuestionsList() {
        questionsList.clear();

        // Ajoutez  questions manuellement
        questionsList.add(new Questions(
                "Qu'est-ce que Java?",
                "Un langage de programmation",
                "Un système d'exploitation",
                "Un navigateur web",
                "Un outil de développement",
                1
        ));

        questionsList.add(new Questions(
                "Quel mot-clé est utilisé pour déclarer une variable constante en Java?",
                "constant",
                "final",
                "immutable",
                "static",
                2
        ));

        questionsList.add(new Questions(
                "Quelle est la sortie de ce code?\n\nint x = 5;\nSystem.out.println(x++);",
                "5",
                "6",
                "0",
                "Compilation error",
                2
        ));

        questionsList.add(new Questions(
                "Quel est le résultat de l'expression suivante?\n\n3 + 4 * 2",
                "14",
                "11",
                "10",
                "7",
                2
        ));

        questionsList.add(new Questions(
                "Quel est le type de données approprié pour stocker un nombre entier?",
                "int",
                "String",
                "boolean",
                "double",
                1
        ));

        questionsList.add(new Questions(
                "Quelle est la syntaxe correcte pour déclarer un tableau en Java?",
                "array[] arr = new array[10];",
                "int[] arr = new int[10];",
                "Array arr = new Array(10);",
                "int arr[10];",
                2
        ));

        questionsList.add(new Questions(
                "Quelle méthode est appelée automatiquement lorsqu'un objet est créé?",
                "main()",
                "run()",
                "start()",
                "constructor()",
                3
        ));

        questionsList.add(new Questions(
                "Quelle classe est utilisée pour lire l'entrée utilisateur depuis la console en Java?",
                "Scanner",
                "BufferedReader",
                "InputStream",
                "Console",
                1
        ));

        questionsList.add(new Questions(
                "Quelle interface est utilisée pour implémenter le mécanisme de gestion des événements en Java?",
                "ActionListener",
                "EventHandler",
                "EventListener",
                "Runnable",
                3
        ));

        questionsList.add(new Questions(
                "Quelle est la sortie de ce code?\n\nString str = \"Hello, world!\";\nSystem.out.println(str.length());",
                "12",
                "13",
                "11",
                "Compilation error",
                2
        ));



        setQuestion();
        loadingDialog.dismiss();
    }

    private void setQuestion(){
        timer.setText(String.valueOf(10));
        question.setText(questionsList.get(0).getQuestion());
        option1.setText(questionsList.get(0).getOptionA());
        option2.setText(questionsList.get(0).getOptionB());
        option3.setText(questionsList.get(0).getOptionC());
        option4.setText(questionsList.get(0).getOptionD());
        qCount.setText(String.valueOf(1)+"/"+String.valueOf(questionsList.size()));
        starttimer();
        quesNum=0;
    }
    private void starttimer(){
        countDown =new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished<10000)
                    timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();

            }
        };
        countDown.start();
    }

    @Override
    public void onClick(View view) {
        int selectedOption =0;
        switch (view.getId()){
            case R.id.option1:
                selectedOption=1;
                break;
            case R.id.option2:
                selectedOption=2;
                break;
            case R.id.option3:
                selectedOption=3;
                break;
            case R.id.option4:
                selectedOption=4;
                break;
            default:
                break;
        }
        countDown.cancel();
        checkAnswer(selectedOption,view);
    }
    private void checkAnswer(int selectedOption,View view){
        if(selectedOption==questionsList.get(quesNum).getCorrectAns())
        {
            //reponse juste
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            //fausse réponse
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionsList.get(quesNum).getCorrectAns()){
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
            incorrect++;
        }
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },500);

    }
    private void changeQuestion(){
        if (quesNum<questionsList.size()-1)
        {
            quesNum++;
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(quesNum + 1)  +  "/"  +  String.valueOf(questionsList.size()));
            timer.setText(String.valueOf(10));
            starttimer();


        }else{
            //va vers  score activity
            Intent intent=new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionsList.size()));
            intent.putExtra("score",String.valueOf(score));
            intent.putExtra("incorrect",String.valueOf(incorrect));
            unans=questionsList.size()-(score+incorrect);
            intent.putExtra("unanswered",String.valueOf(unans));

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
    private void playAnim(View view,final int value, int viewNum){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }
                    // Action à effectuer à la fin de l'animation
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (value==0)
                        {
                            switch (viewNum){
                                case 0:
                                    ((TextView)view).setText(questionsList.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionD());
                                    break;
                            }

                            if (viewNum!=0)
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
                            playAnim(view,1,viewNum);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }
                    // Action à effectuer si l'animation est annulée
                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("tu veux quitter ")
                .setMessage("vous voulez quitté ")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        QuestionActivity.super.onBackPressed();
                        countDown.cancel();
                    }
                }).create().show();

    }
}