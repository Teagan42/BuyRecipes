package rocks.teagantotally.buy_recipes.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.services.RecipeService

/**
 * REST controller for managing recipes.
 *
 * This controller provides endpoints to retrieve recipes.
 *
 * @param recipeService The service used to manage recipes.
 */
@RestController
@RequestMapping("/products", produces = ["application/json"])
class ProductController(
        private val recipeService: RecipeService,
) {
  /**
   * Retrieves a list of all recipes.
   *
   * @return A list of recipes.
   */
  @GetMapping() @ResponseBody fun getProducts(): List<Product> = recipeService.getProducts()
}
