package online.hydroflow.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import online.hydroflow.R;
import online.hydroflow.AppController;
import online.hydroflow.chart.Vendor;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;
import online.hydroflow.utils.Constants;

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

    private final Vendor vendor = new Vendor();

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

        // Creates the mask pattern CPF
        final MaskedTextChangedListener maskCPF = new MaskedTextChangedListener(
                "[000].[000].[000]-[00]",
                true,
                inputCPF,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                    }
                }
        );
        // Mask the listener CPF
        inputCPF.addTextChangedListener(maskCPF);

        // Creates the mask pattern PHONE
        final MaskedTextChangedListener maskPHONE = new MaskedTextChangedListener(
                "([00]) [00000]-[0000]",
                true,
                inputPhone,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                    }
                }
        );
        // Mask the listener PHONE
        inputPhone.addTextChangedListener(maskPHONE);

        // Creates the mask pattern CEP
        final MaskedTextChangedListener maskCEP = new MaskedTextChangedListener(
                "[00000]-[000]]",
                true,
                inputCEP,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                    }
                }
        );
        // Mask the listener CEP
        inputCEP.addTextChangedListener(maskCEP);

        Log.d(TAG, "##### RegisterActivity - OK #####");
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
                Constants.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "##### PHP Register Response: " + response.toString() + " #####");
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("ERROR");

                    // Check for error node in json
                    if (!error) {
                        // User successfully stored into MySQL, retriving its JSON data from PHP
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

                        vendor.addIntent(RegisterActivity.this, MainActivity.class);

                        String txt = getString(R.string.toast_register_ok);
                        vendor.addToast(txt, RegisterActivity.this);

                    } else {
                        // Error occurred in registration. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        vendor.addToast(errorMsg, RegisterActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    vendor.addToast(getString(R.string.toast_error_password_conn), RegisterActivity.this);
                } else {
                    vendor.addToast(error.getMessage(), RegisterActivity.this);
                }
                Log.e(TAG, "##### Registration Error: " + error.getMessage() + " #####");
                hideDialog();
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to URL register.php
                Map<String, String> params = new HashMap<>();
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
            vendor.addToast(txt, RegisterActivity.this);
        } else if (!Vendor.isEmailValid(email)) {
            String txt = getString(R.string.toast_error_email);
            vendor.addToast(txt, RegisterActivity.this);
        } else if (!Vendor.isPasswordValid(password)) {
            String txt = getString(R.string.toast_error_password_register);
            vendor.addToast(txt, RegisterActivity.this);
        } else {
            registerUser(name, cpf, phone, address, cep, email, password);
        }
    }

    // Link to Login Screen
//    public void goLoginActivity(View v) {
//        vendor.addIntent(RegisterActivity.this, LoginActivity.class);
//    }

    // Go to Login Activity
    public void onBackPressed() {
        vendor.addIntent(RegisterActivity.this, LoginActivity.class);
    }

}
