package com.sparta.post03.exception;

import com.sparta.post03.exception.entityException.CommentException.CommentIdNotFoundException;
import com.sparta.post03.exception.entityException.ErrorCode;
import com.sparta.post03.exception.entityException.ErrorResponse;
import com.sparta.post03.exception.entityException.MemberException.*;
import com.sparta.post03.exception.entityException.PostException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //중복 아이디 존재 여부
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicateMemberException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATE_MEMBER_ID, ErrorCode.DUPLICATE_MEMBER_ID.getMessage(), ErrorCode.DUPLICATE_MEMBER_ID.getDetail());
        log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    //비밀번호 비밀번호 확인 일치 여부
    @ExceptionHandler(BadPasswordConfirmException.class)
    public ResponseEntity<ErrorResponse> handlerBadPasswordConfirmException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.BAD_PASSWORD_CONFIRM, ErrorCode.BAD_PASSWORD_CONFIRM.getMessage(), ErrorCode.BAD_PASSWORD_CONFIRM.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //존재하는 회원이 없을때
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerMemberNotFoundException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.MEMBER_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND.getDetail());
        log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //비빌번호가 맞는지 확인
    @ExceptionHandler(BadPasswordException.class)
    public ResponseEntity<ErrorResponse> handlerBadPasswordException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.BAD_PASSWORD, ErrorCode.BAD_PASSWORD.getMessage(), ErrorCode.BAD_PASSWORD.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //Access 토큰이 있는지 없는지
    @ExceptionHandler(AccessTokenNotExistException.class)
    public ResponseEntity<ErrorResponse> handlerAccessTokenNotExistException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.AUTHORIZATION_IS_EMPTY,
                                                       ErrorCode.AUTHORIZATION_IS_EMPTY.getMessage(),
                                                       ErrorCode.AUTHORIZATION_IS_EMPTY.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //Refresh 토큰이 있는지 없는지
    @ExceptionHandler(RefreshTokenNotExistException.class)
    public ResponseEntity<ErrorResponse> handlerRefreshTokenNotExistException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.REFRESH_TOKEN_IS_EMPTY,
                ErrorCode.REFRESH_TOKEN_IS_EMPTY.getMessage(),
                ErrorCode.REFRESH_TOKEN_IS_EMPTY.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    //토큰값이 맞는지
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handlerTokenInvalidException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    //제목 확인
    @ExceptionHandler(TitleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerTitleNotFoundException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TITLE_NOT_FOUND, ErrorCode.TITLE_NOT_FOUND.getMessage(), ErrorCode.TITLE_NOT_FOUND.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    //내용 확인
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerContentNotFoundException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.CONTENT_NOT_FOUND, ErrorCode.CONTENT_NOT_FOUND.getMessage(), ErrorCode.CONTENT_NOT_FOUND.getDetail());
        log.error("error: {}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //게시글 id 확인
    @ExceptionHandler(PostIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerPostIdNotFoundException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.POST_ID_NOT_FOUND, ErrorCode.POST_ID_NOT_FOUND.getMessage(), ErrorCode.POST_ID_NOT_FOUND.getDetail());
        log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //댓글 id 확인
    @ExceptionHandler(CommentIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> CommentIdNotFoundException(Exception e){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.COMMENT_ID_NOT_FOUND, ErrorCode.COMMENT_ID_NOT_FOUND.getMessage(), ErrorCode.COMMENT_ID_NOT_FOUND.getDetail());
        log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
