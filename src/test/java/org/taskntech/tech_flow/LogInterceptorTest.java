package org.taskntech.tech_flow;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.taskntech.tech_flow.interceptor.LoggingInterceptor;

import static org.assertj.core.api.Assertions.assertThat;

public class LogInterceptorTest {
    private LoggingInterceptor loggingInterceptor;
    private LogCaptor logCaptor;


    @BeforeEach
    public void setUp() {
        loggingInterceptor = new LoggingInterceptor();
        logCaptor = LogCaptor.forClass(LoggingInterceptor.class); // Captures logs for LoggingInterceptor
    }

    @Test
    public void testprehandle() throws Exception {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setMethod("GET");
        mockRequest.setRequestURI("/test");

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // Call the preHandle method
        loggingInterceptor.preHandle(mockRequest, mockResponse, null);

        // Verify the captured log messages
        assertThat(logCaptor.getInfoLogs())
            .containsExactly("Incoming Request: Method = GET, URI = /test");
    }
}



