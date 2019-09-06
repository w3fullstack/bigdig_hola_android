package com.hola.hola.model.body;

import com.hola.hola.model.DocumentUploadResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 05.10.18.
 */

public class MessageBody {
  String text;
  List<DocumentUploadResponse> documents;

  public MessageBody(String text){
    this.text = text;
    documents = new ArrayList<>();
  }

  public MessageBody(String text, List<DocumentUploadResponse> documents) {
    this.text = text;
    this.documents = documents;
  }
}
