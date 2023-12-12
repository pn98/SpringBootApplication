package edu.leicester.co2103.controller;

import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Position;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/convenors")
public class ConvenorRestController {
    @Autowired
    public ConvenorRepository convenorRepo;

    @Autowired
    public ModuleRepository moduleRepo;

    //Returns all convenors in the repo including their modules and sessions
    @GetMapping
    public ResponseEntity<Iterable<Convenor>> getAll() {
        Iterable<Convenor> convenors;
        try {
            convenors = convenorRepo.findAll();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (convenors == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convenors);
    }

    //Used to create a new convenor, a name and position must be provided
    @PostMapping
    public ResponseEntity<Convenor> newConvenor(@RequestParam String name,
                                @RequestParam Position position) {
        Convenor convenor = new Convenor();
        convenor.setName(name);
        convenor.setPosition(position);
        try {
            convenorRepo.save(convenor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(convenor);
    }

    //Gets a specific convenor based on their id
    @GetMapping("/{id}")
    public ResponseEntity<Convenor> getById(@PathVariable Long id) {
        Optional<Convenor> convenorTest = convenorRepo.findById(id);
        if (convenorTest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convenorTest.get());
    }

    //Update a convenor based on their id
    @PutMapping("/{id}")
    public ResponseEntity<Convenor> updateById(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam Position position) {
        Optional<Convenor> convenorTest = convenorRepo.findById(id);
        if (convenorTest.isPresent()) {
            Convenor convenor = convenorTest.get();
            convenor.setName(name);
            convenor.setPosition(position);
            convenorRepo.save(convenor);
            return ResponseEntity.ok(convenor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Deletes a specific convenor based on their id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConvenor(@PathVariable Long id) {
        Optional<Convenor> convenorTest = convenorRepo.findById(id);
        if (convenorTest.isPresent()) {
            Convenor convenorToDelete = convenorTest.get();
            List<Module> modulesToDelete = new ArrayList<>();

            convenorToDelete.getModules().clear();
            convenorRepo.save(convenorToDelete);

            for (Module module : modulesToDelete) {
                moduleRepo.delete(module);
            }

            convenorRepo.delete(convenorToDelete);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //Returns a list of all of a specific convenor's modules
    @GetMapping("/{id}/modules")
    public ResponseEntity<List<Module>> getModules(@PathVariable Long id) {
        Optional<Convenor> convenorTest = convenorRepo.findById(id);
        if (convenorTest.isPresent()) {
            Convenor convenor = convenorTest.get();
            return ResponseEntity.ok(convenor.getModules());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
