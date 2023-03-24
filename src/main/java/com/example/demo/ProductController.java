package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

    @RequestMapping("/")
    public String home() {
        return "principal";
    }
	
	@GetMapping("/products/new")
	public String showProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "product-form";
	}

	@PostMapping("/products")
	public String saveProduct(@ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	@GetMapping("/products")
	public String showProducts(Model model) {
		List<Product> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "product-list";
	}

	@GetMapping("/products/{id}/delete")
	public String deleteProduct(@PathVariable("id") long id) {
		productService.deleteProductById(id);
		return "redirect:/products";
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		productService.deleteProductById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/products/{id}/edit")
	public String showEditForm(@PathVariable("id") long id, Model model) {
		try {
			Optional<Product> optionalProduct = productService.getProductById(id);
			Product product = optionalProduct
					.orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
			model.addAttribute("product", product);
			return "edit-product";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
			List<Product> productList = productService.getAllProducts();
			model.addAttribute("productList", productList);
			return "product-list";
		}
	}

	@PostMapping("/products/{id}/edit")
	public String submitEditProductForm(@PathVariable("id") long id, @ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	// outros métodos do controlador aqui
}
