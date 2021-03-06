package com.creitu.minhasFinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.creitu.minhasFinancas.api.dto.LancamentoDTO;
import com.creitu.minhasFinancas.model.enums.EStatusLancamento;
import com.creitu.minhasFinancas.model.enums.ETipoLancamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table( name = "lancamento", schema = "public")
@EqualsAndHashCode( of = {"id"})
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lancamento {

	@Id
	@SequenceGenerator(name = "lancamentoSeq", sequenceName = "lancamento_id_multi_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "lancamentoSeq")
	@Column(unique = true, nullable = false)
	@Getter @Setter private Long id;
	
	@Column(name = "descricao", length = 500)
	@Getter @Setter private String descricao;
	
	@Column(name = "valor")
	@Getter @Setter private BigDecimal valor;
	
	@Column(name = "mes")
	@Getter @Setter private Integer mes;
	
	@Column(name = "ano")
	@Getter @Setter private Integer ano;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "_usuario", foreignKey = @ForeignKey(name = "fk_usuario"))
	@Getter @Setter private Usuario usuario;
	
	@Column(name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Getter @Setter private LocalDate dataCadastro;
	
	@Column(name = "tipo_lancamento")
	@Enumerated(value = EnumType.STRING)
	@Getter @Setter private ETipoLancamento tipoLancamento;
	
	@Column(name = "status_lancamento")
	@Enumerated(value = EnumType.STRING)
	@Getter @Setter private EStatusLancamento statusLancamento;

	public Lancamento(LancamentoDTO lancamentoDTO, Usuario usuario) {
		
		this.id 	= lancamentoDTO.getId();
		this.mes 	= lancamentoDTO.getMes();
		this.ano 	= lancamentoDTO.getAno();
		this.valor 		= lancamentoDTO.getValor();
		this.usuario 	= usuario;
		this.descricao 	= lancamentoDTO.getDescricao();
		this.tipoLancamento 	= ETipoLancamento.valueOf(lancamentoDTO.getTipo());
		this.statusLancamento 	= EStatusLancamento.valueOf(lancamentoDTO.getStatus());
	}
	
	@PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDate.now();
    }

	public Lancamento(String descricao, Integer mes, Integer ano) {
		this.mes 	= mes;
		this.ano 	= ano;
		this.descricao 	= descricao;
	}
}
