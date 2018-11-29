# SMART TRASH

Proyecto SOA UNLaM 2C2018 Miércoles

## Sistemas Operativos Avanzados

### Integrantes del equipo

* Fabián Norberto Escobar ([FabianNorbertoEscobar](https://github.com/FabianNorbertoEscobar))<br>
* Ezequiel Luis Tejerina ([EzequielTejerina](https://github.com/EzequielTejerina))<br>
* Emmanuel Estigarribia ([emmanuel-estigarribia](https://github.com/emmanuel-estigarribia))<br>
* Cristian Vera ([CristianGVera](https://github.com/CristianGVera))<br>
* Martín Ferrarese ([ferrafmz](https://github.com/ferrafmz))<br>

### Sinopsis

Smart Trash es una papelera inteligente que permite arrojar papeles para mantener ordenado y limpio nuestro ambiente, ya sea en el ámbito laboral, de estudio o simplemente en el hogar, con la facilidad de no tener que abrir el tacho ni levantarse del asiento ya que este se abrirá automáticamente cuando detecte que se lo utilice. Esto nos motivó mucho ya que nos suele ocurrir que estamos ocupados realizando otras tareas y ante la necesidad de tirarlos y el tener que evaluar suspender por un momento nuestras actividades, suele optarse por no tirar los bollos de papel y simplemente ir colocándolos a un costado hasta que se forme una montaña de papel o hasta la próxima vez que tengamos que levantarnos y pasar cerca del cesto para poder arrojarlos.

Ya que esta idea ya fue implementada anteriormente, decidimos agregar algunas funcionalidades para distinguirla como información del estado de llenado del tacho con alertas de luces y sonido, y control de la luminosidad de la habitación en la que se encuentre. Además, se brinda la posibilidad de comunicación con una aplicación mobile para Android desde la cual se puede abrir y cerrar la papelera sin la necesidad de estar tan cerca del tacho para que pueda detectar que se desea usarlo. A modo de entretenimiento se ofrece la emisión de una simpática melodía como lo es la Marcha Imperial de Star Wars para distenderse por unos instantes y un modo que llamamos Tacho Loco en que la tapa abre y cierra el tacho rápidamente y de forma indeterminada, y también se puede cambiar a amarillo la luz del led RGB.

## Hardware utilizado
    
### Sistema embebido Arduino
1 placa de prototipado Arduino Uno<br>
1 cable USB puerto serial Arduino Uno 30cm<br>
1 mini Protoboard 830 puntos<br>
1 kit de jumpers con sockets<br>
1 tira de headers macho-macho<br>
6 resistores de 220Ω 5%<br>
1 resistor de 1KΩ 5%<br>
1 módulo Bluetooth HC06<br>
1 LED rojo 5mm<br>
1 LED verde 5mm<br>
1 LED azul 5mm<br>
1 módulo LED RGB<br>
1 pulsador Tact Switch 6mm x 6mm x 5mm<br>
1 micro servo motor Tower Pro Sg90 1.2kg<br>
1 Piezo Buzzer activo 5v<br>
1 fotorresistor Ldr 5mm<br>
2 sensor PIR HC Sr501<br>
1 sensor IR Pcb A0106<br>
1 papelera<br>
1 fuente transformador 9v 1a<br>

### Aplicación Android
Smartphones con sistema operativo Android (para probar la app)<br>
Sensor de proximidad<br>
Acelerómetro<br>
Giroscopio<br>

## Software utilizado

IDE Arduino Genuino 1.8.7 (para el desarrollo del sistema embebido para la placa Arduino Uno)<br>
IDE Android Studio 3.2.1 (para el desarrollo de la aplicación mobile para Android)<br>
Android SDK 28.0.1 (software development kit para Android)<br>
ISO Android 4.0.3 Ice Cream Sandwich (para la simulación de la aplicación mobile para Android)<br>
Tinkercad Autodesk 2018 (para la simulación previa implementación del sistema embebido Arduino)<br>
