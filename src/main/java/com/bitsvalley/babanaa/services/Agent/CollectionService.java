package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Collection;
import com.bitsvalley.babanaa.repositories.Agent.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepository;
    public List<?> getAgentCollections(Long agentId) {
        // Retrieve collections for the given agentId
        return collectionRepository.findByAgentId(agentId);
    }

    public Object getAllCollections(String query) {
        // Retrieve all collections based on the provided query
        return collectionRepository.findAll();
    }

    public Collection createCollection(Collection collection) {
        // Create a new collection
        return collectionRepository.save(collection);
    }

    public Object getCollectionById(Long id) {
        // Retrieve a collection by its ID
        return collectionRepository.findById(id);
    }

    public Collection updateCollection(Long id, Collection collection) {
        // Update an existing collection
        Collection existingCollection = (Collection) getCollectionById(id);
        if (existingCollection == null) {
            return null;
        }
        existingCollection.setAmountCollected(collection.getAmountCollected());
        existingCollection.setCurrency(collection.getCurrency());
        existingCollection.setDate(collection.getDate());
        existingCollection.setDueDate(collection.getDueDate());
        existingCollection.setExpectedAmount(collection.getExpectedAmount());
        existingCollection.setNotes(collection.getNotes());
        existingCollection.setStatus(collection.getStatus());
        return collectionRepository.save(existingCollection);
    }

    public void deleteCollection(Long id) {
        // Delete a collection by its ID
        collectionRepository.deleteById(id);

    }

    public Object getSummary() {
        // Retrieve a summary of collections
        // Create a summary map to hold the summary data
        Map<String, Object> summary = new HashMap<>();

        // Retrieve all collections
        List<Collection> collections = collectionRepository.findAll();

        // Initialize counters
        double totalCollected = 0.0;
        double totalExpected = 0.0;
        int completedCount = 0;
        int pendingCount = 0;

        // Calculate the totals and counts
        for (Collection collection : collections) {
            totalCollected += collection.getAmountCollected();
            totalExpected += collection.getExpectedAmount();

            if ("completed".equalsIgnoreCase(collection.getStatus())) {
                completedCount++;
            } else if ("pending".equalsIgnoreCase(collection.getStatus())) {
                pendingCount++;
            }
        }

        // Populate the summary map
        summary.put("totalCollected", totalCollected);
        summary.put("totalExpected", totalExpected);
        summary.put("completedCount", completedCount);
        summary.put("pendingCount", pendingCount);

        return summary;

    }
}
