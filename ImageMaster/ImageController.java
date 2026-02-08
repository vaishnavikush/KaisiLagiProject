package com.example.kaisi_lagi.ImageMaster;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class ImageController {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    MovieRepository movieRepository;
    MovieMaster movie_Id;
    @GetMapping("setImg")
    public String setImg()
    {
        return "ImageUploadPage";
    }

    @PostMapping("/success")
    @ResponseBody()
    public String upload(@RequestParam("movie_id")Long id,@RequestParam("image")MultipartFile img)throws Exception
    {
        try{ ImageMaster imageMaster =new ImageMaster();
            Optional<MovieMaster> movie= movieRepository.findById(id);
            movie_Id=movie.get();
            imageMaster.setMovie(movie_Id);
            imageMaster.setMovieImages(img.getBytes());
            imageRepository.save(imageMaster);
            return "confirm";

        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/getimages")
    public String getImages(Model model){
        model.addAttribute("allImages",imageRepository.findAll());
        return "showImages";
    }

    @GetMapping("/images/{id:[0-9]+}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageMaster image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found!"));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getMovieImages());

    }

}
