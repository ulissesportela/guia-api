package com.ulisses.guiaapi.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {

	private Long id;

	private String descricao;

	private Integer mes;

	private Integer ano;

	private Long usuario;
	
	private BigDecimal valor;
	
	private String tipo;

	private String status;
}
