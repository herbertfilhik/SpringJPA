package com.example.demo;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/home")
	public String home() {
		return "home";
	}

	@RequestMapping("/principal")
	public String home(Model model, Principal principal) {
		String currentUser = principal.getName();
		model.addAttribute("currentUser", currentUser);
		return "principal";
	}

	/**
	 * Mostra o formulário para adicionar um novo produto.
	 */
	@GetMapping("/products/new")
	@Secured("ROLE_USER")
	public String showProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "product-form";
	}

	/**
	 * Salva um novo produto na base de dados.
	 */
	@PostMapping("/products")
	@Secured("ROLE_USER")
	public String saveProduct(@ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	/**
	 * Lista todos os produtos cadastrados na base de dados.
	 */
	@GetMapping("/products")
	@Secured("ROLE_USER")
	public String showProducts(Model model) {
		List<Product> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "product-list";
	}

	/**
	 * Remove um produto da base de dados.
	 */
	@DeleteMapping("/products/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		LOGGER.info("Deleting product with ID {}", id);
		productService.deleteProductById(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Mostra o formulário para editar um produto existente.
	 */
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

	/**
	 * Salva as alterações feitas em um produto existente.
	 */
	@PostMapping("/products/{id}/edit")
	@Secured("ROLE_USER")
	public String submitEditProductForm(@PathVariable("id") long id, @ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	/**
	 * Realiza o logout do usuário e redireciona para a página de login.
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		new SecurityContextLogoutHandler().logout(request, null, null);
		return "redirect:/login?logout";
	}

	// outros métodos do controlador aqui
}