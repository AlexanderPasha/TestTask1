package com.mannydev.superdealtz.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Класс для получения данных c сервера
 */

public class Controller {
    HttpURLConnection urlConnection;
    URL url;
    BufferedReader reader;


    public Controller(){
        urlConnection = null;
        url=null;
        reader = null;

    }
    //Получаем стрим
    public InputStream getInputStream(String link) throws IOException {
        url = new URL(link);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        return inputStream;
    }
    //Читаем стрим и возвращаем строку в JSON формате
    public String getJSONFromStream(InputStream inputStream) throws IOException {
        if(inputStream!=null){
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        }
        return null;
    }
}
