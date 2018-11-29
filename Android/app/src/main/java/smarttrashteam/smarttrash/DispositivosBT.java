package smarttrashteam.smarttrash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class DispositivosBT extends AppCompatActivity {

    //Depuración de LOGCAT
    private static final String TAG = "DispositivosBT";
    // String que se enviara a la actividad principal, mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    ListView ListaDispositivos;

    // Declaracion da variables para el bluetooth
    private BluetoothAdapter BTAdapter;
    private ArrayAdapter BTArrayDispositivosVinculados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispositivos_bt);
    }

    @Override
    public void onResume(){
        super.onResume();

        VerificarEstadoBT();

        // Inicializa la array que contendra la lista de los dispositivos bluetooth vinculados
        BTArrayDispositivosVinculados = new ArrayAdapter(this, R.layout.nombre_dispositivos);
        // Presenta los dispositivos vinculados en el ListView
        ListaDispositivos = findViewById(R.id.IdListaDispositivos);
        ListaDispositivos.setAdapter(BTArrayDispositivosVinculados);
        ListaDispositivos.setOnItemClickListener(mDeviceClickListener);
        // Obtiene el adaptador local Bluetooth adapter
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        // Obtiene un conjunto de dispositivos actualmente emparejados y los agrega a 'pairedDevices'
        Set <BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();

        // Adiciona un dispositivos previo emparejado al array
        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice d1evice : pairedDevices) { //EN CASO DE ERROR LEER LA ANTERIOR EXPLICACION
                BTArrayDispositivosVinculados.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    // Configura un (on-click) para la lista
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            // Obtener la dirección MAC del dispositivo, que son los últimos 17 caracteres en la vista
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Realiza un intent para iniciar la siguiente actividad
            // mientras toma un EXTRA_DEVICE_ADDRESS que es la dirección MAC.
            Intent i = new Intent(DispositivosBT.this, ControlarBT.class);
            i.putExtra(EXTRA_DEVICE_ADDRESS, address);
            startActivity(i);
        }
    };

    private void VerificarEstadoBT() {
        // Comprueba que el dispositivo tiene Bluetooth y que está encendido
        BTAdapter= BluetoothAdapter.getDefaultAdapter();
        if(BTAdapter == null) {
            Toast.makeText(getBaseContext(), "Este dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (BTAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth Activado...");
            } else {
                //Solicita al usuario que active Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}
