package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry) {
        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }

    // Get journal entry by ID
    @GetMapping("/id/{id}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId id) {
        return journalEntryService.findById(id).orElse(null);
    }

    // Delete journal entry by ID
    @DeleteMapping("/id/{id}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId id) {
         journalEntryService.deleteById(id);
         return true;
    }

    // Update journal entry by ID
    @PutMapping("/id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id,
                                               @RequestBody JournalEntry newEntry) {
        JournalEntry old = journalEntryService.findById(id).orElse(null);

        if(old != null){

            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
        }
        journalEntryService.saveEntry(old);
        return old;
    }
}

//controller ----> service ----> repository
