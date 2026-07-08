package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {

    private Map<ObjectId, JournalEntry> journalEntries = new HashMap<>();

    // Get all journal entries
    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    // Create a new journal entry
    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry) {
        journalEntries.put(myEntry.getId(), myEntry);
        return true;
    }

    // Get journal entry by ID
    @GetMapping("/id/{id}")
    public JournalEntry getJournalEntryById(@PathVariable Long id) {
        return journalEntries.get(id);
    }

    // Delete journal entry by ID
    @DeleteMapping("/id/{id}")
    public JournalEntry deleteJournalEntryById(@PathVariable Long id) {
        return journalEntries.remove(id);
    }

    // Update journal entry by ID
    @PutMapping("/id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id,
                                               @RequestBody JournalEntry myEntry) {

        journalEntries.put(id, myEntry);
        return journalEntries.get(id);
    }
}
