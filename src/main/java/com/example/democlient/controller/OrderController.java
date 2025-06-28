package com.example.democlient.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.democlient.model.Order;
import com.example.democlient.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Órdenes", description = "Operaciones relacionadas con las órdenes realizadas por clientes")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Listar todas las órdenes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Órdenes listadas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Order>>> listAll() {
        List<EntityModel<Order>> orders = orderService.listAll().stream()
            .map(order -> EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).delete(order.getId())).withRel("eliminar"),
                linkTo(methodOn(OrderController.class).update(order.getId(), order)).withRel("actualizar")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orders,
            linkTo(methodOn(OrderController.class).listAll()).withSelfRel()));
    }

    @Operation(summary = "Obtener una orden por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Order>> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(EntityModel.of(order,
            linkTo(methodOn(OrderController.class).getOrderById(id)).withSelfRel(),
            linkTo(methodOn(OrderController.class).delete(id)).withRel("eliminar"),
            linkTo(methodOn(OrderController.class).listAll()).withRel("todas")));
    }

    @Operation(summary = "Crear una nueva orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Orden creada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Error en los datos enviados", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Order>> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.create(order);
        return ResponseEntity.status(201).body(EntityModel.of(createdOrder,
            linkTo(methodOn(OrderController.class).getOrderById(createdOrder.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).listAll()).withRel("todas")));
    }

    @Operation(summary = "Actualizar una orden existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden actualizada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Order>> update(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        Order updatedOrder = orderService.update(order);
        if (updatedOrder == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(EntityModel.of(updatedOrder,
            linkTo(methodOn(OrderController.class).getOrderById(id)).withSelfRel(),
            linkTo(methodOn(OrderController.class).listAll()).withRel("todas"),
            linkTo(methodOn(OrderController.class).delete(id)).withRel("eliminar")));
    }

    @Operation(summary = "Eliminar una orden por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Orden eliminada exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            orderService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar órdenes por ID de cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Órdenes del cliente obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "204", description = "El cliente no tiene órdenes", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/client/{idClient}")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> getOrdersByClient(@PathVariable Long idClient) {
        List<Order> orders = orderService.getOrdersByIdClient(idClient);
        if (orders.isEmpty()) return ResponseEntity.noContent().build();

        List<EntityModel<Order>> orderModels = orders.stream()
            .map(order -> EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).delete(order.getId())).withRel("eliminar")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orderModels,
            linkTo(methodOn(OrderController.class).getOrdersByClient(idClient)).withSelfRel()));
    }
}
