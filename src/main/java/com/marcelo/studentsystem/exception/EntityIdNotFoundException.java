package com.marcelo.studentsystem.exception;

/**
 * Custom exception for entities that were not found in the database
 */
public class EntityIdNotFoundException extends RuntimeException {
    public EntityIdNotFoundException(Long id, String entity) {
        super(String.format("%s id not found : %d", entity, id));
    }
}
