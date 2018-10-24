#include<Servo.h>

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

//variables
Servo servo;
int papeles = 0;

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

void setup()
{
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
  servo.write(0);
  digitalWrite(ledVerde, HIGH);

  //calibrar pir
  for(int i = 0; i > 30; i++)
  {
    delay(1000);
  }
  delay(50);
}

void loop()
{ 
  if(detectaProximidad() && !tachoLleno()){
    abrirTacho();
    tirarPapel();
  }else{
    cerrarTacho();
  }

  if(tachoLleno()){
    alarma();
  }
  
  if(pulsadorPresionado()){
    vaciarTacho();
  }

  if(muchaLuz()){
    darkblueRGB();
  }else{
    magentaRGB();
  }
}

boolean detectaProximidad(){
  return digitalRead(pir1)|| digitalRead(pir2);
}

boolean tachoLleno(){
  return digitalRead(ir);
}

void abrirTacho(){
  servo.write(180);
  digitalWrite(ledAzul, HIGH);
  delay(500);
}

void tirarPapel(){
  papeles++;
}

void cerrarTacho(){
  servo.write(0);
  digitalWrite(ledAzul, LOW);
  delay(500);
}

void alarma(){
  digitalWrite(ledVerde, LOW);
  digitalWrite(ledRojo, HIGH);
  marchaImperial();
}

void marchaImperial()
{
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

void tono(int frecuencia, int duracion)
{
  tone(piezoBuzzer, frecuencia, duracion);
  delay(duracion);
  noTone(piezoBuzzer);
  delay(50);
}

void primeraSeccion()
{
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

void segundaSeccion()
{
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

boolean pulsadorPresionado(){
  return digitalRead(pulsador);
}

void vaciarTacho(){
  papeles = 0;
  digitalWrite(ledRojo, LOW);
  digitalWrite(ledVerde, HIGH);
}

boolean muchaLuz(){
  return analogRead(fotoResistor) < 550;
}

void darkblueRGB(){
  digitalWrite(rgbRojo, 0);
  digitalWrite(rgbVerde, 0);
  digitalWrite(rgbAzul, 139);
}

void magentaRGB(){
  digitalWrite(rgbRojo, 255);
  digitalWrite(rgbVerde, 0);
  digitalWrite(rgbAzul, 255);
}
