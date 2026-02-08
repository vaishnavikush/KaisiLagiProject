package com.example.kaisi_lagi;

import com.example.kaisi_lagi.CategoryMaster.CategoryRepository;
import com.example.kaisi_lagi.ImageMaster.ImageMaster;
import com.example.kaisi_lagi.ImageMaster.ImageRepository;
import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.MovieCast.MovieCastRepository;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleRepository;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.UserMaster.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MovieDetailController {


    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    ReviewRepository reviewRepository;

//    @Autowired
//    SubCategoryRepository subCategoryRepository;

    @Autowired
    MovieCastRepository movieCastRepository;


//Movie List

    @GetMapping("allMovie")
    public String allMovie(Model mdl) {
        List<MovieMaster> movie = movieRepository.findAll();

        mdl.addAttribute("movie", movie);
        return "ShowAllMovie";
    }

    //  Getting all details of movie
    @GetMapping("/movies/{movie_id}")
    public String ShowDetail(@PathVariable Long movie_id,@RequestParam(value="MovieRateSuccess", required=false) Boolean success , Model mdl, HttpSession session ) {

        //  Boolean success= (Boolean)mdl.getAttribute("MovieRateSuccess");
        System.out.println("success"+success);
        if(Boolean.TRUE.equals(success))
        {
            mdl.addAttribute("MovieRateSuccess",true);
            System.out.println("ATtribute "+success);
        }


        Boolean flag= (Boolean)session.getAttribute("MovieRateSuccess");
        if(Boolean.TRUE.equals(flag))
        {
            mdl.addAttribute("MovieRateSuccess",true);
            System.out.println("ATtribute "+success);
            session.removeAttribute("MovieRateSuccess");
        }
//

// /  ____________________________________________________________
        //Fatching user to store in session (rating- userId)
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        System.out.println(user+"UUUser");





        // String name= user.get().getUsername();
        //System.out.println(name);

//        Long id = (Long) session.getAttribute("User");
//        System.out.println(id);

        //-------------------------------------------------------

        //Movie_master
        Optional<MovieMaster> movie = movieRepository.findById(movie_id);
        if (movie.isEmpty()) {
            return "allMovie";
        }
        mdl.addAttribute("movie", movie.get());
        session.setAttribute("Movie",movie_id);

        //Image_master  Fetch image and trailer

        if (movie.isPresent()) {

            MovieMaster mv = movie.get();

            // Fetch all images (img column)
            List<ImageMaster> images = imageRepository.findByMovie(mv);
            mdl.addAttribute("image", images);


            // Fetch trailer (url column)
            Optional<ImageMaster> trailerOpt =
                    imageRepository.findFirstByMovieAndUrlIsNotNull(mv);

            if (trailerOpt.isPresent()) {
                mdl.addAttribute("trailerUrl", trailerOpt.get().getUrl());
            }
        }

        // movieCast_master
        List<MovieCast> cast = movieCastRepository.findAllByMovie(movie.get());


        List<PeopleMaster> people = new ArrayList<>();

        List<PeopleMaster> actor= new ArrayList<>();
        List<PeopleMaster> singer= new ArrayList<>();
        List<PeopleMaster> director= new ArrayList<>();
        for (MovieCast c : cast) {

            PeopleMaster ppl = c.getPeople();
            String role= ppl.getRole();
            if(role.equalsIgnoreCase("actor"))
                actor.add(ppl);
            else if(role.equalsIgnoreCase("singer"))
                singer.add(ppl);
            else if(role.equalsIgnoreCase("director"))
                director.add(ppl);
            else
                people.add(ppl);
        }

        mdl.addAttribute("ppl", people);
        mdl.addAttribute("singer",singer);
        mdl.addAttribute("actor",actor);
        mdl.addAttribute("director",director);

        /// FATCH REVIEW AND COMMENTS
        try{
            Long cmtCount=0l;
            Long rateCount=0l,one=0l,two=0l,three=0l,four=0l,five=0l;



            Long totalReview= reviewRepository.countByMovie(movie.get());
            List<ReviewMaster> reviewMaster= reviewRepository.findByMovie(movie.get());
            List<ReviewMaster> comment= new ArrayList<>();
            for(ReviewMaster c : reviewMaster)
            {
                if(c.getPeople()==null)
                {
                    String cmt= c.getComment();
                    int rate = c.getRating();
                    if(cmt != null && !cmt.isBlank())
                    {
                        cmtCount++;
                        comment.add(c);
                    }
                    if(rate!=0)
                    {
                        rateCount++;

                        if(rate==1)
                            one++;
                        else if(rate==2)
                            two++;
                        else if(rate==3)
                            three++;
                        else if(rate==4)
                            four++;
                        else if (rate==5)
                            five++;
                    }


                }

            }
            mdl.addAttribute("comment",comment);

            LocalDate dt= movie.get().getReleaseDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            mdl.addAttribute("releasedDate", dt.format(formatter));


            Double avgRate;
            if(rateCount==0)
                avgRate=0.0;
            else
                avgRate= (double) (((1*one)+(2*two)+(3*three)+(4*four)+(5*five))/rateCount);
            mdl.addAttribute("avgRate",avgRate);
            mdl.addAttribute("rateCount",rateCount);
            System.out.println("SESSION SIZE: " + session.getAttributeNames().hasMoreElements());

            //FETCH Review and rating if found then edit ,else submit as fresh one
            // Get User safely



//            if (userId != null) {
//                usr = userRepository.findById(userId).orElse(null);
//
//            }

            // mdl.addAttribute("userId",userId);


            MovieMaster mv = movieRepository.findById(movie_id)
                    .orElse(null);

            if (mv == null) {
                return "redirect:/not-found";
            }

            List<ReviewMaster> reviewList = reviewRepository.findAllByUser(user);

            for (ReviewMaster e : reviewList) {

                if (e.getPeople() == null) {

                    if (e.getMovie().getMovieId().equals(mv.getMovieId())) {

                        System.out.println(e.getComment() + e.getRating());

                        mdl.addAttribute("rateExists", e.getRating());
                        mdl.addAttribute("commentExists", e.getComment());
                    }
                }
            }
            return "MovieDetailPage";
        } catch (RuntimeException e) {
            System.out.println("hola...");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/image/{imgId}")
    public ResponseEntity<byte[]> Image(@PathVariable(required = false) Long imgId)
    {
        if (imgId == null) {
            System.out.println("Image ID is NULL!");
            return ResponseEntity.badRequest().build();
        }

        Optional<ImageMaster> img = imageRepository.findById(imgId);

        if (img.isEmpty() || img.get().getMovieImages() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(img.get().getMovieImages());
    }


    @GetMapping("/profile/{pId}")
    public ResponseEntity<byte[]> getPeopleImage(@PathVariable Long pId) throws IOException {

        Optional<PeopleMaster> img = peopleRepository.findById(pId);

        if (img.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        byte[] image = img.get().getImage();

        String type = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));

        if (type == null)
        {
            type = "image/jpeg"; // default
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(type))
                .body(image);


    }

//      @GetMapping("/")


}