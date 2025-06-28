package com.example.democlient.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.democlient.model.Product;
import com.example.democlient.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos", description = "Operaciones relacionadas con los productos")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Listar todos los productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos listados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "500", description = "Error al obtener productos", content = @Content)
    })
    @GetMapping
    public CollectionModel<EntityModel<Product>> listar() {
        List<EntityModel<Product>> products = productService.listAll().stream()
            .map(product -> EntityModel.of(product,
                linkTo(methodOn(ProductController.class).obtenerPorId(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).actualizar(product.getId(), product)).withRel("actualizar"),
                linkTo(methodOn(ProductController.class).eliminar(product.getId())).withRel("eliminar")))
            .collect(Collectors.toList());

        return CollectionModel.of(products,
            linkTo(methodOn(ProductController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Obtener un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> obtenerPorId(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Product> resource = EntityModel.of(product,
            linkTo(methodOn(ProductController.class).obtenerPorId(id)).withSelfRel(),
            linkTo(methodOn(ProductController.class).actualizar(id, product)).withRel("actualizar"),
            linkTo(methodOn(ProductController.class).eliminar(id)).withRel("eliminar"),
            linkTo(methodOn(ProductController.class).listar()).withRel("listar"));

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Error al crear el producto", content = @Content)
    })
    @PostMapping
    public EntityModel<Product> crear(@RequestBody Product product) {
        Product created = productService.create(product);
        return EntityModel.of(created,
            linkTo(methodOn(ProductController.class).obtenerPorId(created.getId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).actualizar(created.getId(), created)).withRel("actualizar"),
            linkTo(methodOn(ProductController.class).eliminar(created.getId())).withRel("eliminar"),
            linkTo(methodOn(ProductController.class).listar()).withRel("listar"));
    }

    @Operation(summary = "Actualizar un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> actualizar(@PathVariable Long id, @RequestBody Product product) {
        Product existing = productService.getProductById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());

        Product updated = productService.update(existing);
        return ResponseEntity.ok(EntityModel.of(updated,
            linkTo(methodOn(ProductController.class).obtenerPorId(updated.getId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).eliminar(updated.getId())).withRel("eliminar"),
            linkTo(methodOn(ProductController.class).listar()).withRel("listar")));
    }

    @Operation(summary = "Eliminar un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear varios productos a la vez")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Productos creados correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Error al crear productos", content = @Content)
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<Product>> bulkCreate(@RequestBody List<Product> products) {
        List<Product> saved = productService.bulkCreate(products);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
