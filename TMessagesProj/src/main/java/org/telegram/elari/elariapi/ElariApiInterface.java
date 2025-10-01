package org.telegram.elari.elariapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import  org.telegram.elari.elariapi.pojo.*;
import org.telegram.elari.elariapi.pojo.channels.Channels;
import org.telegram.elari.elariapi.pojo.channels.ChannelsRequest;
import org.telegram.elari.elariapi.pojo.location.LocationsRequest;
import org.telegram.elari.elariapi.pojo.sendmessegesrequest.SendMessagesRequest;
import org.telegram.elari.elariapi.pojo.toggle.AppOptions;

public interface ElariApiInterface {

    @POST()
    Call<ChatResponse> sendChats(@Body ChatListBody body);

    @POST()
    Call<NewChatResponse> sendChatAddingRequest(@Body NewChatRequest body);

    @POST()
    Call<Response> setAccessForNewGroup(@Body AccessRequest body);

    @GET()
    Call<SearchNewChatPossibility> getSearchingSettings(@Query("dev_type") String devType,
                                                        @Query("man_id") String imei,
                                                        @Query("ApiKey") String apiKey);
    @GET()
    Call<Stickers> getStickers();

    @POST()
    Call<Response> sendMessageStat(@Body StatRequest body);

    @GET()
    Call<GetChatsResponse> getChats(@Query("man_id") String manId,
                                    @Query("dev_type") String devType,
                                    @Query("ApiKey") String apiKey);

    @POST()
    Call<Response> sendLocation(@Body LocationBody body);

    @POST()
    Call<Response> sendMessagesStat(@Body SendMessagesRequest body);

    @POST()
    Call<Channels> getChannels(@Body ChannelsRequest body);

    @POST()
    Call<Response> restotoreAccount(@Body DeleteAccountRequest body);
    @POST()
    Call<Response> removeAccount(@Body DeleteAccountRequest body);

    @POST()
    Call<GetBoundCodeResponse> checkBound(@Body CheckBoundRequest body);

    @POST()
    Call<Response> setCurrentLocation(@Body LocationsRequest body);

    @POST()
    Call<Response> sendPushToApp(@Body SendPushToAppRequest body);

    @POST()
    Call<GetBoundingCodeResponse> getBoundCode(@Body GetBoundCodeRequest body);

    @POST()
    Call<Response> sendChatOpenEvent(@Body ChatOpenEvent body);

    @GET()
    Call<TestCodeResponse> getTestCode(@Query("ApiKey") String apiKey);

    @GET()
    Call<AppOptions> getAppsSettings(@Query("namespace") String namespace,
                                     @Query("country") String country,
                                     @Query("ApiKey") String ApiKey);


}
