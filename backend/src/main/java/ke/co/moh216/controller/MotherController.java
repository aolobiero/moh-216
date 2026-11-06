package ke.co.moh216.controller;

import ke.co.moh216.entity.Mother;
import ke.co.moh216.service.MotherService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mothers")
public class MotherController {

    private final MotherService motherService;
    private final ke.co.moh216.service.QrCodeService qrCodeService;


    public MotherController(
            MotherService motherService,
            ke.co.moh216.service.QrCodeService qrCodeService) {
        this.motherService = motherService;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping
    public ResponseEntity<Mother> registerMother(@RequestBody Mother mother) {
        Mother savedMother = motherService.registerMother(mother);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMother);
    }

    @GetMapping
    public ResponseEntity<List<Mother>> getAllMothers(
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(motherService.searchMothers(search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mother> getMotherById(@PathVariable Long id) {
        return ResponseEntity.ok(motherService.getMotherById(id));
    }

    @GetMapping(value = "/{id}/qr", produces = "image/png")
    public ResponseEntity<byte[]> getMotherQrCode(@PathVariable Long id) {
        Mother mother = motherService.getMotherById(id);

        byte[] image = qrCodeService.generateQrCode(mother.getQrToken());

        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .body(image);
    }
}
