package com.codedev.demo;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Déclaration des éléments de l'interface utilisateur
    private TextView createAcc;
    private EditText edname;
    private EditText edpass;
    private Button button;

    // Liste des utilisateurs enregistrés
    private ArrayList<User> userCredentials=new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAcc = findViewById(R.id.createAcc);
        edname = findViewById(R.id.edname);
        edpass = findViewById(R.id.edpass);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Récupérer les valeurs des champs de nom et de mot de passe
                String name = edname.getText().toString();
                String pass = edpass.getText().toString();
                // Vérifier si les champs sont vides
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(MainActivity.this, "saisir un nom et un mot de passe", Toast.LENGTH_SHORT).show();
                } else {
                    // Appeler la méthode de connexion
                    login(name, pass);
                }
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, registerActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    private void login(String name, String pass) {
        boolean userFound = false;

        for (User user : userCredentials) {

            // Vérifier si les informations d'identification correspondent à un utilisateur enregistré
            if (user.getNom().equals(name) && user.getPassword().equals(pass)) {
                Toast.makeText(MainActivity.this, "connexion réussi", Toast.LENGTH_SHORT).show();
                // L'utilisateur est authentifié, il se dirige vers l'activité des questions
                startActivity(new Intent(MainActivity.this, infoActivity.class));
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            Toast.makeText(MainActivity.this, "nom ou mot de passe invalides", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Récupérer le nom et le mot de passe de l'utilisateur nouvellement inscrit
            String name = data.getStringExtra("name");
            String password = data.getStringExtra("password");
            User newUser = new User(name, password);
            userCredentials.add(newUser);
            Toast.makeText(MainActivity.this, "L'utilisateur s'est inscrit avec succès", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}


