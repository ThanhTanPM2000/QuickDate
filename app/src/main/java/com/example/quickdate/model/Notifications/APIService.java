package com.example.quickdate.model.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAKyhDiis:APA91bGPVy7CTFITgZEraeorl6ma-qDVxi2a1vN4LRXrHK5TVACAi5QSkW2T0Ryae1ReioKhvXRs4Pk82S7Xv6hFIcsedgZNwoEeste8U_ofWrbVbDm4XQkEWzjpfg2Fr7BF_j5SLBMm"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
