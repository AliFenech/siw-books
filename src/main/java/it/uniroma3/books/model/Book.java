package it.uniroma3.books.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank		//vincolo sui dati
    private String title;
	
	@NotNull
	@Min(1800)
	@Max(2025)
    private Integer year;
	
	@Column(name="imagepath")
    private String imagePath;
    
    @ManyToMany
    private List<Autor> autori;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setUrlImage(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<Autor> getAutori() {
		return autori;
	}

	public void setAutori(List<Autor> autori) {
		this.autori = autori;
	}

	@Override
	public int hashCode() {
		return Objects.hash(autori, title, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(autori, other.autori) && Objects.equals(title, other.title)
				&& Objects.equals(year, other.year);
	}
    
    
}
