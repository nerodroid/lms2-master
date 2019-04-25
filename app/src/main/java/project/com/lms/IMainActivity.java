package project.com.lms;

import project.com.lms.models.Note;

/**
 * Created by User on 5/14/2018.
 */

public interface IMainActivity {

    void createNewNote(String title, String content);

    void updateNote(Note note);

    void onNoteSelected(Note note);

    void deleteNote(Note note);
}
