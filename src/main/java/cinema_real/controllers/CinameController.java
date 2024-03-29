package cinema_real.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cinema_real.models.Cinemas;
import cinema_real.models.CinemasDto;
import cinema_real.service.ProductRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cinemas")
public class CinameController {
	@Autowired
	private ProductRepository repo;

	public ProductRepository getRepo() {
		return repo;
	}

	public void setRepo(ProductRepository repo) {
		this.repo = repo;
	}
	@GetMapping({"","/"})
	public String showCinemasList(Model model)
	{
		List<Cinemas> cinemas = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
		model.addAttribute("cinemas",cinemas);
		return "cinemas/index";
	}
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CinemasDto cinemaDto =new CinemasDto();
		model.addAttribute("cinemaDto", cinemaDto);

		return "cinemas/CreateCinema";
	}
	
	@PostMapping("/create")
	public String createProduct(@Valid @ModelAttribute CinemasDto cinemaDto,BindingResult result ) {
		if(cinemaDto.getImageFileName().isEmpty()) {
			result.addError(new FieldError("productDto","imageFile","The image file is required"));
		}
		if(result.hasErrors()) {
			return "cinemas/CreateCinema";
		}
		
		//save the image file
		MultipartFile image=cinemaDto.getImageFileName();
		Date createdAt=new Date();
		String storageFileName=createdAt.getTime()+"_"+image.getOriginalFilename();
		try {
			String uploadDir ="public/images/";
			Path uploadPath =Paths.get(uploadDir);
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try(InputStream inputStream =image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
			}
			
		}catch(Exception ex) {
			System.out.println("Exception: "+ex.getMessage());
		}
		
	    Cinemas cinema =new Cinemas();
	    cinema.setTitle(cinemaDto.getTitle());
	    cinema.setAverageRating(cinemaDto.getAverageRating());
	    cinema.setImageFileName(storageFileName);
	    cinema.setCretaedAt(createdAt);
	    cinema.setDirector(cinemaDto.getDirector());
	    cinema.setGenre(cinemaDto.getGenre());
	  cinema.setReleaseYear(cinemaDto.getReleaseYear());
	  
	  repo.save(cinema);
		
		return "redirect:/cinemas";
	}
	
	@GetMapping("/edit")
	public String showEditPage(Model model,@RequestParam int id) {
		try {
			Cinemas cinema = repo.findById(id).orElse(null);;
			model.addAttribute("cinema",cinema);
			CinemasDto cinemaDto= new CinemasDto();
			cinemaDto.setId(cinema.getId());
			cinemaDto.setTitle(cinema.getTitle());
		    cinemaDto.setAverageRating(cinema.getAverageRating());
//		    cinema.setImageFileName(storageFileName);
//		    cinema.setCretaedAt(createdAt);
		    cinemaDto.setDirector(cinema.getDirector());
		    cinemaDto.setGenre(cinema.getGenre());
		  cinemaDto.setReleaseYear(cinema.getReleaseYear());
		  model.addAttribute("cinemaDto",cinemaDto);
			
		}catch(Exception ex){
			System.out.println("Exception: "+ex.getMessage());
			return "redirect:/cinemas";
		}
		
		
		
		return "cinemas/EditCinema";
	}
	@PostMapping("/edit")
	public String updateProduct(Model model,@RequestParam int id,@Valid @ModelAttribute CinemasDto cinemaDto,BindingResult result) {
		try {Cinemas cinema=repo.findById(id).get();
		model.addAttribute("cinema",cinema);
		if(result.hasErrors()) {
			return "cinemas/EditCinema";
		}
		
		if(!cinemaDto.getImageFileName().isEmpty()) {
			String uploadDir="public/images/";
			Path oldImagePath=Paths.get(uploadDir+cinema.getImageFileName());
			try {
				Files.delete(oldImagePath);
			}
			catch(Exception ex) {
				System.out.println("Exception: "+ex.getMessage());
			}
			MultipartFile image=cinemaDto.getImageFileName();
			Date createdAt =new Date();
			String storageFileName=createdAt.getTime() +"_"+image.getOriginalFilename();
			try(InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
			}
			cinema.setImageFileName(storageFileName);
		}
		
		cinema.setTitle(cinemaDto.getTitle());
	    cinema.setAverageRating(cinemaDto.getAverageRating());
//	    cinema.setImageFileName(storageFileName);
//	    cinema.setCretaedAt(createdAt);
	    cinema.setDirector(cinemaDto.getDirector());
	    cinema.setGenre(cinemaDto.getGenre());
	  cinema.setReleaseYear(cinemaDto.getReleaseYear());
	  
	  repo.save(cinema);
		
		
		
		}
catch(Exception ex){System.out.println("Exception:"+ex.getMessage());}
		return "redirect:/cinemas";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(@RequestParam int id) {
		try {
			Cinemas cinema =repo.findById(id).get();
			Path imagePath=Paths.get("pulic/image/"+cinema.getImageFileName());
			try {Files.delete(imagePath);}
			catch(Exception ex){
				System.out.println("Exception:"+ex.getMessage());
			}
			repo.delete(cinema);
			
		}catch(Exception ex) {
			System.out.println("Exception: "+ex.getMessage());
		}
		return "redirect:/cinemas";
		
	}
	@GetMapping("/sortByIdAsc")
	public String sortCinemasByIdAsc(Model model) {
	    List<Cinemas> cinemas = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
	    model.addAttribute("cinemas", cinemas);
	    return "cinemas/index";
	}
	@GetMapping("/sortByIdDesc")
	public String sortCinemasByIdDesc(Model model) {
	    List<Cinemas> cinemas = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
	    model.addAttribute("cinemas", cinemas);
	    return "cinemas/index";
	}

	
}
