package com.example.lab7b_rifhan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button references
        ImageButton btnCall = findViewById(R.id.btnCall);
        ImageButton btnMessage = findViewById(R.id.btnMessage);
        ImageButton btnWebsite = findViewById(R.id.btnWebsite);
        ImageButton btnEmail = findViewById(R.id.btnEmail);

        // Phone call action
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+60355435452"));
            startActivity(intent);
        });

        // WhatsApp message action
        btnMessage.setOnClickListener(v -> {
            String phoneNumber = "+60132121704";
            String message = "Hello from my Android app!";
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + phoneNumber.replace("+", "") + "?text=" + Uri.encode(message)));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Open website action
        btnWebsite.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fskm.uitm.edu.my/v5/index.php/en/"));
            startActivity(intent);
        });

        // Send email action
        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:aihakim@uitm.edu.my"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
            intent.putExtra(Intent.EXTRA_TEXT, "Dear Sir/Madam,");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });
    }
}