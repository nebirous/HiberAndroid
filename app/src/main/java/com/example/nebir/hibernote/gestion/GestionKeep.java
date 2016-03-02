package com.example.nebir.hibernote.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.nebir.hibernote.BD.Contrato;
import com.example.nebir.hibernote.pojo.Keep;
import com.example.nebir.hibernote.pojo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GestionKeep {

    private String urlDestino = "http://192.168.1.25:8080/Netbeans/go";
    private Context ctx;

    public GestionKeep(Context context) {
        ctx =context;
    }

    public GestionKeep() {
    }

    public List<Keep> consulta(Usuario u) {
        List<Keep> keeps = new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destino = urlDestino + "?tabla=keep&op=read&login=" + login + "&origen=android&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                res += linea;
            }
            in.close();
            JSONObject obj = new JSONObject(res);
            JSONArray array = (JSONArray) obj.get("r");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = (JSONObject) array.get(i);
                Keep keep = new Keep(o.getInt("ida"), o.getString("cont"), 1, u.getEmail());
                keeps.add(keep);
            }
            return keeps;
        } catch (MalformedURLException e) {
            Log.v("Error URL", e.toString());
        } catch (IOException e) {
            Log.v("Error IO", e.toString());
        } catch (JSONException e) {
            Log.v("Error JSON", e.toString());
        }
        return null;
    }

    public List<Keep> actualizarIdAndroid(List<Keep> l, Usuario u) {
        List<Keep> d= new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        List<Keep> uKeep= consulta(u);
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            for (Keep k : l) {
                String contenido= k.getContenido().replaceAll("\\s","|");
                        String destino = urlDestino + "?tabla=keep&op=actualiza&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
                        url = new URL(destino);
                        in = new BufferedReader(new InputStreamReader(url.openStream()));

                    destino = urlDestino + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
                    url = new URL(destino);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));


                    String linea;
                    while ((linea = in.readLine()) != null) {
                        res += linea;
                    }
                    in.close();

                    k.setLogin(u.getEmail());
                    k.setEstado(1);
                    ContentValues values=k.getContentValues();
                    String[] argumentos = { k.getId() + "" };
                    ctx.getContentResolver().update(Contrato.TablaNota.CAMBIARESTADO,values,null,argumentos);

                d.add(k);
            }
            return d;

        } catch (MalformedURLException e) {
            Log.v("Error URL", e.toString());
        } catch (IOException e) {
            Log.v("Error IO", e.toString());
        }
        return null;

    }
    public List<Keep> upload(List<Keep> l, Usuario u) {

        List<Keep> d= new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        List<Keep> uKeep= consulta(u);
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            for (Keep k : l) {
                if(k.getEstado()==0) {
                    String contenido= k.getContenido().replaceAll("\\s","|");

                    if(uKeep.contains(k)){
                        String destino = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
                        url = new URL(destino);

                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                    String destino = urlDestino + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
                    url = new URL(destino);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));

                    String linea;
                    while ((linea = in.readLine()) != null) {
                        res += linea;
                    }
                    in.close();

                    k.setLogin(u.getEmail());
                    k.setEstado(1);
                    ContentValues values=k.getContentValues();
                    String[] argumentos = { k.getId() + "" };
                    ctx.getContentResolver().update(Contrato.TablaNota.CAMBIARESTADO, values, null, argumentos);
                }
                d.add(k);
            }
            return d;

        } catch (MalformedURLException e) {
            Log.v("Error URL", e.toString());
        } catch (IOException e) {
            Log.v("Error IO", e.toString());
        }
        return null;

    }

    public void delete(Keep k){
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(k.getLogin(), "UTF-8");
            String contenido=k.getContenido().replaceAll("\\s","|");
            String destinor = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
            url = new URL(destinor);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            Log.v("Error URL", e.toString());
        } catch (IOException e) {
            Log.v("Error IO", e.toString());
        }

    }

    public void refresh(Keep k){
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(k.getLogin(), "UTF-8");
            String contenido=k.getContenido().replaceAll("\\s","|");
            String destino = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            destino = urlDestino + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + contenido + "&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            Log.v("Error URL", e.toString());
        } catch (IOException e) {
            Log.v("Error IO", e.toString());
        }

    }
}
