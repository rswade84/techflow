package org.taskntech.tech_flow.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    //handlerInterceptor allows for http requests/response to be intercepted
    //at three points
    //1. before request is received (prehandle)
    //2. before response is sent (posthandle) -- didnt need for the scope of its function
    //3. after response is sent (after completion)


    //instance of a slf4j logger that will communicate with log4j2 to print to files and console
    //slf4j allows you to work to with any logging framework-- this app uses log4j2
    //LoggerFactory.getLogger(LoggingInterceptor.class)
        //loggerfactroy-- creates instance of a logger class -- allows one to use different frameworks
        //LoggingInterceptor.class-- ties all logs to a specific class- where they originate from
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    //HttpServletRequest request: request being sent
    //HttpServletResponse response: can edit what part of the response will be but really no response at this point
    //Object handler: target controller

    //read requests before controller receives them
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //URI gets path or route from controller
        logger.info("Incoming Request: Method = {}, URI = {}", request.getMethod(), request.getRequestURI());// logs them
        return true; // Continue the request-- allows the controller to receive the request
    }

    //HttpServletRequest request: request that was sent
    //HttpServletResponse response: response sent from controller
    //Object handler: controller that sent the response

    //reads response after controller has gotten the request
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Outgoing Response: Status = {}", response.getStatus());// logs them
    }
}


