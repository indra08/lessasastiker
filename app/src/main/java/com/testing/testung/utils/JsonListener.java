package com.testing.testung.utils;

import org.json.JSONObject;

public interface JsonListener {
    void Succes (JSONObject json);
    void Failed (String mes);
}
