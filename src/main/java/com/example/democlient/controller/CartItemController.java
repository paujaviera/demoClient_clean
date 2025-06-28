package com.example.democlient.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.democlient.model.CartItem;
import com.example.democlient.service.CartItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ítems del carrito", description = "Operaciones de productos dentro del carrito de compras")
@RestController
@RequestMapping("api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Operation(summary = "Listar todos los ítems del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítems listados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItem.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CartItem>>> getAllCartItems() {
        List<EntityModel<CartItem>> items = cartItemService.listAll().stream()
            .map(item -> EntityModel.of(item,
                linkTo(methodOn(CartItemController.class).getCartItemById(item.getId())).withSelfRel(),
                linkTo(methodOn(CartItemController.class).deleteCartItem(item.getId())).withRel("eliminar")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(items,
            linkTo(methodOn(CartItemController.class).getAllCartItems()).withSelfRel()));
    }

    @Operation(summary = "Obtener un ítem del carrito por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítem encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItem.class))),
        @ApiResponse(responseCode = "404", description = "Ítem no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CartItem>> getCartItemById(@PathVariable Long id) {
        Optional<CartItem> item = cartItemService.getById(id);
        return item.map(cartItem -> ResponseEntity.ok(
                    EntityModel.of(cartItem,
                        linkTo(methodOn(CartItemController.class).getCartItemById(id)).withSelfRel(),
                        linkTo(methodOn(CartItemController.class).getAllCartItems()).withRel("todos"),
                        linkTo(methodOn(CartItemController.class).deleteCartItem(id)).withRel("eliminar"))))
                   .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo ítem en el carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítem creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItem.class))),
        @ApiResponse(responseCode = "400", description = "Error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<CartItem>> createCartItem(@RequestBody CartItem cartItem) {
        CartItem created = cartItemService.create(cartItem);
        return ResponseEntity.ok(EntityModel.of(created,
            linkTo(methodOn(CartItemController.class).getCartItemById(created.getId())).withSelfRel(),
            linkTo(methodOn(CartItemController.class).getAllCartItems()).withRel("todos"),
            linkTo(methodOn(CartItemController.class).deleteCartItem(created.getId())).withRel("eliminar")));
    }

    @Operation(summary = "Actualizar un ítem del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítem actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItem.class))),
        @ApiResponse(responseCode = "404", description = "Error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CartItem>> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItemDetails) {
        Optional<CartItem> optionalItem = cartItemService.getById(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        CartItem item = optionalItem.get();
        item.setProduct(cartItemDetails.getProduct());
        item.setQuantity(cartItemDetails.getQuantity());

        CartItem updated = cartItemService.update(item);
        return ResponseEntity.ok(EntityModel.of(updated,
            linkTo(methodOn(CartItemController.class).getCartItemById(id)).withSelfRel(),
            linkTo(methodOn(CartItemController.class).getAllCartItems()).withRel("todos"),
            linkTo(methodOn(CartItemController.class).deleteCartItem(id)).withRel("eliminar")));
    }

    @Operation(summary = "Eliminar un ítem del carrito por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ítem eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Error al eliminar ítem", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}