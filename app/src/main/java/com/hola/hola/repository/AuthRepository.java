package com.hola.hola.repository;

import com.hola.hola.model.body.RegisterBody;

public interface AuthRepository {
    void verificateCode(String phone, String code);

    void registerUser(RegisterBody register);
}
