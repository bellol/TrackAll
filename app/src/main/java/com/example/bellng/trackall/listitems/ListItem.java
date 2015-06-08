package com.example.bellng.trackall.listitems;

import com.example.bellng.trackall.DatabaseHelper;

/**
 * Created by bellng on 10/05/2015.
 */
public interface ListItem {

    String getTitle();

    String getDescription();

    String getImageURL();

    void update();

    void addToDatabase(DatabaseHelper dbHelper);

    void deleteFromDatabase(DatabaseHelper dbHelper);

    void editName(DatabaseHelper dbHelper, String name);

    boolean isUpdating();
}
