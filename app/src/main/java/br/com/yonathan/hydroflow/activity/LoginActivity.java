package br.com.yonathan.hydroflow.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.yonathan.hydroflow.R;
import br.com.yonathan.hydroflow.sql.AppController;
import br.com.yonathan.hydroflow.utils.Vendor;
import br.com.yonathan.hydroflow.sql.SQLiteHandler;
import br.com.yonathan.hydroflow.sql.SessionManager;
import br.com.yonathan.hydroflow.utils.Constants;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private final Vendor vendor = new Vendor();

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

        Log.d(TAG, "##### LoginActivity - OK #####");
    }

    // function to verify login details in MySQL DB
    private void loginUser(final String email, final String senha) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        // Get TXT for pDialog
        String txt = getString(R.string.pDialog_login);
        pDialog.setMessage(txt);
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                Constants.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "##### PHP Login Response: " + response.toString() + " #####");
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("ERROR");

                    // Check for error node in json
                    if (!error) {
                        // User successfully Logged In into MySQL, retriving its JSON data from PHP
                        String s00 = jObj.getString("ID");

                        JSONObject usuario = jObj.getJSONObject("Usuario");

                        String s01 = usuario.getString("NOME");
                        String s02 = usuario.getString("CPF");
                        String s03 = usuario.getString("TELEFONE");
                        String s04 = usuario.getString("ENDERECO");
                        String s05 = usuario.getString("CEP");
                        String s06 = usuario.getString("EMAIL");
                        String s07 = usuario.getString("SENHA");
                        String s08 = usuario.getString("DATA_CRIADO");
                        String s09 = usuario.getString("DATA_ATUALIZADO");

                        // Now store these data into Android using SQLite
                        db.addUser(s00, s01, s02, s03, s04, s05, s06, s07, s08, s09);

                        session.setLogin(true);

                        vendor.addIntent(LoginActivity.this, MainActivity.class);

                    } else {
                        String txt = getString(R.string.toast_login_faild);
                        vendor.addToast(txt, LoginActivity.this);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    vendor.addToast("Json error: " + e.getMessage(), LoginActivity.this);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    vendor.addToast(getString(R.string.toast_error_password_conn), LoginActivity.this);
                } else {
                    vendor.addToast(error.getMessage(), LoginActivity.this);
                }
                Log.e(TAG, "##### Login Error: " + error.getMessage() + " #####");
                hideDialog();
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to the URL login.php
                Map<String, String> params = new HashMap<>();
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
            vendor.addToast(txt, LoginActivity.this);
        } else {
            loginUser(email, password);
        }
    }

    // Go to Register Activity
    public void goRegisterActivity(View v) {
        vendor.addIntent(LoginActivity.this, RegisterActivity.class);
    }

}
