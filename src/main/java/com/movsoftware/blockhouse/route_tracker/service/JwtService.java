package com.movsoftware.blockhouse.route_tracker.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


@Service
public class JwtService {

    @Value("${auth.server.url}")
    private String authServerUrl;

    private final OkHttpClient client;

    public JwtService(OkHttpClient client) {
        this.client = client;
    }

    public List<String> getPermissionsFromJwt(String jwt) {
        // Create JSON payload
        JSONObject jsonRequest = new JSONObject()
                .put("idToken", jwt);

        // Create RequestBody
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        okhttp3.RequestBody requestBody = okhttp3.RequestBody
                .create(jsonRequest.toString().getBytes(StandardCharsets.UTF_8), JSON);

        // Create Request
        Request request = new Request.Builder()
                .url(authServerUrl + "/verifyTokenAndGetPermissions")
                .post(requestBody)
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseBodyString = responseBody.string();

                // Manually parse the JSON response into a PermissionsResponse object
                JSONObject jsonResponse = new JSONObject(responseBodyString);
                JSONArray jsonPermissionsArray = jsonResponse.getJSONArray("permissions");
                List<String> permissions = new ArrayList<>();
                for (int i = 0; i < jsonPermissionsArray.length(); i++) {
                    permissions.add(jsonPermissionsArray.getString(i));
                }

                // At this point, 'permissions' contains the permissions from the response
                return permissions;
            } else {
                throw new RuntimeException("Empty response body");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify JWT or get permissions", e);
        }
    }
}
