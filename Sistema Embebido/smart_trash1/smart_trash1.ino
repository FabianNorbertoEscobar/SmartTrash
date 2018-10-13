#include<Servo.h>

byte ledVerde = 13;
byte ledAzul = 12;
byte ledRojo = 8;

byte rgbRojo = 11;
byte rgbVerde = 10;
byte rgbAzul = 9;

byte pir3 = 7;
byte pir2 = 6;
byte pir1 = 5;

byte microServo = 4;
byte piezoBuzzer = 3;
byte pulsador = 2;
byte fotoResistor = A0;

Servo servo;

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
}

void loop()
{
  
}
