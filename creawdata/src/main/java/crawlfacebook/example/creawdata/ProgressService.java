package crawlfacebook.example.creawdata;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class ProgressService {
    
    private AtomicInteger progress = new AtomicInteger(0);
    
    public void executeLongRunningProcess() {
        // Perform your long-running process
        for (int i = 0; i <= 100; i++) {
            // Update the progress
            progress.set(i);
            // Simulate some delay
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(5000);
            progress.set(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public int getProgress() {
        // Retrieve the current progress
        return progress.get();
    }
}
