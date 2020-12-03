package com.unifacisa.gamestore.games.controllers;


import com.unifacisa.gamestore.games.entities.Game;
import com.unifacisa.gamestore.games.repositories.GameRepository;
import com.unifacisa.gamestore.shared.entities.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin()
@RequestMapping(value = "/api/v2/games", produces = "application/json")
@RestController
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping()
    @ApiOperation(value = "Retorna todos os jogos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acesssar este recurso"),
            @ApiResponse(code = 404, message = "Endpoint não encontrado"),
            @ApiResponse(code = 500, message = "Erro no servidor")
    })
    @CrossOrigin
    public Iterable<Game> getAll(){
        return gameRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @ApiOperation(value = "Retorna um jogo, selecionando por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acesssar este recurso"),
            @ApiResponse(code = 404, message = "Endpoint não encontrado"),
            @ApiResponse(code = 500, message = "Erro no servidor")
    })
    public ResponseEntity getById(@PathVariable int id){
        Response<Optional<Game>> response = new Response<Optional<Game>>();

        try{
            Optional<Game> game = gameRepository.findById(id);

            if(game.isPresent()){
                response.setStatus(HttpStatus.FOUND.value());
                response.setDados(game);

                return ResponseEntity.ok(response);
            }

            response.setStatus(HttpStatus.NOT_FOUND.value());

            response.getErros().put("1","Game not found");



            return ResponseEntity.ok(response);

        }catch (Exception  e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErros().put("1", "Falha ao cadastrar novo paciente");

            return ResponseEntity.ok(response);
        }

    }

    @PostMapping()
    @ApiOperation(value = "Adiciona um jogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acesssar este recurso"),
            @ApiResponse(code = 404, message = "Endpoint não encontrado"),
            @ApiResponse(code = 500, message = "Erro no servidor")
    })
    public ResponseEntity createGame(@Valid @RequestBody Game game, BindingResult result){
        Response<Game> response = new Response<Game>();

        try {

            if(result.hasErrors()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());

                for(ObjectError error : result.getAllErrors()) {
                    String key = String.valueOf(response.getErros().size() + 1);

                    response.getErros().put(key, error.getDefaultMessage());
                }

                return ResponseEntity.ok(response);
            }
            response.setStatus(HttpStatus.OK.value());
            Game newGame = gameRepository.save(game);

            response.setDados(newGame);

            return ResponseEntity.ok(response);

        }catch (Exception  e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErros().put("1", "Falha ao cadastrar novo paciente");

            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/sell/{id}")
    @ApiOperation(value = "Vende de um jogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acesssar este recurso"),
            @ApiResponse(code = 404, message = "Endpoint não encontrado"),
            @ApiResponse(code = 500, message = "Erro no servidor")
    })
    public ResponseEntity sellGame(@PathVariable int id){
        Response response = new Response();
        try{
            if(!gameRepository.existsById(id)){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.getErros().put("1", "Jogo não encontrada");
                return ResponseEntity.ok(response);
            };

            Game game = gameRepository.findById(id).get();

            int numeroDeVendas = game.getNumeroDeVendas();
            game.setNumeroDeVendas(numeroDeVendas + 1);

            gameRepository.save(game);

            return new ResponseEntity("Venda concluida",HttpStatus.OK);

        }catch (Exception  e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErros().put("1", "Falha ao realizar venda");

            return ResponseEntity.ok(response);
        }


    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletar um jogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acesssar este recurso"),
            @ApiResponse(code = 404, message = "Endpoint não encontrado"),
            @ApiResponse(code = 500, message = "Erro no servidor")
    })
    public ResponseEntity deleteGame(@PathVariable int id){
        Response response = new Response();
        try{
            if(!gameRepository.existsById(id)){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.getErros().put("1", "Jogo não encontrada");
                return ResponseEntity.ok(response);
            };

            gameRepository.deleteById(id);

            return new ResponseEntity("Jogo deletado",HttpStatus.OK);

        }catch (Exception  e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErros().put("1", "Falha ao tentar excluir jogo");

            return ResponseEntity.ok(response);
        }

    }

    @PutMapping()
    @ApiOperation(value = "Atualiza os dados de um jogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acesssar este recurso"),
            @ApiResponse(code = 404, message = "Endpoint não encontrado"),
            @ApiResponse(code = 500, message = "Erro no servidor")
    })
    public ResponseEntity updateGame(Game gameInfo){
        try{
            gameRepository.save(gameInfo);

            return new ResponseEntity("Jogo atualizado",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
