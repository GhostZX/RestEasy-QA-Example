package com.unit1.stepDefs;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.unit1.context.Context;
import com.unit1.models.ReqResUserModel;
import com.unit1.rest.ReqResTasks;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Pesho on 15-Sep-17.
 */
public final class RestApiStepDefs {

    private Context context;
    private ReqResTasks reqResTasks;

    @Inject
    public RestApiStepDefs(final Context context, final ReqResTasks reqResTasks) {
        this.context = context;
        this.reqResTasks = reqResTasks;
    }

    @Given("^a user lists all available users$")
    public void userListsAllUsers() {
        final List<ReqResUserModel> users = reqResTasks.listUsers().getData();
        final Gson gson = new Gson();
        context.writeOut("All users:", gson.toJson(users));
        context.saveData("users" , users);

    }

    @When("^user \"([^\"]*)\" (doesn't )?exist$")
    public void userCannotFindAmongExisting(final String userName, final String negate) {
        List<ReqResUserModel> users = (List<ReqResUserModel>) context.getSavedData("users");
        for (ReqResUserModel user : users) {
            if (negate != null) {
                Assert.assertNotEquals("User already exists!", user.getFirstName(), userName.split(" ")[0]);
                Assert.assertNotEquals("User already exists!", user.getLastName(), userName.split(" ")[1]);
            } else if (user.getFirstName().equals(userName.split(" ")[0])
                    && user.getLastName().equals(userName.split(" ")[1])){
                        context.saveData("userIndex", user.getId());
            }
        }
        if (negate == null) {
            Assert.assertNotNull("User does not exist.", context.getSavedData("userIndex"));
        }
    }

    @Then("^the user create a new user \"([^\"]*)\"$")
    public void userCreatesNewUser(final String userNames) {
        final String[] splitUserNames = userNames.split(" ");
        ReqResUserModel userModel = new ReqResUserModel();
        userModel.setFirstName(splitUserNames[0]);
        userModel.setLastName(splitUserNames[1]);
        userModel.setAvatar("avatarPic");

        final Response createResp = reqResTasks.createUser(userModel);
        context.writeOut("User created:", createResp.readEntity(String.class));
        Assert.assertEquals("Response code is not 201", createResp.getStatus(), 201);
    }

    @Then("the user can be updated to \"([^\"]*)\"$")
    public void canBeUpdate(final String userNameTobeUsed) {
        final String userId  = (String) context.getSavedData("userIndex");

        ReqResUserModel userModel = new ReqResUserModel();
        userModel.setFirstName(userNameTobeUsed.split(" ")[0]);
        userModel.setLastName(userNameTobeUsed.split(" ")[1]);

        final Response response = reqResTasks.updateUser(userId, userModel);
        context.writeOut("User updated:", response.readEntity(String.class));
        Assert.assertTrue("Response code is not 200.", response.getStatus() == 200);
    }
}
