package com.baykus.messageapp.Fragments;

import com.baykus.messageapp.Notifications.MyResponse;
import com.baykus.messageapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAaaU8X8s:APA91bGtiwQQ_Xf0iU41UG9gRVmZmqxo_OIDUEtceTDprs1Iu0Zo0kGiNpthJA5PjsCkK2DnylCFxWjV45o_s0S1TQH_sEPNF_lhCLaFOT3dedJk0JtfKHyfa8USp8nyA6AH5CCaXeQO"
            }
    )


    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}
