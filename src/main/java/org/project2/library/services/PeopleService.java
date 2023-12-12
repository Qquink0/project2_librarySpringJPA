package org.project2.library.services;

import org.hibernate.Hibernate;
import org.project2.library.models.Book;
import org.project2.library.models.Person;
import org.project2.library.repositories.PeopleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    public Person show(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> show(String name) {
        return peopleRepository.findByName(name);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    public List<Book> join(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            Date currentDate = new Date();

            for (Book book : person.get().getBooks()) {
                if (currentDate.getTime() - book.getData().getTime() > (10 * 24 * 60 * 60 * 1000)) {
                    book.setDelay(true);
                }
            }

            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
