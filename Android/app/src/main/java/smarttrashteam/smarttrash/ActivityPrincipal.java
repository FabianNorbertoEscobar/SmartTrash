package smarttrashteam.smarttrash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityPrincipal extends AppCompatActivity {

    Button BtnMenuBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);

        BtnMenuBT = findViewById(R.id.IdBtnMenuBT);

        BtnMenuBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verDispositivos = new Intent(getApplicationContext(), DispositivosBT.class);
                startActivity(verDispositivos);
            }
        });
    }


}
