package com.manish.redditapp.redditapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.manish.redditapp.redditapp.model.Feed;
import com.manish.redditapp.redditapp.model.entry.Entry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "https://www.reddit.com/r/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);

        Call<Feed> call = feedAPI.getFeed();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
               // Log.d(TAG, "onResponse: Feed : " + response.body().getEntrys());
                Log.d(TAG, "onResponse: Server Response : " + response.toString());

                List<Entry> entrys = response.body().getEntrys();

                Log.d(TAG, "onResponse: entrys : " + entrys.toString());


                ArrayList<Post> posts = new ArrayList<>();
                for(int i = 0; i<entrys.size(); i++){
                    ExtractXML extractXML1 = new ExtractXML(entrys.get(0).getContent(), "<a href=");
                    List<String> postContent = extractXML1.start();

                    ExtractXML extractXML2 = new ExtractXML(entrys.get(0).getContent(), "<img src=");
                    try{
                        postContent.add(extractXML2.start().get(0));
                    }catch (NullPointerException e){
                        postContent.add(null);
                        Log.d(TAG, "onResponse: NullpointerException " + e.getMessage());
                    }catch (IndexOutOfBoundsException e){
                        postContent.add(null);
                        Log.d(TAG, "onResponse: IndexOutOfBoundsException " + e.getMessage());
                    }

                    int lastPosition = postContent.size()-1;
                    posts.add(new Post(
                            entrys.get(i).getTitle(),
                            entrys.get(i).getAuthor().getName(),
                            entrys.get(i).getUpdated(),
                            postContent.get(0),
                            postContent.get(lastPosition)
                    ));

                }

                for (int j = 0; j<posts.size(); j++){
                    Log.d(TAG, "onResponse: \n"+
                            "PostURL: " + posts.get(j).getPostURL() + "\n" +
                            "ThumbnailURL: " + posts.get(j).getThumbnailURL() + "\n" +
                            "Title: " + posts.get(j).getTitle() + "\n"+
                            "Author: " + posts.get(j).getAuthor() + "\n" +
                            "updated: " + posts.get(j).getDate_updated() + "\n" );
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.d(TAG, "onFailure: unable to retrieve RSS feed. " + t.getMessage());
                Toast.makeText(MainActivity.this, "An error occured.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
