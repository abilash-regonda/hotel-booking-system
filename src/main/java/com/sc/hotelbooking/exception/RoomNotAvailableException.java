package com.sc.hotelbooking.exception;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
@StandardException
public class RoomNotAvailableException extends RuntimeException{

}
