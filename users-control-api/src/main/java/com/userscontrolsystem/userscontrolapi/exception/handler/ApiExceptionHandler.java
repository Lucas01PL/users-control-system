package com.userscontrolsystem.userscontrolapi.exception.handler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.userscontrolsystem.userscontrolapi.exception.BusinessException;
import com.userscontrolsystem.userscontrolapi.exception.EntityNotFoundException;
import com.userscontrolsystem.userscontrolapi.exception.UserNotAdministratorException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Problem.Field> campos = new ArrayList<>();
		
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new Problem.Field(nome, mensagem));
		}
		
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(OffsetDateTime.now());
		problem.setTitle("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
		problem.setFields(campos);
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		Problem problema = new Problem();
		problema.setStatus(status.value());
		problema.setDateTime(OffsetDateTime.now());
		problema.setTitle(ex.getMessage());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusiness(BusinessException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		Problem problema = new Problem();
		problema.setStatus(status.value());
		problema.setDateTime(OffsetDateTime.now());
		problema.setTitle(ex.getMessage());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(UserNotAdministratorException.class)
	public ResponseEntity<Object> handleUserNotAdministrator(UserNotAdministratorException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		
		Problem problema = new Problem();
		problema.setStatus(status.value());
		problema.setDateTime(OffsetDateTime.now());
		problema.setTitle("Usuário não está autorizado. Usuário não é Administrador.");
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
}