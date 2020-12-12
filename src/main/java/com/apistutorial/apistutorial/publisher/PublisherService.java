package com.apistutorial.apistutorial.publisher;

import com.apistutorial.apistutorial.exception.LibraryResourceAlreadyExistException;
import com.apistutorial.apistutorial.exception.LibraryResourceNotFoundException;
import com.apistutorial.apistutorial.util.LibraryApiUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherService {

    private PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public void  addPublisher(Publisher publisherToBeAdded)
            throws LibraryResourceAlreadyExistException {

        PublisherEntity publisherEntity = new PublisherEntity(
            publisherToBeAdded.getName(),
            publisherToBeAdded.getEmailId(),
            publisherToBeAdded.getPhoneNumber()
        );

        PublisherEntity addedPublisher = null;

        try{
            addedPublisher = publisherRepository.save(publisherEntity);
        }catch(DataIntegrityViolationException e){
            throw new LibraryResourceAlreadyExistException("Publisher already exist!");
        }

        publisherToBeAdded.setPublisherId(addedPublisher.getPublisherid());
    }

    public Publisher getPublisher(Integer publisherId) throws LibraryResourceNotFoundException {

        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
        Publisher publisher = null;

        if(publisherEntity.isPresent()){

            PublisherEntity pe = publisherEntity.get();
            publisher = createPublisherFromEntity(pe);
        }else{
            throw new LibraryResourceNotFoundException("Publisher Id: " + publisherId + " Not Found");
        }

        return publisher;
    }

    private Publisher createPublisherFromEntity(PublisherEntity pe) {

        return new Publisher(pe.getPublisherid(), pe.getName(), pe.getEmailId(), pe.getPhoneNumber());
    }

    public void updatePublisher(Publisher publisherToBeUpdated) throws LibraryResourceNotFoundException {

        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());
        Publisher publisher = null;

        if(publisherEntity.isPresent()){

            PublisherEntity pe = publisherEntity.get();
            if(LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getEmailId())){
                pe.setEmailId(publisherToBeUpdated.getEmailId());
            }
            if(LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getPhoneNumber())){
                pe.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
            }
            publisherRepository.save(pe);
            publisherToBeUpdated = createPublisherFromEntity(pe);
        }else{
            throw new LibraryResourceNotFoundException("Publisher Id: " + publisherToBeUpdated.getPublisherId() + " Not Found");
        }


    }

    public void deletePublisher(Integer publisherId) throws LibraryResourceNotFoundException {

        try{
            publisherRepository.deleteById(publisherId);
        }catch(EmptyResultDataAccessException e){
            throw new LibraryResourceNotFoundException("Publisher Id: " + publisherId + " Not Found");
        }

    }
}
