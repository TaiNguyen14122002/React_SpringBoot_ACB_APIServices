package com.TaiNguyen.ACBBank.LoggingFilter;

import com.TaiNguyen.ACBBank.Modal.ApiLog;
import com.TaiNguyen.ACBBank.Repository.ApiLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private final AtomicInteger logCounter = new AtomicInteger(1);

    private static final String LOG_FILE_PATH = "/Users/tainguyen/Desktop/ACBBank_API/log.txt";

    @Autowired
    private ApiLogRepository apiLogRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);

        long timeTaken = System.currentTimeMillis() - startTime;
        String requestBody = getStringValue(contentCachingRequestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(contentCachingResponseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

//        LOGGER.info("Filter Logs: METHOD = {}; REQUESTURI = {}; REQUEST = {}; RESPONSE CODE = {}; RESPONSE BODY = {}; TIME TAKEN = {}",
//                request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody, timeTaken
//                );

        int sequenceNumber = logCounter.getAndIncrement();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logEntry = String.format("Filter Logs: SEQ = %d; TIMESTAMP = %s; METHOD = %s; REQUESTURI = %s; REQUEST = %s; RESPONSE CODE = %d; RESPONSE BODY = %s; TIME TAKEN = %d ms",
                sequenceNumber, timestamp, request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody, timeTaken
        );

        LOGGER.info("Filter Logs: {}", logEntry);
        writeLogToFile(logEntry);

        ApiLog log = new ApiLog();
        log.setMethod(request.getMethod());
        log.setRequestUri(request.getRequestURI());
        log.setRequest(requestBody);
        log.setResponseCode(response.getStatus());
        log.setResponseBody(responseBody);
        log.setTimeTaken(timeTaken);

//        apiLogRepository.save(log);


        contentCachingResponseWrapper.copyBodyToResponse();

    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length,characterEncoding);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    private void writeLogToFile( String logEntry){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(logEntry);
            writer.newLine();


        }catch(IOException e){
            LOGGER.error("Không thể ghi log vào file txt", e);
        }
    }
}
