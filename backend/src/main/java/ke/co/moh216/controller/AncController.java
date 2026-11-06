package ke.co.moh216.controller;

import jakarta.validation.Valid;
import ke.co.moh216.dto.AncVisitRequest;
import ke.co.moh216.dto.PregnancyRequest;
import ke.co.moh216.entity.AncVisit;
import ke.co.moh216.entity.Pregnancy;
import ke.co.moh216.service.AncService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/anc")
public class AncController {
    private final AncService ancService;

    public AncController(AncService ancService) {
        this.ancService = ancService;
    }

    @PostMapping("/pregnancies")
    public Pregnancy createPregnancy(@Valid @RequestBody PregnancyRequest request) {
        return ancService.createPregnancy(request);
    }

    @GetMapping("/pregnancies")
    public List<Pregnancy> getPregnancies(@RequestParam Long motherId) {
        return ancService.getPregnancies(motherId);
    }

    @PostMapping("/pregnancies/{pregnancyId}/visits")
    public AncVisit addVisit(@PathVariable Long pregnancyId, @Valid @RequestBody AncVisitRequest request) {
        return ancService.addVisit(pregnancyId, request);
    }

    @GetMapping("/pregnancies/{pregnancyId}/visits")
    public List<AncVisit> getVisits(@PathVariable Long pregnancyId) {
        return ancService.getVisits(pregnancyId);
    }
}
