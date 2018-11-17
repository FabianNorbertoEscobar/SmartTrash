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

public class ManejarTapa extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorAcelerometroListener;
    private Sensor SensorAcelerometro;
    private Button BtnVolverDeAcelerometro;
    private ConnectedThread Miconexion;
    private boolean TapaAbierta = false;
    private DetectorSacudida DetectorSacudida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometro);

        //Se usa el SensorManager para el control de los sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Se obtiene el sensor de proximidad
        SensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        VerificarEstadoSensores();
        Miconexion = (ConnectedThread)getIntent().getExtras().getSerializable("ConexionBluetooth");

        BtnVolverDeAcelerometro = findViewById(R.id.IdBtnVolverDeAcelerometro);

        BtnVolverDeAcelerometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(sensorAcelerometroListener);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //----------SENSOR-------------
        DetectorSacudida = new DetectorSacudida();
        DetectorSacudida.setOnShakeListener(new smarttrashteam.smarttrash.DetectorSacudida.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if(TapaAbierta == false){
                    Miconexion.write("a");
                    TapaAbierta = true;
                }else{
                    Miconexion.write("c");
                    TapaAbierta = false;
                }

            }
        });
        sensorManager.registerListener(DetectorSacudida, SensorAcelerometro, SensorManager.SENSOR_DELAY_UI);
        //-----------FIN SENSOR--------
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(sensorAcelerometroListener);
    }

    private void VerificarEstadoSensores(){
        if(SensorAcelerometro == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no posee acelerómetro", Toast.LENGTH_LONG).show();
        }
    }
}
