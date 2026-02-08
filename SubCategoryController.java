package com.example.kaisi_lagi;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.CategoryMaster.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Controller
public class SubCategoryController {

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    CategoryMaster cate_id;
    Long CategoryId;

    String Cate_Name;
    //ADD NEW SUBCATEGORY
    @GetMapping("/addCate")
    public String subCate(Model mdl)
    {
        mdl.addAttribute("cateId",cate_id);
        mdl.addAttribute("category",Cate_Name);
        return "SubCategoryForm";
    }

    @PostMapping("/subCateForm")
    public String subcate(@RequestParam("name") String name ,Model mdl)
    {

        SubCategory subcate= new SubCategory();
        name= name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();

        subcate.setName(name);
        subcate.setCategory(cate_id);


        subCategoryRepository.save(subcate);

        // to know category name
        CategoryId= cate_id.getId();

        Optional<CategoryMaster> category= categoryRepository.findById(CategoryId);
        String cateName= category.get().getName();
        mdl.addAttribute("category",cateName);
        Cate_Name= cateName;

        List<SubCategory> subCate= subCategoryRepository.findByCategory(cate_id);
        mdl.addAttribute("subcategory",subCate);

        return "ShowSubcate";

    }

    //TO DELETE SUBCATEGORY
    @GetMapping("/deleteSubCat/{id}")
    public String deleteSubCat(@PathVariable Long id)
    {
        subCategoryRepository.deleteById(id);
        return "ShowSubcate";
    }

    //TO EDIT SUBCATEGORY
    @GetMapping("/editSubCat/{id}")
    public String editSubcate(@PathVariable Long id,Model mdl)
    {
        SubCategory cate= subCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Exception: "+id));
        mdl.addAttribute("subCate",cate);
        return "EditSubCate";
    }

    //SAVE CHANGES
    @PostMapping("/subcate/update/{id}")
    public String SaveUpdate(@PathVariable Long id, @ModelAttribute("subcate") SubCategory updated)
    {

        SubCategory subCategory=subCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id:"+id));
        String name=updated.getName();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        subCategory.setName(name);
        subCategoryRepository.save(subCategory);
        return "ShowSubcate";
    }

    // show subcategory by cate_id
    @GetMapping("/showSubcate/{id}/{cate}")
    public String showSubCate(@PathVariable CategoryMaster id, Model mdl,@PathVariable Long cate)
    {
        cate_id= id;

        // to know category name
        Optional<CategoryMaster> category= categoryRepository.findById(cate);
        String cateName= category.get().getName();
        mdl.addAttribute("category",cateName);
        Cate_Name= cateName;

        List<SubCategory> subCate= subCategoryRepository.findByCategory(id);
        mdl.addAttribute("subcategory",subCate);
        return "ShowSubcate";
    }
}
