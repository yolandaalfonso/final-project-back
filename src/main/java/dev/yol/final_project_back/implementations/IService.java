package dev.yol.final_project_back.implementations;

import java.util.List;

public interface IService <T,S> {
    public List<T> getEntities();
    public T createEntity(S dto);
    public T getById(Long id);
    public T updateEntity(Long id, S dto);
    public void deleteEntity(Long id);
}
