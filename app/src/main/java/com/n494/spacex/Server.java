package com.n494.spacex;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx3.Rx3Apollo;
import com.n494.RocketsQuery;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;

class Server {
    private static ApolloClient apolloClient;

    public static Observable<Response<RocketsQuery.Data>> fetchRockets() {
        ApolloQueryCall<RocketsQuery.Data> call = getApolloClient()
                .query(new RocketsQuery());

        return Rx3Apollo.from(call)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter((dataResponse -> dataResponse.getData() != null));
    }

    private static ApolloClient getApolloClient() {
        if (apolloClient == null) {
            //Build the Apollo Client
            String serverUrl = "https://api.spacex.land/graphql/";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            apolloClient = ApolloClient.builder()
                    .serverUrl(serverUrl)
                    .okHttpClient(okHttpClient)
                    .build();
        }

        return apolloClient;
    }
}
