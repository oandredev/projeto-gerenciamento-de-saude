package edu.senacsp.health_management.advice;

import edu.senacsp.health_management.annotation.ApiMessage;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import edu.senacsp.health_management.dto.response.general.SuccessResponse;

@RestControllerAdvice
public class SuccessResponseWrapper implements ResponseBodyAdvice<Object>
{
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
    {
        return returnType.hasMethodAnnotation(ApiMessage.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selelctedConverterType, ServerHttpRequest request, ServerHttpResponse response)
    {
        String message = returnType.getMethodAnnotation(ApiMessage.class).value();
        return new SuccessResponse<>(body, message);
    }
}