package com.sc.hotelbooking.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

  @NotNull
  public String name;

  @NotNull
  public int roomNumber;

  @NotNull
  public LocalDate bookingDate;

}
