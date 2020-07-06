package com.example.joggr;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            System.out.println("hello");
            CalendarQuickstart.main();
        } catch (IOException e) {
            System.out.println("uhohs");
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            System.out.println("uhohs");
            e.printStackTrace();
        }

    }


}