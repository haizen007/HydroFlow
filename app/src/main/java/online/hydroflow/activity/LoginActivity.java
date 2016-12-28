package online.hydroflow.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import online.hydroflow.R;
import online.hydroflow.app.AppConfig;
import online.hydroflow.app.AppController;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.tv_email);
        inputPassword = (EditText) findViewById(R.id.tv_password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * function to verify login details in MySQL DB
     */
    private void checkLogin(final String email, final String senha) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        // Get TXT for pDialog
        String txt = getString(R.string.pDialog_login);
        pDialog.setMessage(txt);
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("ERROR");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        String id = jObj.getString("ID");

                        JSONObject usuario = jObj.getJSONObject("Usuario");

                        String nome = usuario.getString("NOME");
                        String cpf = usuario.getString("CPF");
                        String telefone = usuario.getString("TELEFONE");
                        String endereco = usuario.getString("ENDERECO");
                        String cep = usuario.getString("CEP");
                        String email = usuario.getString("EMAIL");
                        String senha = usuario.getString("SENHA");
                        String data_criado = usuario.getString("DATA_CRIADO");

                        // Inserting row in users table
                        db.addUser(id, nome, cpf, telefone, endereco, cep, email, senha, data_criado);
                        // Create login session
                        session.setLogin(true);
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
//                        Error in login. Get the error message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        String txt = getString(R.string.toast_login_faild);
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to URL login.php
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("senha", senha);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // Login button, login then go to Main
    public void Login(View v) {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data in the form
        if (email.isEmpty() || password.isEmpty()) {
            String txt = getString(R.string.toast_login_data_null);
            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
//        } else if (!isEmailValid(email)) {
//            String txt = getString(R.string.toast_error_email);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
        } else {
            // Login user
            checkLogin(email, password);
        }
    }

    // Link to Register Screen
    public void goRegisterActivity(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    // Check for a valid email
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
