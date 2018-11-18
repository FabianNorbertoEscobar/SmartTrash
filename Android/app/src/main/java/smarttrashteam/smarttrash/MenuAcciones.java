package smarttrashteam.smarttrash;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MenuAcciones extends AppCompatActivity {

    //-----Bluetooth
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
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    //-----Fin Bluetooth

    Button BtnActivarSonido, BtnManejarTapa, BtnVaciarTacho, BtnDesconectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_acciones);

        Intent intent = getIntent();
//        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);

        BtnActivarSonido = findViewById(R.id.IdBtnActivarSonido);
        BtnManejarTapa = findViewById(R.id.IdBtnManejarTapa);
        BtnVaciarTacho = findViewById(R.id.IdBtnVaciarTacho);
        BtnDesconectar = findViewById(R.id.IdBtnVolverDeAcelerometro);

        BtnActivarSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaActivarSonido = new Intent(getApplicationContext(), ActivarSonido.class);
                //pantallaActivarSonido.putExtra("ConexionBluetooth", MiConexionBT);
                pantallaActivarSonido.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(pantallaActivarSonido);
            }
        });
        BtnManejarTapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaManejarTapa = new Intent(getApplicationContext(), ManejarTapa.class);
                pantallaManejarTapa.putExtra("ConexionBluetooth", MiConexionBT);
                startActivity(pantallaManejarTapa);
            }
        });
        BtnVaciarTacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaVaciarTacho = new Intent(getApplicationContext(), VaciarTacho.class);
                pantallaVaciarTacho.putExtra("ConexionBluetooth", MiConexionBT);
                startActivity(pantallaVaciarTacho);
            }
        });
        BtnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try {btSocket.close();}
                    catch (IOException e)
                    { Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();;}
                }
                Intent desconectarBluetooth = new Intent(getApplicationContext(), ActivityPrincipal.class);
                startActivity(desconectarBluetooth);
                finish();
            }
        });

//        bluetoothIn = new Handler() {
//            public void handleMessage(android.os.Message msg) {
////                if (msg.what == handlerState) {
////                    String readMessage = (String) msg.obj;
////                    DataStringIN.append(readMessage);
////
////                    int endOfLineIndex = DataStringIN.indexOf("#");
////
////                    if (endOfLineIndex > 0) {
////                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
////                        TxtDatos.setText("Dato: " + dataInPrint);
////                        DataStringIN.delete(0, DataStringIN.length());
////                    }
////                }
//            }
//        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();
    }

    @Override
    public void onResume() {
        super.onResume();

//        //-------------BLUETOOTH----
//        //Consigue la direccion MAC desde DeviceListActivity via intent
//        Intent intent = getIntent();
//        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
//        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);
//        //Setea la direccion MAC
//        BluetoothDevice device = btAdapter.getRemoteDevice(address);
//
//        try
//        {
//            btSocket = createBluetoothSocket(device);
//        } catch (IOException e) {
//            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
//        }
//        // Establece la conexión con el socket Bluetooth.
//        try
//        {
//            btSocket.connect();
//        } catch (IOException e) {
//            try {
//                btSocket.close();
//            } catch (IOException e2) {}
//        }
//        MiConexionBT = new ConnectedThread(btSocket);
//        MiConexionBT.start();
        //----------FIN BLUETOOTH------
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
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
//    private class ConnectedThread extends Thread implements Serializable
//    {
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket)
//        {
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//            try
//            {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) { }
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run()
//        {
//            byte[] buffer = new byte[256];
//            int bytes;
//
//            // Se mantiene en modo escucha para determinar el ingreso de datos
//            while (true) {
//                try {
//                    bytes = mmInStream.read(buffer);
//                    String readMessage = new String(buffer, 0, bytes);
//                    // Envia los datos obtenidos hacia el evento via handler
//                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
//                } catch (IOException e) {
//                    break;
//                }
//            }
//        }
//        //Envio de trama
//        public void write(String input)
//        {
//            try {
//                mmOutStream.write(input.getBytes());
//            }
//            catch (IOException e)
//            {
//                //si no es posible enviar datos se cierra la conexión
//                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//    }
}
