# Expvp contribution terms

If you contribute code, concepts or anything else to this project, you agree that these contributions be bundled with
this project and licensed under its license, which can be found at `src/main/resources/LICENSE.txt`.

# Code style

Please use sane code style and auto-format code with standard IntelliJ code style. 
Some special code style guidelines are delivered with the project. Just press `Ctrl+Alt+F`.

Do not put empty lines after class declarations, write fields immediately afterwards, like so:

````java
public class SomeClass extends Something implements Thing, OtherThing {
    public static final String SOME_DATA_PATH = "config.some.data";
    private final ThingModule module;
}
````

# Commit messages

All commit messages **must** comply to the following standards:

 - Commit messages start with an uppercase letter, like a sentence. This is very important.
 - If a commit introduces multiple changes, those must all be in the first line, separated by commas and
     all starting with a new uppercase letter. If that would be too long, a meaningful summary must be found.
	 *Example:* `Add yolo module, Remove old yolo code`
 - The first line is a *meaningful* summary of the changes and should not be any longer than 72 characters
 - After two line breaks, additional description of the changes can be added, if the first line does not 
    fully and clearly describe everything the commit does and why it does it that way.
 - The first line must be written in **present tense**. 
 - An additional `[x.x.x]` tag may be added to indicate that that commit changes the project version.
 - An additional `[BREAKING]` tag may be added to indicate important changes that require changes in
    runtime or development environments. This tag may also be used for major API changes, to help 
	with writing new code.
 - Any related tickets must be noted using the following format: (regex) `#\d{1,6}`. 
    Multiple tickets must be separated by commas. To make the Bug Tracker automatically mark the ticket
	as fixed, use the following format: `Fixes #358`.
	
Here are some examples of complying ("good") commit messages:

 * `[1.6.6] Refactor Module Framework with Dependency Injection`
    
    This commit changes the internal architecture of the Module Framework from a mostly-static
    API based on MTCModuleAdapter to a more dynamic, instance-based approach. In this new
    architecture, all API access is done through ModuleManager, which is stored in a field of
    MTC. Internal loading is done via the package-private ModuleLoader. No changes are necessary
    to existing modules.
      
    Dependency Injection is possible via the new @InjectModule annotation. It injects loaded
    modules into annotated fields based on their declared type. It offers a parameter to declare
    a dependency as required, i.e. the module won't load if it isn't fulfilled. Cyclic
    dependencies are possible if both sides mark the dependency as non-required. Instances are
    injected upon enable and removed (set to null) when the target module (i.e. the module that
    is depended upon) is disabled. If the dependency is required, the dependant module is disabled too.
  
    This commit also adds test cases for ModuleLoader. Please treat them with respect, it has been a
     lot of hassle to get them to work with mocking MTC. Some test-related things have been changed in
    the poms to allow for the tests to run correctly.
	
	_(yes, the whole thing)_
    
 * `Relocate Fanciful to lib package to prevent incompatibilities`
 * `RepeaterModule: Fix null appearing in message list`
 
Here are some examples of non-complying commit messages that will be rejected:

 * `rename MainInventoryOpenListener to VehicleInventoryOpenListener & ignore cancelled events & priority NORMAL -> HIGHEST`
     (way too long first line, first letter lowercase)
 * `fix #386` (not meaningful)
 * `Added HolographicDisplays source because Maven sucks` (not present tense)
 
# Library usage

For nullity annotations, please only use `javax.annotation.Nonnull` and `javax.annotation.Nullable`. These are guaranteed
to be available and do not need extra libraries.

For JSON chat messages, please stick with the Spigot chat API. Use XYC's improved `XyComponentBuilder` instead of
Spigot's `ComponentBuilder` - It provides extra utility methods and more readable syntax.

Prefer `com.google.common.base.Preconditions` over Apache's `Validate`. When using them at the start of a method to
check arguments and/or state, put a blank line after all precondition checks.

# Command help messages

Command help messages must follow a consistent style. That style is as follows for Expvp:

````
/command sub additem --some-option=<arg> <argument> <argument> [optional] [optional...] 
````

Note that the `--some-option` part is not usual Minecraft style and should be avoided. It is not 
yet used in Expvp, but if it were to be used, a library would have to be included for argument 
parsing.

Also note that this is also valid:

````
/command sub additem <Item[:Data value]>
````
