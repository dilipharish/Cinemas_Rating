package cinema_real.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class CinemasDto {
	@NotEmpty(message = "The title is required")
    private String title;
	 private Long id;
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	@NotEmpty(message = "The genre is required")
    private String genre;
    
    @NotEmpty(message = "The director is required")
    private String director;
    
   @Min(0)
   private double averageRating;
   
   @Min(1888)
   private int releaseYear;
   
   private MultipartFile imageFileName;

public MultipartFile getImageFileName() {
	return imageFileName;
}

public void setImageFileName(MultipartFile imageFileName) {
	this.imageFileName = imageFileName;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}
  
       
}
