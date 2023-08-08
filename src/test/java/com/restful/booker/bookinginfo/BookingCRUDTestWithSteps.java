package com.restful.booker.bookinginfo;

import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class BookingCRUDTestWithSteps extends TestBase {
    static String firstName = "PrimUser" + TestUtils.getRandomValue();
    static String updateFirstName = "Update" + TestUtils.getRandomValue();
    static String lastName = "Testing" + TestUtils.getRandomValue();
    static String additionalNeeds = "Breakfast";
    static int bookingId;

    static String token;

    @Steps
    BookingSteps bookingSteps;

    @Title("This will return token")
    @Test
    public void test001() {
        token = bookingSteps.fetchToken("admin","password123");
        System.out.println("Token :" + token);
        Assert.assertNotNull(token);
    }


    @Title("This will create booking")
    @Test()
    public void test002() {
        System.out.println("====================" + token);
        bookingId = bookingSteps.creatingBooking(firstName,lastName,111, true, additionalNeeds);
        System.out.println("bookingId :" + bookingId);
        Assert.assertNotNull(bookingId);
    }


    @Title("This will update booking with firstname")
    @Test
    public void test003() {
        String updatedFirstNameResult =  bookingSteps.updatingBooking(updateFirstName,lastName,111,true,additionalNeeds, token, bookingId);
        System.out.println("updatedFirstNameResult -- " + updatedFirstNameResult);
        Assert.assertEquals(updateFirstName, updatedFirstNameResult);
    }

    @Title("This will fetch booking details by booking id")
    @Test
    public void test004() {
        HashMap<String, Object> bookingMap = bookingSteps.fetchDetailsByBookingId(bookingId, token);
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

        bookingSteps.deleteBooking(bookingId,token).statusCode(201);
        bookingSteps.fetchBooking(bookingId,token).statusCode(404);



    }


}
