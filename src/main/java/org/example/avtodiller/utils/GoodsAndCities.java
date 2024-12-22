package org.example.avtodiller.utils;

import org.example.avtodiller.models.CityModel;
import org.example.avtodiller.models.GoodModel;

import java.util.List;

public class GoodsAndCities {
    public List<CityModel> cities;
    public List<GoodModel> goods;
    public String role;

    public GoodsAndCities(List<CityModel> cities, List<GoodModel> goods, String role) {
        this.cities = cities;
        this.goods = goods;
        this.role = role;
    }
}
