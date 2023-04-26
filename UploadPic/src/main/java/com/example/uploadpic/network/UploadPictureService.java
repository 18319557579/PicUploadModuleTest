package com.example.uploadpic.network;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadPictureService {

    @Multipart
    @POST("ac/user-uploadImage")
    Call<ResponseBody> uploadPicture(@Part MultipartBody.Part picture);

    @Multipart
    @POST("ac/user-uploadImage")
    Observable<ResponseBody> uploadPictureRx(@Part MultipartBody.Part picture);
}
