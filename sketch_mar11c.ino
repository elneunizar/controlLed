#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>

int led=D0;
ESP8266WiFiMulti WiFiMulti;
void setup() {
  Serial.begin(115200);
  WiFiMulti.addAP("Omah Joglo","87654321");

  pinMode(led,OUTPUT);
  digitalWrite(led,LOW);

}

void loop() {
  String json;
  if(WiFiMulti.run()==WL_CONNECTED) {
    HTTPClient http;

    //URL server yang dipakai
    http.begin("http://192.168.1.115/data.json");

    // http get status code
    int httpCode=http.GET();
    if(httpCode>0) {
     // Serial.printf("HTTP 1.1 GET code: %d\n",httpCode);

      if(httpCode==HTTP_CODE_OK) {
        json = http.getString();
        Serial.println(json);

        if(json == "{\"switch\":\"1\"}") {
        digitalWrite(led,HIGH);
        Serial.println("Turn On Led");
        //
      } else {
        digitalWrite(led,LOW);
        Serial.println("Turn Off Led");
      }
    }
    else {
      Serial.printf("HTTP 1.1 GET failed, error: %s\n",http.errorToString(httpCode).c_str());
    }
    
    }
  }
}
