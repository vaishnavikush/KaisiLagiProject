package com.example.kaisi_lagi.CategoryMaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/Category")
    public String category()
    {
        return "Category";
    }


    //      ADD CATEGORY
    @GetMapping("/addCategory")
    public String setCat()
    {
        return "CategoryForm";
    }

    @PostMapping("/categoryFill")
    public String getCate(@RequestParam("name")String name, Model model)
    {
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();

        CategoryMaster cat = categoryRepository.findByName(name);
        if(cat == null) {
            CategoryMaster category = new CategoryMaster();


            category.setName(name);

            categoryRepository.save(category);
        }
        else {
            System.out.println(cat);
            model.addAttribute("AlreadyExists",true);
            return "CategoryForm";
        }
        return "redirect:/showCategory";

    }
    //      SHOW ALL CATEGORY
    @GetMapping("/showCategory")
    public String showAll(Model mdl)
    {
        List<CategoryMaster> cate = categoryRepository.findAll();
        mdl.addAttribute("category",cate);

        return "ShowCategory";
    }

    //      DELETE CATEGORY
    @GetMapping("/deleteCat/{id:[0-9]+}")
    public String DeleteCate(@PathVariable long id)
    {
        categoryRepository.deleteById(id);
        return "redirect:/showCategory";
    }


    //      EDIT CATEGORY
    @GetMapping("/editCate/{id:[0-9]+}")
    public String editCate(@PathVariable Long id,Model mdl)
    {
        CategoryMaster category= categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category id: " + id));

        mdl.addAttribute("cate",category);

        return "EditCategory";
    }

    //save change
    @PostMapping("/category/update/{id:[0-9]+}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("cate") CategoryMaster updatedCate)
    {
        CategoryMaster category= categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Student id: " + id));
        String name=updatedCate.getName();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        category.setName(name);
        categoryRepository.save(category);

        return "redirect:/showCategory";
    }
    @Component
    public class CategoryConverter implements Converter<String, CategoryMaster> {

        @Autowired
        private CategoryRepository categoryRepository;

        @Override
        public CategoryMaster convert(String source) {
            if (source == null || source.isBlank()) {
                return null;
            }
            return categoryRepository.findById(Long.valueOf(source))
                    .orElseThrow();
        }
    }

}
