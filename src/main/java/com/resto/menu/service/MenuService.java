package com.resto.menu.service;

import com.resto.menu.ResourceNotFoundException;
import com.resto.menu.entity.Menu;
import com.resto.menu.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    public Menu updateMenu(Long id, Menu menuDetails) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu not found for this id :: " + id));
        menu.setTitle(menuDetails.getTitle());
        menu.setPrice(menuDetails.getPrice());
        menu.setImage(menuDetails.getImage());
        return menuRepository.save(menu);
    }
}
