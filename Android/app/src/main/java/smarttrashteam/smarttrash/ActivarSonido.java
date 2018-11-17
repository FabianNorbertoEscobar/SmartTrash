package smarttrashteam.smarttrash;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class  ActivarSonido extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorGiroscopoListener;
    private Sensor SensorGiroscopo;
    private Button BtnVolverDeGiroscopo;
    private ConnectedThread Miconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giroscopo);

        BtnVolverDeGiroscopo = findViewById(R.id.IdBtnVolverDeGiroscopo);

        //Se usa el SensorManager para el control de los sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Se obtiene el sensor de proximidad
        SensorGiroscopo = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        VerificarEstadoSensores();
        Miconexion = (ConnectedThread)getIntent().getExtras().getSerializable("ConexionBluetooth");

        BtnVolverDeGiroscopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(sensorGiroscopoListener);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //----------SENSOR-------------
        sensorGiroscopoListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[2] > 6f) { // Giro antihorario
                    Miconexion.write("p");
                } else if(sensorEvent.values[2] < -6f) { // Giro Horario
                    Miconexion.write("q");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorGiroscopoListener, SensorGiroscopo, SensorManager.SENSOR_DELAY_GAME);
        //-----------FIN SENSOR--------
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(sensorGiroscopoListener);
    }

    private void VerificarEstadoSensores(){
        if(SensorGiroscopo == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no posee giróscopo", Toast.LENGTH_LONG).show();
        }
    }
}
