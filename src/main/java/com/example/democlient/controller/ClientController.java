package com.example.democlient.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.democlient.model.Client;
import com.example.democlient.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clientes", description = "Operaciones relacionadas con los clientes")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Listar todos los clientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clientes listados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
        @ApiResponse(responseCode = "500", description = "Error al obtener los clientes", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Client>>> listAll() {
        List<EntityModel<Client>> clients = clientService.listAll().stream()
            .map(client -> EntityModel.of(client,
                linkTo(methodOn(ClientController.class).listById(client.getIdClient())).withSelfRel(),
                linkTo(methodOn(ClientController.class).deleteById(client.getIdClient())).withRel("eliminar"),
                linkTo(methodOn(ClientController.class).update(client.getIdClient(), client)).withRel("actualizar")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(clients,
            linkTo(methodOn(ClientController.class).listAll()).withSelfRel()));
    }

    @Operation(summary = "Obtener un cliente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/{idClient}")
    public ResponseEntity<EntityModel<Client>> listById(@PathVariable Long idClient) {
        Client client = clientService.listById(idClient);
        return ResponseEntity.ok(EntityModel.of(client,
            linkTo(methodOn(ClientController.class).listById(idClient)).withSelfRel(),
            linkTo(methodOn(ClientController.class).listAll()).withRel("todos"),
            linkTo(methodOn(ClientController.class).deleteById(idClient)).withRel("eliminar")));
    }

    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
        @ApiResponse(responseCode = "400", description = "Error al crear el cliente", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Client>> create(@RequestBody Client client) {
        Client clientCreate = clientService.create(client);
        return ResponseEntity.ok(EntityModel.of(clientCreate,
            linkTo(methodOn(ClientController.class).listById(clientCreate.getIdClient())).withSelfRel(),
            linkTo(methodOn(ClientController.class).listAll()).withRel("todos"),
            linkTo(methodOn(ClientController.class).deleteById(clientCreate.getIdClient())).withRel("eliminar")));
    }

    @Operation(summary = "Actualizar un cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PutMapping("/{idClient}")
    public ResponseEntity<EntityModel<Client>> update(@PathVariable Long idClient, @RequestBody Client client) {
        client.setIdClient(idClient);
        Client clientUpdate = clientService.update(client);
        return ResponseEntity.ok(EntityModel.of(clientUpdate,
            linkTo(methodOn(ClientController.class).listById(idClient)).withSelfRel(),
            linkTo(methodOn(ClientController.class).listAll()).withRel("todos"),
            linkTo(methodOn(ClientController.class).deleteById(idClient)).withRel("eliminar")));
    }

    @Operation(summary = "Eliminar un cliente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @DeleteMapping("/{idClient}")
    public ResponseEntity<Void> deleteById(@PathVariable Long idClient) {
        clientService.deleteById(idClient);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear varios clientes a la vez")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clientes creados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
        @ApiResponse(responseCode = "400", description = "Error al crear clientes", content = @Content)
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<Client>> bulkCreate(@RequestBody List<Client> clients) {
        List<Client> created = clientService.bulkCreate(clients);
        return ResponseEntity.ok(created);
    }
}
