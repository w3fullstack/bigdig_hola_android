package com.hola.hola.centrifuge;

/**
 * Created by dan on 05.10.18.
 */

public class CentrifugeModel {
  private int userId;
  private String token;
  private long tokenTimestamp;

  public CentrifugeModel(int userId, String token, long tokenTimestamp){
    this.userId = userId;
    this.token = token;
    this.tokenTimestamp = tokenTimestamp;
  }
}
