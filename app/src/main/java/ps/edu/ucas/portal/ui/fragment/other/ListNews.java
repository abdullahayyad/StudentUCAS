package ps.edu.ucas.portal.ui.fragment.other;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.adapter.NewsAdapter;
import ps.edu.ucas.portal.model.NewsObject;
import ps.edu.ucas.portal.model.User;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.ui.MainContainerActivity;
import ps.edu.ucas.portal.ui.NewsDetailActivity;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static ps.edu.ucas.portal.service.WebService.RESULT;

/**
 * Created by Ayyad on 11/27/2015.
 */
public class ListNews extends Fragment implements NewsAdapter.OnItemClicked,WebService.OnResponding {


    static final String URL_NEWS = "url_news";
    static final String LIST_NEWS_NAME = "list_news";

    WebService.RequestAPI url;
    RecyclerView myRecycle;
    NewsAdapter adapter;

    ArrayList<NewsObject> newsObjects;

    LinearLayout waiting, retry_con, main_layout_retry;

    public static ListNews getInstance(WebService.RequestAPI url) {
        ListNews myListEvent = new ListNews();
        Bundle bundle = new Bundle();

        bundle.putSerializable(URL_NEWS, url);

        myListEvent.setArguments(bundle);

        return myListEvent;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            url = (WebService.RequestAPI) bundle.get(URL_NEWS);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_news, container, false);


        myRecycle = (RecyclerView) v.findViewById(R.id.news_list);

        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));


        waiting = (LinearLayout) v.findViewById(R.id.waiting_layout);
        retry_con = (LinearLayout) v.findViewById(R.id.retry_layout);
        main_layout_retry = (LinearLayout) v.findViewById(R.id.main_layout_retry);
        main_layout_retry.setVisibility(View.GONE);


        Button btn_retry = (Button) v.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebService().startRequest(url,null,ListNews.this);
                ShowRetry(false);
                ShowProgress(true);
            }
        });

        new WebService().startOldRequest(url,null,this);
        return v;
    }


    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == url) {
                    try {

                        Log.e("objectResult.gLT)",""+objectResult.get(RESULT).toString());

                        InputStream stream = new ByteArrayInputStream(objectResult.get(RESULT).toString().getBytes());


                        DocumentBuilderFactory documentBuilderFactory=

                                DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder =
                                documentBuilderFactory.newDocumentBuilder();

                        Document document = documentBuilder.parse(stream);
                        Element mainElement = document.getDocumentElement();
                        NodeList nodeListDefinition =
                                mainElement.getElementsByTagName("item");

                        Node currentDefinition;
                        NodeList nodeListDDefinitionChild;
                        Node currentDefinitionChild;
                        NodeList nodeListDictionary;
                        Node currentDictionary;
                        ArrayList<NewsObject> newsObjects = new ArrayList<NewsObject>();

                        for(int i=0;i<nodeListDefinition.getLength();i++){
                            NewsObject newsObject =new NewsObject()  ;
                            currentDefinition = nodeListDefinition.item(i);
                            nodeListDDefinitionChild = currentDefinition.getChildNodes();

                            for(int j=0;j<nodeListDDefinitionChild.getLength();
                                j++){
                                currentDefinitionChild=nodeListDDefinitionChild.item(j);
                                if(currentDefinitionChild.getNodeName().equalsIgnoreCase("title")){
                                    Log.d("title","title = "+currentDefinitionChild.getTextContent());
                                    newsObject.setTitle(currentDefinitionChild.getTextContent());
                                }

                                if(currentDefinitionChild.getNodeName().equalsIgnoreCase("description")){
                                    Log.d("description","description = "+currentDefinitionChild.getTextContent());
                                    newsObject.setDescription(currentDefinitionChild.getTextContent());
                                }

                                if(currentDefinitionChild.getNodeName().equalsIgnoreCase("link")){
                                    Log.d("description","description = "+currentDefinitionChild.getTextContent());
                                    newsObject.setLink(currentDefinitionChild.getTextContent());
                                }


                                if(currentDefinitionChild.getNodeName().equalsIgnoreCase("pubDate")){
                                    Log.d("pubDate","pubDate = "+currentDefinitionChild.getTextContent());
                                    newsObject.setPubDate(currentDefinitionChild.getTextContent());
                                }


                                if(currentDefinitionChild.getNodeName().equalsIgnoreCase("image"))
                                {
                                    nodeListDictionary = currentDefinitionChild.getChildNodes();
                                    for(int k=0;k<nodeListDictionary.getLength();k++)
                                    {
                                        currentDictionary = nodeListDictionary.item(k);

                                        if(currentDictionary.getNodeName().equalsIgnoreCase("url")){
                                            Log.d("url","url = "+currentDictionary.getTextContent());
                                            newsObject.setImageURL(currentDictionary.getTextContent());
                                        }

                                        if(currentDictionary.getNodeName().equalsIgnoreCase("link")){
                                            Log.d("link","link = "+currentDictionary.getTextContent());

                                        }
                                    }
                                }
                            }
                            newsObjects.add(newsObject);
                        }

                        this.newsObjects = newsObjects;
                        ShowProgress(false);
                        if (getActivity() != null) {
                            adapter = new NewsAdapter(getActivity(),  this.newsObjects);
                            myRecycle.setAdapter(adapter);
                            myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter.setOnItemClicked(ListNews.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }

                }

                break;

            default:
                ShowProgress(false);
                ShowRetry(true);
                break;
        }

    }


    @Override
    public void getItemObject(View view, NewsObject news) {
        Intent i = new Intent(view.getContext(), NewsDetailActivity.class);
        i.putExtra("news", news);
        startActivity(i);
    }





    public void ShowRetry(boolean show) {
        if (show) {
            main_layout_retry.setVisibility(View.VISIBLE);
            retry_con.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.GONE);
        } else {
            retry_con.setVisibility(View.GONE);
            main_layout_retry.setVisibility(View.GONE);
        }
    }


    public void ShowProgress(boolean show) {
        if (show) {
            main_layout_retry.setVisibility(View.VISIBLE);
            waiting.setVisibility(View.VISIBLE);
            retry_con.setVisibility(View.GONE);
        } else {

            waiting.setVisibility(View.GONE);
            main_layout_retry.setVisibility(View.GONE);
        }
    }


}
