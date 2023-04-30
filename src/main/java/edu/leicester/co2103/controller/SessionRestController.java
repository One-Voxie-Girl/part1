package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SessionRestController {

    @Autowired
    SessionRepository repo;

    @DeleteMapping("/sessions")
    public ResponseEntity<?> deleteSessions() {

        List<Session> sessions = (List<Session>) repo.findAll();
        if (!sessions.isEmpty()){
            repo.deleteAll();
            return ResponseEntity.ok(null);
        }
        return new ResponseEntity<ErrorInfo>(new ErrorInfo("No sessions to delete"),
                HttpStatus.NOT_FOUND);

    }



}
