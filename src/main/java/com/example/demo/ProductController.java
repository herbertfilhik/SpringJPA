package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/principal")
	public String home() {
		return "principal";
	}

	@GetMapping("/products/new")
	@Secured("ROLE_USER")
	public String showProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "product-form";
	}

	@PostMapping("/products")
	@Secured("ROLE_USER")
	public String saveProduct(@ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	@GetMapping("/products")
	@Secured("ROLE_USER")
	public String showProducts(Model model) {
		List<Product> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "product-list";
	}

	@GetMapping("/products/{id}/delete")
	@Secured("ROLE_USER")
	public String deleteProduct(@PathVariable("id") long id) {
		productService.deleteProductById(id);
		return "redirect:/products";
	}

	@DeleteMapping("/products/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		productService.deleteProductById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/products/{id}/edit")
	@Secured("ROLE_USER")
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
	@Secured("ROLE_USER")
	public String submitEditProductForm(@PathVariable("id") long id, @ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		new SecurityContextLogoutHandler().logout(request, null, null);
		return "redirect:/login?logout";
	}

	// outros métodos do controlador aqui
}
