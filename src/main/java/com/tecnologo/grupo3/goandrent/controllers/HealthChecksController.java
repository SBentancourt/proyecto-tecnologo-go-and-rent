package com.tecnologo.grupo3.goandrent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/healthchecks")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class HealthChecksController {

    @Autowired
    private DataSource ds;

    @GetMapping
    public ResponseEntity<?> healthChecks() throws SQLException {
        Connection connection = ds.getConnection();
        try {
            if (connection != null && !connection.isClosed()){
                connection.prepareStatement("SELECT 1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
