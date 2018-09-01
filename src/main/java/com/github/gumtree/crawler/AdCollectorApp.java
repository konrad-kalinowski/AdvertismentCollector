package com.github.gumtree.crawler;

import com.github.gumtree.crawler.config.AppConfiguration;
import com.github.gumtree.crawler.config.QueueConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.IOException;


@SpringBootApplication
@Import({AppConfiguration.class, QueueConfiguration.class})
public class AdCollectorApp implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplication(AdCollectorApp.class).run(args);
    }

    @Override
    public void run(String... args) {


        try {
            System.out.println("Finish initialization, waiting for key press.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
