package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ModuleRestController {

    @Autowired
    ModuleRepository repo;
    @Autowired
    SessionRepository repo1;

    @GetMapping("/modules")
    public ResponseEntity<List<Module>> listAllModules() {
        List<Module> modules = (List<Module>) repo.findAll();
        if (modules.isEmpty()) {
            return new ResponseEntity<List<Module>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Module>>(modules, HttpStatus.OK);
    }

    @PostMapping("/modules")
    public ResponseEntity<?> createModule(@RequestBody Module module, UriComponentsBuilder ucBuilder) {

        if (repo.existsById(module.getCode())) {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("A module with code " + module.getCode() + " already exists."),
                    HttpStatus.CONFLICT);
        }
        repo.save(module);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/modules/{code}").buildAndExpand(module.getCode()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/modules/{code}")
    public ResponseEntity<?> getModule(@PathVariable("code") String code) {

        if (repo.findById(code).isPresent()) {
            Module module = repo.findById(code).get();
            return new ResponseEntity<Module>(module, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/modules/{code}")
    public ResponseEntity<?> updateModule(@PathVariable("code") String code, @RequestBody Module newModule) {

        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();
            currentModule.setTitle(newModule.getTitle());
            currentModule.setLevel(newModule.getLevel());
            currentModule.setOptional(newModule.isOptional());
            currentModule.getSessions().clear();
            currentModule.setSessions(newModule.getSessions());
            repo.save(currentModule);
            return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/modules/{code}")
    public ResponseEntity<?> deleteModule(@PathVariable("code") String code) {

        if (repo.findById(code).isPresent()) {
            repo.deleteById(code);
            return ResponseEntity.ok(null);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);

    }

    @GetMapping("/modules/{code}/sessions")
    public ResponseEntity<?> getModuleSessions(@PathVariable("code") String code) {

        if (repo.findById(code).isPresent()) {
            List<Session> sessions = repo.findById(code).get().getSessions();

            return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
    }

    @PostMapping("/modules/{code}/sessions")
    public ResponseEntity<?> createModuleSession(@PathVariable("code") String code,@RequestBody Session session, UriComponentsBuilder ucBuilder) {


        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();
            currentModule.getSessions().add(session);
            repo.save(currentModule);


        }else {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/modules/{code}/sessions").buildAndExpand(code).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }


    @PutMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> putUpdateModuleSession(@PathVariable("code") String code,@PathVariable("id") long id,@RequestBody Session session) {

        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();
            List<Session> currentSessions=currentModule.getSessions();
            for (int i = 0; i < currentSessions.size(); i++){
                if (currentSessions.get(i).getId()==id){
                    Session currentSession=currentSessions.get(i);
                    currentSessions.remove(i);
                    currentSession.setDuration(session.getDuration());
                    currentSession.setTopic(session.getTopic());
                    currentSession.setDatetime(session.getDatetime());
                    currentSessions.add(currentSession);
                    //currentModule.getSessions().clear();
                    //currentModule.getSessions().addAll(currentSessions);
                    repo.save(currentModule);
                    return new ResponseEntity<Module>(currentModule, HttpStatus.OK);

                }
            }
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);



        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);

    }

    @PatchMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> patchUpdateModuleSession(@PathVariable("code") String code,@PathVariable("id") long id,@RequestBody Session session) {

        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();
            List<Session> currentSessions=currentModule.getSessions();
            for (int i = 0; i < currentSessions.size(); i++){
                if (currentSessions.get(i).getId()==id){
                    Session currentSession=currentSessions.get(i);
                    currentSessions.remove(i);
                    currentSession.setDuration(session.getDuration());
                    currentSession.setTopic(session.getTopic());
                    currentSession.setDatetime(session.getDatetime());
                    currentSessions.add(currentSession);
                    //currentModule.getSessions().clear();
                    //currentModule.getSessions().addAll(currentSessions);
                    repo.save(currentModule);
                    return new ResponseEntity<Module>(currentModule, HttpStatus.OK);

                }
            }
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);



        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> deleteModuleSession(@PathVariable("code") String code,@PathVariable("id") long id) {

        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();
            List<Session> currentSessions=currentModule.getSessions();
            for (int i = 0; i < currentSessions.size(); i++){
                if (currentSessions.get(i).getId()==id){
                    currentSessions.remove(i);
                    currentModule.setSessions(currentSessions);
                    repo.save(currentModule);
                    return ResponseEntity.ok(null);
                }else {
                    return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                            HttpStatus.NOT_FOUND);
                }

            }


        }
        return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);


    }


    @GetMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> getModuleSession(@PathVariable("code") String code,@PathVariable("id") long id) {

        if (repo.findById(code).isPresent()) {
            List<Session> sessions = repo.findById(code).get().getSessions();
            for (int i = 0; i < sessions.size(); i++) {
                if (sessions.get(i).getId() == id) {
                    return new ResponseEntity<Session>(sessions.get(i), HttpStatus.OK);
                }
            }
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session with id " + id + " not found"),
                    HttpStatus.NOT_FOUND);

        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
    }




}
