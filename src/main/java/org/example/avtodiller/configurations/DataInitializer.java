package org.example.avtodiller.configurations;

import org.example.avtodiller.models.CityModel;
import org.example.avtodiller.models.RoleModel;
import org.example.avtodiller.repositories.CitiesRepository;
import org.example.avtodiller.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final CitiesRepository citiesRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, CitiesRepository citiesRepository) {
        this.roleRepository = roleRepository;
        this.citiesRepository = citiesRepository;
    }

    private void initRoles() {
        RoleModel admin = roleRepository.findByName("ROLE_ADMIN").orElseGet(()->{
            RoleModel role = new RoleModel();
            role.setName("ROLE_ADMIN");
            return  roleRepository.save(role);
        });
        RoleModel user = roleRepository.findByName("ROLE_USER").orElseGet(()->{
            RoleModel role = new RoleModel();
            role.setName("ROLE_USER");
            return  roleRepository.save(role);
        });
    }

    private void initCities() {
        CityModel city = citiesRepository.findByName("Москва").orElseGet(()->{
            CityModel cityModel = new CityModel();
            cityModel.setName("Москва");
            return citiesRepository.save(cityModel);
        });
        CityModel SaintP = citiesRepository.findByName("Санкт-Петербург").orElseGet(()->{
            CityModel cityModel = new CityModel();
            cityModel.setName("Санкт-Петербург");
            return citiesRepository.save(cityModel);
        });
    }

    public void run(String... args) throws Exception {
        initRoles();
        initCities();
    }
}
