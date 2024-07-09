package com.resto.menu.controller;

import com.google.gson.Gson;
import com.resto.menu.ResourceNotFoundException;
import com.resto.menu.dto.MenuDTO;
import com.resto.menu.entity.Menu;
import com.resto.menu.service.FileStorageService;
import com.resto.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private FileStorageService fileStorageService;

    private final Gson gson = new Gson();

    @GetMapping
    public List<Menu> getAllMenus() {
        return menuService.findAll();
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public Menu createMenu(@RequestPart("menu") String menuDTOString,
                           @RequestPart("imageFile") MultipartFile imageFile) {
        MenuDTO menuDTO = gson.fromJson(menuDTOString, MenuDTO.class);

        String fileName = fileStorageService.storeFile(imageFile);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        Menu menu = new Menu();
        menu.setTitle(menuDTO.getTitle());
        menu.setPrice(menuDTO.getPrice());
        menu.setImage(fileDownloadUri);

        return menuService.save(menu);
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable Long id) {
        return menuService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu not found for this id :: " + id));
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public Menu updateMenu(@PathVariable Long id,
                           @RequestPart("menu") String menuDTOString,
                           @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        MenuDTO menuDTO = gson.fromJson(menuDTOString, MenuDTO.class);

        Menu menu = menuService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu not found for this id :: " + id));

        menu.setTitle(menuDTO.getTitle());
        menu.setPrice(menuDTO.getPrice());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(imageFile);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();
            menu.setImage(fileDownloadUri);
        }

        return menuService.save(menu);
    }


    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id) {
        menuService.deleteById(id);
    }
}
