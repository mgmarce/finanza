package com.grupo8.appfinanza;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

public class Inicio extends AppCompatActivity {
    MaterialCardView btnIrCategorias, btnIrGastos, btnIrEstadisticas, btnIngresos;
    Button btnCerrarSesion;
    ShapeableImageView imageView;
    TextView txtnombre, txtCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String nombre = getIntent().getStringExtra("nombre");
        String correo = getIntent().getStringExtra("correo");
        String foto = getIntent().getStringExtra("foto");

        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtCorreo = findViewById(R.id.txtCorreo);
        ShapeableImageView imgUsuario = findViewById(R.id.imgViewUsuario);

        txtNombre.setText(nombre);
        txtCorreo.setText(correo);

        if (foto != null && !foto.isEmpty()) {
            Glide.with(this).load(foto).into(imgUsuario);
        } else {
            imgUsuario.setImageResource(R.drawable.usuario);
        }

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Inicio.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnIngresos = findViewById(R.id.btnIngresos);
        btnIngresos.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Ingresos.class);
            startActivity(intent);
        });
        btnIrCategorias = findViewById(R.id.btnIrCategorias);
        btnIrCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Categorias.class);
            startActivity(intent);
        });
        btnIrGastos = findViewById(R.id.btnIrGastos);
        btnIrGastos.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Gastos.class);
            startActivity(intent);
        });

        btnIrEstadisticas = findViewById(R.id.btnEstadistica);
        btnIrEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Estadisticas.class);
            startActivity(intent);
        });
    }
}