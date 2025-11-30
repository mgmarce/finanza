package com.grupo8.appfinanza;

import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    //QUITAR ESTOS BOTONES
    //Button btnIrCategorias, btnIrGastos, btnIrEstadisticas;
    FirebaseAuth auth;
    ImageView btnGoogle;
    GoogleSignInClient googleSignInClient;
    //ShapeableImageView imageView;
    //TextView txtnombre, txtCorreo;
    TextView txtRegistrarse;

    EditText txtCorreoLogin, txtPasswordLogin;
    Button btnLogin;
    UserDatabase userDB;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode() == RESULT_OK){
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                //auth = FirebaseAuth.getInstance(); //PODRIA DAR ERROR ACA
                                //Glide.with(MainActivity.this).load(auth.getCurrentUser().getPhotoUrl()).into(imageView);
                                //txtnombre.setText(auth.getCurrentUser().getDisplayName());
                                //txtCorreo.setText(auth.getCurrentUser().getEmail());

                                auth = FirebaseAuth.getInstance();

                                // Obtener datos del usuario
                                String nombre = auth.getCurrentUser().getDisplayName();
                                String correo = auth.getCurrentUser().getEmail();
                                String foto = auth.getCurrentUser().getPhotoUrl() != null
                                        ? auth.getCurrentUser().getPhotoUrl().toString()
                                        : "";

                                // ENVIAR DATOS A OTRA ACTIVITY
                                Intent intent = new Intent(MainActivity.this, Inicio.class);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("correo", correo);
                                intent.putExtra("foto", foto);
                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "Conexion existosa", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, "Conexion NO existosa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (ApiException e){
                    e.printStackTrace();
                }
            }

        }

    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, options);


        auth = FirebaseAuth.getInstance();

        ImageView btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut();
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        txtRegistrarse = findViewById(R.id.txtSignup);
        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registrarse.class);
                startActivity(intent);
            }
        });

        txtCorreoLogin = findViewById(R.id.editTxtEmailMain);
        txtPasswordLogin = findViewById(R.id.editTextClaveMain);
        btnLogin = findViewById(R.id.btnIngresarMain);

        userDB = new UserDatabase(this);
        btnLogin.setOnClickListener(v -> loginLocal());

        Cursor c = userDB.getReadableDatabase().rawQuery("SELECT * FROM users", null);
        while (c.moveToNext()) {
            System.out.println(
                    "USER → id=" + c.getInt(0) +
                            " name=" + c.getString(1) +
                            " email=" + c.getString(2) +
                            " password=" + c.getString(3) +
                            " google=" + c.getInt(4)
            );
        }
        c.close();


        /*SignInButton signInButton = findViewById(R.id.btnGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });*/

        /*
        imageView = findViewById(R.id.imgViewUsuario);
        txtnombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        btnIrCategorias = findViewById(R.id.btnIrCategorias);
        btnIrCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Categorias.class);
            startActivity(intent);
        });
        btnIrGastos = findViewById(R.id.btnIrGastos);
        btnIrGastos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Gastos.class);
            startActivity(intent);
        });

        btnIrEstadisticas = findViewById(R.id.btnEstadistica);
        btnIrEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Estadisticas.class);
            startActivity(intent);
        });*/

    }

    private void loginLocal() {

        String correo = txtCorreoLogin.getText().toString().trim();
        String password = txtPasswordLogin.getText().toString().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = userDB.getUser(correo);

        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }

        int idxPassword = cursor.getColumnIndexOrThrow("password");
        int idxName = cursor.getColumnIndexOrThrow("name");

        String dbPassword = cursor.isNull(idxPassword) ? "" : cursor.getString(idxPassword);
        String nombre = cursor.getString(idxName);

        cursor.close();

        if (!password.equals(dbPassword)) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, Inicio.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("correo", correo);
        startActivity(intent);

        Toast.makeText(this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
    }




}