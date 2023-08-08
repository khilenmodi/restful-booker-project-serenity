package com.restful.booker.bookinginfo;

import com.restful.booker.constant.EndPoints;
import com.restful.booker.model.BookingDates;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.model.TokenPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

public class BookingCRUDTest extends TestBase {

    static String firstName = "PrimUser" + TestUtils.getRandomValue();
    static String updateFirstName = "Update" + TestUtils.getRandomValue();
    static String lastName = "Testing" + TestUtils.getRandomValue();
    static String additionalNeeds = "Breakfast";
    static int bookingId;

    static String token;


    @Title("This will return token")
    @Test
    public void test001() {
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setUsername("admin");
        tokenPojo.setPassword("password123");

        token = SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .body(tokenPojo)
                .post("/auth")
                .then().log().all().statusCode(200)
                .extract().path("token");

        System.out.println("Token :" + token);
        Assert.assertNotNull(token);

    }

    @Title("This will create booking")
    @Test()
    public void test002() {
        System.out.println("====================" + token);

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalPrice(111);
        bookingPojo.setDepositPaid(true);
        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin("");
        bookingdates.setCheckout("");
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalNeeds(additionalNeeds);


        bookingId = SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .body(bookingPojo)
                .post("/booking")
                .then().log().all().statusCode(200)
                .extract()
                .path("bookingid");

        System.out.println("bookingId :" + bookingId);
        Assert.assertNotNull(bookingId);

    }

    @Title("This will update booking with firstname")
    @Test
    public void test003() {

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(updateFirstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalPrice(111);
        bookingPojo.setDepositPaid(true);
        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin("");
        bookingdates.setCheckout("");
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalNeeds(additionalNeeds);


        String updatedFirstNameResult = SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .body(bookingPojo)
                .when()
                .put("/booking" + EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all().statusCode(200)
                .extract()
                .path("firstname");


        System.out.println("updatedFirstNameResult -- " + updatedFirstNameResult);
        Assert.assertEquals(updateFirstName, updatedFirstNameResult);
    }

    @Title("This will fetch booking details by booking id")
    @Test
    public void test004() {

        HashMap<String, Object> bookingMap = SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .when()
                .get("/booking" + EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then().statusCode(200)
                .extract()
                .path("");

        System.out.println("updateFirstName - " + updateFirstName);
        System.out.println("lastName - " + lastName);
        System.out.println("additionalNeeds - " + additionalNeeds);
        Assert.assertThat(bookingMap, hasValue(updateFirstName));
        Assert.assertThat(bookingMap, hasValue(lastName));
        Assert.assertThat(bookingMap, hasValue(additionalNeeds));

    }

    @Title("This will delete booking")
    @Test
    public void test005() {

        SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .when()
                .delete("/booking" + EndPoints.DELETE_BOOKING_BY_ID)
                .then()
                .statusCode(201);

        SerenityRest.given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .pathParam("bookingID", bookingId)
                .when()
                .get("/booking" + EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then()
                .statusCode(404);

    }
}
