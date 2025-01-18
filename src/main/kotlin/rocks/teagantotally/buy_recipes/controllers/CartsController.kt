package rocks.teagantotally.buy_recipes.controllers

import jakarta.transaction.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rocks.teagantotally.buy_recipes.data.entities.Cart
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.services.CartService

/**
 * REST controller for managing shopping carts.
 *
 * @param cartService The service used to handle cart operations.
 */
@RestController
@RequestMapping("/carts", produces = ["application/json"])
@Transactional
class CartsController(
        private val cartService: CartService,
) {
  @GetMapping() fun getCarts(): List<Cart> = cartService.getCarts()

  /**
   * Retrieves a cart by its ID.
   *
   * @param id The ID of the cart to retrieve.
   * @return The cart with the specified ID, or null if not found.
   */
  @GetMapping("/{id}")
  fun getCart(
          @PathVariable id: Int,
  ): Cart = cartService.getCart(id)

  /**
   * Adds a recipe to the cart by recipe object.
   *
   * @param id The ID of the cart.
   * @param recipe The recipe to add to the cart.
   * @return The updated cart.
   */
  @PostMapping("/{id}/add_recipe", consumes = ["application/json"], produces = ["application/json"])
  fun addRecipeToCart(
          @PathVariable id: Int,
          @RequestBody recipe: Recipe,
  ): Cart = cartService.addRecipeToCart(id, recipe)

  /**
   * Adds a recipe to the cart by recipe ID.
   *
   * @param id The ID of the cart.
   * @param recipeId The ID of the recipe to add to the cart.
   * @return The updated cart.
   */
  @PostMapping("/{id}/add_recipe/{recipeId}")
  fun addRecipeToCart(
          @PathVariable id: Int,
          @PathVariable recipeId: Int,
  ): Cart = cartService.addRecipeToCart(id, recipeId)

  /**
   * Removes a recipe from the cart.
   *
   * @param id The ID of the cart.
   * @param recipeId The ID of the recipe to remove from the cart.
   * @return The updated cart.
   */
  @DeleteMapping("/{id}/recipes/{recipeId}")
  fun removeRecipeFromCart(
          @PathVariable id: Int,
          @PathVariable recipeId: Int,
  ): Cart = cartService.removeRecipeFromCart(id, recipeId)
}
