package com.main.movie.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException implements Supplier {
    public ResourceNotFound(String message){
        super(message);
    }

    @Override
    public Object get() {
        throw this;
    }
}
