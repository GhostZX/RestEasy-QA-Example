package com.unit1.rest;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.unit1.models.ReqResUserModel;
import com.unit1.models.ReqResUsersModel;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Petar Petrov on 07-Nov-17.
 */
public class ReqResTasks {

    private ReqResApi reqResApi;
    private Properties properties;

    @Inject
    public ReqResTasks() throws IOException {
        this.properties = new Properties();
        final InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/conf/config.properties");
        this.properties.load(inputStream);
        this.reqResApi = getTarget().proxy(ReqResApi.class);
    }

    public ReqResUsersModel listUsers() {
        final Gson gson = new Gson();
        final String responseJson = reqResApi.listUsers(1000).readEntity(String.class);
        return gson.fromJson(responseJson, ReqResUsersModel.class);
    }

    public Response createUser(final ReqResUserModel userModel) {
        return reqResApi.createUser(getJsonString(userModel));
    }

    public Response updateUser(final String id, final ReqResUserModel userModel) {
        return reqResApi.updateUser(id, getJsonString(userModel));
    }

    private ResteasyWebTarget getTarget() {
        final ResteasyClient client = new ResteasyClientBuilder().build();
        return client.target(properties.getProperty("reqresUrl"));
    }

    private String getJsonString(final Object object) {
        return new Gson().toJson(object);
    }
}
