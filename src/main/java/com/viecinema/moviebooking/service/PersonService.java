package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.PersonDTO;
import com.viecinema.moviebooking.model.Person;
import com.viecinema.moviebooking.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PersonDTO getPersonById(int personId) {
        return personRepository.findById(personId).map(this::convertToDTO).orElse(null);
    }

    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = convertToEntity(personDTO);
        person = personRepository.save(person);
        return convertToDTO(person);
    }

    public void deletePerson(int personId) {
        personRepository.deleteById(personId);
    }

    private PersonDTO convertToDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPersonId(person.getPersonId());
        personDTO.setName(person.getName());
        personDTO.setBio(person.getBio());
        personDTO.setDateOfBirth(person.getDateOfBirth());
        personDTO.setNationality(person.getNationality());
        return personDTO;
    }

    private Person convertToEntity(PersonDTO personDTO) {
        Person person = new Person();
        person.setPersonId(personDTO.getPersonId());
        person.setName(personDTO.getName());
        person.setBio(personDTO.getBio());
        person.setDateOfBirth(personDTO.getDateOfBirth());
        person.setNationality(personDTO.getNationality());
        return person;
    }
}
