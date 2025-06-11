package com.dalong;

import com.dalong.easytier.EasyTierLibrary;
import com.sun.jna.ptr.PointerByReference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    public static void main(String[] args) throws IOException {
        EasyTierLibrary library = EasyTierLibrary.INSTANCE;
        var config  = Files.readString(Path.of("app.yaml"));
        var result = library.run_network_instance(config);
        if (result ==0) {
            System.out.println("Network instance started successfully.");
            while (true) {
                try {
                    Thread.sleep(3000);
                    int maxLength = 10;
                    EasyTierLibrary.KeyValuePair[] infos = (EasyTierLibrary.KeyValuePair[])
                            (new EasyTierLibrary.KeyValuePair()).toArray(maxLength);
                    int count = EasyTierLibrary.INSTANCE.collect_network_infos(infos, maxLength);
                    for (int i = 0; i < count; i++) {
                        String key = infos[i].key.getString(0);
                        String value = infos[i].value.getString(0);
                        System.out.println(key + ": " + value);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Application interrupted: " + e.getMessage());
                    break; // Exit the loop if interrupted
                }
            }
        } else {
            System.out.println("Failed to start network instance. Error code: " + result);
            var errorMsg = new PointerByReference();
            library.get_error_msg(errorMsg);
            System.out.println("Error message: " + errorMsg.getValue().getString(0));
        }

    }
}
