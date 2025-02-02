package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class HomeworkViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Homework>> homeworkList = new MutableLiveData<>(new ArrayList<>());

    public HomeworkViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Homework>> getHomeworkList() {
        return homeworkList;
    }

    public void addHomework(Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList != null) {
            currentList.add(homework);
            homeworkList.setValue(currentList);
        }
    }

    public void deleteHomework(Homework homework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList != null) {
            currentList.remove(homework);
            homeworkList.setValue(currentList);
        }
    }

    public void updateHomework(Homework oldHomework, Homework newHomework) {
        List<Homework> currentList = homeworkList.getValue();
        if (currentList != null) {
            int index = currentList.indexOf(oldHomework);
            if (index != -1) {
                currentList.set(index, newHomework);
                homeworkList.setValue(currentList);
            }
        }
    }


}
