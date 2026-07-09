package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.service.JournalEntryService;
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
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;


    // Get all journal entries
    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    // Create a new journal entry
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try{
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry);
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
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id) {

        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);

        if (journalEntry.isPresent()) {
            journalEntryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update journal entry by ID
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry newEntry) {

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

            journalEntryService.saveEntry(old);

            return new ResponseEntity<>(old, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

//controller ----> service ----> repository
