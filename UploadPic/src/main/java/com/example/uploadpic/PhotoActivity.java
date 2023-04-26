package com.example.uploadpic;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.uploadpic.network.ServiceCreator;
import com.example.uploadpic.network.UploadPictureService;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class PhotoActivity extends AppCompatActivity {

    List<ImageBean> imageBeanList = new ArrayList<>();

    PhotoAdapter photoAdapter;

    MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        myHandler = new MyHandler(this);

        RecyclerView rec = findViewById(R.id.rv_photo);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(staggeredGridLayoutManager);

        photoAdapter = new PhotoAdapter(imageBeanList);
        rec.setAdapter(photoAdapter);

        findViewById(R.id.img_cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHandler.sendEmptyMessage(MyHandler.FINISH_ACTIVITY);
            }
        });

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = photoAdapter.getUri();
                if (filePath == null) {
                    Toast.makeText(PhotoActivity.this, "你还未选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                UploadPictureService service = ServiceCreator.INSTANCE.create(UploadPictureService.class);
                File file = new File(filePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                service.uploadPictureRx(body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.d("Daisy", "返回的内容：" + responseBody.string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Daisy", "文件上传失败" + e);
                        myHandler.sendEmptyMessage(MyHandler.FINISH_ACTIVITY);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Daisy", "文件上传完成");
                        Toast.makeText(PhotoActivity.this, "文件上传成功", Toast.LENGTH_SHORT).show();
                        myHandler.sendEmptyMessage(MyHandler.FINISH_ACTIVITY);
                    }
                });
            }
        });

        applyForRight();

    }

    /**
     * 获取图片前需要先进行权限检查
     */
    private void applyForRight() {
        PermissionX.init(PhotoActivity.this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                if (allGranted) {
                    myHandler.sendEmptyMessage(MyHandler.GET_PHOTO);
                } else {
                    Toast.makeText(PhotoActivity.this, "你拒绝了以下权限:" + deniedList, Toast.LENGTH_LONG).show();
                    myHandler.sendEmptyMessage(MyHandler.FINISH_ACTIVITY);
                }
            }
        });
    }

    /**
     * 从手机中获得图片
     */
    private void getPhotoList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageBeanList = FileUtils.initAllImgInThePhone();
                myHandler.sendEmptyMessage(MyHandler.SET_LIST);
            }
        }).start();
    }

    /**
     * 将图片设置到适配器中
     */
    private void setList() {
        photoAdapter.setList(imageBeanList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) myHandler.removeCallbacksAndMessages(null);
    }

    private static class MyHandler extends Handler {
        public static final int GET_PHOTO = 1;
        public static final int FINISH_ACTIVITY = 2;
        public static final int SET_LIST = 3;

        private final WeakReference<PhotoActivity> content;

        private MyHandler(PhotoActivity content) {
            super(Looper.myLooper());
            this.content = new WeakReference<>(content);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            PhotoActivity activity = content.get();
            if (activity != null) {
                switch (msg.what) {
                    case GET_PHOTO:
                        activity.getPhotoList();
                        break;
                    case FINISH_ACTIVITY:
                        activity.finish();
                        break;
                    case SET_LIST:
                        activity.setList();
                        break;
                }
            }
        }
    }

}