package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="clientes")
public class Cliente implements Serializable {

  /**
   * Mensajes de error de validación
   */
  private final String EMPTY_FIELD_ERROR_MSG = "Este campo no puede estar vacío";
  private final String EMAIL_FORMAT_ERROR_MSG = "El formato de email es incorrecto";
  private final String TEXT_SIZE_ERROR_MSG = "El tamaño del texto debe ser de entre {min} y {max} caracteres";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = EMPTY_FIELD_ERROR_MSG)
  @Size(min=4, max=12, message = TEXT_SIZE_ERROR_MSG)
  @Column(nullable = false)
	private String nombre;
 
  @NotEmpty(message = EMPTY_FIELD_ERROR_MSG)
  @Column(nullable = false)
	private String apellido;

  @NotEmpty(message = EMPTY_FIELD_ERROR_MSG)
  @Email(message = EMAIL_FORMAT_ERROR_MSG)
  @Column(nullable = false, unique = true)
	private String email;
	
	@Column(name="create_at", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date createAt;


	@PrePersist
	private void prePersist() {
		createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
