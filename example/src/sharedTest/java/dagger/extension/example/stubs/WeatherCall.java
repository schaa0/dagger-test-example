package dagger.extension.example.stubs;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherCall<T> implements Call<T>
{

    private Class<T> clazz;
    private String response;
    private boolean isExecuted;

    public WeatherCall(Class<T> clazz, String response)
    {
        this.clazz = clazz;
        this.response = response;
    }

    @Override
    public Response<T> execute() throws IOException
    {
        isExecuted = true;
        T data = new Gson().fromJson(response, clazz);
        return Response.success(data);
    }

    @Override
    public void enqueue(Callback<T> callback)
    {
        try
        {
            callback.onResponse(this, execute());
        } catch (IOException e)
        {
            callback.onFailure(this, e);
        }
    }

    @Override
    public boolean isExecuted()
    {
        return isExecuted;
    }

    @Override
    public void cancel()
    {
        throw new UnsupportedOperationException("not implemented!");
    }

    @Override
    public boolean isCanceled()
    {
        throw new UnsupportedOperationException("not implemented!");
    }

    @Override
    public Call<T> clone()
    {
        throw new UnsupportedOperationException("not implemented!");
    }

    @Override
    public Request request()
    {
        throw new UnsupportedOperationException("not implemented!");
    }
}

