package com.example.democlient.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.democlient.model.Cart;
import com.example.democlient.model.CartItem;
import com.example.democlient.model.Product;
import com.example.democlient.service.CartService;
import com.example.democlient.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Carrito", description = "Operaciones del carrito de compras")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Operation(summary = "Listar todos los carritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carritos listados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cart>>> getAllCarts() {
        List<EntityModel<Cart>> carts = cartService.listAll().stream()
            .map(cart -> EntityModel.of(cart,
                linkTo(methodOn(CartController.class).getCartById(cart.getId())).withSelfRel(),
                linkTo(methodOn(CartController.class).deleteCart(cart.getId())).withRel("eliminar")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(carts,
            linkTo(methodOn(CartController.class).getAllCarts()).withSelfRel()));
    }

    @Operation(summary = "Agregar producto al carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto agregado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "400", description = "Cantidad inválida o producto no encontrado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content)
    })
    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addProductToCart(@PathVariable Long cartId, @RequestBody CartItem newItem) {
        if (newItem.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("La cantidad de productos debe ser mayor a 0");
        }

        Optional<Cart> optionalCart = cartService.getCartById(cartId);
        if (!optionalCart.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Cart cart = optionalCart.get();
        Product product = productService.getProductById(newItem.getProduct().getId());
        if (product == null) {
            return ResponseEntity.badRequest().body("Producto no encontrado");
        }

        cart.addProduct(product, newItem.getQuantity());
        Cart updatedCart = cartService.updateCart(cart);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "Eliminar producto del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado del carrito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "404", description = "Error al eliminar producto", content = @Content)
    })
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        Optional<Cart> optionalCart = cartService.getCartById(cartId);
        if (!optionalCart.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = optionalCart.get();
        cart.removeProduct(productId);
        Cart updatedCart = cartService.updateCart(cart);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "Crear un nuevo carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "400", description = "Error al crear carrito", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Cart>> createCart(@RequestBody Cart cart) {
        Cart createdCart = cartService.createCart(cart);
        return ResponseEntity.ok(EntityModel.of(createdCart,
            linkTo(methodOn(CartController.class).getCartById(createdCart.getId())).withSelfRel(),
            linkTo(methodOn(CartController.class).getAllCarts()).withRel("todos")));
    }

    @Operation(summary = "Obtener un carrito por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cart>> getCartById(@PathVariable Long id) {
        Optional<Cart> cart = cartService.getCartById(id);
        if (!cart.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityModel.of(cart.get(),
            linkTo(methodOn(CartController.class).getCartById(id)).withSelfRel(),
            linkTo(methodOn(CartController.class).getAllCarts()).withRel("todos"),
            linkTo(methodOn(CartController.class).updateCart(id, cart.get())).withRel("actualizar"),
            linkTo(methodOn(CartController.class).deleteCart(id)).withRel("eliminar")));
    }

    @Operation(summary = "Actualizar un carrito existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cart>> updateCart(@PathVariable Long id, @RequestBody Cart cartDetails) {
        Optional<Cart> optionalCart = cartService.getCartById(id);
        if (!optionalCart.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Cart cart = optionalCart.get();
        cart.setItems(cartDetails.getItems());
        cart.setClient(cartDetails.getClient());
        Cart updatedCart = cartService.updateCart(cart);
        return ResponseEntity.ok(EntityModel.of(updatedCart,
            linkTo(methodOn(CartController.class).getCartById(updatedCart.getId())).withSelfRel(),
            linkTo(methodOn(CartController.class).getAllCarts()).withRel("todos"),
            linkTo(methodOn(CartController.class).deleteCart(updatedCart.getId())).withRel("eliminar")));
    }

    @Operation(summary = "Eliminar un carrito por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carrito eliminado correctamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
