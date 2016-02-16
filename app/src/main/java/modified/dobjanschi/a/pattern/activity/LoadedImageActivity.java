package modified.dobjanschi.a.pattern.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import modified.dobjanschi.a.pattern.R;

/**
 * @author Rustem
 */
public class LoadedImageActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "http://www.planwallpaper.com/static/images/i-should-buy-a-boat.jpg";
    private ImageView imageView;

    public static void start(Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, LoadedImageActivity.class), null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loaded_image);
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Picasso.with(this).load(IMAGE_URL).into(imageView);
    }
}