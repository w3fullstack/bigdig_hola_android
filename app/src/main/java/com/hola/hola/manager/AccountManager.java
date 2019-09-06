package com.hola.hola.manager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hola.hola.REST.BooleanDeserializer;
import com.hola.hola.centrifuge.CentrifugeModel;
import com.hola.hola.model.User;
import com.hola.hola.model.UserContact;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by dan on 05.10.18.
 */

public class AccountManager {
  public static int currentAccountId;
  private static CentrifugeModel centrifugeModel;

  public static UserContact currentUser;

  public static PublishSubject<UserContact> currentUserSubject = PublishSubject.create();

  public static CentrifugeModel getCentrifugeModel() {
    return centrifugeModel;
  }

  public static void setCentrifugeModel(CentrifugeModel centrifugeModel) {
    AccountManager.centrifugeModel = centrifugeModel;
  }

  public static void setCurrentUserFromJson(JsonObject body) {
    BooleanDeserializer deserializer = new BooleanDeserializer();
    UserContact user = new GsonBuilder()
            .registerTypeAdapter(Boolean.class, deserializer)
            .registerTypeAdapter(boolean.class, deserializer)
            .create()
            .fromJson(body, UserContact.class);
    currentUser = user;
    currentUserSubject.onNext(user);
  }

  public static void setCurrentUser(UserContact userContact) {
    currentUser = userContact;
    currentUserSubject.onNext(userContact);
  }
}
