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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ControlarBT extends AppCompatActivity {

    Button BtnAbrirTacho, BtnCerrarTacho, BtnDesconectarBT;

    TextView TxtTachoLleno, TxtSacudida, TxtTachoLocoAntihorario, TxtTachoLocoHorario;

    //-------------------------------------------
    Handler bluetoothIn;
    final int handlerState = 0;
    private boolean TapaAbierta;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MiConeccionBT;
    private SensorManager sensorManager;
    private SensorEventListener sensorProximidadListener, sensorGiroscopoListener;
    private DetectorSacudida DetectorSacudida;
    private Sensor SensorProximidad, SensorAcelerometro, SensorGiroscopo;

    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;
    //-------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_controlar_bt);
        TapaAbierta = false;
        BtnDesconectarBT = findViewById(R.id.IdBtnDesconectar);
        TxtTachoLleno = findViewById((R.id.IdTxtTachoLleno));


        //Se usa el SensorManager para el control de los sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Se obtiene el sensor de proximidad
        SensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        SensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorGiroscopo = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        VerificarEstadoSensores();

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0) {
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);

                        //TxtDatos.setText("Dato: " + dataInPrint);

                        if(dataInPrint.equals("v")){
                            TxtTachoLleno.setVisibility(View.INVISIBLE);
                        }

                        if(dataInPrint.equals("l")){
                            TxtTachoLleno.setVisibility(View.VISIBLE);
                        }

                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();

        //-----------LISTENERS BOTONES--------

        BtnDesconectarBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (btSocket!=null)
                {

                    try {
                        MiConeccionBT.write("s");
                        btSocket.close();
                    }

                    catch (IOException e)
                    { Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();;}
                }
                finish();
            }
        });
        //-------------FIN LISTENERS----------
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume()
    {
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
        MiConeccionBT = new ConnectedThread(btSocket);
        MiConeccionBT.start();

        MiConeccionBT.write("b");
        //----------FIN BLUETOOTH------

        //----------SENSORES-----------

        //----------Comportamiento sensor proximidad
        sensorProximidadListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent evento) {
                if(evento.values[0] < SensorProximidad.getMaximumRange()) {

                    MiConeccionBT.write("j");

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorProximidadListener, SensorProximidad, 2 * 1000 * 1000);
        //----------FIN---Comportamiento sensor proximidad

        //----------Comportamiento sensor aceleración
        DetectorSacudida = new DetectorSacudida();
        DetectorSacudida.setOnShakeListener(new smarttrashteam.smarttrash.DetectorSacudida.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if(TapaAbierta == false){
                    MiConeccionBT.write("a");
                    TapaAbierta = true;
                }else{
                    MiConeccionBT.write("c");
                    TapaAbierta = false;
                }
            }
        });
        sensorManager.registerListener(DetectorSacudida, SensorAcelerometro, SensorManager.SENSOR_DELAY_UI);
        //----------FIN---Comportamiento sensor aceleración

        //----------Comportamiento sensor giróscopo
        sensorGiroscopoListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[2] > 6f) { // Giro antihorario

                    MiConeccionBT.write("l");
                } else if(sensorEvent.values[2] < -6f) { // Giro Horario
                    MiConeccionBT.write("n");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorGiroscopoListener, SensorGiroscopo, SensorManager.SENSOR_DELAY_GAME);
        //----------FIN---Comportamiento sensor giróscopo

        //----------FIN SENSORES--------
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        { // Cuando se sale de la aplicación esta parte permite que no quede nada activo
            btSocket.close();
            sensorManager.unregisterListener(DetectorSacudida);
            sensorManager.unregisterListener(sensorGiroscopoListener);
            sensorManager.unregisterListener(sensorProximidadListener);

        } catch (IOException e2) {}
    }


    private void VerificarEstadoSensores(){
        if(SensorProximidad == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no posee sensor de proximidad", Toast.LENGTH_LONG).show();
        }
        if(SensorGiroscopo == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no posee giróscopo", Toast.LENGTH_LONG).show();
        }
        if(SensorAcelerometro == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no posee acelerómetro", Toast.LENGTH_LONG).show();
        }
    }

    //Comprueba que el dispositivo Bluetooth Bluetooth está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //Envio de trama
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
