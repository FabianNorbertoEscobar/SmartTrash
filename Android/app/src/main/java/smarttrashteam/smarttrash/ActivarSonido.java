package smarttrashteam.smarttrash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class  ActivarSonido extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorGiroscopoListener;
    private Sensor SensorGiroscopo;
    private Button BtnVolverDeGiroscopo;
    private ConnectedThread Miconexion;

    //Bluetooth
    Handler bluetoothIn;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MiConexionBT;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    final int handlerState = 0;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;
    //---------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giroscopo);


        //Se usa el SensorManager para el control de los sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Se obtiene el sensor de proximidad
        SensorGiroscopo = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        VerificarEstadoSensores();
        Miconexion = (ConnectedThread)getIntent().getExtras().getSerializable("ConexionBluetooth");

        BtnVolverDeGiroscopo = findViewById(R.id.IdBtnVolverDeGiroscopo);

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

        //-------------BLUETOOTH----
        //Consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = getIntent();
        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try
        {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {}
        }
        MiConexionBT = new ConnectedThread(btSocket);
        MiConexionBT.start();

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

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
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
