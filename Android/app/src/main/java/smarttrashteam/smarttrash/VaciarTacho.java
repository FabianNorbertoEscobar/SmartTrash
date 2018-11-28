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

public class VaciarTacho extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorProximidadListener;
    private Sensor SensorProximidad;
    private Button BtnVolverDeProximidad;
    private ConnectedThread Miconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximidad);

        //Se usa el SensorManager para el control de los sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Se obtiene el sensor de proximidad
        SensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        VerificarEstadoSensores();
        Miconexion = (ConnectedThread)getIntent().getExtras().getSerializable("ConexionBluetooth");

        BtnVolverDeProximidad = findViewById(R.id.IdBtnVolverDeProximidad);

        BtnVolverDeProximidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(sensorProximidadListener);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //----------SENSOR-------------
        sensorProximidadListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent evento) {
                if(evento.values[0] < SensorProximidad.getMaximumRange()) {
                    Miconexion.write("v");
                } else {
                    Miconexion.write("t");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorProximidadListener, SensorProximidad, 2 * 1000 * 1000);
        //-----------FIN SENSOR--------
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(sensorProximidadListener);
    }

    private void VerificarEstadoSensores(){
        if(SensorProximidad == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no posee sensor de proximidad", Toast.LENGTH_LONG).show();
        }
    }
}
