package com.marcelo.studentsystem.exception;

public class EntityIdNotFoundException extends RuntimeException {
    public EntityIdNotFoundException(Long id, String entity) {
        super(String.format("%s id not found : %d", entity, id));
    }
}
