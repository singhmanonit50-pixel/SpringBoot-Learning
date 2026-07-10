package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.service.JournalEntryService;
import com.example.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // Get all journal entries
    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {

        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> all = user.getJournalEntries();

        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Create a new journal entry
    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry , @PathVariable String username) {
        try{


            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry , username);
            return new ResponseEntity<>(myEntry , HttpStatus.CREATED);

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    // Get journal entry by ID
    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {

        Optional<JournalEntry> journalEntry =  journalEntryService.findById(id);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get() , HttpStatus.OK);
        }return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete journal entry by ID
    @DeleteMapping("/id/{username}/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id , @PathVariable String username) {

        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);

        if (journalEntry.isPresent()) {
            journalEntryService.deleteById(id , username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update journal entry by ID
    @PutMapping("/id/{username}/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry newEntry,
            @PathVariable String username) {

        JournalEntry old = journalEntryService.findById(id).orElse(null);

        if (old != null) {

            old.setTitle(
                    newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()
                            ? newEntry.getTitle()
                            : old.getTitle());

            old.setContent(
                    newEntry.getContent() != null && !newEntry.getContent().isEmpty()
                            ? newEntry.getContent()
                            : old.getContent());


            journalEntryService.saveEntry(old, username);

            return new ResponseEntity<>(old, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

//controller ----> service ----> repository
