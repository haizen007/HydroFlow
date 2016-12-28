package online.hydroflow.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputName;
    private EditText inputCPF;
    private EditText inputPhone;
    private EditText inputAddress;
    private EditText inputCEP;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = (EditText) findViewById(R.id.tv_name);
        inputCPF = (EditText) findViewById(R.id.tv_cpf);
        inputPhone = (EditText) findViewById(R.id.tv_phone);
        inputAddress = (EditText) findViewById(R.id.tv_address);
        inputCEP = (EditText) findViewById(R.id.tv_cep);
        inputEmail = (EditText) findViewById(R.id.tv_email);
        inputPassword = (EditText) findViewById(R.id.tv_password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Function to store user in MySQL database will post params
     */
    private void registerUser(final String nome, final String cpf, final String telefone, final String endereco, final String cep, final String email, final String senha) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        // get TXT for pDialog
        String txt = getString(R.string.pDialog_register);
        pDialog.setMessage(txt);
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("ERROR");

                    // Check for error node in json
                    if (!error) {
                        // User successfully stored into MySQL, retriving its JSON data
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

                        // Now store the user into Android using SQLite
                        db.addUser(id, nome, cpf, telefone, endereco, cep, email, senha, data_criado);
                        // Changed to TRUE for auto Login after Register
                        session.setLogin(true);

                        // get TXT for Toast
                        String txt = getString(R.string.toast_register_ok);
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();

                        // Launch LoginActivity to checks for setLogin, if TRUE, then > MainActivity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error occurred in registration. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to URL register.php
                Map<String, String> params = new HashMap<String, String>();
                params.put("nome", nome);
                params.put("cpf", cpf);
                params.put("telefone", telefone);
                params.put("endereco", endereco);
                params.put("cep", cep);
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

    // Register Button Click event
    public void Register(View v) {
        String name = inputName.getText().toString().trim();
        String cpf = inputCPF.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String cep = inputCEP.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cpf.isEmpty()) {
            String txt = getString(R.string.toast_register_data_null);
            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
//        } else if (!isEmailValid(email)) {
//            String txt = getString(R.string.toast_error_email);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
//        } else if (!isPasswordValid(password)) {
//            String txt = getString(R.string.toast_error_password);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
        } else {
            // Register user
            registerUser(name, cpf, phone, address, cep, email, password);
        }
    }

    //  Link to Login Screen
    public void goLoginActivity(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    //  go back to Login Screen
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    // Check for a valid email
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // Check for a password longer than 7
    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

}
