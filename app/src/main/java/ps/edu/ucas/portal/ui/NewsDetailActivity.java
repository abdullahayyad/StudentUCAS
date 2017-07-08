package ps.edu.ucas.portal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

 import com.squareup.picasso.Picasso;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.NewsObject;

/**
 * Created by Ayyad on 11/29/2015.
 */
public class NewsDetailActivity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.layout_news);
        final NewsObject newsObject = getIntent().getExtras().getParcelable("news");


        TextView title = (TextView) findViewById(R.id.news_Title);
        TextView date = (TextView) findViewById(R.id.news_date);
        TextView description = (TextView) findViewById(R.id.news_description);
        ImageView image = (ImageView) findViewById(R.id.img_news);
        Button linkNews = (Button) findViewById(R.id.link_news);

        title.setText(newsObject.getTitle());
        date.setText(newsObject.getPubDate());
        description.setText(newsObject.getDescription());

         String urlImage = newsObject.getImageURL();
         if (urlImage != null)
             if (!urlImage.isEmpty())
         Picasso.with(this).load(urlImage).into(image);


        linkNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(newsObject.getLink());
            }
        });
    }



    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


}
