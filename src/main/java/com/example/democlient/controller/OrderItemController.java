package com.example.democlient.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.democlient.model.OrderItem;
import com.example.democlient.service.OrderItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ítems de Orden", description = "Operaciones de los productos dentro de una orden")
@RestController
@RequestMapping("/api/orderitems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Operation(summary = "Listar todos los ítems de órdenes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítems listados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
        @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<OrderItem>>> listAll() {
        List<EntityModel<OrderItem>> items = orderItemService.listAll().stream()
            .map(item -> EntityModel.of(item,
                linkTo(methodOn(OrderItemController.class).getOrderItemById(item.getId())).withSelfRel(),
                linkTo(methodOn(OrderItemController.class).delete(item.getId())).withRel("eliminar"),
                linkTo(methodOn(OrderItemController.class).update(item.getId(), item)).withRel("actualizar")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(items,
            linkTo(methodOn(OrderItemController.class).listAll()).withSelfRel()));
    }

    @Operation(summary = "Obtener un ítem de orden por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítem encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
        @ApiResponse(responseCode = "404", description = "Ítem no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderItem>> getOrderItemById(@PathVariable Long id) {
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        if (orderItem == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(EntityModel.of(orderItem,
            linkTo(methodOn(OrderItemController.class).getOrderItemById(id)).withSelfRel(),
            linkTo(methodOn(OrderItemController.class).listAll()).withRel("todos"),
            linkTo(methodOn(OrderItemController.class).delete(id)).withRel("eliminar")));
    }

    @Operation(summary = "Crear un nuevo ítem de orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ítem creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<OrderItem>> create(@RequestBody OrderItem orderItem) {
        OrderItem created = orderItemService.create(orderItem);
        return ResponseEntity.status(201).body(EntityModel.of(created,
            linkTo(methodOn(OrderItemController.class).getOrderItemById(created.getId())).withSelfRel(),
            linkTo(methodOn(OrderItemController.class).listAll()).withRel("todos")));
    }

    @Operation(summary = "Actualizar un ítem de orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítem actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
        @ApiResponse(responseCode = "404", description = "Ítem no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<OrderItem>> update(@PathVariable Long id, @RequestBody OrderItem orderItem) {
        orderItem.setId(id);
        OrderItem updated = orderItemService.update(orderItem);
        return ResponseEntity.ok(EntityModel.of(updated,
            linkTo(methodOn(OrderItemController.class).getOrderItemById(id)).withSelfRel(),
            linkTo(methodOn(OrderItemController.class).listAll()).withRel("todos"),
            linkTo(methodOn(OrderItemController.class).delete(id)).withRel("eliminar")));
    }

    @Operation(summary = "Eliminar un ítem de orden por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ítem eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ítem no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
