# hotel-booking-system
## hotel booking api

## To Run locally

```
mvn clean install
```
```
mvn spring-boot:run
```

- It will build and run test case implemented in `ReservationControllerTest`

### Configuration
- Can configure required number of rooms in hotel using property `hotel.room.size` in application.properties.

### Included test case are

- Creation of reservation 
- Not allowed to book room more than the capacity configured
- Not allowed to book same room multiple times on same date
- Get Number rooms available by date
- Get Booking details by guest name.

### Curl for Api's

Create Reservation
```
curl --location --request POST 'http://localhost:6067/services/reservations' \
--header 'Content-Type: application/json' \
--header 'Cookie: XSRF-TOKEN=e1abcc91-8660-42a0-abd1-a680f2ebbf01' \
--data-raw '    {
"name": "abhi",
"roomNumber": "2",
"bookingDate": "2023-03-08"
}
```

Check rooms availability by date
```
curl --location --request GET 'http://localhost:6067/services/reservations/2023-03-09' \
--header 'Cookie: XSRF-TOKEN=e1abcc91-8660-42a0-abd1-a680f2ebbf01'
```

Get room booking details by name
```
curl --location --request GET 'http://localhost:6067/services/reservations?guestName=abhi' \
--header 'Cookie: XSRF-TOKEN=e1abcc91-8660-42a0-abd1-a680f2ebbf01'
```



