package com.hola.hola.REST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hola.hola.model.AllChats;
import com.hola.hola.model.body.BugReportBody;
import com.hola.hola.model.body.BurnMessagesBody;
import com.hola.hola.model.body.ChangeAdminBody;
import com.hola.hola.model.ChatAddInfo;
import com.hola.hola.model.body.ChangePasswordBody;
import com.hola.hola.model.body.DeleteUserBody;
import com.hola.hola.model.body.EditPersonalInfoBody;
import com.hola.hola.model.body.GroupChatBody;
import com.hola.hola.model.body.LoginBody;
import com.hola.hola.model.LoginResponse;
import com.hola.hola.model.body.MessageBody;
import com.hola.hola.model.MessagesResponse;
import com.hola.hola.model.body.PostPhoneBody;
import com.hola.hola.model.PostSendCode;
import com.hola.hola.model.body.RegisterBody;
import com.hola.hola.model.Success;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Add @Header("X-CSRF-TOKEN") to all queries except getCSRF
 * In parameter set HolaRESTClient.getCsrfToken()
 */

public interface HolaApi {
    Integer TRANSLATE_ENABLED = 1;
    Integer TRANSLATE_DISABLED = 0;
    String METHOD_UPLOAD_DOCUMENT = "/documents/upload";
    String METHOD_UPLOAD_CHAT_PIC = "/app/chat/change-avatar/";

    @GET("/csrf")
    Call<String> getCSRF();

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/auth/register/send-code")
    Call<Success> sendConfirmationCode(
            @Header("X-CSRF-TOKEN") String csrfToken, @Body PostPhoneBody phone);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/auth/register/confirm")
    Call<Success> confirmPhone(
            @Header("X-CSRF-TOKEN") String csrfToken, @Body PostSendCode code);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/auth/register")
    Call<Success> register(@Header("X-CSRF-TOKEN") String csrfToken,
                           @Body RegisterBody register);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/auth/login")
    Call<LoginResponse> login(@Header("X-CSRF-TOKEN") String csrfToken,
                              @Header("Accept-Language") String language,
                              @Body LoginBody register);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/chat/start/{user_id}")
    Call<ChatAddInfo> startChat(
            @Header("X-CSRF-TOKEN") String csrfToken, @Path("user_id") int userId);


    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/chat/messages/{chat_id}")
    Call<Void> sendMessage(
            @Header("X-CSRF-TOKEN") String csrfToken, @Path("chat_id") int chatId, @Body MessageBody body, @Header("Accept-Language") String language);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @GET("/app/chat/list")
    Call<AllChats> getChatList(@Header("X-CSRF-TOKEN") String csrfToken,
                               @Header("Accept-Language") String language,
                               @Query("translateMessages") Integer translateEnabled);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @GET("/app/chat/messages/{messages_id}")
    Call<MessagesResponse> getChatMessages(
            @Header("X-CSRF-TOKEN") String csrfToken, @Path("messages_id") int messagesId,
            @Header("Accept-Language") String language, @Query("translateMessages") Integer translateEnabled);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @GET("/app/user/auto-complete")
    Call<JsonArray> getUsersByFilter(
            @Header("X-CSRF-TOKEN") String csrfToken, @Query("term") String filterString);


    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/chat/start-group")
    Call<ChatAddInfo> startGroupChat(
            @Header("X-CSRF-TOKEN") String csrfToken, @Body GroupChatBody body);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/chat/change-admin/{chat_id}")
    Call<Void> chatChangeAdmin(
            @Header("X-CSRF-TOKEN") String csrfToken, @Path("chat_id") int chatId, @Body ChangeAdminBody body);


    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/chat/delete-from-chat/{chat_id}")
    Call<Void> chatDeleteUser(
            @Header("X-CSRF-TOKEN") String csrfToken, @Path("chat_id") int chatId, @Body DeleteUserBody body);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/chat/burn/{chat_id}")
    Call<Void> chatBurnMessages(
            @Header("X-CSRF-TOKEN") String csrfToken, @Path("chat_id") int chatId, @Body BurnMessagesBody body);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/report-bug")
    Call<Void> sendBugReport(
            @Header("X-CSRF-TOKEN") String csrfToken, @Body BugReportBody body);

    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @GET("/app/user/data")
    Call<JsonObject> getUserData(@Header("X-CSRF-TOKEN") String csrfToken);


    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/user/data")
    Call<JsonObject> updateUserData(@Header("X-CSRF-TOKEN") String csrfToken, @Body EditPersonalInfoBody body);


    @Headers({
            "Content-Type: application/json", "X-Requested-With: XMLHttpRequest"
    })
    @POST("/app/user/change-pass")
    Call<Void> changePassword(@Header("X-CSRF-TOKEN") String csrfToken, @Body ChangePasswordBody body);
//  @Multipart
//  @Headers({"X-Requested-With: XMLHttpRequest"
//  }) @POST("/document/upload") Call<DocumentUploadResponse> uploadFile(@Header("X-CSRF-TOKEN") String csrfToken,
//                                                                       @Part MultipartBody.Part file);


}
