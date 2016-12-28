package online.hydroflow.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.helper.Mask;
import com.redmadrobot.inputmask.model.CaretString;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import online.hydroflow.R;
import online.hydroflow.app.AppConfig;
import online.hydroflow.app.AppController;
import online.hydroflow.app.AppVendor;
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

        // Creates the mask pattern CPF
        MaskedTextChangedListener maskCPF = new MaskedTextChangedListener(
                "[000].[000].[000]-[00]",
                true,
                inputCPF,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onExtracted(@NotNull String value) {
                    }

                    @Override
                    public void onMandatoryCharactersFilled(boolean complete) {
                    }
                }
        );
        // Mask the listener CPF
        inputCPF.addTextChangedListener(maskCPF);

        // Creates the mask pattern PHONE
        MaskedTextChangedListener maskPHONE = new MaskedTextChangedListener(
                "([00]) [00000]-[0000]",
                true,
                inputPhone,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onExtracted(@NotNull String value) {
                    }

                    @Override
                    public void onMandatoryCharactersFilled(boolean complete) {
                    }
                }
        );
        // Mask the listener PHONE
        inputPhone.addTextChangedListener(maskPHONE);

        // Creates the mask pattern CEP
        MaskedTextChangedListener maskCEP = new MaskedTextChangedListener(
                "[00000]-[000]]",
                true,
                inputCEP,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onExtracted(@NotNull String value) {
                    }

                    @Override
                    public void onMandatoryCharactersFilled(boolean complete) {
                    }
                }
        );
        // Mask the listener CEP
        inputCEP.addTextChangedListener(maskCEP);

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
                Log.d(TAG, "##### PHP Register Response: " + response.toString() + " #####");
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

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        String txt = getString(R.string.toast_register_ok);
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();

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

        AppVendor vendor = new AppVendor();

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
        } else if (!vendor.isEmailValid(email)) {
            String txt = getString(R.string.toast_error_email);
            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
        } else if (!vendor.isPasswordValid(password)) {
            String txt = getString(R.string.toast_error_password_register);
            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
        } else {
            registerUser(name, cpf, phone, address, cep, email, password);
        }
    }

    // Link to Login Screen
    public void goLoginActivity(View v) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    //  go back to Login Screen
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
