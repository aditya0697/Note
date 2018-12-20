package com.example.adityapatel.note;

import java.util.List;

public interface AllUserNoteConsumer<T>  {
    void consume(List<NoteData> objects);
}
