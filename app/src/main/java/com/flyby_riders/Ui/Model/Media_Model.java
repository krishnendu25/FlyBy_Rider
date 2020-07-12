package com.flyby_riders.Ui.Model;

import android.graphics.Bitmap;

/**
 * Created by KRISHNENDU MANNA on 22,June,2020
 */
public class Media_Model {
    Bitmap bitmap;
    String Photo_Name;
    String File_Name;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPhoto_Name() {
        return Photo_Name;
    }

    public void setPhoto_Name(String photo_Name) {
        Photo_Name = photo_Name;
    }

    public String getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(String file_Name) {
        File_Name = file_Name;
    }
}
