package com.wadektech.mtihanirevise.pdfViewer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.wadektech.mtihanirevise.R;

public class ItemDetailActivity extends AppCompatActivity {
    PDFView pdfView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        //display PDF to viewer
        pdfView = findViewById(R.id.pdf_viewer);
    }
}

