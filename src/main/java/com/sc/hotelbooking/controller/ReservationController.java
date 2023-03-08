package com.sc.hotelbooking.controller;

import com.sc.hotelbooking.model.Reservation;
import com.sc.hotelbooking.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("reservations")
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping
  public ResponseEntity<String> create(@Validated @RequestBody Reservation request) {
    reservationService.createReservation(request);
    return ResponseEntity.ok("Reservation made successfully!");
  }

  @GetMapping("{date}")
  public ResponseEntity<String> getAvailableRoomsByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
    int availableRooms = reservationService.getRoomsAvailableOnDate(date);
    return ResponseEntity.ok(String.format("Number of rooms available on date [%s] are [%s]", date.toString(), availableRooms));
  }

  @GetMapping
  public ResponseEntity<List<Reservation>> getBookingsByName(@RequestParam String guestName){
    return ResponseEntity.ok(reservationService.getBookingsByName(guestName));
  }

}
