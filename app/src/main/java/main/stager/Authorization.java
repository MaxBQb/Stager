package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Authorization extends AppCompatActivity {
    private Button btn_registr_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_form);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        btn_registr_form = (Button) findViewById(R.id.btn_registration);

        btn_registr_form.setOnClickListener(
                v -> {
                    Intent intent = new Intent(".Registration");
                    startActivity(intent);
                }
        );
    }

}
