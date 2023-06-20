package com.codedev.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity {

    private EditText name;
    private EditText passwordacc;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        passwordacc = findViewById(R.id.passwordacc);
        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName = name.getText().toString();
                String getPassword = passwordacc.getText().toString();

                if (TextUtils.isEmpty(getName) || TextUtils.isEmpty(getPassword)) {
                    Toast.makeText(registerActivity.this, "veuiilez entrer le nom et le mot de passe", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    intent.putExtra("name", getName);
                    intent.putExtra("password", getPassword);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }
}
