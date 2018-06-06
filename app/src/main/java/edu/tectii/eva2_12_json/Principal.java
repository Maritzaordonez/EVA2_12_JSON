package edu.tectii.eva2_12_json;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Principal extends AppCompatActivity {
    EditText edtTxtNoProd;
    TextView txtVxDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        edtTxtNoProd = (EditText)findViewById(R.id.edtTxtNoProd);
        txtVxDatos = (TextView)findViewById(R.id.txtVwDatos);
    }

    public void onClick(View v){
            new JSONConection().execute(edtTxtNoProd.getText().toString());
    }
    //necesitamos un mecanismo asincrono para conectarnos

    class JSONConection extends AsyncTask<String,Void,String>{
        String sDBPath = "http://10.1.8.189/android_connect/get_data_param.php";
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String sClave = "";
            try {
                                    //odificar la clave del producto
                sClave = "?pid=" + URLEncoder.encode(strings[0],"UTF-8");
                URL url = new URL(sDBPath +sClave);
                HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
                if(httpCon.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader bfReader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                    result = bfReader.readLine();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray productos = null;
            if (s != null){
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    productos = jsonObj.getJSONArray("product");
                    for (int i = 0; i< productos.length(); i++){
                        JSONObject jsRegistro = productos.getJSONObject(i);
                        txtVxDatos.append("Clave = " + jsRegistro.getString("productid") + "\n");
                        txtVxDatos.append("Nombre = " + jsRegistro.getString("productname") + "\n");
                        txtVxDatos.append("Precio Unitario = " + jsRegistro.getString("unitprice") + "\n");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
