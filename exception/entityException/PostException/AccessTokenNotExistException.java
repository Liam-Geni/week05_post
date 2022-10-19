package com.sparta.post03.exception.entityException.PostException;

import com.sparta.post03.exception.entityException.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessTokenNotExistException extends BaseException{
    private String message;


}
