package com.aaa.gerrix.volleycache.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.gerrix.volleycache.R;
import com.aaa.gerrix.volleycache.model.Lost;

public class DetailActivity extends AppCompatActivity {

    private TextView title, id;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initUi();
        setView();

    }

    private void setView() {
        Bundle bundle = getIntent().getExtras();
        byte[] image1 = bundle.getByteArray("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(image1, 0, image1.length);

        Lost lost = bundle.getParcelable("lost");

        title.setText(lost.getTitle());
        id.setText(String.valueOf(lost.getId()));
        image.setImageBitmap(bitmap);
    }

    private void initUi() {
        title = (TextView) findViewById(R.id.title);
        id = (TextView) findViewById(R.id.id);
        image = (ImageView) findViewById(R.id.imageView);
    }
}
