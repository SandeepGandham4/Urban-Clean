package com.example.worker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.workermanagement.exception.AssignmentNotFoundException;
import com.example.workermanagement.exception.GlobalExceptionHandler;
import com.example.workermanagement.exception.WorkerNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleWorkerNotFoundException_shouldReturnNotFoundResponse() {
        // Arrange
        WorkerNotFoundException exception = new WorkerNotFoundException("Worker not found");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleWorkerNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Worker not found", response.getBody());
    }

    @Test
    void handleAssignmentNotFoundException_shouldReturnNotFoundResponse() {
        // Arrange
        AssignmentNotFoundException exception = new AssignmentNotFoundException("Assignment not found");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleAssignmentNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Assignment not found", response.getBody());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerErrorResponse() {
        // Arrange
        Exception exception = new Exception("Generic error");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Generic error", response.getBody());
    }
}