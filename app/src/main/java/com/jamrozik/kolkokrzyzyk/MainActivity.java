package com.jamrozik.kolkokrzyzyk;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] przyciski = new Button[3][3];
    private boolean ruchGracza1 = true;
    private int licznik;
    private int punktyGracz1;
    private int punktyGracz2;
    private TextView textGracz1;
    private TextView textGracz2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textGracz1 = findViewById(R.id.text_view_p1);
        textGracz2 = findViewById(R.id.text_view_p2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                przyciski[i][j] = findViewById(resID);
                przyciski[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }
    // klikniecie gracza i skutek czyli o lub x lub nic nie moze zrobic
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (ruchGracza1) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        licznik++;
        if (sprawdzWygrana()) {
            if (ruchGracza1) {
                graczJedenWygrywa();
            } else {
                graczDwaWygrywa();
            }
        } else if (licznik == 9) {
            remis();
        } else {
            ruchGracza1 = !ruchGracza1;
        }
    }
    //sprawdzenie wygranej lub remisu jezeli sa 3 takie same w rzedzie lub nie ma juz miejsca
    private boolean sprawdzWygrana() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = przyciski[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
    //wygrana gracza dodaje mu punkt, remis nie daje punktu nikomu, macierz jest czyszczona, gotowa do kolejnej gry
    private void graczJedenWygrywa() {
        punktyGracz1++;
        Toast.makeText(this, "Gracz 1 wygrywa!", Toast.LENGTH_SHORT).show();
        aktualizacjaTabeliWynikow();
        resetTablicy();
    }

    private void graczDwaWygrywa() {
        punktyGracz2++;
        Toast.makeText(this, "Gracz 2 wygrywa!", Toast.LENGTH_SHORT).show();
        aktualizacjaTabeliWynikow();
        resetTablicy();
    }

    private void remis() {
        Toast.makeText(this, "Remis!", Toast.LENGTH_SHORT).show();
        resetTablicy();
    }
    // dodawanie punktu do tabeli
    private void aktualizacjaTabeliWynikow() {
        textGracz1.setText("Gracz 1: " + punktyGracz1);
        textGracz2.setText("Gracz 2: " + punktyGracz2);
    }
    // przygotowanie macierzy na kolejna gre
    private void resetTablicy() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                przyciski[i][j].setText("");
            }
        }
        licznik = 0;
        ruchGracza1 = true;
    }
    // zaczynanie gry od poczatku, zerowanie punktow dla obu graczy
    private void resetGame() {
        punktyGracz1 = 0;
        punktyGracz2 = 0;
        aktualizacjaTabeliWynikow();
        resetTablicy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", licznik);
        outState.putInt("player1Points", punktyGracz1);
        outState.putInt("player2Points", punktyGracz2);
        outState.putBoolean("player1Turn", ruchGracza1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        licznik = savedInstanceState.getInt("roundCount");
        punktyGracz1 = savedInstanceState.getInt("player1Points");
        punktyGracz2 = savedInstanceState.getInt("player2Points");
        ruchGracza1 = savedInstanceState.getBoolean("player1Turn");
    }
}