package gal.cifpacarballeira.unidad4_tarea7gestordeberes;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    private HomeworkViewModel homeworkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de componentes
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        homeworkViewModel = new ViewModelProvider(this).get(HomeworkViewModel.class);


        // Crear y configurar el adaptador
        adapter = new HomeworkAdapter(new ArrayList<>(), this::showBottomSheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Este código sería lo mismo que la anterior línea
        // adapter = new HomeworkAdapter(homeworkList, this::showBottomSheet);
        // ¿Por qué le paso ese segundo parámetro?
        // Porque le estoy pasando la función que quiero que se lance al hacer click en un elemento
        // Investiga sobre "operador de referencia de método en Java"


        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configuración del botón flotante
        fab.setOnClickListener(v -> showAddHomeworkDialog(null));

        homeworkViewModel.getHomeworkList().observe(this, updatedList -> {
            adapter = new HomeworkAdapter(updatedList, this::showBottomSheet);
            recyclerView.setAdapter(adapter);
        });
    }

    private void showAddHomeworkDialog(Homework homeworkToEdit) {

        NewHomeworkDialogFragment dialog = new NewHomeworkDialogFragment();
        // Pasarle el objeto Homework al diálogo si se está editando
        if (homeworkToEdit != null) {
            Bundle args = new Bundle();
            args.putParcelable("homework", homeworkToEdit);
            dialog.setArguments(args);
        }
        dialog.setOnHomeworkSavedListener(homework -> {
                if (homeworkToEdit == null) {
                    homeworkViewModel.addHomework(homework);
                } else {
                    homeworkViewModel.updateHomework(homeworkToEdit, homework);
                }

                });
        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
//
//        AddHomeworkDialogFragment dialog = AddHomeworkDialogFragment.newInstance(homeworkToEdit);
//        dialog.setOnHomeworkSavedListener(homework -> {
//            if (homeworkToEdit == null) {
//                homeworkList.add(homework);
//            } else {
//                homeworkList.set(homeworkList.indexOf(homeworkToEdit), homework);
//            }
//            adapter.notifyDataSetChanged();
//        });
//        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
    }

    private void showBottomSheet(Homework homework) {
        // Creación del diálogo
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflar el layout del diálogo
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_homework_options, null);

        // Asignar acciones a los botones

        // Opción de editar
        view.findViewById(R.id.editOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showAddHomeworkDialog(homework);
        });

        // Opción de eliminar
        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showDeleteConfirmation(homework);
        });


        // Opción de marcar como completada
        view.findViewById(R.id.completeOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            homework.setCompleted(true);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show();
        });

        // Mostrar el diálogo
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showDeleteConfirmation(Homework homework) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este deber?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    homeworkViewModel.deleteHomework(homework);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
