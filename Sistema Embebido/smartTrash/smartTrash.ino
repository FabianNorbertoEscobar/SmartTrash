#include<Servo.h>
#include<SoftwareSerial.h>

//pines
byte ledVerde = 13; // tacho con espacio
byte ledAzul = 12; // tapa abierta
byte ledRojo = 8; // tacho lleno

byte rgbRojo = 11; 
byte rgbAzul = 10; 
byte rgbVerde = 9; // cambio de color según luz del ambiente

byte ir = 7; // adentro (nivel)
byte pir2 = 6; // arriba (proximidad)
byte pir1 = 5; // adelante (proximidad)

byte microServo = 4; // controlador de la tapa
byte piezoBuzzer = 3; // alarma marcha imperial
byte pulsador = 2; // vaciar tacho
byte fotoResistor = A0; // luz del ambiente

byte Rx = 0; // Rx Bluetooth
byte Tx = 1; // Tx Bluetooth

//variables
Servo servo;
SoftwareSerial bluetooth(Rx, Tx);
char comando = 's';
bool flagMuchaLuz = false;
int consultaTachoLleno = 0;
unsigned long milisAct;

//frecuencias de la marcha imperial
const float c = 261.63; // Do (Octava 0)
const float d = 293.66; // Re (Octava 0)
const float e = 329.63; // Mi (Octava 0)
const float f = 349.23; // Fa (Octava 0)
const float g = 392.00; // Sol (Octava 0)
const float gS = 415.30;  // Sol# (Octava 0)
const float a = 440.00; // La (Octava 0)
const float b = 466.16; // La# (Octava 0)
const float cH = 523.25;   // Do (Octava 1)
const float cSH = 554.37;  // Do# (Octava 1)
const float dH = 587.33; // Re (Octava 1)
const float dSH = 622.25; // Re# (Octava 1)
const float eH = 659.26; // Mi (Octava 1)
const float fH = 698.46; // Fa (Octava 1)
const float fSH = 739.99; // Fa# (Octava 1)
const float gH = 783.99;  // Sol (Octava 1)
const float gSH = 830.61; // Sol# (Octava 1)
const float aH = 880.00; // La (Octava 1)

void setup(){
  //modo de los pines
  pinMode(ledVerde, OUTPUT);
  pinMode(ledAzul, OUTPUT);
  pinMode(ledRojo, OUTPUT);
  
  pinMode(rgbRojo, OUTPUT);
  pinMode(rgbVerde, OUTPUT);
  pinMode(rgbAzul, OUTPUT);
  
  pinMode(ir, INPUT);
  pinMode(pir2, INPUT);
  pinMode(pir1, INPUT);
  
  servo.attach(microServo);
  pinMode(piezoBuzzer, OUTPUT);
  pinMode(pulsador, INPUT);

  //inicializar componentes
  Serial.begin(9600);
  //servo.write(180);
  digitalWrite(ledVerde, HIGH);
  bluetooth.begin(38400);
  
  //calibrar pir
  for(int i = 0; i > 30; i++){
    delay(1000);
  }
  delay(50);
  milisAct = 0;
  tone(piezoBuzzer, 440, 300);
  noTone(piezoBuzzer);
  tone(piezoBuzzer, 300, 440);
}

void loop(){
  if(bluetooth.available() > 0){
    comando = bluetooth.read();
    
    if(comando == 'b'){
      digitalWrite(ledAzul, HIGH);
      while(comando != 's'){        
        recibirComandoBT();
        bluetooth.flush();
        if(tachoLleno()){
          alarma();
        }
      }
      digitalWrite(ledAzul, LOW);
    }
  }    
  modoSensores();
}

void modoSensores(){
  if(pulsadorPresionado()){
     vaciarTacho();
  }
  if(!tachoLleno())
  {
    if(detectaProximidad()){
      abrirTacho();
    }
    else
    {
      cerrarTacho();
    }
  }
  else{
    alarma();
  }
  /*if(muchaLuz()){
    apagarRGB();
    if(!flagMuchaLuz){
      flagMuchaLuz = true;      
    }
  }else{
    magentaRGB();
    if(flagMuchaLuz){
      flagMuchaLuz = false;      
    }
  }*/
  if(!pocaLuz()){
    apagarRGB();
  }else{
    magentaRGB();
  }
}

boolean detectaProximidad(){
  return digitalRead(pir1) == HIGH || digitalRead(pir2)== HIGH;
}

boolean tachoLleno(){  
  if(digitalRead(ir) == HIGH){
    consultaTachoLleno = 0;
    return false;
  }
  else{
    consultaTachoLleno++;
  }
  
  if(consultaTachoLleno == 1){
    milisAct = millis();
  }

  if(millis() - milisAct >= 5000){
    return true;
  }
  else{
    return false;
  }
}

void abrirTacho(){
  servo.write(90);
  delay(600);
}

void cerrarTacho(){
  servo.write(180);
  delay(600);
}

void alarma(){
  enviarComandoBT('l');
  cerrarTacho();
  digitalWrite(ledVerde, LOW);
  digitalWrite(ledRojo, HIGH);
  while(!pulsadorPresionado()){
    tone(piezoBuzzer, 440, 300);
    noTone(piezoBuzzer);
    tone(piezoBuzzer, 300, 440);
  }
  vaciarTacho();
}

boolean pulsadorPresionado(){
  return digitalRead(pulsador);
}

void vaciarTacho(){
  digitalWrite(ledVerde, HIGH);    
  digitalWrite(ledRojo, HIGH);
  abrirTacho();
  while(!pulsadorPresionado()){
    Serial.println("vaciando");
  }
  digitalWrite(ledRojo, LOW);
  cerrarTacho();
  enviarComandoBT('v');
}

boolean muchaLuz(){
  Serial.println(analogRead(fotoResistor));
  return analogRead(fotoResistor) > 920 && analogRead(fotoResistor) < 1000;
}

boolean pocaLuz(){
  Serial.println(analogRead(fotoResistor));
  return analogRead(fotoResistor) > 920 && analogRead(fotoResistor) < 1000;
}

void apagarRGB(){
  digitalWrite(rgbRojo, 0);
  digitalWrite(rgbVerde, 0);
  digitalWrite(rgbAzul, 0);
}

void magentaRGB(){
  digitalWrite(rgbRojo, 255);
  digitalWrite(rgbVerde, 0);
  digitalWrite(rgbAzul, 255);
}

void darkvioletRGB(){
  digitalWrite(rgbRojo, 148);
  digitalWrite(rgbVerde, 0);
  digitalWrite(rgbAzul, 211);
}

void amarilloRGB(){
  digitalWrite(rgbRojo, 255);
  digitalWrite(rgbVerde, 255);
  digitalWrite(rgbAzul, 0);
}

void recibirComandoBT(){
  if(bluetooth.available() > 0){
    comando = bluetooth.read();
    switch(comando){
      case 'a':
        abrirTacho();
        break;
      case 'c':
        cerrarTacho();
        break;
      case 'v':
        vaciarTacho();
        break;
      case 'j':
        tachoLoco();
        break;
      case 'm':
        modoMarcha();
        break;
      case 'l':
        amarilloRGB();
        break;
      case 'n':
        apagarRGB();
        break;
    }
  }
}

void enviarComandoBT(char comando){
  if(bluetooth.available() > 0){
    bluetooth.write(comando);
  }
}

void modoMarcha(){
  darkvioletRGB();
  marchaImperial();
  magentaRGB();
}

void tachoLoco(){
  for(int i = 0; i < 5; i++){
    abrirTacho();
    delay(600);
    cerrarTacho();
    delay(600);
  }  
}

//MARCHA IMPERIAL------------------------------------
void marchaImperial(){
  primeraSeccion();

  segundaSeccion();

  tono(f, 250);  
  tono(gS, 500);  
  tono(f, 350);  
  tono(a, 125);
  tono(cH, 500);
  tono(a, 375);  
  tono(cH, 125);
  tono(eH, 650);

  delay(500);

  segundaSeccion();

  tono(f, 250);  
  tono(gS, 500);  
  tono(f, 375);  
  tono(cH, 125);
  tono(a, 500);  
  tono(f, 375);  
  tono(cH, 125);
  tono(a, 650);  

  delay(650);
}

void tono(int frecuencia, int duracion){
  tone(piezoBuzzer, frecuencia, duracion);
  delay(duracion);
  noTone(piezoBuzzer);
  delay(50);
}

void primeraSeccion(){
  tono(a, 500);
  tono(a, 500);    
  tono(a, 500);
  tono(f, 350);
  tono(cH, 150);  
  tono(a, 500);
  tono(f, 350);
  tono(cH, 150);
  tono(a, 650);

  delay(500);

  tono(eH, 500);
  tono(eH, 500);
  tono(eH, 500);  
  tono(fH, 350);
  tono(cH, 150);
  tono(gS, 500);
  tono(f, 350);
  tono(cH, 150);
  tono(a, 650);

  delay(500);
}

void segundaSeccion(){
  tono(aH, 500);
  tono(a, 300);
  tono(a, 150);
  tono(aH, 500);
  tono(gSH, 325);
  tono(gH, 175);
  tono(fSH, 125);
  tono(fH, 125);    
  tono(fSH, 250);

  delay(325);

  tono(a, 250);
  tono(dSH, 500);
  tono(dH, 325);  
  tono(cSH, 175);  
  tono(cH, 125);  
  tono(b, 125);  
  tono(cH, 250);  

  delay(350);
}
//FIN MARCHA IMPERIAL
