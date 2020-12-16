package com.example.quickdate.utility;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class UploadImageProcessAsyncTask extends AsyncTask<Void, Double, Void> {
    Double progress;
    ConstraintLayout constraintLayout;

    public UploadImageProcessAsyncTask(ConstraintLayout constraintLayout, Double progress){
        this.constraintLayout = constraintLayout;
        this.progress = progress;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        publishProgress(progress);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
        Snackbar.make(constraintLayout, progress + "% Upload", 1000).show();
    }
}
