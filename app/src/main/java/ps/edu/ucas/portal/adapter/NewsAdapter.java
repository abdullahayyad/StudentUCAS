package ps.edu.ucas.portal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.NewsObject;
import ps.edu.ucas.portal.view.MyLinearLayout;

/**
 * Created by Ayyad on 7/14/2015.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<NewsObject> news;
    private Context myContext;
    private OnItemClicked onItemClicked;


    public NewsAdapter(Context myContext, ArrayList<NewsObject> news) {
        inflater = LayoutInflater.from(myContext);
        this.myContext = myContext;
        this.news = news;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_news, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(news.get(position).getTitle() + "");
           holder.description.setText(news.get(position).getDescription() + "");
        holder.date.setText(news.get(position).getPubDate() + "");
        String urlImage = news.get(position).getImageURL();
        if (urlImage != null)
            if (!urlImage.isEmpty())
                Picasso.with(myContext).load(urlImage).into(holder.image_news);


    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
         TextView description;
        TextView date;
        ImageView image_news;
        MyLinearLayout mlinear;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
                description = (TextView) itemView.findViewById(R.id.news_detail);
            date = (TextView) itemView.findViewById(R.id.news_date);
            image_news = (ImageView) itemView.findViewById(R.id.image_news);

            mlinear = (MyLinearLayout) itemView.findViewById(R.id.myLinear);
            mlinear.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (onItemClicked != null) {
                onItemClicked.getItemObject(v, news.get(getAdapterPosition()));
            }
        }
    }


    public interface OnItemClicked {
        void getItemObject(View view, NewsObject news);

    }


    public void setOnItemClicked(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

}
