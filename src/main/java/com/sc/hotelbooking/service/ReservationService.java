package com.sc.hotelbooking.service;

import com.sc.hotelbooking.exception.RoomNotAvailableException;
import com.sc.hotelbooking.model.Reservation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  private List<Reservation> reservations = Collections.synchronizedList(new ArrayList<Reservation>());

  @Value("${hotel.room.size}")
  private int roomSize;

  public void createReservation(Reservation reservation) {
    List<Reservation> bookedRoomsList = getBookingsByDate(reservation.getBookingDate());
    bookedRoomsList.stream().filter(room -> room.getRoomNumber() == reservation.roomNumber).findAny()
        .ifPresent(a -> {
          throw new RoomNotAvailableException(String.format(
              "Room No.[%s] is already booked for the date [%s] ", reservation.getRoomNumber(), reservation.getBookingDate().toString()));
        });
    if (bookedRoomsList.size() < roomSize) {
      reservations.add(reservation);
    } else {
      throw new RoomNotAvailableException("Rooms are full!");
    }
  }

  public int getRoomsAvailableOnDate(LocalDate date){
    return roomSize- getBookingsByDate(date).size();
  }

  public List<Reservation> getBookingsByName(String guestName){
    return reservations.stream()
        .filter(bookedRoom -> bookedRoom.getName().equals(guestName)).collect(Collectors.toList());
  }

  public List<Reservation> getBookingsByDate(LocalDate date){
    return reservations.stream()
        .filter(bookedRoom -> bookedRoom.getBookingDate().equals(date)).collect(Collectors.toList());
  }
}
