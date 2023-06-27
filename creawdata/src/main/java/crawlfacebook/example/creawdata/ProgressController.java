package crawlfacebook.example.creawdata;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgressController {
    
    @Autowired
    private ProgressService progressService;
    
    @GetMapping("/startProcess")
    public ResponseEntity<String> startProcess() {
        // Start the long-running process asynchronously
        CompletableFuture.runAsync(() -> progressService.executeLongRunningProcess());
        return ResponseEntity.ok("Process started");
    }
    
    @GetMapping("/progress")
    public ResponseEntity<Integer> getProgress() {
        // Retrieve the current progress from the service
        int progress = progressService.getProgress();
        return ResponseEntity.ok(progress);
    }
}

