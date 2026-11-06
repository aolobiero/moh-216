package ke.co.moh216.controller;

import jakarta.validation.Valid;
import ke.co.moh216.dto.ChildRequest;
import ke.co.moh216.dto.ChildResponse;
import ke.co.moh216.service.ChildService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @PostMapping
    public ResponseEntity<ChildResponse> registerChild(@Valid @RequestBody ChildRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(childService.registerChild(request));
    }

    @GetMapping
    public List<ChildResponse> searchChildren(
        @RequestParam(required = false) String search
    ) {
        return childService.searchChildren(search);
    }

    @GetMapping("/{id}")
    public ChildResponse getChild(@PathVariable Long id) {
        return childService.getChild(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
