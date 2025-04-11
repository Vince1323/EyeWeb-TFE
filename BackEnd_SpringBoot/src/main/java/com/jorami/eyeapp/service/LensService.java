package com.jorami.eyeapp.service;

import com.jorami.eyeapp.model.Lens;

import java.util.List;

public interface LensService {

    List<Lens> getLensesByLensManufacturer(Long lensManufacturerId);

    void importLensesFromWebSite();

}
