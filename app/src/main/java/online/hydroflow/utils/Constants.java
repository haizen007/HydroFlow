package online.hydroflow.utils;

import java.text.SimpleDateFormat;

public class Constants {

    public static final String URL_LOGIN = "http://hydroflow.online/android/v1.3.1/login.php";
    public static final String URL_REGISTER = "http://hydroflow.online/android/v1.3.1/register.php";

    public static final String FIREBASE_VALUE_USUARIO = "usuario";
    public static final String FIREBASE_VALUE_ATUAL_CONSUMO = "atualConsumo";
    public static final String FIREBASE_VALUE_ATUAL_HORA = "atualHora";


    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
//    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
