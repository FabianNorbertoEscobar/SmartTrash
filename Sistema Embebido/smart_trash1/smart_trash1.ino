#include<Servo.h>

byte ledVerde = 13;
byte ledAzul = 12;
byte ledRojo = 8;

byte rgbRojo = 11;
byte rgbVerde = 10;
byte rgbAzul = 9;

byte pir3 = 7; // detecta modo (arriba)
byte pir2 = 6; // detecta nivel (adentro)
byte pir1 = 5; // detecta cercania de la persona (adelante)

byte microServo = 4;
byte piezoBuzzer = 3;
byte pulsador = 2;
byte fotoResistor = A0;

int contadorBasura = 0;
Servo servo;

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
  pinMode(ledVerde, OUTPUT);
  pinMode(ledAzul, OUTPUT);
  pinMode(ledRojo, OUTPUT);
  
  pinMode(rgbRojo, OUTPUT);
  pinMode(rgbVerde, OUTPUT);
  pinMode(rgbAzul, OUTPUT);
  
  pinMode(pir3, INPUT);
  pinMode(pir2, INPUT);
  pinMode(pir1, INPUT);
  
  servo.attach(microServo);
  pinMode(piezoBuzzer, OUTPUT);
  pinMode(pulsador, INPUT);
  
  servo.write(0);
  digitalWrite(ledVerde, HIGH);
  Serial.begin(9600);
  for(int i = 0; i <= 100; i++){
    delay(100);
  }
}

void loop()
{
  if( digitalRead(pir1) == HIGH){
      Serial.println("tapa abierta");
        servo.write(180);
      /*if(digitalRead(pir2) == HIGH){
        contadorBasura++;
        digitalWrite(ledVerde, LOW);
        digitalWrite(ledAzul, HIGH);
      }*/
  }else{
     servo.write(0);
     Serial.println("tapa cerrada");
     /*if(digitalRead(pir2) == HIGH){
        digitalWrite(ledAzul, LOW);
        digitalWrite(ledRojo, HIGH);
     }*/
  }
  
//  noTone(piezoBuzzer);
  if(digitalRead(pulsador) == HIGH){
    digitalWrite(ledVerde, HIGH);
    digitalWrite(ledAzul, LOW);
    digitalWrite(ledRojo, LOW);
    contadorBasura = 0;
    digitalWrite(piezoBuzzer,LOW);
  }else{
    digitalWrite(piezoBuzzer,HIGH);
  }

  //efectoRGB();
  
  //marchaImperial(); 
}

void efectoRGB(){
  analogWrite(rgbRojo, 255);
  analogWrite(rgbVerde, 204);
  analogWrite(rgbAzul, 102);
  
  delay(100);
  
  
  analogWrite(rgbRojo, 51);
  analogWrite(rgbVerde, 204);
  analogWrite(rgbAzul, 255);
  
  delay(100);
  
  for(int i = 0; i < 10; i++){
     
    analogWrite(rgbRojo, 255);
    analogWrite(rgbVerde, 0);
    analogWrite(rgbAzul, 0);
     
    delay(100);
      
    
    analogWrite(rgbRojo, 255);
    analogWrite(rgbVerde, 255);
    analogWrite(rgbAzul, 255);
    
    delay(100);
  }
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
