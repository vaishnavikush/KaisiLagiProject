//package com.example.kaisi_lagi.PeopleMaster;

//import com.example.kaisi_lagi.MovieCast.MovieCast;
//import com.example.kaisi_lagi.MovieCast.MovieCastRepository;
//import com.example.kaisi_lagi.MovieMaster.MovieMaster;
//import com.example.kaisi_lagi.MovieMaster.MovieRepository;
//import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//public class PeopleDetailController {
//
//    @Autowired
//    PeopleRepository peopleRepository;
//
//    @Autowired
//    MovieCastRepository movieCastRepository;
//
//    @Autowired
//    MovieRepository movieRepository;
//
//    @Autowired
//    ReviewRepository reviewRepository;
//
//    // Display People Detail Page
////    @GetMapping("/people/detail/{pid}")
////    public String showPeopleDetail(@PathVariable Long pid, Model mdl, HttpSession session) {
////
////        Optional<PeopleMaster> people = peopleRepository.findById(pid);
////
////        if (people.isEmpty()) {
////            return "redirect:/";
////        }
////
////        mdl.addAttribute("people", people.get());
////
////        // Filmography - All movies worked in
////        List<MovieCast> filmography = movieCastRepository.findAllByPeople(people.get());
////        mdl.addAttribute("filmography", filmography);
////
////        session.setAttribute("People", pid);
////
////        return "peopleDetail";
////    }
//
//    // Show People Image in Page
//    @GetMapping("/people_image/{pid}")
//    public ResponseEntity<byte[]> peopleImage(@PathVariable Long pid) {
//
//        Optional<PeopleMaster> ppl = peopleRepository.findById(pid);
//
//        if (ppl.isEmpty() || ppl.get().getImage() == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(ppl.get().getImage());
//    }
//
//    @GetMapping("/setpeople")
//    public String setPeople() {
//        return "PeopleMasterForm";
//    }
//
//    @PostMapping("/peopleset")
//    public String peopleset(@RequestParam String peopleName, @RequestParam String role
//            , @RequestParam MultipartFile image, @RequestParam String people_bio,@RequestParam String people_awards,@RequestParam String people_debut,@RequestParam LocalDate people_dob) {
//        PeopleMaster people = new PeopleMaster();
//        //for saving data
//        String[] parts = peopleName.trim().split("\\s+");
//        StringBuilder finalName = new StringBuilder();
//
//        for (String part : parts) {
//            if (!part.isEmpty()) {
//                finalName.append(Character.toUpperCase(part.charAt(0)))
//                        .append(part.substring(1).toLowerCase())
//                        .append(" ");
//            }
//        }
//
//        peopleName = finalName.toString().trim();
//
//
////        String name=peopleName.substring(0,1).toUpperCase()+peopleName.substring(1).toLowerCase();
//        people.setPeopleName(peopleName);
//        people.setPeople_bio(people_bio);
//        people.setPeople_dob(people_dob);
//        people.setPeople_awards(people_awards);
//        people.setPeople_debut(people_debut);
//
//        people.setRole(role);
//        //for saving image
//        System.out.println("Hola kaskkj...");
////        people.setPoints(points);
//        try {
//            if (image != null && !image.isEmpty()) {
//                people.setImage(image.getBytes());
//                System.out.println("Hola....");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        peopleRepository.save(people);
//        return "DATASAVEFORM";
//    }
//
//    //    for
////    showing data;
////
//    @GetMapping("/showpeople")
//    public String showpeople(Model model) {
//        Iterable<PeopleMaster> people = peopleRepository.findAll();
//        List<PeopleMaster> listpeople = new ArrayList<>();
//        people.forEach(listpeople::add);
//        model.addAttribute("peopleshow", listpeople);
//        return "PeopleMasterShowForm";
//
//    }
//
//    @GetMapping("/getimage/{pid}")
//    public ResponseEntity<byte[]> getImage(@PathVariable("pid") Long pid) {
////     int i=Integer.parseInt(pid);
//        PeopleMaster peopleMaster = peopleRepository.findById(pid).orElse(null);
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(peopleMaster.getImage());
//    }
//
////    @GetMapping("/people/{pid}/movies")
////    public String showAllMoviesOfPerson(@PathVariable Long pid, Model model) {
////
////        // 1. Person fetch
////        PeopleMaster person = peopleRepository.findById(pid)
////                .orElseThrow(() -> new RuntimeException("Person not found"));
////
////        model.addAttribute("person", person);
////
////        // 2. MovieCast se saari movies nikaalo
////        List<MovieCast> castList = movieCastRepository.findAllByPeople(person);
////
////        // 3. Sirf movies extract karo
////        List<MovieMaster> allMovies = castList.stream()
////                .map(MovieCast::getMovie)
////                .distinct()
////                .toList();
////
////        model.addAttribute("allMovies", allMovies);
////
////        return "ShowAllMovie"; // 👈 ye aapka ALL MOVIES HTML page
////    }
//
//    @GetMapping("/people/{pid}/movies")
//    public String showAllMoviesOfPerson(@PathVariable Long pid, Model model) {
//
//        // 1. Fetch person
//        PeopleMaster person = peopleRepository.findById(pid)
//                .orElseThrow(() -> new RuntimeException("Person not found"));
//
//        model.addAttribute("person", person);
//
//        // 2. Fetch cast entries
//        List<MovieCast> castList = movieCastRepository.findAllByPeople(person);
//
//        // 3. Extract unique movies
//        List<MovieMaster> allMovies = castList.stream()
//                .map(MovieCast::getMovie)
//                .distinct()
//                .toList();
//
//        model.addAttribute("allMovies", allMovies);
//
//        return "ShowAllMovie";
//    }
//
//    @GetMapping("/people/{pid}")
//    public String showPeopleDetail(@PathVariable Long pid, Model mdl, HttpSession session) {
//
//        Optional<PeopleMaster> people = peopleRepository.findById(pid);
//
//        if (people.isEmpty()) {
//            return "redirect:/";
//        }
//
//        mdl.addAttribute("person", people.get());
//
//        // Get all movies this person worked in
//        List<MovieCast> castList = movieCastRepository.findAllByPeople(people.get());
//
//        // Extract unique movies
//        List<MovieMaster> featuredMovies = castList.stream()
//                .map(MovieCast::getMovie)
//                .distinct()
//                .toList();
//
//        mdl.addAttribute("featuredMovies", featuredMovies);
//
//        session.setAttribute("People", pid);
//
//        return "peopleDetail";
//
package com.example.kaisi_lagi.PeopleMaster;

import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.MovieCast.MovieCastRepository;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Controller
public class PeopleController {

    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    MovieCastRepository movieCastRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    // ✅ Display People Detail Page - UPDATED METHOD
    @GetMapping("/people/{pid}")
    public String showPeopleDetail(@PathVariable Long pid, Model mdl, HttpSession session) {

        Optional<PeopleMaster> people = peopleRepository.findById(pid);

        if (people.isEmpty()) {
            return "redirect:/";
        }

        mdl.addAttribute("person", people.get());

        // ✅ Get all movies this person worked in
        List<MovieCast> castList = movieCastRepository.findAllByPeople(people.get());

        // ✅ Extract unique movies
        List<MovieMaster> allMovies = castList.stream()
                .map(MovieCast::getMovie)
                .distinct()
                .toList();

        // ✅ Send movies to featured section (HTML will show only first 4)
        mdl.addAttribute("featuredMovies", allMovies);

        session.setAttribute("People", pid);

        return "peopleDetailPage";
    }

    // Show People Image in Page
    @GetMapping("/people_image/{pid}")
    public ResponseEntity<byte[]> peopleImage(@PathVariable Long pid) {

        Optional<PeopleMaster> ppl = peopleRepository.findById(pid);

        if (ppl.isEmpty() || ppl.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(ppl.get().getImage());
    }

    @GetMapping("/setpeople")
    public String setPeople(Model model) {
        model.addAttribute("activePage", "setpeople");

        return "PeopleMasterForm";
    }
//
//    @PostMapping("/peopleset")
//    public String peopleset(@RequestParam String peopleName, @RequestParam String role
//            , @RequestParam MultipartFile image, @RequestParam String people_bio,@RequestParam String people_awards,@RequestParam String people_debut,@RequestParam LocalDate people_dob,
//                            @RequestParam int debut_date) {
//        PeopleMaster people = new PeopleMaster();
//        //for saving data
//        String[] parts = peopleName.trim().split("\\s+");
//        StringBuilder finalName = new StringBuilder();
//
//        for (String part : parts) {
//            if (!part.isEmpty()) {
//                finalName.append(Character.toUpperCase(part.charAt(0)))
//                        .append(part.substring(1).toLowerCase())
//                        .append(" ");
//            }
//        }
//
//        peopleName = finalName.toString().trim();
//
//        people.setPeopleName(peopleName);
//        people.setPeople_bio(people_bio);
//        people.setPeople_dob(people_dob);
//        people.setPeople_awards(people_awards);
//        people.setPeople_debut(people_debut);
//        people.setDebut_date(debut_date);
//
//        people.setRole(role);
//        //for saving image
//        System.out.println("Hola kaskkj...");
//        try {
//            if (image != null && !image.isEmpty()) {
//                people.setImage(image.getBytes());
//                System.out.println("Hola....");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        peopleRepository.save(people);
////        return "DATASAVEFORM";
//        return "redirect:/showpeople";
//    }

@PostMapping("/peopleset")
public String peopleset(
        @RequestParam String peopleName,
        @RequestParam String role,
        @RequestParam MultipartFile image,
        @RequestParam String people_bio,
        @RequestParam(value = "people_awards", required = false) String people_awards,
        @RequestParam String people_debut,
        @RequestParam LocalDate people_dob,
        @RequestParam String debut_date
) {
    PeopleMaster people = new PeopleMaster();

    // Format name
    String[] parts = peopleName.trim().split("\\s+");
    StringBuilder finalName = new StringBuilder();
    for (String part : parts) {
        finalName.append(Character.toUpperCase(part.charAt(0)))
                .append(part.substring(1).toLowerCase())
                .append(" ");
    }

    people.setPeopleName(finalName.toString().trim());
    people.setPeople_bio(people_bio);
    people.setPeople_dob(people_dob);
    people.setPeople_awards(people_awards);
    people.setPeople_debut(people_debut);
    people.setDebut_date(Integer.parseInt(debut_date.trim()));
    people.setRole(role);

    try {
        if (image != null && !image.isEmpty()) {
            people.setImage(image.getBytes());
        }
    } catch (Exception e) {
        throw new RuntimeException("Image upload failed", e);
    }

    peopleRepository.save(people);

    return "redirect:/showpeople"; // MUCH better UX
}


    @GetMapping("/showpeople")
    public String showpeople(Model model) {
        Iterable<PeopleMaster> people = peopleRepository.findAll();
        List<PeopleMaster> listpeople = new ArrayList<>();
        people.forEach(listpeople::add);
        model.addAttribute("peopleshow", listpeople);
        model.addAttribute("activePage", "showpeople");
        return "PeopleMasterShowForm";

    }

    @GetMapping("/getimage/{pid}")
    public ResponseEntity<byte[]> getImage(@PathVariable("pid") Long pid) {
        PeopleMaster peopleMaster = peopleRepository.findById(pid).orElse(null);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(peopleMaster.getImage());
    }
    @GetMapping("/delete/{pid}")
    public String DeletePeople(@PathVariable long pid) {
        peopleRepository.deleteById(pid);
//        return "redirect:/showpeople";
        return "redirect:/showpeople";
    }
    //for update
    @GetMapping  ("/updatepeople/{pid}")
    public String getPageUpadate(@PathVariable("pid") long pid , Model model) {
        Optional<PeopleMaster> optionalpeople = peopleRepository.findById(pid);
        model.addAttribute("updatepeoplemaster",optionalpeople.get());
        return "updatepage";
    }

    //
    @PostMapping("/updatepeople")
    public String updatebyid( @ModelAttribute("updatepeoplemaster") PeopleMaster peopleMaster,
                              @RequestParam("img") MultipartFile img) {
        Optional<PeopleMaster> optionalpeople = peopleRepository.findById(peopleMaster.getPid());
        PeopleMaster master=optionalpeople.get();
        master.setPeopleName(peopleMaster.getPeopleName());
        master.setRole(peopleMaster.getRole());
        master.setPeople_dob(peopleMaster.getPeople_dob());
        master.setPeople_debut(peopleMaster.getPeople_debut());
        master.setPeople_awards(peopleMaster.getPeople_awards());
        master.setPeople_bio(peopleMaster.getPeople_bio());
        master.setRole(peopleMaster.getRole());
////        master.setPoints(peopleMaster.getPoints());
//        master.setImage(peopleMaster.getImage());
        try {
            if (img!=null && !img.isEmpty()) {
//                master.setImage(peopleMaster.getImage());
                master.setImage(img.getBytes());
                System.out.println("Hola....");
            }
        } catch (Exception e) {
            System.out.print(e);
            throw new RuntimeException(e);
        }
        peopleRepository.save(master);
        return "redirect:/showpeople";

    }


    // SEARCH BY ROLE (SHOW IN SAME PAGE)
    @PostMapping("/searchbyrole")
    public String SearchByRole(@RequestParam("role") String role, Model model) {

        List<PeopleMaster> searchlist;

        if (role == null || role.isEmpty()) {
            searchlist = peopleRepository.findAll();
        } else {
            searchlist = peopleRepository.findByRoleIgnoreCase(role);
        }

        model.addAttribute("peopleshow", searchlist);

        return "PeopleMasterShowForm"; // same page
    }



    // SEARCH BY NAME (SHOW IN SAME PAGE)
    @GetMapping("/serachbyname")
    public String SearchByName(@RequestParam("peopleName") String peopleName, Model model) {

        List<PeopleMaster> searchList;

        if (peopleName == null || peopleName.isEmpty()) {
            searchList = peopleRepository.findAll();
        } else {
            searchList = peopleRepository.findByPeopleNameContainingIgnoreCase(peopleName);
        }

        model.addAttribute("peopleshow", searchList);

        return "PeopleMasterShowForm"; // same page
    }


    @GetMapping("/filmography")
    public String filmographypage(){
        return "peopleDetailPage";
    }


    // ✅ Show all movies of a person
    @GetMapping("/people/{pid}/movies")
    public String showAllMoviesOfPerson(@PathVariable Long pid, Model model) {

        // 1. Fetch person
        PeopleMaster person = peopleRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        model.addAttribute("person", person);

        // 2. Fetch cast entries
        List<MovieCast> castList = movieCastRepository.findAllByPeople(person);

        // 3. Extract unique movies
        List<MovieMaster> allMovies = castList.stream()
                .map(MovieCast::getMovie)
                .distinct()
                .toList();

        model.addAttribute("allMovies", allMovies);

        return "ShowAllMovie";
    }
}