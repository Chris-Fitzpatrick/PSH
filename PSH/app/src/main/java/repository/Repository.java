package repository;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CFitzpatrick on 10/9/16.
 */

class Repository {

    private Retrofit retrofit;
    private Gson gson= new Gson();
   // APIInterface apiInterface;

    public Repository(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //this.apiInterface = retrofit.create(APIInterface.class);
    }
}
