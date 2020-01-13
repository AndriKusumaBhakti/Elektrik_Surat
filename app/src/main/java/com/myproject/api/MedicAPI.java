package com.myproject.api;

import com.myproject.model.ModelResponse;
import com.myproject.model.request.StatusAppRequest;
import com.myproject.model.response.CheckStatusAppResponse;
import com.myproject.model.response.ModelResponsePassword;
import com.myproject.model.response.ResponseAllSurat;
import com.myproject.model.response.ResponseFileSurat;
import com.myproject.model.response.ResponseJenisSurat;
import com.myproject.model.response.ResponseLogin;
import com.myproject.model.response.ResponseRequestSurat;
import com.myproject.model.response.ResponseScanQr;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;

public interface MedicAPI {

    @POST("/check-version")
    public CheckStatusAppResponse checkStatusApp(@Body StatusAppRequest request);

    @POST("/webservices/api.php")
    public ModelResponsePassword requestPassword(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ResponseLogin cekLogin(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ResponseJenisSurat jenisSurat(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ResponseRequestSurat reqJenisSurat(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ModelResponse createSurat(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ResponseFileSurat reqSurat(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ResponseAllSurat reqAllSurat(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ModelResponse reqApproveSurat(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ModelResponse updatePass(@Body MultipartTypedOutput request);

    @POST("/webservices/api.php")
    public ResponseScanQr scanQrCode(@Body MultipartTypedOutput request);
}