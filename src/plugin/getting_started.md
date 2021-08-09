# Getting Started

## Gradle Setup
To get started making a WTHIT plugin, add the following to your `build.gradle`

### Adding Repository
```groovy
repositories {
  maven { url "https://maven.bai.lol" }
}
```

### Declaring Dependencies
=== "Fabric"
    ```gradle
    dependencies {
      // compile against the API
      modCompileOnly "mcp.mobius.waila:wthit-api:fabric-${wthitVersion}"

      // run against the full jar
      modRuntime "mcp.mobius.waila:wthit:fabric-${wthitVersion}"
    }
    ```
=== "Forge"
    ```gradle
    dependencies {
      // compile against the API
      compileOnly fg.deobf("mcp.mobius.waila:wthit-api:forge-${wthitVersion}")

      // run against the full jar
      runtimeOnly fg.deobf("mcp.mobius.waila:wthit:forge-${wthitVersion}")
    }
    ```

??? note "Why compiling against the API jar?"
    When you compile against the full jar and use non API classes, your mod could break any time WTHIT updates.
    On the other hand, the API jar is guaranteed to be stable. No breaking changes without deprecation time.

    If you found yourself needing to touch non API classes, [open an issue on GitHub](https://github.com/badasintended/wthit/issues/new?assignees=&labels=api&template=api.md&title=).


## Creating Plugins

### Making a Plugin Class
Make a class that implements `IWailaPlugin`
```java
public class MyWailaPlugin implements IWailaPlugin {
  @Override
  public void register(IRegistrar registrar) {
      // register your component here
  }
}
```


### Registering Plugins
=== "Fabric"
    In your `fabric.mod.json` add a custom value
    ```json
    {
      "waila:plugins": {
        "id": "mymod:my_plugin",
        "initializer": "foo.bar.Baz",
      }
    }
    ```
    `waila:plugins` can also be an array of objects instead of a singular object.    
    A required field can be added to specify mods required for that plugin to be loaded.
    It can either be a single string or an array of strings.
    ```json
    {
      "waila:plugins": {
        "id": "mymod:my_plugin",
        "initializer": "foo.bar.Baz",
        "required": "mod_a" 
      }
    }
    ```
=== "Forge"
    Annotate your plugin class with `@WailaPlugin`:
    ```java
    @WailaPlugin(id = "mymod:waila_plugin")
    public class MyWailaPlugin implements IWailaPlugin {}
    ```
    A `required` array can be added to specify mods required for that plugin to be loaded.
    ```java
    @WailaPlugin(id = "mymod:waila_plugin", required = "jei")
    public class MyWailaPlugin implements IWailaPlugin {}
    ```
