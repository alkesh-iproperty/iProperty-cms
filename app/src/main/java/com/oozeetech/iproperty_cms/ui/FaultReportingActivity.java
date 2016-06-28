package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.adapter.FaultRepairingAdapter;
import com.oozeetech.iproperty_cms.models.AddFaultResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class FaultReportingActivity extends BaseActivity {

//    @Bind(R.id.gridFaultReporting)
//    GridView gridFaultReporting;
//    @Bind(R.id.llPlaceHolder)
//    LinearLayout llPlaceHolder;
    @Bind(R.id.btnTakePicture)
    Button btnTakePicture;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitle)
    TextView tvTitle;

    EditText etTitle;
    EditText etDesc;

    FaultRepairingAdapter adapter;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imgPath;
    File imgFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_reporting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
//        adapter = new FaultRepairingAdapter(getActivity());
        //gridFaultReporting.setAdapter(adapter);
        tvTitle.setText(getString(R.string.title_fault_reporting));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fault_report,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_view_all){

            Intent intent = new Intent(getActivity(),ViewAllFaultsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnTakePicture)
    public void onTakePicture(View v){

        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            imgFile = null;
            try {
                imgFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fault_details,null);
            etTitle = (EditText) v.findViewById(R.id.etFaultName);
            etDesc = (EditText) v.findViewById(R.id.etFaultDesc);
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).
                    setView(v)
                    .setPositiveButton(R.string.hint_submit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(isValid()) {
                                sendFaultReport();
                                dialogInterface.dismiss();
                            }
                        }
                    })
                    .setNegativeButton(R.string.hint_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

        }
    }

    private File createImageFile() throws IOException {

        Calendar calendar = Calendar.getInstance();
        String imageFileName = ""+calendar.getTimeInMillis();
        File storageDir = getExternalFilesDir(Constants.FOLDER_PATH);
        File image = new File(storageDir+"/"+imageFileName+".jpg");
        return image;
    }

    public boolean isValid(){

        if(etTitle.getText().length()<=0){
            showToast(R.string.err_fault_title, Toast.LENGTH_SHORT);
            return false;
        }else if(etDesc.getText().length()<=0){
            showToast(R.string.err_description, Toast.LENGTH_SHORT);
            return false;
        }else {
            return true;
        }
    }

    public void sendFaultReport(){

        try {

            showProgress("");
            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(),RequestParamsUtils.RES_ID,""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(),RequestParamsUtils.CONDO_ID,""));
            params.put(RequestParamsUtils.FAULT_NAME, etTitle.getText().toString());
            params.put(RequestParamsUtils.FAULT_DESC, etDesc.getText().toString());
            params.put(RequestParamsUtils.FAULT_PHOTO,  imgFile);
            params.put(RequestParamsUtils.TOKEN,Utils.getPref(getActivity(),RequestParamsUtils.TOKEN,""));
            Debug.e("AddFaultParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.post(new URLs().ADD_FAULT, params, new AllFaultsHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class AllFaultsHandler extends AsyncResponseHandler {

        public AllFaultsHandler(Activity context) {
            super(context);
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String response) {

            try {
                Debug.e("", "AddFaultResponse# " + response);
                if (response != null && response.length() > 0) {

                    dismissProgress();

                    AddFaultResponse res = new Gson().fromJson(
                            response, new TypeToken<AddFaultResponse>() {
                            }.getType());

                    if (res.st == 1) {

                        getActivity().showToast(R.string.msg_success_add_fault,Toast.LENGTH_SHORT);

                    } else if (res.st == 401 || res.st == 402) {

                        getActivity().showToast(R.string.msg_upload_failed, Toast.LENGTH_SHORT);

                    } else if (res.st == 504) {

                        getActivity().showToast(R.string.msg_upload_failed, Toast.LENGTH_SHORT);

                    } else if (res.st == 505) {

                        getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {

        }
    }

}
