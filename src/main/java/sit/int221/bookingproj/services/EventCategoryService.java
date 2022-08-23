package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.exception.NotFoundException;
import sit.int221.bookingproj.exception.UniqueEventCategoryNameException;
import sit.int221.bookingproj.repositories.EventCategoryRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventCategoryService {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @ExceptionHandler(UniqueEventCategoryNameException.class)
    public void handleUniqueEventCategoryNameException() {}
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundEventException() {}

    public List<EventCategoryDto> getAllEventCategoryDto(){
        return eventCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "eventCategoryId")).stream().map(this::castEventCategoryDto).collect(Collectors.toList());
    }

    public Optional<EventCategory> getEventCategoryById(Integer id) throws NotFoundException {
        return Optional.ofNullable(eventCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Can not find for id " + id)));
    }

    public EventCategory createEventCategory(EventCategory newEventCategory) throws UniqueEventCategoryNameException {
        if(checkUniqueName(newEventCategory)){
            newEventCategory.setEventCategoryName(newEventCategory.getEventCategoryName().trim());
            newEventCategory.setEventCategoryDescription(newEventCategory.getEventCategoryDescription().trim());
            return eventCategoryRepository.saveAndFlush(newEventCategory);
        }
        else{
            throw new UniqueEventCategoryNameException("event category name must be unique");
        }
    }

    public EventCategory updateEventCategory(Integer id, @Valid EventCategory updateEventCategory) throws UniqueEventCategoryNameException {
        Optional<EventCategory> optionalEventCategory = eventCategoryRepository.findById(id);
        if(optionalEventCategory.isPresent()){
            if(checkUniqueName(updateEventCategory)){
                return eventCategoryRepository.saveAndFlush(updateEventCategory);
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can not find event category id" + id);
        }
        return updateEventCategory;
    }

    public boolean checkUniqueName(EventCategory eventCategory) throws UniqueEventCategoryNameException {
        boolean check;
        EventCategory eventCategory1 = eventCategoryRepository.findAllByEventCategoryName(eventCategory.getEventCategoryName());
        if(eventCategory1 == null || eventCategory.getEventCategoryId() == eventCategory1.getEventCategoryId()){
            check = true;
        }
        else{
            check = false;
            throw new UniqueEventCategoryNameException("Event Category Name must be Unique");
        }
        return check;
    }

    public EventCategoryDto castEventCategoryDto(EventCategory eventCategory){
        EventCategoryDto eventCategoryDto = new EventCategoryDto();
        eventCategoryDto.setEventCategoryId(eventCategory.getEventCategoryId());
        eventCategoryDto.setEventCategoryName(eventCategory.getEventCategoryName());
        eventCategoryDto.setEventCategoryDescription(eventCategory.getEventCategoryDescription());
        eventCategoryDto.setEventDuration(eventCategory.getEventDuration());
        return  eventCategoryDto;
    }
}
