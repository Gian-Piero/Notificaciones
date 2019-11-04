package com.example.notificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static int CORRECTOS = 0;
    public static int INCORRECTOS = 0;
    private EditText txtValor1;
    private EditText txtValor2;
    private EditText txtResultado;
    private Button btnComprobar;

    private TextView txtCorrectas;
    private TextView txtIncorrectas;

    private NotificationManager notificationManager;
    static final String CANAL_ID ="mi_canal";
    static final int NOTIFICACION_ID =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtValor1 = findViewById(R.id.editTextValor1);
        txtValor2 = findViewById(R.id.editTextValor2);
        txtResultado = findViewById(R.id.editTextResultado);
        txtCorrectas = findViewById(R.id.textCorrectas);
        txtIncorrectas = findViewById(R.id.textIncorrectas);

        ponerNumeroAleatorios();
        btnComprobar = findViewById(R.id.buttonComprobar);

        btnComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor1 = Integer.parseInt(txtValor1.getText().toString());
                int valor2 = Integer.parseInt(txtValor2.getText().toString());
                try{
                    int resultado = Integer.parseInt(txtResultado.getText().toString());
                    if (resultado == (valor1+valor2)){
                        MainActivity.CORRECTOS++;
                        cambiarTextos();
                        ponerNumeroAleatorios();
                        if (MainActivity.CORRECTOS == 10)
                        {
                            //Creamos notificacion
                            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            //Creamos el canal. SOLO puede hacerse en dispositivos con ver. 8 o más.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel notificationChannel = new NotificationChannel(
                                        CANAL_ID, "Mis notificaciones",
                                        NotificationManager.IMPORTANCE_DEFAULT);
                                notificationChannel.setDescription("Canal que muestra que se han hecho 10 aciertos");
                                notificationManager.createNotificationChannel(notificationChannel);
                            }

                            //Creo el pendeing intent para que vuelva a lanzar esta actividad al hacer click en la notificacion
                            Intent i = new Intent(MainActivity.this, MainActivity.class);
                            PendingIntent intencionPendiente = PendingIntent.getActivity(MainActivity.this, 0, i, 0);

                            //Pongo los parametros para construir la notificacion
                            NotificationCompat.Builder notibuilder = new NotificationCompat.Builder(MainActivity.this,CANAL_ID);
                            notibuilder.setSmallIcon(R.drawable.ic_suma);
                            notibuilder.setWhen(System.currentTimeMillis()+1000*60*60);
                            notibuilder.setContentTitle("Victoria");
                            notibuilder.setContentText("Has realizado 10 sumas con exito, pulsa aqui para volver a empezar");
                            notibuilder.setContentIntent(intencionPendiente);

                            //Creo la notificacion con el FLAG para que al pulsar se cierre la notifcacion
                            Notification notification = notibuilder.build();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;

                            //Cuando pulse en la notificacion que lance una actividad
                            notificationManager.notify(NOTIFICACION_ID, notification);
                        }
                    }
                    else
                    {
                        MainActivity.INCORRECTOS++;
                        cambiarTextos();
                        ponerNumeroAleatorios();
                    }
                }catch (NumberFormatException e)
                {
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Rellena el resultado", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });
    }

    public void ponerNumeroAleatorios(){
        int valor1 =  (int)((Math.random()*101));
        int valor2 =  (int)((Math.random()*101));

        txtValor1.setText(valor1+ "");
        txtValor2.setText(valor2 + "");
        txtResultado.setText("");
    }

    public void cambiarTextos()
    {
        txtCorrectas.setText("PREGUNTAS CORRECTAS : " + MainActivity.CORRECTOS);
        txtIncorrectas.setText("INCORRECTAS : " + MainActivity.INCORRECTOS);
    }


}
