package com.sc.hotelbooking;

import com.sc.hotelbooking.model.Reservation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test-application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerTest {

  TestRestTemplate restTemplate = new TestRestTemplate();
  HttpHeaders headers = new HttpHeaders();

  @Test
  public void creatReservationTest_thenStatus200(){
    Reservation reservation = new Reservation("abhi",1, LocalDate.now());
    HttpEntity<Reservation> entity = new HttpEntity<Reservation>(reservation,headers);
    ResponseEntity<String> response = restTemplate.exchange(
        createURLWithPort("/services/reservations"),
        HttpMethod.POST, entity, String.class);
    Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assert.assertEquals("Reservation made successfully!",response.getBody());
  }

  @Test
  public void creatReservationTestMoreThanHotelRoomSizeOnSameDate_thenStatus409(){
    List<Reservation> reservationList = new ArrayList<Reservation>();
    reservationList.add(new Reservation("abhi",1, LocalDate.now()));
    reservationList.add(new Reservation("abhi",2, LocalDate.now()));
    reservationList.add(new Reservation("abhi",3, LocalDate.now()));
    reservationList.add(new Reservation("abhi",4, LocalDate.now()));
    ResponseEntity<String> response = null;
    for(int i=0;i<reservationList.size();i++) {
      HttpEntity<Reservation> entity = new HttpEntity<Reservation>(reservationList.get(i), headers);
     response = restTemplate.exchange(
          createURLWithPort("/services/reservations"),
          HttpMethod.POST, entity, String.class);
    }
    Assert.assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    Assert.assertTrue(response.getBody().contains("Rooms are full!"));
  }

  @Test
  public void creatReservationTestToBookSameRoomOnSameDate_thenStatus409(){
    List<Reservation> reservationList = new ArrayList<Reservation>();
    reservationList.add(new Reservation("abhi",1, LocalDate.now()));
    reservationList.add(new Reservation("abhi",1, LocalDate.now()));
    ResponseEntity<String> response = null;
    for(int i=0;i<reservationList.size();i++) {
      HttpEntity<Reservation> entity = new HttpEntity<Reservation>(reservationList.get(i), headers);
      response = restTemplate.exchange(
          createURLWithPort("/services/reservations"),
          HttpMethod.POST, entity, String.class);
    }
    Assert.assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    Assert.assertTrue(response.getBody().contains(String.format(
        "Room No.[%s] is already booked for the date [%s] ", reservationList.get(1).getRoomNumber(),
        reservationList.get(1).getBookingDate().toString())));
  }

  @Test
  public void testAvailableRoomsByDate_thenStatus200(){
    List<Reservation> reservationList = new ArrayList<Reservation>();
    reservationList.add(new Reservation("abhi",1, LocalDate.now()));
    reservationList.add(new Reservation("abhi",2, LocalDate.now()));
    for(int i=0;i<reservationList.size();i++) {
      restTemplate.exchange(
          createURLWithPort("/services/reservations"),
          HttpMethod.POST, new HttpEntity<Reservation>(reservationList.get(i), headers), String.class);
    }
    String date = LocalDate.now().toString();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        createURLWithPort("/services/reservations/"+date),
        HttpMethod.GET, entity, String.class);

    Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    System.out.println("--->"+response.getBody().toString());
    Assert.assertTrue(response.getBody().contains(String.format("Number of rooms available on date [%s] are [%s]", date, 1)));
  }

  @Test
  public void testBookingByName_thenStatus200() throws JSONException {
    List<Reservation> reservationList = new ArrayList<Reservation>();
    reservationList.add(new Reservation("abhi",1, LocalDate.now()));
    reservationList.add(new Reservation("John",2, LocalDate.now()));
    for(int i=0;i<reservationList.size();i++) {
      HttpEntity<Reservation> entity = new HttpEntity<Reservation>(reservationList.get(i), headers);
      ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/services/reservations"),
          HttpMethod.POST, entity, String.class);
    }
    String date = LocalDate.now().toString();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        createURLWithPort("/services/reservations/?guestName=John"),
        HttpMethod.GET, entity, String.class);
    String expected = "[{\"name\":\"John\",\"roomNumber\":2,\"bookingDate\":\"2023-03-08\"}]";
    Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    JSONAssert.assertEquals(expected, response.getBody(), false);
  }


  private String createURLWithPort(String uri) {
    return "http://localhost:" + 6067 + uri;
  }

}
