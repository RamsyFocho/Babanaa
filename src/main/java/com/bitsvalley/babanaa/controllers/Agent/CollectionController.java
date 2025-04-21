package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.services.Agent.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bitsvalley.babanaa.domains.Agent.Collection;

import java.util.Map;

@RestController
@RequestMapping("/api/agent/collection")
public class CollectionController {
    @Autowired private CollectionService collectionService;


    @GetMapping
    public ResponseEntity<?> getAllCollections(@RequestParam(name = "query", required = false) String query) {
        try {
            return ResponseEntity.ok(collectionService.getAllCollections(query));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCollection(@RequestBody Collection collection) {
        try {
            Collection createdCollection = collectionService.createCollection(collection);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCollection);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(collectionService.getCollectionById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollection(@PathVariable Long id, @RequestBody Collection collection) {
        try {
            Collection updatedCollection = collectionService.updateCollection(id, collection);
            return ResponseEntity.ok(updatedCollection);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollection(@PathVariable Long id) {
        try {
            collectionService.deleteCollection(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary() {
        try {
            return ResponseEntity.ok(collectionService.getSummary());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
