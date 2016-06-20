package gelber.turismo;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gelber.turismo.bean.*;
import gelber.turismo.volley.WebService;

public class Login extends AppCompatActivity {

    private TextView txtCorreo, txtPassword;
    private Button btnLogin, btnRegistro;
    private Usuario userLogged = null;

    @Override
    protected void onCreate(Bundle savedInstance){
        getSupportActionBar().hide();
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        txtCorreo = (TextView) findViewById(R.id.txtCorreo);
        txtPassword = (TextView) findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                Map<String, String> params = new HashMap<String, String>();
                String correo = txtCorreo.getText().toString();
                String password = txtPassword.getText().toString();
                params.put("nick", txtCorreo.getText().toString());
                params.put("contrasena", txtPassword.getText().toString());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebService.autenticar,
                        new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray listaUsuarios=response.getJSONArray("user");
                            if(listaUsuarios.length()>0){
                                JSONObject user=listaUsuarios.getJSONObject(0);
                                //Snackbar.make(, "Bienvenido " + user.getString("nick"), Snackbar.LENGTH_LONG).show();
                                userLogged =new Usuario(
                                      user.getInt("id_Usuario"),
                                        user.getString("nombre"),
                                        user.getString("nick"),
                                        user.getString("correo"),
                                        user.getString("contrasena"),
                                        response.getString("token"),
                                        response.getString("exp")
                                );
                                startActivity(new Intent(Login.this, Inicio.class));
                            }else{
                                Snackbar.make(v, "Verifique sus credenciales", Snackbar.LENGTH_LONG).show();
                            }
                        }catch(Exception ex){
                            Log.e("Response exception ",ex.getMessage());
                        }

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError err){
                       // Log.d("Error:", err.getMessage());
                    }
                }

                );
                WebService.getInstance(v.getContext()).addToRequestQueue(request);
            }
        });
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Inicio.class));
            }
        });
    }

}
