/*
 * Created By: Hiren Patel
 * Project Name: Restful-Booker-API-Serenity-Week-19
 */

package com.restfulbooker.bookinginfo;

import com.restfulbooker.model.BookingDates;
import com.restfulbooker.restfulinfo.BookingSteps;
import com.restfulbooker.testbase.TestBase;
import com.restfulbooker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class BookingCRUDTestWithSteps extends TestBase {

    static String firstname = "Harry" + TestUtils.getRandomValue();
    static String lastname = "Patel" + TestUtils.getRandomValue();
    static int totalprice = 1000;
    static Boolean depositpaid = true;
    static String additionalneeds = " ";
    static Date checkin = new Date(122, 01, 21);
    static Date checkout = new Date(122, 01, 24);
    static int bookingId;


    @Steps
    BookingSteps bookingSteps;


    @Title("This will create a new Booking")
    @Test
    public void test001() {
        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin(checkin);
        bookingdates.setCheckout(checkout);
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        response.log().all().statusCode(200);
    }

    @Title("Verify if the Booking was done correctly")
    @Test
    public void test002() {
        ArrayList<HashMap<String, Object>> value = bookingSteps.getBookingByFirstname(firstname);
        Assert.assertThat(value.get(0), hasValue(firstname));
        bookingId = (Integer) value.get(0).get("bookingid");
        System.out.println(bookingId);
    }
    @Title("Update the Booking and verify the updated information")
    @Test
    public void test003() {
        firstname = firstname + "_updated";
        lastname = lastname + "_updated";
        additionalneeds = "Gluten free Food";

        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin(checkin);
        bookingdates.setCheckout(checkout);
        bookingSteps.updateStudent(bookingId, firstname, lastname, totalprice, depositpaid,  bookingdates, additionalneeds).log().all().statusCode(200);
        ArrayList<HashMap<String, Object>> value = bookingSteps.getBookingByFirstname(firstname);
        Assert.assertThat(value.get(0), hasValue(firstname));
    }
    @Title("Delete the Booking and verify if the Booking is deleted!")
    @Test
    public void test004() {
        bookingSteps.deleteBooking(bookingId).statusCode(201);//it should be 204
        bookingSteps.getBookingById(bookingId).statusCode(404);
    }

}
