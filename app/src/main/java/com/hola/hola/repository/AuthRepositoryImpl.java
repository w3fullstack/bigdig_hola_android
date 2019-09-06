package com.hola.hola.repository;

import android.text.TextUtils;

import com.hola.hola.REST.HolaApi;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.body.RegisterBody;
import com.hola.hola.model.Success;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {

  public interface IRegisterResponse{
    void finishRegister(boolean status);
    void serverError(String message);
  }

  private HolaApi api;
  private IRegisterResponse registerResponse;

  private String getCsrfToken() {
    return HolaRESTClient.getCsrfToken();
  }

  public AuthRepositoryImpl(IRegisterResponse registerResponse) {
    this.registerResponse = registerResponse;
    api = HolaRESTClient.get();
  }

  @Override
  public void verificateCode(String phone, String code) {
  }

  @Override
  public void registerUser(RegisterBody register) {
//    api.confirmPhone(getCsrfToken(), new PostSendCode(register.getPhoneNumber(), register.getCode()))
//        .enqueue(new Callback<Success>() {
//          @Override public void onResponse(Call<Success> call, Response<Success> response) {
//            register(register);
//          }
//
//          @Override public void onFailure(Call<Success> call, Throwable t) {
//            registerResponse.finishRegister(false);
//          }
//        });
      register(register);
  }

  private void register(RegisterBody register) {
    api.register(getCsrfToken(), register).enqueue(new Callback<Success>() {
      @Override public void onResponse(Call<Success> call, Response<Success> response) {
        if(response.code()==200) {
          registerResponse.finishRegister(true);
        }else {
          registerResponse.finishRegister(false);
          try {
            JSONObject jsonErr = new JSONObject(response.errorBody().string());
            String err = jsonErr.getString("error");
            if(!TextUtils.isEmpty(err)){
              registerResponse.serverError(err);
            }
          } catch (Throwable e) {
            e.printStackTrace();
            registerResponse.serverError("Unknown error");
          }
        }
      }

      @Override public void onFailure(Call<Success> call, Throwable t) {
        registerResponse.finishRegister(false);
        registerResponse.serverError(t.getLocalizedMessage());
      }
    });
  }
}
