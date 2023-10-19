package dev.abidux.duxconfig.config;

import dev.abidux.duxconfig.annotation.Config;

@Config("example.yml")
public class ExampleConfig {

    public boolean SHOULD_SEND_MESSAGE;
    public String EXAMPLE_MESSAGE;
    public int EXAMPLE_INT;
    public double EXAMPLE_DOUBLE;

}