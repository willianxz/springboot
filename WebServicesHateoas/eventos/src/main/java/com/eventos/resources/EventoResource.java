package com.eventos.resources;


import com.eventos.models.Evento;
import com.eventos.repository.EventoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Api(value="API REST Eventos")
@RestController
@RequestMapping("/evento")
public class EventoResource {

    @Autowired
    private EventoRepository er;


    @ApiOperation(value="Retorna uma lista de Eventos")
    @GetMapping(produces = "application/json")
    public @ResponseBody
    Iterable<Evento> listaEventos(){
      Iterable<Evento> listaEventos = er.findAll();
      ArrayList<Evento> eventos = new ArrayList<Evento>();
      for(Evento evento : listaEventos) {
          long codigo = evento.getCodigo();
          evento.add(linkTo(methodOn(EventoResource.class).evento(codigo)).withSelfRel());
          eventos.add(evento);
      }
        return eventos;
    }

    @ApiOperation(value="Retorna um evento especifico")
    @GetMapping(value="/{codigo}", produces = "application/json")
    public @ResponseBody Evento evento(@PathVariable(value="codigo") long codigo){
        Evento evento = er.findByCodigo(codigo);
        evento.add(linkTo(methodOn(EventoResource.class).listaEventos()).withRel("Lista de Eventos"));
        return evento;
    }


    @ApiOperation(value="Salva um Evento")
    @PostMapping()
    public Evento cadastraEvento(@RequestBody @Valid Evento evento){
        return er.save(evento);
    }

    @ApiOperation(value="Deleta um Evento")
    @DeleteMapping()
    public Evento  deletaEvento(@RequestBody Evento evento){
         er.delete(evento);
        return evento;
    }


}
