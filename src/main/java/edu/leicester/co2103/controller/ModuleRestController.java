package edu.leicester.co2103.controller;

import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ModuleRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/modules")
public class ModuleRestController {
    @Autowired
    public ModuleRepository moduleRepo;

    //Returns all modules in the repo
    @GetMapping
    public ResponseEntity<Iterable<Module>> getAll() {
        Iterable<Module> modules;
        try {
            modules = moduleRepo.findAll();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (modules == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(modules);
    }

    @PostMapping
    public ResponseEntity<Module> newModule(@RequestParam String code,
                            @RequestParam String title,
                            @RequestParam int level,
                            @RequestParam boolean optional) {
        Module module = new Module();
        module.setCode(code);
        module.setTitle(title);
        module.setLevel(level);
        module.setOptional(optional);
        try {
            moduleRepo.save(module);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(module);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Module> updateModuleByCode(@PathVariable String code,
                                     @RequestParam String title,
                                     @RequestParam int level,
                                     @RequestParam boolean optional) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();
            module.setTitle(title);
            module.setLevel(level);
            module.setOptional(optional);
            moduleRepo.save(module);
            return ResponseEntity.ok(module);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteModule(@PathVariable String code) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            moduleRepo.deleteById(code);
            return ResponseEntity.ok("Module with code " + code + " deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{code}/sessions")
    public ResponseEntity<List<Session>> listSessions(@PathVariable String code) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();
            return ResponseEntity.ok(module.getSessions());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{code}/sessions")
    public ResponseEntity<Session> addSession(@PathVariable String code,
                              @RequestParam String topic,
                              @RequestParam Timestamp time,
                              @RequestParam int duration) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();

            Session session = new Session();

            session.setTopic(topic);
            session.setDatetime(time);
            session.setDuration(duration);

            List<Session> sessions = module.getSessions();
            sessions.add(session);
            module.setSessions(sessions);

            moduleRepo.save(module);

            return ResponseEntity.ok(session);

        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{code}/sessions/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable String code, @PathVariable int id) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();
            List<Session> sessions = module.getSessions();

            for (Session session : sessions) {
                if (session.getId() == id) {
                    return ResponseEntity.ok(session);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{code}/sessions/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable String code, @PathVariable int id,
                                 @RequestParam String topic,
                                 @RequestParam Timestamp time,
                                 @RequestParam int duration) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();

            List<Session> sessions = module.getSessions();
            for (Session session : sessions) {
                if (session.getId() == id) {
                    session.setTopic(topic);
                    session.setDatetime(time);
                    session.setDuration(duration);
                    moduleRepo.save(module);
                    return ResponseEntity.ok(session);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/{code}/sessions/{id}")
    public ResponseEntity<Session> updateSessionPartial(@PathVariable String code, @PathVariable int id,
                                        @RequestParam(required = false) String topic,
                                        @RequestParam(required = false) Timestamp time,
                                        @RequestParam(required = false) int duration) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();

            List<Session> sessions = module.getSessions();

            for (Session session : sessions) {
                if (session.getId() == id) {
                    if (topic != null) {
                        session.setTopic(topic);
                    }
                    if (time != null) {
                        session.setDatetime(time);
                    }
                    if (duration != 0) {
                        session.setDuration(duration);
                    }
                    moduleRepo.save(module);
                    return ResponseEntity.ok(session);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{code}/sessions/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable String code, @PathVariable int id) {
        Optional<Module> moduleTest = moduleRepo.findById(code);
        if (moduleTest.isPresent()) {
            Module module = moduleTest.get();

            List<Session> sessions = module.getSessions();

            for (Session session : sessions) {
                if (session.getId() == id) {
                    sessions.remove(session);
                    moduleRepo.save(module);
                    return ResponseEntity.status(HttpStatus.OK).body("Session with id " + id + " deleted");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with id " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module with code " + code + " not found");
    }
}