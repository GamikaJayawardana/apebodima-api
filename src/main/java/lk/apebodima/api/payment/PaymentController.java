package lk.apebodima.api.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final DialogGenieService dialogGenieService;

    public PaymentController(DialogGenieService dialogGenieService) {
        this.dialogGenieService = dialogGenieService;
    }

    @PostMapping("/genie-notify")
    public ResponseEntity<Void> paymentNotify(@RequestParam Map<String, String> notification) {
        dialogGenieService.processNotify(notification);
        return ResponseEntity.ok().build();
    }
}