package com.example.gymiot.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gymiot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PerfilFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;

    private EditText editTextName;
    private Button buttonHobbie1, buttonHobbie2, buttonHobbie3;
    private ImageView profileImageView;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profileImages");

        // Obtener los elementos de la interfaz
        editTextName = view.findViewById(R.id.editTextName);
        buttonHobbie1 = view.findViewById(R.id.buttonHobbie1);
        buttonHobbie2 = view.findViewById(R.id.buttonHobbie2);
        buttonHobbie3 = view.findViewById(R.id.buttonHobbie3);
        profileImageView = view.findViewById(R.id.profileImageView);

        ImageView editProfileImageButton = view.findViewById(R.id.editProfileImageButton);
        Button saveProfileButton = view.findViewById(R.id.saveProfileButton);

        // Cargar los datos del perfil actual
        loadUserProfile();

        // Listener para editar el hobbie al hacer clic en el botón
        buttonHobbie1.setOnClickListener(v -> openEditDialog(buttonHobbie1));
        buttonHobbie2.setOnClickListener(v -> openEditDialog(buttonHobbie2));
        buttonHobbie3.setOnClickListener(v -> openEditDialog(buttonHobbie3));

        // Guardar los cambios en Firebase
        saveProfileButton.setOnClickListener(v -> saveUserProfile());

        // Listener para cambiar la imagen de perfil
        editProfileImageButton.setOnClickListener(v -> openFileChooser());

        return view;
    }

    // Método para abrir un cuadro de diálogo para editar el hobbie
    private void openEditDialog(Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edita tu hobbie");

        final EditText input = new EditText(getContext());
        input.setText(button.getText().toString());
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newHobbie = input.getText().toString();
            button.setText(newHobbie);  // Actualizar el botón con el nuevo hobbie
            saveUserProfile();  // Guardar los cambios en Firebase
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Método para cargar el perfil del usuario
    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    editTextName.setText(documentSnapshot.getString("name"));
                    buttonHobbie1.setText(documentSnapshot.getString("hobbie1"));
                    buttonHobbie2.setText(documentSnapshot.getString("hobbie2"));
                    buttonHobbie3.setText(documentSnapshot.getString("hobbie3"));

                    // Cargar la imagen de perfil desde Firebase Storage
                    String imageUrl = documentSnapshot.getString("profileImageUrl");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this).load(imageUrl).into(profileImageView);
                    }
                }
            });
        }
    }

    // Método para guardar el perfil del usuario
    private void saveUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("name", editTextName.getText().toString());
            userProfile.put("hobbie1", buttonHobbie1.getText().toString());
            userProfile.put("hobbie2", buttonHobbie2.getText().toString());
            userProfile.put("hobbie3", buttonHobbie3.getText().toString());

            if (imageUri != null) {
                // Subir la imagen de perfil a Firebase Storage
                StorageReference fileRef = storageReference.child(userId + ".jpg");
                fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        userProfile.put("profileImageUrl", uri.toString());
                        db.collection("users").document(userId)
                                .set(userProfile)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Perfil actualizado", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al actualizar perfil", Toast.LENGTH_SHORT).show());
                    });
                });
            } else {
                // Guardar solo los datos si no se cambia la imagen
                db.collection("users").document(userId)
                        .set(userProfile)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Perfil actualizado", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al actualizar perfil", Toast.LENGTH_SHORT).show());
            }
        }
    }

    // Método para abrir el selector de archivos
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
