package am.plotnikov.example;

import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
                .banner(false)
                .start();
    }
}
