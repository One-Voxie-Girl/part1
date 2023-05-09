package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ConvenorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;

@RestController
public class ConvenorRestController {

    @Autowired
    ConvenorRepository repo;

    @GetMapping("/convenors")
    public ResponseEntity<List<Convenor>> listAllConvenors() {
        List<Convenor> convenors = (List<Convenor>) repo.findAll();
        if (convenors.isEmpty()) {
            return new ResponseEntity<List<Convenor>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Convenor>>(convenors, HttpStatus.OK);
    }

    @PostMapping("/convenors")
    public ResponseEntity<?> createConvenor(@RequestBody  Convenor convenor, UriComponentsBuilder ucBuilder) {

        if (repo.existsById(convenor.getId())) {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("A convenor named " + convenor.getName() + " already exists."),
                    HttpStatus.CONFLICT);
        }
        repo.save(convenor);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/convenors/{id}").buildAndExpand(convenor.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/convenors/{id}")
    public ResponseEntity<?> getConvenor(@PathVariable("id") long id) {

        if (repo.findById(id).isPresent()) {
            Convenor convenor = repo.findById(id).get();
            return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found"),
                    HttpStatus.NOT_FOUND);
    }

    @PutMapping("/convenors/{id}")
    public ResponseEntity<?> updateConvenor(@PathVariable("id") long id, @RequestBody Convenor newConvenor) {

        if (repo.findById(id).isPresent()) {
            Convenor currentConvenor = repo.findById(id).get();
            currentConvenor.setName(newConvenor.getName());
            currentConvenor.setPosition(newConvenor.getPosition());

            currentConvenor.getModules().clear();
            currentConvenor.getModules().addAll(newConvenor.getModules());

            repo.save(currentConvenor);
            return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);

    }


    @DeleteMapping("/convenors/{id}")
    public ResponseEntity<?> deleteConvenor(@PathVariable("id") long id) {

        if (repo.findById(id).isPresent()) {
            repo.findById(id).get().getModules().clear();
            repo.deleteById(id);
            return ResponseEntity.ok(null);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);

    }

    @GetMapping("/convenors/{id}/modules")
    public ResponseEntity<?> getConvenorModules(@PathVariable("id") long id) {

        if (repo.findById(id).isPresent()) {
            List<Module> modules = repo.findById(id).get().getModules();

            return new ResponseEntity<List<Module>>(modules, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found"),
                    HttpStatus.NOT_FOUND);
    }


}
