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

  analogWrite(rgbRojo, 255);
  analogWrite(rgbVerde, 0);
  analogWrite(rgbAzul, 00);
  
  delay(100);
  
  
  analogWrite(rgbRojo, 0);
  analogWrite(rgbVerde, 0);
  analogWrite(rgbAzul, 255);
  
  delay(100);
  
  for(int i = 0; i < 10; i++){
     
    analogWrite(rgbRojo, 255);
    analogWrite(rgbVerde, 0);
    analogWrite(rgbAzul, 0);
     
    delay(100);
      
    
    analogWrite(rgbRojo, 255);
    analogWrite(rgbVerde, 0);
    analogWrite(rgbAzul, 255);
    
    delay(100);
  }
  
  

  
  
}
