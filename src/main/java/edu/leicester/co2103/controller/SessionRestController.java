package edu.leicester.co2103.controller;

import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sessions")
public class SessionRestController {
    @Autowired
    SessionRepository sessionRepo;
    @Autowired
    ModuleRepository moduleRepo;

    @DeleteMapping()
    public ResponseEntity<String> deleteAllSessions() {
        try {
            sessionRepo.deleteAll();
            return ResponseEntity.ok("All Sessions Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Sessions");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Session>> getSessions(@RequestParam(required = false) Long id,
                                                    @RequestParam(required = false) String code) {
        if (id != null && code != null) {
            Optional<Module> moduleTest = moduleRepo.findById(code);
            if (moduleTest .isPresent()) {
                Module module = moduleTest.get();
                List<Session> sessions = module.getSessions();

                List<Session> sessionList = new ArrayList<>();

                for (Session session : sessions) {
                    if (session.getId() == id) {
                        sessionList.add(session);
                    }
                }
                if (sessionList.isEmpty()) {
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.ok(sessionList);
                }
            }
        }
        if (code != null) {
            Optional<Module> moduleTest = moduleRepo.findById(code);
            if (moduleTest .isPresent()) {
                Module module = moduleTest.get();
                List<Session> sessions = module.getSessions();
                if (sessions.isEmpty()) {
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.ok(sessions);
                }
            }
            return ResponseEntity.notFound().build();
        }
        if (id != null) {
            Optional<Session> sessionTest = sessionRepo.findById(id);
            if (sessionTest.isPresent()) {
                List<Session> sessionList = new ArrayList<>();
                sessionTest.ifPresent(sessionList::add);
                return ResponseEntity.ok(sessionList);
            }
            return ResponseEntity.notFound().build();
        } else {
            Iterable<Session> sessions = sessionRepo.findAll();
            List<Session> sessionList = new ArrayList<>();
            for (Session session : sessions) {
                sessionList.add(session);
            }
            if (sessionList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(sessionList);
            }
        }
    }

}
