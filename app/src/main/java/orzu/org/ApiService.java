package orzu.org;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/generate")
    Call<String> generatePolls();

    @POST("/update")
    Call<String> updatePolls(@Body RequestBody body);
}
