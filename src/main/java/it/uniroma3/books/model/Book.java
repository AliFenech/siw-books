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
	
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] image;


	
    
    @ManyToOne
    private Autor autore;

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

	public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

	public Autor getAutore() {
		return autore;
	}

	public void setAutore(Autor autore) {
		this.autore = autore;
	}

	@Override
	public int hashCode() {
		return Objects.hash(autore, title, year);
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
		return Objects.equals(autore, other.autore) && Objects.equals(title, other.title)
				&& Objects.equals(year, other.year);
	}
    
    
}
