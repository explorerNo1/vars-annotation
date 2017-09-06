package org.mbari.m3.vars.annotation.services.panoptes.v1;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.mbari.m3.vars.annotation.model.ImageUploadResults;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author Brian Schlining
 * @since 2017-08-31T13:07:00
 */
public interface PanoptesWebService {

    @Multipart
    @POST("{camera_id}/{deployment_id}/{filename}")
    Call<ImageUploadResults> uploadImage(@Path("camera_id") String cameraId,
                                         @Path("deployment_id") String deploymentId,
                                         @Path("name") String filename,
                                         @Part MultipartBody.Part image,
                                         @Part("name") RequestBody name,
                                         @HeaderMap Map<String, String> headers);

    @GET("{camera_id}/{deployment_id}/{filename}")
    Call<ImageUploadResults> findImage(@Path("camera_id") String cameraId,
                                       @Path("deployment_id") String deploymentId,
                                       @Path("name") String filename);
}