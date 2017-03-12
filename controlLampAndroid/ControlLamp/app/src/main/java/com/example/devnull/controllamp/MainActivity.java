package com.example.devnull.controllamp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private boolean valueTurn=false;
    private Button button;
    private EditText ip;
    private TextView check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip=(EditText)findViewById(R.id.ip);
        button=(Button)findViewById(R.id.control);
        check=(TextView)findViewById(R.id.check);
     //   String param;
       // BasicNameValuePair p=
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setText(ip.getText().toString());
                SendPostRequest req=new SendPostRequest();
                valueTurn=!valueTurn;
                if(valueTurn) {
                    button.setText("Turn Off");
                    new SendPostRequest().execute(ip.getText().toString(),"switch=1");
                }
                else {
                    button.setText("Turn On");
                    new SendPostRequest().execute(ip.getText().toString(),"switch=0");
                }
            }
        });
    }

    private class SendPostRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection con=null;
            String ip=params[0];
            try {
                String param=params[1];
                url=new URL("http://"+ip+"/interface.php");
                con=(HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                //con.setRequestProperty("Content-Type","utf-8");
                //con.setRequestProperty("Content-Length",Integer.toString(param.getBytes().length));
                //con.setRequestProperty("Content-Language","en-US");

                con.setDoInput(true);
                con.setDoOutput(true);

                //Send Request
                DataOutputStream wr=new DataOutputStream(con.getOutputStream());
                wr.writeBytes(param);
                wr.flush();
                wr.close();

                //Get Response
                InputStream in=con.getInputStream();
                BufferedReader rd=new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuffer res=new StringBuffer();
                while((line=rd.readLine())!=null) {
                    res.append(line+"\r");
                }
                rd.close();
                return res.toString();
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;

            }
            finally {
                if(con!=null) {
                    con.disconnect();
                }
            }
        }
        @Override
        protected void onPostExecute(String result) {

        }
    }
}
