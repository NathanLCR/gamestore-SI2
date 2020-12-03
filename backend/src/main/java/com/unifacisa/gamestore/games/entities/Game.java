package com.unifacisa.gamestore.games.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor @Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Titulo obrigatorio")
    @ApiModelProperty(value = "Titulo do jogo")
    private String titulo;

    @NotEmpty(message = "imagem Obrigatorio")
    @ApiModelProperty(value = "Endereço url da Imagem")
    private String imagem;

    @NotNull(message = "Preco obrigatorio")
    @Min(value = 0, message = "Preço nao valido")
    @ApiModelProperty(value = "Preço do jogo")
    private double preco;

    @Column()
    private int numeroDeVendas = 0;
}
