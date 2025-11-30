package com.grupo8.appfinanza;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Registrarse extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    ImageView btnGoogle;
    TextView txtIrLogin;
    EditText txtNombre, txtCorreo, txtPassword;
    Button btnRegistrar;
    UserDatabase userDB;

    // Para recibir resultado
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtIrLogin = findViewById(R.id.txtIrLogin);

        txtIrLogin.setOnClickListener(v -> {
            // Abrir la actividad de login
            Intent intent = new Intent(Registrarse.this, MainActivity.class);
            startActivity(intent);
            finish(); // opcional, si no quieres que vuelva a registrarse al presionar back
        });


        txtNombre = findViewById(R.id.editTxtNombreRegister);
        txtCorreo = findViewById(R.id.editTxtEmailRegister);
        txtPassword = findViewById(R.id.editTxtPassRegister);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // BASE DE DATOS
        userDB = new UserDatabase(this);
        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        // BOTÓN GOOGLE
        btnGoogle = findViewById(R.id.btnGoogleRegister);

        // CONFIGURAR GOOGLE
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, options);

        // SIEMPRE MOSTRAR SELECCIÓN DE CUENTA
        googleSignInClient.revokeAccess();

        // Registrar el resultado
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();

                        if (account != null) {
                            String nombre = account.getDisplayName();
                            String correo = account.getEmail();
                            String fotoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

                            Toast.makeText(this, "Registrado: " + nombre, Toast.LENGTH_SHORT).show();

                            // ENVIAR A OTRA PANTALLA O GUARDAR EN SQLITE
                            Intent intent = new Intent(Registrarse.this, MainActivity.class);
                            intent.putExtra("nombre", nombre);
                            intent.putExtra("correo", correo);
                            intent.putExtra("foto", fotoUrl);
                            startActivity(intent);
                            finish();
                        }
                    }
                }


        );


        // Acción del botón
        btnGoogle.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(intent);
        });
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el correo ya existe
        if (userDB.existsByEmail(correo)) {
            Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insertar (google = false)
        long result = userDB.insertUser(nombre, correo, password, false);

        if (result > 0) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            // Enviar a MainActivity
            Intent intent = new Intent(Registrarse.this, MainActivity.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("correo", correo);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
        }
    }
}
