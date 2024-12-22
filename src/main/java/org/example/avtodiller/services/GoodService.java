package org.example.avtodiller.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.avtodiller.dtos.CreateGoodDto;
import org.example.avtodiller.dtos.EditGoodDto;
import org.example.avtodiller.models.CityModel;
import org.example.avtodiller.models.GoodModel;
import org.example.avtodiller.models.UserModel;
import org.example.avtodiller.repositories.CitiesRepository;
import org.example.avtodiller.repositories.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.example.avtodiller.utils.GoodsAndCities;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodService {
    private final GoodRepository goodRepository;
    private final CitiesRepository citiesRepository;
    private final UserService userService;
    private Comparator<GoodModel> compare(String direction, String field){
        return new Comparator<GoodModel>() {
            @Override
            public int compare(GoodModel o1, GoodModel o2) {
                try
                {
                    Field f = GoodModel.class.getDeclaredField(field);
                    f.setAccessible(true);
                    Comparable value1 = (Comparable) f.get(o1);
                    Comparable value2 = (Comparable) f.get(o2);
                    if(direction.equals("DESC"))
                    {
                        return value2.compareTo(value1);
                    }
                    return value1.compareTo(value2);
                }

                catch(NoSuchFieldException|IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    public GoodsAndCities list(HttpServletRequest request)
    {
        UserModel user = userService.getUser(request);
        return new GoodsAndCities(citiesRepository.findAll(), goodRepository.findAll(), user.getRole().getName());
    }
    public GoodModel getById(Long id)
    {
        return goodRepository.findById(id).orElse(null);
    }
    public void saveGood(CreateGoodDto good, HttpServletRequest request)
    {
        UserModel currentUser = userService.getUser(request);
        GoodModel goodModel = new GoodModel();
        goodModel.setName(good.name);
        goodModel.setContent(good.content);
        goodModel.setUser(currentUser);
        goodModel.setDateOfArrival(good.dateOfArrival);
        goodModel.setDateOfDeparture(good.dateOfDeparture);
        goodModel.setCityOfArrival(citiesRepository.findById(good.cityOfArrival).orElse(null));
        goodModel.setCityOfDeparture(citiesRepository.findById(good.cityOfDeparture).orElse(null));

        goodRepository.save(goodModel);
    }
    public void editGood(EditGoodDto good)
    {
        GoodModel goodModel = goodRepository.findById(good.id).orElse(null);
        assert goodModel != null;
        goodModel.setName(good.name);
        goodModel.setContent(good.content);
        goodModel.setDateOfArrival(good.dateOfArrival);
        goodModel.setDateOfDeparture(good.dateOfDeparture);
        goodModel.setCityOfArrival(citiesRepository.findById(good.cityOfArrival).orElse(null));
        goodModel.setCityOfDeparture(citiesRepository.findById(good.cityOfDeparture).orElse(null));
        goodRepository.save(goodModel);
    }
    public void deleteGood(Long id)
    {
        goodRepository.deleteById(id);
    }

    public GoodsAndCities filtered(String content, String name, String cityOfDeparture, String dateOfDeparture, String cityOfArrival, String dateOfArrival, HttpServletRequest request)
    {
        UserModel user = userService.getUser(request);
        CityModel arr_city = citiesRepository.findByName(cityOfDeparture).orElse(null);
        CityModel dep_city = citiesRepository.findByName(cityOfArrival).orElse(null);
        List<GoodModel> goods =  goodRepository.findByField(content, name, dep_city, dateOfDeparture, arr_city, dateOfArrival);
        List<CityModel> cities = citiesRepository.findAll();
        return new GoodsAndCities(cities, goods, user.getRole().getName());
    }
}
