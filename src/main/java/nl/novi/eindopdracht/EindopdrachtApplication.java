package nl.novi.eindopdracht;

import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class EindopdrachtApplication {

    public static void main(String[] args) {
        SpringApplication.run(EindopdrachtApplication.class, args);
    }

    @Component
    public class DataLoader implements CommandLineRunner {

        private final CategoryRepository categoryRepository;

        public DataLoader(CategoryRepository categoryRepository) {
            this.categoryRepository = categoryRepository;
        }

        @Override
        public void run(String... args) throws Exception {

            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("Gezondheid"));
                categoryRepository.save(new Category("Vervoer"));
                categoryRepository.save(new Category("Gezelschap"));
                categoryRepository.save(new Category("Winkelhulp"));
            }
        }
    }
}
