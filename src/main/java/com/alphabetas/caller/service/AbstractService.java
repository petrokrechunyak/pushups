package com.alphabetas.caller.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AbstractService<T> {

    T getById(Long id, Update update);

    T save(T t);

    void delete(T t);

}
