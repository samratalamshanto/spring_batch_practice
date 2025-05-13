package com.samratalam.spring_batch_practice.config.watchservice;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CustomerBatchWatcherService {
    private final WatchService watchService = FileSystems.getDefault().newWatchService();
    private final Path directory = Paths.get("src/main/resources/file-watcher");

    public CustomerBatchWatcherService() throws IOException {
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        directory.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        );
        startWatching();
    }

    private void startWatching() {
        CompletableFuture.runAsync(() -> {
            try {
                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path file = directory.resolve((Path) event.context());
                        log.info("Watch event: {}, FileName={}", kind.name(), file.getFileName());
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            //job launch here
                            log.info("Will Job Launch, Watch event: {}, FileName={}", kind.name(), file.getFileName());
                        }
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        log.error("Key Watcher no longer valid; Watch event has been reset");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Directory watcher interrupted, errorMsg={}", e.getMessage());
            } catch (Exception e) {
                log.error("Error in CustomerBatchWatcherService", e);
            }
        });
    }

    @PreDestroy
    @SneakyThrows
    public void cleanUp() {
        if (watchService != null) {
            watchService.close();
            log.info("Customer batch watcher service closed.");
        }
    }
}
