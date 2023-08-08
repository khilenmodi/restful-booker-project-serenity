package com.restful.booker.bookinginfo;

import com.restful.booker.constant.EndPoints;
import com.restful.booker.model.BookingDates;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.model.TokenPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;

public class BookingSteps {

    @Step("Fetching token with userName : {0}, password: {1}")
    public String fetchToken(String userName, String password){
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setUsername(userName);
        tokenPojo.setPassword(password);

        return SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .body(tokenPojo)
                .post("/auth")
                .then().log().all().statusCode(200)
                .extract().path("token");
    }
    @Step("Creating booking with firstName : {0}, lastName: {1}, totalPrice: {2}, depositPaid: {3}, additionalNeeds: {4}")
    public int creatingBooking(String firstName, String lastName, int totalPrice, boolean depositPaid, String additionalNeeds){
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalPrice(totalPrice);
        bookingPojo.setDepositPaid(depositPaid);
        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin("");
        bookingdates.setCheckout("");
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalNeeds(additionalNeeds);


        return SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .body(bookingPojo)
                .post("/booking")
                .then().log().all().statusCode(200)
                .extract()
                .path("bookingid");
    }

    @Step("Updating booking with firstName : {0}, lastName: {1}, totalPrice: {2}, depositPaid: {3}, additionalNeeds: {4}, token: {5}, bookingId:{6}")
    public String updatingBooking(String firstName, String lastName, int totalPrice, boolean depositPaid, String additionalNeeds, String token, int bookingId){
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalPrice(totalPrice);
        bookingPojo.setDepositPaid(depositPaid);
        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin("");
        bookingdates.setCheckout("");
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalNeeds(additionalNeeds);


        return SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .body(bookingPojo)
                .when()
                .put("/booking" + EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all().statusCode(200)
                .extract()
                .path("firstname");

    }

    @Step("Fetch details by bookingId : {0}, token: {1}")
    public HashMap<String, Object> fetchDetailsByBookingId(int bookingId, String token){
        return SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .when()
                .get("/booking" + EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then().statusCode(200)
                .extract()
                .path("");
    }

    @Step("Deleting booking  information with booking: {0}, token: {1}")
    public ValidatableResponse deleteBooking(int bookingId, String token) {
        return SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .when()
                .delete("/booking" + EndPoints.DELETE_BOOKING_BY_ID)
                .then();
    }

    @Step("Fetch booking  information with booking: {0}, token: {1}")
    public ValidatableResponse fetchBooking(int bookingId, String token) {
        return SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .when()
                .get("/booking" + EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then();
    }

}
