package com.example.trainer.api.exceptionHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionReader extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource mensagemUsu;
	
	
@Override
protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
	String mensagemUsuario = mensagemUsu.getMessage("mensagem.invalida",null, LocaleContextHolder.getLocale()); 
	String mensagemDesenvolvendor = ex.getCause().toString();
	List<Erro> erros = Arrays.asList(new Erro(mensagemDesenvolvendor,mensagemUsuario));
	return handleExceptionInternal(ex,erros, headers, HttpStatus.BAD_REQUEST, request);}

@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
	List<Erro> erros = criarListaErros(ex.getBindingResult());
	return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
}
     private List<Erro> criarListaErros(BindingResult result){
    	 List <Erro> erros = new ArrayList<>() ;
    	 
    	 for(FieldError erro : result.getFieldErrors()) {
          String mensagemUsuario = mensagemUsu.getMessage(erro, LocaleContextHolder.getLocale()) ;
         String mensagemDesenvolvedor = erro.toString();
          erros.add(new Erro(mensagemDesenvolvedor,mensagemUsuario));
    	 } 
      
      return erros;
} 

public class Erro{
	private String mensagemDesenvolvendor;
	private String mensagemUsuario;
	
	
	public Erro(String mensagemDesenvolvendor, String mensagemUsuario) {
		super();
		this.mensagemDesenvolvendor = mensagemDesenvolvendor;
		this.mensagemUsuario = mensagemUsuario;
	}
	public String getMensagemDesenvolvendor() {
		return mensagemDesenvolvendor;
	}
	public String getMensagemUsuario() {
		return mensagemUsuario;
	}
	
}

}
