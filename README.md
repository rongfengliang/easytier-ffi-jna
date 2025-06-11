# libeasytier-ffi java jna library

##  using 

* add github repository

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/rongfengliang/*</url>
  </repository>
</repositories>
```

* add deps

```xml
<dependencies>
 <dependency>
    <groupId>com.example</groupId>
    <artifactId>test</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

* code

app.yaml

```yaml
instance_name = "xxxxx"
instance_id = "xxxxx"
dhcp = true
listeners = [
"tcp://0.0.0.0:11010",
"udp://0.0.0.0:11010",
"wg://0.0.0.0:11011",
]
rpc_portal = "0.0.0.0:0"

[network_identity]
network_name = "xxxxx"
network_secret = "xxxxx"

[[peer]]
uri = "tcp://xxx:11010"

[flags]

```

Application.java

> should add  -Deasytier.library.path=`<path to libeasytier.so>` to run

```java
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

```