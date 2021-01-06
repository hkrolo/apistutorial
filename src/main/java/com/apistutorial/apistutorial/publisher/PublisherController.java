package com.apistutorial.apistutorial.publisher;

import com.apistutorial.apistutorial.exception.LibraryResourceAlreadyExistException;
import com.apistutorial.apistutorial.exception.LibraryResourceBadRequestException;
import com.apistutorial.apistutorial.exception.LibraryResourceNotFoundException;
import com.apistutorial.apistutorial.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {

    private static Logger logger = LoggerFactory.getLogger(PublisherController.class);
    private PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping(path = "/{publisherId}")
    public ResponseEntity getPublisher(@PathVariable Integer publisherId,
                                       @RequestHeader(value = "Trace-id", defaultValue = "") String traceId)
            throws LibraryResourceNotFoundException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }

        Publisher publisher = publisherService.getPublisher(publisherId, traceId);

        return new ResponseEntity<>(publisherService.getPublisher(publisherId, traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addPublisher(@Valid @RequestBody Publisher publisher,
                                       @RequestHeader(value = "Trace-id", defaultValue = "") String traceId)
            throws LibraryResourceAlreadyExistException {

        logger.debug("Request to add publisher: {}", publisher);

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}, ", traceId);

        publisherService.addPublisher(publisher, traceId);

        logger.debug("Returning response for TraceId: {},", traceId);
        return new ResponseEntity<>(publisher, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{publisherId}")
    public ResponseEntity updatePublisher(@PathVariable Integer publisherId, @Valid @RequestBody Publisher publisher,
                                          @RequestHeader(value = "Trace-id", defaultValue = "") String traceId)
            throws LibraryResourceNotFoundException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }

        publisher.setPublisherId(publisherId);
        publisherService.updatePublisher(publisher, traceId);

        return new ResponseEntity<>(publisher, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{publisherId}")
    public ResponseEntity deletePublisher(@PathVariable Integer publisherId,
                                          @RequestHeader(value = "Trace-id", defaultValue = "") String traceId)
            throws LibraryResourceNotFoundException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }

        publisherService.deletePublisher(publisherId, traceId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchPublisher(@RequestParam String name,
                                             @RequestHeader(value = "Trace-id", defaultValue = "") String traceId)
            throws LibraryResourceBadRequestException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }

        if(!LibraryApiUtils.doesStringValueExist(name)){
            throw new LibraryResourceBadRequestException(traceId, "Please enter a name to search Publisher");
        }

        return new ResponseEntity<>(publisherService.searchPublisher(name, traceId), HttpStatus.OK);
    }
}
